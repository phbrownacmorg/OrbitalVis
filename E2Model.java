import java.util.ArrayList;
//import javax.media.opengl.*;

/**
 * The Model class for the E2 reaction
 *
 * Copyright 2010 Peter Brown <phbrown@acm.org> and Madonna King
 *
 * This code is distributed under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the
 * license or (at your option) any later version.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

public class E2Model extends Model {
  public static final double MAX_WITHDRAWAL = 5.0 * Atom.SP3_SP3_BOND_LENGTH;
  
  private double carbonXRot = -109.5;
  private double hydro2InitialZ = 0;
  private double oHClosestZ = 0;

  // Atoms/groups
  private Hydroxide oh;
  private SP3Atom carb1, carb2;
  private SP3Atom chlor;
  private Methyl meth1, meth2;
  private Atom hydro1, hydro2, hydro3;
    
  // bonds
  private Bond hydro2_oh;
  private Bond carb1_carb2;
  private Bond carb1_meth1, carb1_hydro1, carb2_chlor, carb2_hydro3, carb2_meth2;
  private Bond carb1_hydro2;

  public E2Model() {
    
    carb1 = new SP3Atom(new Point3D(0, 0, -Atom.BOND_LENGTH));
    carb1.setRot(carbonXRot, 0, 0);  // 0 orbital interacts with the leaving H, and eventually forms the pi-bond.
    
    carb2 = new SP3Atom(new Point3D(0, 0, Atom.BOND_LENGTH)); 
    carb2.setRot(180 + carbonXRot, 0, 0);  // 0 orbital interacts with the leaving Cl, and eventually forms the pi-bond.
    
    oh = new Hydroxide(new Point3D(0, 0, -2*MAX_WITHDRAWAL), AtomOrGroup.Charge.MINUS);
            
    hydro1 = new Atom(new Point3D(), carb1);
    hydro2 = new Atom();
    hydro3 = new Atom(new Point3D(), carb2);
    
    chlor = new SP3Atom(new Point3D(), carb2);
    
    meth1 = new Methyl(new Point3D(), AtomOrGroup.Charge.NEUTRAL, carb1);
    meth2 = new Methyl(new Point3D(), AtomOrGroup.Charge.NEUTRAL, carb2);
    
    Point3D orb0Vec = carb1.getAbsOrbitalVector(0).scale(Atom.S_TO_SP3_BOND_LENGTH);
    hydro2.setLoc(carb1.getX() + orb0Vec.x(), carb1.getY() + orb0Vec.y(), carb1.getZ() + orb0Vec.z());
    hydro2InitialZ = hydro2.getZ();
    oHClosestZ = hydro2InitialZ - 1.5*(Atom.S_TO_SP3_BOND_LENGTH);

    setHydrogenLocations();
    
    // Create the Bonds
    hydro2_oh = new Bond(hydro2, oh, Bond.State.BROKEN);
    carb1_carb2 = new Bond(carb1, carb2, Bond.State.FULL);
    carb1_meth1 = new Bond(carb1, meth1, Bond.State.FULL);
    carb1_hydro1 = new Bond(carb1, hydro1, Bond.State.FULL);
    carb1_hydro2 = new Bond(carb1, hydro2, Bond.State.FULL);
    carb2_chlor = new Bond(carb2, chlor, Bond.State.FULL);
    carb2_hydro3 = new Bond(carb2, hydro3, Bond.State.FULL);
    carb2_meth2 = new Bond(carb2, meth2, Bond.State.FULL);
    
  }
  
  private void setHydrogenLocations() {
    // Figure out the proper rotation for the hydrogens.  This depends on the inside-outness of the carbon.
    double rotation = -carb1.getXRotation();
      // -109.5 * (1.0 - carb1.getInsideOutness()) - ((180.0 - 109.5) * carb1.getInsideOutness());
    
    // Set the chlorine
    final double BOND_LENGTH_FUZZ_FACTOR = 1.05; // Arbitrary
    final double WITHDRAWAL_DISTANCE_FACTOR = 2; // Arbitrary
    double scalingFactor = Atom.SP3_SP3_BOND_LENGTH * BOND_LENGTH_FUZZ_FACTOR 
      + WITHDRAWAL_DISTANCE_FACTOR * Math.max(0, getT() - 0.4)
      + 8 * Math.max(0, getT() - 0.6);
    Point3D carb2orb0Vec = carb2.getOrbitalVector(0).scale(scalingFactor);
    chlor.setLoc(carb2orb0Vec.x(), carb2orb0Vec.y(), carb2orb0Vec.z());
    chlor.setRot(180, 0, 0);
    
    // Set the CH3 that's bonded to carb1 (on orbital 3)
    Point3D carb1orb3Vec = carb1.getOrbitalVector(3).scale(Atom.SP3_SP3_BOND_LENGTH);
    meth1.setLoc(carb1orb3Vec.x(), carb1orb3Vec.y(), carb1orb3Vec.z());
//    double rotY = Math.toDegrees(Math.atan(carb1orb3Vec.x() / carb1orb3Vec.z()));
//    double rotX = Math.toDegrees(Math.asin(carb1orb3Vec.y() / Atom.SP3_SP3_BOND_LENGTH));
    meth1.setRot(0, 180 - carb1.getXRotation(), -30); //(rotX, rotY, 0);

    // Set the H that's on carb1's orbital 2
    Point3D carb1orb2Vec = carb1.getOrbitalVector(2).scale(Atom.S_TO_SP3_BOND_LENGTH);
    hydro1.setLoc(carb1orb2Vec.x(), carb1orb2Vec.y(), carb1orb2Vec.z());
    
    // Set the CH3 that's on carb2's orbital 2
    Point3D carb2orb2Vec = carb2.getOrbitalVector(2).scale(Atom.SP3_SP3_BOND_LENGTH);
    meth2.setLoc(carb2orb2Vec.x(), carb2orb2Vec.y(), carb2orb2Vec.z());
    //rotY = Math.toDegrees(Math.atan(carb2orb2Vec.x() / carb2orb2Vec.z()));
    //rotX = Math.toDegrees(Math.asin(carb2orb2Vec.y() / Atom.SP3_SP3_BOND_LENGTH));
    meth2.setRot(0, 180 - rotation, 30); //(rotX, 180 + rotY, 0);
    
    // Set the H that's on carb2's orbital 3
    Point3D carb2orb3Vec = carb2.getOrbitalVector(3).scale(Atom.S_TO_SP3_BOND_LENGTH);
    hydro3.setLoc(carb2orb3Vec.x(), carb2orb3Vec.y(), carb2orb3Vec.z());
  }

  public ArrayList<Drawable> createDrawList(boolean twoD) {
    ArrayList<Drawable> result = new ArrayList<Drawable>();
    if (twoD) { // Add the bonds as well
      result.add(new BondView(hydro2_oh));
      result.add(new BondView(carb1_carb2));
      result.add(new BondView(carb1_meth1));
      result.add(new BondView(carb1_hydro1));
      result.add(new BondView(carb1_hydro2));
      result.add(new BondView(carb2_chlor));
      result.add(new BondView(carb2_hydro3));
      result.add(new BondView(carb2_meth2));
    }
    result.add(meth1.createView());
    result.add(meth2.createView());
    result.add(oh.createView(HydroxideView.TEXT_HO));
    result.add(carb1.createView("C", AtomView.C_BLACK));
    result.add(carb2.createView("C", AtomView.C_BLACK));
    result.add(hydro1.createView());
    result.add(hydro2.createView());
    result.add(hydro3.createView());
    result.add(chlor.createView("Cl", AtomView.CL_GREEN));
    return result;
  }
  
  public void setT(double newT) {
    super.setT(newT);
    
    // Set the angles betwen the carbon's orbitals, and the proportion of its center orbital
    double insideOutness = Math.min(0.5, Math.max(0, (getT() - 0.05) * (0.5/0.9)));
    double divergence = 2 * insideOutness; //Math.min(1.0, Math.max(0, (getT() - 0.05) * (1.0/0.9)));
    //double insideOutnessOffset =  Math.max(0, Math.min(0.5, ((0.5/0.6) * (getT() - 0.3))));
    //double insideOutness = 0.5 + insideOutnessOffset;
    //double insideOutness = Math.min(1.0, Math.max(0.5, ((0.5/0.6) * (getT() - 0.3)) + 0.5));
    //double divergence = 1.0 - (4 * (insideOutness - 0.5) * (insideOutness - 0.5));
    carb1.setInsideOutness(insideOutness);
    carb1.setP0Divergence(divergence);
    carb2.setInsideOutness(insideOutness);
    carb2.setP0Divergence(divergence);
    
    carbonXRot = Math.max(-109.5, (getT() - 0.05) * (19.5/0.9) - 109.5);
    carb1.setRot(carbonXRot, 0, 0);
    carb2.setRot(180 + carbonXRot, 0, 0);
    
    // Set the corresponding locations of the substituents that rotate with the carbons as the carbons change shape
    setHydrogenLocations();
    
    // The oh moves in, hydro1 moves out
    //oh.setLoc(0, 0, (Atom.S_TO_SP3_BOND_LENGTH + Math.max(0, (0.8 - getT()))));
    
    if (getT() < 0.4) {
      oh.setLoc(0, hydro2.getY(), (1.0 - (getT()/0.4)) * -2*MAX_WITHDRAWAL + (getT()/0.4) * oHClosestZ);
      hydro2.setLoc(hydro2.getX(), hydro2.getY(), hydro2InitialZ);
    }
    else if (getT() < 0.5) {
      oh.setLoc(0, hydro2.getY(), oHClosestZ + ((getT() - 0.4)/0.1) * SOrbitalView.RADIUS);
      hydro2.setLoc(hydro2.getX(), hydro2.getY(),
                    (((0.6 - getT())/0.2) * hydro2InitialZ) 
                      + ((getT() - 0.4)/0.2) * (Atom.S_TO_SP3_BOND_LENGTH + oHClosestZ));
    }
    else if (getT() < 0.6) {
      oh.setLoc(0, hydro2.getY(), oHClosestZ + ((0.6 - getT())/0.1) * SOrbitalView.RADIUS);
      hydro2.setLoc(hydro2.getX(), hydro2.getY(),
                    (((0.6 - getT())/0.2) * hydro2InitialZ) 
                      + ((getT() - 0.4)/0.2) * (Atom.S_TO_SP3_BOND_LENGTH + oHClosestZ));
    }
    else {
      double hohZ = ((getT() - 0.6)/0.4) * -(MAX_WITHDRAWAL/2) + ((1.0 - getT())/0.4) * oHClosestZ;
      oh.setLoc(0, hydro2.getY(), hohZ);
      hydro2.setLoc(hydro2.getX(), hydro2.getY(), hohZ + Atom.S_TO_SP3_BOND_LENGTH);
    }
    
    // Update the Bonds
    if (getT() < 0.45) { // Initial state
      hydro2_oh.setState(Bond.State.BROKEN);
      carb1_carb2.setState(Bond.State.FULL);
      carb1_hydro2.setState(Bond.State.FULL);
      carb2_chlor.setState(Bond.State.FULL);
      oh.setCharge(AtomOrGroup.Charge.MINUS);
      chlor.setCharge(AtomOrGroup.Charge.NEUTRAL);
    }
    else if (getT() > 0.55) { // Final state
      hydro2_oh.setState(Bond.State.FULL);
      carb1_carb2.setState(Bond.State.DOUBLE);
      carb1_hydro2.setState(Bond.State.BROKEN);
      carb2_chlor.setState(Bond.State.BROKEN);
      oh.setCharge(AtomOrGroup.Charge.NEUTRAL);
      chlor.setCharge(AtomOrGroup.Charge.MINUS);
    }
    else { // Intermediate
      hydro2_oh.setState(Bond.State.PARTIAL);
      carb1_carb2.setState(Bond.State.FULL_PARTIAL);
      carb1_hydro2.setState(Bond.State.PARTIAL);
      carb2_chlor.setState(Bond.State.PARTIAL);
      oh.setCharge(AtomOrGroup.Charge.PART_MINUS);
      chlor.setCharge(AtomOrGroup.Charge.PART_MINUS);
    }
  }
}
