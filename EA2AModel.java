import java.util.ArrayList;
import javax.media.opengl.*;

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
  private int zSign;
  
  // Atoms/groups
  private SP3Atom bottom_carb;  // Bottom carbon in double bond
  private SP3Atom top_carb;     // Top carbon in double bond
  private Atom bottom_H;
  private Atom top_H;
  private Methyl ch3a;
  private Methyl ch3b;
  
  private Atom nucleophile;
  
  // bonds
  private Bond bottom_carb_nucleophile;
  /**
   * Remember to add the new bonds here please!!!!
   */
  private Bond bottom_carb_H, top_carb_H, bottom_carb_ch3, carb_carb, top_carb_ch3;

  public EA2AModel(int zSign) {
    this.zSign = zSign;
    
    nucleophile = new Atom(new Point3D(0, 0, zSign * (Atom.S_TO_SP3_BOND_LENGTH + Math.max(0, (0.8 - getT())))),
                           AtomOrGroup.Charge.MINUS);
    
    bottom_carb = new SP3Atom();
    bottom_carb.setInsideOutness(0.5);
    bottom_carb.setP0Divergence(1);
    bottom_carb.setRot(0, 0, 180);
    
    bottom_H = new Atom(new Point3D(), AtomOrGroup.Charge.NEUTRAL);
    top_H = new Atom(new Point3D(), AtomOrGroup.Charge.NEUTRAL);
    ch3a = new Methyl(new Point3D(), AtomOrGroup.Charge.NEUTRAL);
    ch3b = new Methyl(new Point3D(), AtomOrGroup.Charge.NEUTRAL);
    top_carb = new SP3Atom();
    top_carb.setInsideOutness(0.5);
    top_carb.setP0Divergence(1);
    setHydrogenLocations();
    
    // Create the Bonds
    bottom_carb_nucleophile = new Bond(bottom_carb, nucleophile, Bond.State.BROKEN);
    bottom_carb_H = new Bond(bottom_carb, bottom_H, Bond.State.FULL);
    top_carb_H = new Bond(top_carb, top_H, Bond.State.FULL);
    bottom_carb_ch3 = new Bond(bottom_carb, ch3a, Bond.State.FULL);
    top_carb_ch3 = new Bond(top_carb, ch3b, Bond.State.FULL);            // creating the top methyl group
    carb_carb = new Bond(bottom_carb, top_carb, Bond.State.DOUBLE);
  }
  
  private void setHydrogenLocations() {
//    // Figure out the proper rotation for the hydrogens.  This depends on the inside-outness of the carbon.
    double rotation = -bottom_carb.getXRotation();

    // Top carbon on orbital 1
    Point3D orb1Vec = bottom_carb.getOrbitalVector(1).scale(Atom.SP3_SP3_BOND_LENGTH);
    top_carb.setLoc(bottom_carb.getX() + orb1Vec.x(), bottom_carb.getY() + orb1Vec.y(), bottom_carb.getZ() + orb1Vec.z());
    top_carb.setRot(180 + 2 * rotation, 0, 0);
    
    // Hydrogen on orbital 2 (orbital 3 on the top carbon)
    Point3D orb2Vec = bottom_carb.getOrbitalVector(2).scale(Atom.S_TO_SP3_BOND_LENGTH);
    bottom_H.setLoc(bottom_carb.getX() + orb2Vec.x(), bottom_carb.getY() + orb2Vec.y(), bottom_carb.getZ() + orb2Vec.z());
    Point3D orb3Vec = top_carb.getOrbitalVector(3).scale(Atom.S_TO_SP3_BOND_LENGTH);
    top_H.setLoc(top_carb.getX() + orb3Vec.x(), top_carb.getY() + orb3Vec.y(), top_carb.getZ() + orb3Vec.z());
    
    // Methyl on orbital 3 (orbital 2 on the top)
    Point3D orb4Vec = bottom_carb.getOrbitalVector(3).scale(Atom.SP3_SP3_BOND_LENGTH);
    ch3a.setLoc(bottom_carb.getX() + orb4Vec.x(), bottom_carb.getY() + orb4Vec.y(), bottom_carb.getZ() + orb4Vec.z());
    
    Point3D orb5Vec = top_carb.getOrbitalVector(2).scale(Atom.SP3_SP3_BOND_LENGTH);
    ch3b.setLoc(top_carb.getX() + orb5Vec.x(), top_carb.getY() + orb5Vec.y(), top_carb.getZ() + orb5Vec.z());
    ch3a.setRot(0, 180 - rotation, 90 - 120);
    ch3b.setRot(0, 175 - rotation, 90 - 75);
  }

  public ArrayList<Drawable> createDrawList(boolean twoD) {
    ArrayList<Drawable> result = new ArrayList<Drawable>();
    if (twoD) { // Add the bonds as well
      //result.add(new BondView(bottom_carb_nucleophile));
      result.add(new BondView(bottom_carb_H));
      result.add(new BondView(top_carb_H));
      result.add(new BondView(bottom_carb_ch3));
      result.add(new BondView(top_carb_ch3));
      result.add(new BondView(carb_carb));
    }

    // Draw the 3-D view back to front
    result.add(ch3a.createView());
    result.add(ch3b.createView());
    //result.add(nucleophile.createView());
    result.add(bottom_carb.createView("C", AtomView.C_BLACK));
    result.add(top_carb.createView("C", AtomView.C_BLACK));
    result.add(bottom_H.createView());
    result.add(top_H.createView());
    return result;
  }
  
  public void setT(double newT) {
    super.setT(newT);
    
    // Set the angles betwen the carbon's orbitals, and the proportion of its center orbital
    double insideOutnessOffset = -zSign * Math.max(0, Math.min(0.5, ((0.5/0.6) * (getT() - 0.3))));
    double insideOutness = 0.5 + insideOutnessOffset;
    //double insideOutness = Math.min(1.0, Math.max(0.5, ((0.5/0.6) * (getT() - 0.3)) + 0.5));
    double divergence = 1.0 - (4 * (insideOutness - 0.5) * (insideOutness - 0.5));
    bottom_carb.setInsideOutness(insideOutness);
    bottom_carb.setP0Divergence(divergence);
    top_carb.setInsideOutness(insideOutness);
    top_carb.setP0Divergence(divergence);

    
    // Set the corresponding locations of the hydrogens
    setHydrogenLocations();
    
    // The nucleophile moves in
    nucleophile.setLoc(0, 0, zSign * (Atom.S_TO_SP3_BOND_LENGTH + Math.max(0, (0.8 - getT()))));
    
    // Update the Bonds
    if (getT() < 0.3) {
      bottom_carb_nucleophile.setState(Bond.State.BROKEN);
      nucleophile.setCharge(AtomOrGroup.Charge.MINUS);
      top_carb.setCharge(AtomOrGroup.Charge.NEUTRAL);
      carb_carb.setState(Bond.State.DOUBLE);
      bottom_carb_ch3.setState(Bond.State.FULL);
      top_carb_ch3.setState(Bond.State.FULL);
    }
    else if (getT() > 0.5) {
      bottom_carb_nucleophile.setState(Bond.State.FULL);
      nucleophile.setCharge(AtomOrGroup.Charge.NEUTRAL);
      top_carb.setCharge(AtomOrGroup.Charge.MINUS);
      carb_carb.setState(Bond.State.FULL);
      bottom_carb_ch3.setState(Bond.State.FULL);
      top_carb_ch3.setState(Bond.State.FULL);
    }
    else {
      bottom_carb_nucleophile.setState(Bond.State.PARTIAL);
      nucleophile.setCharge(AtomOrGroup.Charge.PART_MINUS);
      top_carb.setCharge(AtomOrGroup.Charge.PART_MINUS);
      carb_carb.setState(Bond.State.FULL_PARTIAL);
      bottom_carb_ch3.setState(Bond.State.FULL);
      top_carb_ch3.setState(Bond.State.FULL);
    }
  }
}
