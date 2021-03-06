import java.util.ArrayList;
//import javax.media.opengl.*;

/**
 * The Model class for the E1 reaction
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

public class E1Model extends Model {
  public static final double MAX_WITHDRAWAL = 5.0 * Atom.SP3_SP3_BOND_LENGTH;
  
  private double carbon1ZRot = 109.5;
  private double hydro2InitialX = 0;
  private double oHClosestX = 0;

  // Atoms/groups
  private Hydroxide oh;
  private SP3Atom carb1, carb2;
  private SP3Atom chlor;
  private Methyl meth1, meth2, meth3;
  private Atom hydro1, hydro2;
    
  // bonds
  private Bond hydro2_oh;
  private Bond carb1_carb2;
  private Bond carb1_meth1, carb1_hydro1, carb2_chlor, carb2_meth3, carb2_meth2;
  private Bond hydro2_carb1;

  public E1Model() {
    
    carb1 = new SP3Atom(new Point3D(-Atom.BOND_LENGTH, 0, 0));
    carb1.setRot(0, 0, carbon1ZRot);  // 0 orbital interacts with the leaving H, and eventually forms the pi-bond.
    
    carb2 = new SP3Atom(new Point3D(Atom.BOND_LENGTH, 0, 0)); 
    carb2.setRot(0, 0, 180 + carbon1ZRot);  // 0 orbital interacts with the leaving Cl, and eventually forms the pi-bond.
    
    Point3D orb0Vec = carb1.getAbsOrbitalLoc(0, Atom.S_TO_SP3_BOND_LENGTH);
    hydro2 = new Atom(new Point3D(orb0Vec.x(), orb0Vec.y(), orb0Vec.z()));

    hydro1 = new Atom(new Point3D(), AtomOrGroup.Charge.NEUTRAL, carb1, -0.5, 0.3);
    meth3 = new Methyl(new Point3D(), AtomOrGroup.Charge.NEUTRAL, carb2, 0.5, -0.3);
    
    chlor = new SP3Atom(new Point3D(), carb2);
    
    meth1 = new Methyl(new Point3D(), AtomOrGroup.Charge.NEUTRAL, carb1, -0.2, -0.3);
    meth2 = new Methyl(new Point3D(), AtomOrGroup.Charge.NEUTRAL, carb2, 0.2, 0.3);
    
    hydro2InitialX = hydro2.getX();
    oHClosestX = hydro2InitialX - 1.5*(Atom.S_TO_SP3_BOND_LENGTH);
    oh = new Hydroxide(new Point3D(-1.25 * MAX_WITHDRAWAL, hydro2.getY(), 0), AtomOrGroup.Charge.MINUS);
            
    setHydrogenLocations();
    
    // Create the Bonds
    hydro2_oh = new Bond(hydro2, oh, Bond.State.BROKEN);
    carb1_carb2 = new Bond(carb2, carb1, Bond.State.FULL);
    carb1_meth1 = new Bond(carb1, meth1, Bond.State.FULL);
    carb1_hydro1 = new Bond(carb1, hydro1, Bond.State.FULL);
    hydro2_carb1 = new Bond(hydro2, carb1, Bond.State.FULL);
    carb2_chlor = new Bond(carb2, chlor, Bond.State.FULL);
    carb2_meth3 = new Bond(carb2, meth3, Bond.State.FULL);
    carb2_meth2 = new Bond(carb2, meth2, Bond.State.FULL);
  }
  
  private void setHydrogenLocations() {
    // Set the chlorine
    final double BOND_LENGTH_FUZZ_FACTOR = 1.05; // Arbitrary
    final double WITHDRAWAL_DISTANCE_FACTOR = 1; // Arbitrary
    double scalingFactor = Atom.SP3_SP3_BOND_LENGTH * BOND_LENGTH_FUZZ_FACTOR 
      + WITHDRAWAL_DISTANCE_FACTOR * Math.max(0, getT() - 0.1)
      + 5 * MAX_WITHDRAWAL * Math.max(0, getT() - 0.45);
    Point3D carb2orb0Vec = carb2.getOrbitalVector(0).scale(scalingFactor);
    chlor.setLoc(carb2orb0Vec.x(), carb2orb0Vec.y(), carb2orb0Vec.z());
    double rotation = -carb2.getZRotation();
      // -109.5 * (1.0 - carb1.getInsideOutness()) - ((180.0 - 109.5) * carb1.getInsideOutness());
    chlor.setRot(0, 180, 0);
    
    // Set the CH3 that's bonded to carb1 (on orbital 3)
    Point3D carb1orb3Vec = carb1.getOrbitalVector(3).scale(Atom.SP3_SP3_BOND_LENGTH);
    meth1.setLoc(carb1orb3Vec.x(), carb1orb3Vec.y(), carb1orb3Vec.z());
    meth1.setRot(60, 0, 180 -carb1.getZRotation()); //(rotX, rotY, 0);

    // Set the H that's on carb1's orbital 2
    Point3D carb1orb2Vec = carb1.getOrbitalVector(2).scale(Atom.S_TO_SP3_BOND_LENGTH);
    hydro1.setLoc(carb1orb2Vec.x(), carb1orb2Vec.y(), carb1orb2Vec.z());
    
    // Set the CH3 that's on carb2's orbital 2
    Point3D carb2orb2Vec = carb2.getOrbitalVector(2).scale(Atom.SP3_SP3_BOND_LENGTH);
    meth2.setLoc(carb2orb2Vec.x(), carb2orb2Vec.y(), carb2orb2Vec.z());
//    rotY = Math.toDegrees(Math.atan(carb2orb2Vec.x() / carb2orb2Vec.z()));
//    rotX = Math.toDegrees(Math.asin(carb2orb2Vec.y() / Atom.SP3_SP3_BOND_LENGTH));
    meth2.setRot(-60, 0, 180 + rotation); //(rotX, 180 + rotY, 0);
    
    // Set the H that's on carb2's orbital 3
    Point3D carb2orb3Vec = carb2.getOrbitalVector(3).scale(Atom.SP3_SP3_BOND_LENGTH);
    meth3.setLoc(carb2orb3Vec.x(), carb2orb3Vec.y(), carb2orb3Vec.z());
    meth3.setRot(60, 0, 180 + rotation);
  }

  public ArrayList<Drawable> createDrawList(boolean twoD) {
    ArrayList<Drawable> result = new ArrayList<Drawable>();
    if (twoD) { // Add the bonds as well
      result.add(new BondView(hydro2_oh));
      result.add(new BondView(carb1_carb2));
      result.add(new BondView(carb1_meth1));
      result.add(new BondView(carb1_hydro1));
      result.add(new BondView(hydro2_carb1));
      result.add(new BondView(carb2_chlor));
      result.add(new BondView(carb2_meth3));
      result.add(new BondView(carb2_meth2));
    }
    result.add(hydro1.createView());
    result.add(meth2.createView());
    result.add(chlor.createView("Cl", AtomView.CL_GREEN));
    result.add(carb2.createView("C", AtomView.C_BLACK));
    result.add(carb1.createView("C", AtomView.C_BLACK));
    result.add(hydro2.createView());
    result.add(oh.createView(HydroxideView.TEXT_HO));
    result.add(meth3.createView());
    result.add(meth1.createView());
    return result;
  }
  
  public void setT(double newT) {
    super.setT(newT);
    
    // Set the angles betwen the carbon's orbitals, and the proportion of its center orbital
    double carb1InsideOutness = Math.min(0.5, Math.max(0, (getT() - 0.55) * (0.5/0.4)));
    double carb2InsideOutness = Math.min(0.5, Math.max(0, (getT() - 0.05) * (0.5/0.4)));
    //double divergence = 2 * (Math.max((getT() - 0.5), 0)); //insideOutness; //Math.min(1.0, Math.max(0, (getT() - 0.05) * (1.0/0.9)));
    //double insideOutnessOffset =  Math.max(0, Math.min(0.5, ((0.5/0.6) * (getT() - 0.3))));
    //double insideOutness = 0.5 + insideOutnessOffset;
    //double insideOutness = Math.min(1.0, Math.max(0.5, ((0.5/0.6) * (getT() - 0.3)) + 0.5));
    //double divergence = 1.0 - (4 * (insideOutness - 0.5) * (insideOutness - 0.5));
    carb1.setInsideOutness(carb1InsideOutness);
    carb1.setP0Divergence(2 * carb1InsideOutness);
    carb2.setInsideOutness(carb2InsideOutness);
    carb2.setP0Divergence(2 * carb2InsideOutness);
    
    carbon1ZRot = -Math.max(-109.5, Math.min(0.4, (getT() - 0.55)) * (19.5/0.4) - 109.5);
    carb1.setRot(0, 0, carbon1ZRot);
    double carbon2ZRot = -Math.max(-109.5, Math.min(0.4, (getT() - 0.05)) * (19.5/0.4) - 109.5);
    carb2.setRot(0, 0, 180 + carbon2ZRot);
    
    // Set the corresponding locations of the substituents that rotate with the carbons as the carbons change shape
    setHydrogenLocations();
    
    // The oh moves in, hydro1 moves out
    //oh.setLoc(0, 0, (Atom.S_TO_SP3_BOND_LENGTH + Math.max(0, (0.8 - getT()))));
    
    if (getT() < 0.5) {
      // Do nothing
    }
    else if (getT() < 0.6) {
      oh.setLoc(((0.6 - getT())/0.1) * -1.25 * MAX_WITHDRAWAL + ((getT() - 0.5)/0.1) * oHClosestX, hydro2.getY(), 0);
      hydro2.setLoc(hydro2InitialX, hydro2.getY(), hydro2.getZ());
    }
    else if (getT() < 0.75) {
      oh.setLoc(oHClosestX + ((getT() - 0.6)/0.15) * SOrbitalView.RADIUS, hydro2.getY(), 0);
      hydro2.setLoc((((0.9 - getT())/0.3) * hydro2InitialX) 
              + ((getT() - 0.6)/0.3) * (Atom.S_TO_SP3_BOND_LENGTH + oHClosestX), hydro2.getY(),
                    hydro2.getZ());
    }
    else if (getT() < 0.9) {
      oh.setLoc(oHClosestX + ((0.9 - getT())/0.15) * SOrbitalView.RADIUS, hydro2.getY(), 0);
      hydro2.setLoc((((0.9 - getT())/0.3) * hydro2InitialX) 
              + ((getT() - 0.6)/0.3) * (Atom.S_TO_SP3_BOND_LENGTH + oHClosestX), hydro2.getY(),
              hydro2.getZ());
    }
    else {
      double hohX = ((getT() - 0.9)/0.1) * -(MAX_WITHDRAWAL/1.5) + ((1.0 - getT())/0.1) * oHClosestX;
      oh.setLoc(hohX, hydro2.getY(), 0);
      hydro2.setLoc(hohX + Atom.S_TO_SP3_BOND_LENGTH, hydro2.getY(), hydro2.getZ());
    }
    
    // Update the Bonds
    if (getT() < 0.12) { // Initial state
      hydro2_oh.setState(Bond.State.BROKEN);
      carb1_carb2.setState(Bond.State.FULL);
      hydro2_carb1.setState(Bond.State.FULL);
      carb2_chlor.setState(Bond.State.FULL);
      oh.setCharge(AtomOrGroup.Charge.MINUS);
      chlor.setCharge(AtomOrGroup.Charge.NEUTRAL);
      carb2.setCharge(AtomOrGroup.Charge.NEUTRAL);
    }
    else if (getT() < 0.46) { // Intermediate state 1
      hydro2_oh.setState(Bond.State.BROKEN);
      carb1_carb2.setState(Bond.State.FULL);
      hydro2_carb1.setState(Bond.State.FULL);
      carb2_chlor.setState(Bond.State.PARTIAL);
      oh.setCharge(AtomOrGroup.Charge.MINUS);
      chlor.setCharge(AtomOrGroup.Charge.PART_MINUS);
      carb2.setCharge(AtomOrGroup.Charge.PART_PLUS);
    }
    else if (getT() < 0.7) { // Intermediate state 2
      hydro2_oh.setState(Bond.State.BROKEN);
      carb1_carb2.setState(Bond.State.FULL);
      hydro2_carb1.setState(Bond.State.FULL);
      carb2_chlor.setState(Bond.State.BROKEN);
      oh.setCharge(AtomOrGroup.Charge.MINUS);
      chlor.setCharge(AtomOrGroup.Charge.MINUS);
      carb2.setCharge(AtomOrGroup.Charge.PLUS);
    }
    else if (getT() < 0.91) { // Intermediate state 3
      hydro2_oh.setState(Bond.State.PARTIAL);
      carb1_carb2.setState(Bond.State.FULL_PARTIAL);
      hydro2_carb1.setState(Bond.State.PARTIAL);
      carb2_chlor.setState(Bond.State.BROKEN);
      oh.setCharge(AtomOrGroup.Charge.PART_MINUS);
      chlor.setCharge(AtomOrGroup.Charge.MINUS);
      carb2.setCharge(AtomOrGroup.Charge.PART_PLUS);
    }
    else { // Final state
      hydro2_oh.setState(Bond.State.FULL);
      carb1_carb2.setState(Bond.State.DOUBLE);
      hydro2_carb1.setState(Bond.State.BROKEN);
      carb2_chlor.setState(Bond.State.BROKEN);
      oh.setCharge(AtomOrGroup.Charge.NEUTRAL);
      chlor.setCharge(AtomOrGroup.Charge.MINUS);
      carb2.setCharge(AtomOrGroup.Charge.NEUTRAL);
    }
  }
}
