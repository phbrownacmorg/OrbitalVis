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
  
  private Atom new_H;           // H from the HCl
  private SP3Atom cl;
  
  // bonds
  private Bond cl_new_H;  //maybe
  private Bond bottom_carb_new_H;
  /**
   * Remember to add the new bonds here please!!!!
   */
  private Bond bottom_carb_H, top_carb_H, bottom_carb_ch3, carb_carb, top_carb_ch3;

  public EA2AModel(int zSign) {
    this.zSign = zSign;

    
    bottom_carb = new SP3Atom();
    bottom_carb.setInsideOutness(0.5);
    bottom_carb.setP0Divergence(1);
    bottom_carb.setRot(0, 0, 180);
    
    bottom_H = new Atom(new Point3D(), AtomOrGroup.Charge.NEUTRAL, bottom_carb);
    ch3a = new Methyl(new Point3D(), AtomOrGroup.Charge.NEUTRAL, bottom_carb);
    top_carb = new SP3Atom(new Point3D(), bottom_carb);
    top_carb.setInsideOutness(0.5);
    top_carb.setP0Divergence(1);
    top_H = new Atom(new Point3D(), AtomOrGroup.Charge.NEUTRAL, top_carb);
    ch3b = new Methyl(new Point3D(), AtomOrGroup.Charge.NEUTRAL, top_carb);
    cl = new SP3Atom(new Point3D(), new_H);  // theory test -M
    new_H = new Atom(new Point3D(0, 0, Atom.S_TO_SP3_BOND_LENGTH + Math.max(0, (0.8 - getT()))),
                     AtomOrGroup.Charge.MINUS, top_carb);
    setHydrogenLocations();
    
    // Create the Bonds
    bottom_carb_new_H = new Bond(bottom_carb, new_H, Bond.State.BROKEN);
    bottom_carb_H = new Bond(bottom_carb, bottom_H, Bond.State.FULL);
    top_carb_H = new Bond(top_carb, top_H, Bond.State.FULL);
    bottom_carb_ch3 = new Bond(bottom_carb, ch3a, Bond.State.FULL);
    top_carb_ch3 = new Bond(top_carb, ch3b, Bond.State.FULL);            // creating the top methyl group
    carb_carb = new Bond(bottom_carb, top_carb, Bond.State.DOUBLE);
    cl_new_H = new Bond(cl, new_H, Bond.State.FULL);  //theory
  }
  
  private void setHydrogenLocations() {
//    // Figure out the proper rotation for the hydrogens.  This depends on the inside-outness of the carbon.
    double rotation = -bottom_carb.getXRotation();

    // Top carbon on orbital 1
    Point3D orb1Vec = bottom_carb.getOrbitalVector(1).scale(Atom.SP3_SP3_BOND_LENGTH);
    top_carb.setLoc(orb1Vec.x(), orb1Vec.y(), orb1Vec.z());
    top_carb.setRot(180 + 2 * rotation, 0, 180);
    
    // Hydrogen on orbital 2 (orbital 3 on the top carbon)
    Point3D orb2Vec = bottom_carb.getOrbitalVector(2).scale(Atom.S_TO_SP3_BOND_LENGTH);
    bottom_H.setLoc(orb2Vec.x(), orb2Vec.y(), orb2Vec.z());
    Point3D orb3Vec = top_carb.getOrbitalVector(3).scale(Atom.S_TO_SP3_BOND_LENGTH);
    top_H.setLoc(orb3Vec.x(), orb3Vec.y(), orb3Vec.z());
    
    // Methyl on orbital 3 (orbital 2 on the top)
    orb3Vec = bottom_carb.getOrbitalVector(3).scale(Atom.SP3_SP3_BOND_LENGTH);
    ch3a.setLoc(orb3Vec.x(), orb3Vec.y(), orb3Vec.z());
    ch3a.setRot(0, 180 - rotation, 150); //(0, 180 - rotation, 90 - 120);
    
    // Action of top carbon and its methyl group is altogether more complex
    // First, the position
    orb2Vec = top_carb.getOrbitalVector(2).scale(Atom.SP3_SP3_BOND_LENGTH);
    // Located relative to the top carbon, so we just go down the orbital vector
    
    // [[FIX: WHY ISN'T THE ORBITAL VECTOR GIVING THE CORRECT POSITION 
    //   AT THE END?  (ANSWER: IT IS, BUT IN WORLD COORDINATES NOT RELATIVE
    //   COORDINATES.)]]
    ch3b.setLoc(orb2Vec.x(), orb2Vec.y(), orb2Vec.z());
    
    // Now the rotation, which is the tricky part.  There's no comparable code in Acyl,
    // because in Acyl the top atom is an oxygen, so there aren't any groups attached to
    // its upper orbitals
//    double orbRotX = top_carb.getXRotation();
//    double inout = top_carb.getInsideOutness();
//    double divergence = top_carb.getP0DivergenceAngle();
//    double rotX = top_carb.getRotX();
//    double rotY = top_carb.getRotY();
//    double rotZ = top_carb.getRotZ();
    
//    if ((super.getT() < 0.001) || (super.getT() > 0.999)) {
//      System.out.print("bottom carb: " + bottom_carb.getXRotation() + " "
//                         + bottom_carb.getInsideOutness() + " "
//                         + bottom_carb.getP0DivergenceAngle());
//      System.out.println(" rotation: " + bottom_carb.getRotX() + " "
//                           + bottom_carb.getRotY() + " " + bottom_carb.getRotZ());
//      System.out.print("top carb: " + orbRotX + " " + inout + " " + divergence);
//      System.out.println(" rotation: " + rotX + " " + rotY + " " + rotZ);
//      System.out.println("\ttop methyl rotation: " + 0 + " " + 
//                         (rotY - 180.0 + orbRotX) + " " + rotX);
//    }
    // This really needs a better way of working out chained rotations.
    // Y: rotX is really the rotation needed to hit the *orbital* in X, not the
    //    rotation applied to the whole *atom* in X.  -rotX works great at t = 0,
    //    but is thrown off for higher t-values by top_carb's own rotation.
    // Z: Similarly, while 30 is the correct *starting* value (the orbitals of the
    //    planar-configured atom are rotated up 120 = 30 deg beyond 90), neither 30
    //    nor 19.5 would be the correct *ending* value because of the rotation
    //    applied to top_carb's own frame of reference (specifically, top_carb's
    //    Y-axis has been rotated 2*19.5 deg = 39 deg into its Z).  Key to note here
    //    is that the progression in the Z rotation shouldn't be linear, because the
    //    vertical (in world space) movement of top_carb's orbital 2 isn't linear.  It's
    //    some funky trig thing, and I haven't yet figured it out.
    ch3b.setRot(0, 180 - rotation, 30);
    //ch3b.setRot(0, -270 + 2*orbRotX, -60 + orbRotX);
  }

  public ArrayList<Drawable> createDrawList(boolean twoD) {
    ArrayList<Drawable> result = new ArrayList<Drawable>();
    if (twoD) { // Add the bonds as well
      //result.add(new BondView(bottom_carb_new_H));
      result.add(new BondView(bottom_carb_H));
      result.add(new BondView(top_carb_H));
      result.add(new BondView(bottom_carb_ch3));
      result.add(new BondView(top_carb_ch3));
      result.add(new BondView(carb_carb));
      result.add(new BondView(cl_new_H));
    }

    // Draw the 3-D view back to front
    result.add(ch3a.createView());
    result.add(ch3b.createView());
    result.add(new_H.createView());
    result.add(bottom_carb.createView("C", AtomView.C_BLACK));
    result.add(top_carb.createView("C", AtomView.C_BLACK));
    result.add(bottom_H.createView());
    result.add(top_H.createView());
    result.add(cl.createView("Cl", AtomView.CL_GREEN)); // added but not in correct position
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
    
    // The new_H moves in
    new_H.setLoc(0, 0, (Atom.S_TO_SP3_BOND_LENGTH + Math.max(0, (0.8 - getT()))));
   // cl.setLoc(0, 0, );
    
    // Update the Bonds
    if (getT() < 0.3) {
      bottom_carb_new_H.setState(Bond.State.BROKEN);
      new_H.setCharge(AtomOrGroup.Charge.MINUS);
      cl_new_H.setState(Bond.State.FULL);
      top_carb.setCharge(AtomOrGroup.Charge.NEUTRAL);
      carb_carb.setState(Bond.State.DOUBLE);
      bottom_carb_ch3.setState(Bond.State.FULL);
      top_carb_ch3.setState(Bond.State.FULL);
    }
    else if (getT() > 0.5) {
      bottom_carb_new_H.setState(Bond.State.FULL);
      new_H.setCharge(AtomOrGroup.Charge.NEUTRAL);
      cl_new_H.setState(Bond.State.PARTIAL);
      top_carb.setCharge(AtomOrGroup.Charge.MINUS);
      carb_carb.setState(Bond.State.FULL);
      bottom_carb_ch3.setState(Bond.State.FULL);
      top_carb_ch3.setState(Bond.State.FULL);
    }
    else {
      bottom_carb_new_H.setState(Bond.State.PARTIAL);
      new_H.setCharge(AtomOrGroup.Charge.PART_MINUS);
      top_carb.setCharge(AtomOrGroup.Charge.PART_MINUS);
      carb_carb.setState(Bond.State.FULL_PARTIAL);
      bottom_carb_ch3.setState(Bond.State.FULL);
      top_carb_ch3.setState(Bond.State.FULL);
    }
  }
}
