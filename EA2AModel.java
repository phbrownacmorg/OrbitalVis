import java.util.ArrayList;
//import javax.media.opengl.*;

/**
 * The Model class for electrophilic addition to an alkene
 *
 * Copyright 2011 Peter Brown <phbrown@acm.org>, Megan Dobbins<megan.dobbins@converse.edu>, and Ashleigh Geldenhuys<ashleigh.geldenhuys@converse.edu>
 *
 * This code is distributed under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the
 * license or (at your option) any later version.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

public class EA2AModel extends Model {
  // HCl attacks from the left or the right?
  private int xSign;
  
  // Atoms/groups
  private SP3Atom bottom_carb;  // Bottom carbon in double bond
  private SP3Atom top_carb;     // Top carbon in double bond
  private Atom bottom_H;
  private Atom top_H;
  private Methyl ch3a;
  private Methyl ch3b;
  
  private Atom new_H;           // H from the HCl
  private SP3Atom cl;
  
  // bonds
  private Bond new_H_cl;  //maybe
  private Bond top_carb_new_H;
  /**
   * Remember to add the new bonds here please!!!!
   */
  private Bond bottom_carb_H, top_carb_H, bottom_carb_ch3, carb_carb, top_carb_ch3;

  public EA2AModel(int xSign) {
    this.xSign = xSign;
    
    top_carb = new SP3Atom();
    top_carb.setInsideOutness(0.5);
    top_carb.setP0Divergence(1);
    
    bottom_carb = new SP3Atom(top_carb.getOrbitalVector(1).scale(Atom.SP3_SP3_BOND_LENGTH));
    bottom_carb.setInsideOutness(0.5);
    bottom_carb.setP0Divergence(1);
    bottom_carb.setRot(0, 0, top_carb.getZRotation() + (180 - bottom_carb.getZRotation()));
    
    bottom_H = new Atom(new Point3D(), AtomOrGroup.Charge.NEUTRAL, bottom_carb, -0.6, -0.4);
    ch3a = new Methyl(new Point3D(), AtomOrGroup.Charge.NEUTRAL, -0.3, 0.55);
    top_H = new Atom(new Point3D(), AtomOrGroup.Charge.NEUTRAL, null, 0.3, 0.3);
    ch3b = new Methyl(new Point3D(), AtomOrGroup.Charge.NEUTRAL, bottom_carb, 0.4, -0.8);
    new_H = new Atom(new Point3D(xSign * (Atom.S_TO_SP3_BOND_LENGTH + Math.max(0, (0.8 - getT()))), 0, 0),
                     AtomOrGroup.Charge.NEUTRAL);
    cl = new SP3Atom(new Point3D(new_H.getX() + xSign * Atom.S_TO_SP3_BOND_LENGTH, 0, 0));
    cl.setRot(0, 0, 90 + 90 * xSign);
    
    setHydrogenLocations();
    
    // Create the Bonds
    top_carb_new_H = new Bond(top_carb, new_H, Bond.State.BROKEN);
    bottom_carb_H = new Bond(bottom_carb, bottom_H, Bond.State.FULL);
    top_carb_H = new Bond(top_carb, top_H, Bond.State.FULL);
    bottom_carb_ch3 = new Bond(bottom_carb, ch3b, Bond.State.FULL);
    top_carb_ch3 = new Bond(top_carb, ch3a, Bond.State.FULL);            // creating the top methyl group
    carb_carb = new Bond(top_carb, bottom_carb, Bond.State.DOUBLE);
    new_H_cl = new Bond(new_H, cl, Bond.State.FULL);  //theory
  }
  
  private void setHydrogenLocations() {
//    // Figure out the proper rotation for the hydrogens.  This depends on the inside-outness of the carbon.
    double rotation = -top_carb.getZRotation();

    // Bottom carbon on top carbon's orbital 1 
    Point3D orb1Vec = top_carb.getOrbitalVector(1).scale(Atom.SP3_SP3_BOND_LENGTH);
    bottom_carb.setLoc(orb1Vec.x(), orb1Vec.y(), orb1Vec.z());
    bottom_carb.setRot(0, 0, top_carb.getZRotation() + (180 - bottom_carb.getZRotation()));
    
    // Methyl on top carbon's orbital 2
    Point3D orb2Vec = top_carb.getOrbitalVector(2).scale(Atom.SP3_SP3_BOND_LENGTH);
    ch3a.setLoc(orb2Vec.x(), orb2Vec.y(), orb2Vec.z());
    ch3a.setRot(120, 0, 180 - rotation);
    
    // Methyl on bottom carbon's orbital 3
    Point3D orb3Vec = bottom_carb.getOrbitalVector(3).scale(Atom.SP3_SP3_BOND_LENGTH);
    ch3b.setLoc(orb3Vec.x(), orb3Vec.y(), orb3Vec.z());
    ch3b.setRot(-120, 0, 180 + bottom_carb.getZRotation());
    
    // Hydrogen on orbital 2 (orbital 3 on the top carbon)
    orb2Vec = bottom_carb.getOrbitalVector(2).scale(Atom.S_TO_SP3_BOND_LENGTH);
    bottom_H.setLoc(orb2Vec.x(), orb2Vec.y(), orb2Vec.z());
    orb3Vec = top_carb.getOrbitalVector(3).scale(Atom.S_TO_SP3_BOND_LENGTH);
    top_H.setLoc(orb3Vec.x(), orb3Vec.y(), orb3Vec.z());    
  }

  public ArrayList<Drawable> createDrawList(boolean twoD) {
    ArrayList<Drawable> result = new ArrayList<Drawable>();
    if (twoD) { // Add the bonds as well
      result.add(new BondView(top_carb_new_H));
      result.add(new BondView(bottom_carb_H));
      result.add(new BondView(top_carb_H));
      result.add(new BondView(bottom_carb_ch3));
      result.add(new BondView(top_carb_ch3));
      result.add(new BondView(carb_carb));
      result.add(new BondView(new_H_cl));
    }

    // Draw the 3-D view back to front
    result.add(bottom_H.createView());
    result.add(ch3a.createView());
    result.add(ch3b.createView());
    result.add(new_H.createView());
    result.add(bottom_carb.createView("C", AtomView.C_BLACK));
    result.add(top_carb.createView("C", AtomView.C_BLACK));
    result.add(top_H.createView());
    result.add(cl.createView("Cl", AtomView.CL_GREEN)); // added but not in correct position
    return result;
  }
  
  public void setT(double newT) {
    super.setT(newT);
    
    // Set the angles betwen the carbon's orbitals, and the proportion of its center orbital
    double insideOutnessOffset = -xSign * Math.max(0, Math.min(0.5, ((0.5/0.6) * (getT() - 0.3))));
    double insideOutness = 0.5 + insideOutnessOffset;
    //double insideOutness = Math.min(1.0, Math.max(0.5, ((0.5/0.6) * (getT() - 0.3)) + 0.5));
    double divergence = 1.0 - (4 * (insideOutness - 0.5) * (insideOutness - 0.5));
    bottom_carb.setInsideOutness(0.5); //- insideOutnessOffset);
    bottom_carb.setP0Divergence(divergence);
    top_carb.setInsideOutness(insideOutness);
    top_carb.setP0Divergence(divergence);

    // Set the corresponding locations of the hydrogens
    setHydrogenLocations();
    
    // The new_H moves in
    new_H.setLoc(xSign * (Atom.S_TO_SP3_BOND_LENGTH + Math.max(0, (0.8 - getT()))), 0, 0);
   // cl.setLoc(0, 0, );
    
    // Update the Bonds
    if (getT() < 0.23) {
      top_carb_new_H.setState(Bond.State.BROKEN);
      new_H.setCharge(AtomOrGroup.Charge.NEUTRAL);
      cl.setCharge(AtomOrGroup.Charge.NEUTRAL);
      new_H_cl.setState(Bond.State.FULL);
      bottom_carb.setCharge(AtomOrGroup.Charge.NEUTRAL);
      carb_carb.setState(Bond.State.DOUBLE);
      bottom_carb_ch3.setState(Bond.State.FULL);
      top_carb_ch3.setState(Bond.State.FULL);
    }
    else if (getT() > 0.5) {
      top_carb_new_H.setState(Bond.State.FULL);
      new_H.setCharge(AtomOrGroup.Charge.NEUTRAL);
      cl.setCharge(AtomOrGroup.Charge.MINUS);
      new_H_cl.setState(Bond.State.BROKEN);
      bottom_carb.setCharge(AtomOrGroup.Charge.PLUS);
      carb_carb.setState(Bond.State.FULL);
      bottom_carb_ch3.setState(Bond.State.FULL);
      top_carb_ch3.setState(Bond.State.FULL);
    }
    else {
      top_carb_new_H.setState(Bond.State.PARTIAL);
      new_H.setCharge(AtomOrGroup.Charge.NEUTRAL);
      cl.setCharge(AtomOrGroup.Charge.PART_MINUS);
      new_H_cl.setState(Bond.State.PARTIAL);
      bottom_carb.setCharge(AtomOrGroup.Charge.PART_PLUS);
      carb_carb.setState(Bond.State.FULL_PARTIAL);
      bottom_carb_ch3.setState(Bond.State.FULL);
      top_carb_ch3.setState(Bond.State.FULL);
    }
  }
}
