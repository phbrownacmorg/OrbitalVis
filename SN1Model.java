import java.util.ArrayList;
import javax.media.opengl.*;

/**
 * The Model class for the SN1 reaction
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

public class SN1Model extends Model {
  /** 
   * Distance from the bonded position of the entering/leaving group to its
   * position outside the viewing frustum
   */
  public static final double MAX_WITHDRAWAL = 10.0 * Atom.SP3_SP3_BOND_LENGTH;
  
  // OH attacks from the left or the right?
  private int zSign;

  // Atoms/groups
  private Water water;
  private SP3Atom carb;
  private Ethyl ethyl;
  private Methyl ch3;
  private Atom hydro;
  private SP3Atom chlor;
  
  // bonds
  private Bond carb_water;
  private Bond carb_hydro, carb_ethyl, carb_methyl;
  private Bond carb_chlor;

  public SN1Model(int zSign) {
    this.zSign = zSign;
    water = new Water(new Point3D(0, 0,  zSign * (Atom.SP3_SP3_BOND_LENGTH + (1.0 - getT()) * MAX_WITHDRAWAL)), 
                       AtomOrGroup.Charge.NEUTRAL);
                       // zSign * (2 * Atom.BOND_LENGTH + Math.max(0, (0.5 - getT())))),
    if (zSign > 0) {
      water.setRot(0, 180, 0);
    }
    
    carb = new SP3Atom();
    
    hydro = new Atom();
    ethyl = new Ethyl(new Point3D(), AtomOrGroup.Charge.NEUTRAL);
    ch3 = new Methyl(new Point3D(), AtomOrGroup.Charge.NEUTRAL);
    setHydrogenLocations();
    
    chlor = new SP3Atom(new Point3D(0, 0, Atom.SP3_SP3_BOND_LENGTH + getT() * MAX_WITHDRAWAL)); 
    chlor.setRot(0, 180, 0);
    
    // Create the Bonds
    carb_water = new Bond(carb, water, Bond.State.BROKEN);
    carb_hydro = new Bond(carb, hydro, Bond.State.FULL);
    carb_ethyl = new Bond(carb, ethyl, Bond.State.FULL);
    carb_methyl = new Bond(carb, ch3, Bond.State.FULL);
    carb_chlor = new Bond(carb, chlor, Bond.State.FULL);
  }
  
  private void setHydrogenLocations() {
    double rotation = carb.getXRotation();
    
    // Hang the ethyl on orbital 1
    Point3D orb1Vec = carb.getOrbitalVector(1).scale(Atom.SP3_SP3_BOND_LENGTH);
    ethyl.setLoc(carb.getX() + orb1Vec.x(), carb.getY() + orb1Vec.y(), carb.getZ() + orb1Vec.z());
    ethyl.setRot(180 + rotation, 0, 0);

    // Set methyl on orbital 2
    Point3D orb2Vec = carb.getOrbitalVector(2).scale(Atom.SP3_SP3_BOND_LENGTH);
    ch3.setLoc(carb.getX() + orb2Vec.x(), carb.getY() + orb2Vec.y(), carb.getZ() + orb2Vec.z());
    ch3.setRot(0, 180 + rotation, -90 + 120); // (X up, Y right)
    
    // Hydrogen on orbital 3
    Point3D orb3Vec = carb.getOrbitalVector(3).scale(Atom.S_TO_SP3_BOND_LENGTH);
    hydro.setLoc(carb.getX() + orb3Vec.x(), carb.getY() + orb3Vec.y(), carb.getZ() + orb3Vec.z());
  }

  public ArrayList<Drawable> createDrawList(boolean twoD) {
    ArrayList<Drawable> result = new ArrayList<Drawable>();
    if (twoD) { // Add the bonds as well
      result.add(new BondView(carb_water));
      result.add(new BondView(carb_hydro));
      result.add(new BondView(carb_ethyl));
      result.add(new BondView(carb_methyl));
      result.add(new BondView(carb_chlor));
    }
    if (zSign < 0) {
      result.add(water.createView(WaterView.TEXT_H2O));
    }
    else {
      result.add(water.createView(WaterView.TEXT_OH2));
    }
    result.add(carb.createView("C", AtomView.C_BLACK));
    result.add(hydro.createView());
    result.add(ch3.createView());
    result.add(chlor.createView("Cl", AtomView.CL_GREEN));
    result.add(ethyl.createView());
    return result;
  }
  
  public void setT(double newT) {
    super.setT(newT);
    
    // WHAT HAPPENS TO THE HYBRIDIZATION OF THE ORBITALS AS THE REACTION RUNS?
    
    // Set the angles betwen the carbon's orbitals, and the proportion of its center orbital
    //carb.setInsideOutness(Math.min(1.0, Math.max(0, getT() - 0.2)/0.6));
    if (getT() < 0.35) {
      carb.setInsideOutness(Math.max(0, (0.5/0.3) * (getT() - 0.05)));
    }
    else if (getT() < 0.65) {
      carb.setInsideOutness(0.5);
    }
    else {
      carb.setInsideOutness(Math.max(0, Math.min(1.0, -zSign * (0.5/0.3) * (getT() - 0.65) + 0.5)));
    }
    
    // Set the corresponding locations of the hydrogens
    setHydrogenLocations();
    
    // The chlorine moves away, breaking its bond at t=0.35 // t=0.4
    if (getT() < 0.35) {
      chlor.setLoc(0, 0, Atom.SP3_SP3_BOND_LENGTH + Math.max(0, getT() - 0.05) * (0.1/0.3));
    }
    else {
      chlor.setLoc(0, 0, Atom.SP3_SP3_BOND_LENGTH + 0.1 + Math.pow(Math.max(0, getT() - 0.35) * (MAX_WITHDRAWAL/0.65), 2));
    }
    
    // The hydroxyl moves in, forming its bond at t=0.65 //t=0.6
    //water.setLoc(0, 0, zSign * (2 * Atom.BOND_LENGTH + Math.max(0, (0.5 - getT()))));
    if (getT() > 0.65) {
      water.setLoc(0, 0, zSign * (Atom.SP3_SP3_BOND_LENGTH + Math.max(0, (0.95 - getT())) * (0.1/0.3)));
    }
    else {
      water.setLoc(0, 0, zSign * (Atom.SP3_SP3_BOND_LENGTH + 0.1 + Math.pow(Math.max(0, (0.65 - getT())) * (MAX_WITHDRAWAL/0.65), 2)));
    }
    
    // Update the Bonds
    // PROBABLY NEEDS FIXING; BONDS FORM AND BREAK AT DIFFERENT VALUES OF T
    // Initial state
    if (getT() < 0.06) {
      carb_water.setState(Bond.State.BROKEN);
      carb_chlor.setState(Bond.State.FULL);
      water.setCharge(AtomOrGroup.Charge.NEUTRAL);
      chlor.setCharge(AtomOrGroup.Charge.NEUTRAL);
      carb.setCharge(AtomOrGroup.Charge.NEUTRAL);
    }
    // Final state
    else if (getT() > 0.94) {
      carb_water.setState(Bond.State.FULL);
      carb_chlor.setState(Bond.State.BROKEN);
      chlor.setCharge(AtomOrGroup.Charge.MINUS);
      water.setCharge(AtomOrGroup.Charge.PLUS);
      carb.setCharge(AtomOrGroup.Charge.NEUTRAL);
    }
    // Early intermediate state
    else if (getT() < 0.36) {
      carb_water.setState(Bond.State.BROKEN);
      carb_chlor.setState(Bond.State.PARTIAL);
      water.setCharge(AtomOrGroup.Charge.NEUTRAL);
      chlor.setCharge(AtomOrGroup.Charge.PART_MINUS);
      carb.setCharge(AtomOrGroup.Charge.PART_PLUS);
    }
    // Late intermediate state
    else if (getT() > 0.64) {
      carb_water.setState(Bond.State.PARTIAL);
      carb_chlor.setState(Bond.State.BROKEN);
      water.setCharge(AtomOrGroup.Charge.PART_PLUS);
      chlor.setCharge(AtomOrGroup.Charge.MINUS);
      carb.setCharge(AtomOrGroup.Charge.PART_PLUS);
    }
    // Middle intermediate state
    else {
      carb_water.setState(Bond.State.BROKEN);
      carb_chlor.setState(Bond.State.BROKEN);
      carb.setCharge(AtomOrGroup.Charge.PLUS);
      chlor.setCharge(AtomOrGroup.Charge.MINUS);
      water.setCharge(AtomOrGroup.Charge.NEUTRAL);
    }
  }
}
