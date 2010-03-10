import java.util.ArrayList;
import javax.media.opengl.*;

/**
 * The Model class for the SN1 reaction
 */

public class SN1Model extends Model {
  public static final double MAX_WITHDRAWAL = 10.0 * Atom.SP3_SP3_BOND_LENGTH; // Distance from the bonded position of the entering/leaving group 
                                                                              // to its position outside the viewing frustum
  
  // OH attacks from the left or the right?
  private int zSign;

  // Atoms/groups
  private Hydroxide oh;
  private SP3Atom carb;
  private Ethyl ethyl;
  private Methyl ch3;
  private Atom hydro;
  private SP3Atom chlor;
  
  // bonds
  private Bond carb_oh;
  private Bond carb_hydro, carb_ethyl, carb_methyl;
  private Bond carb_chlor;

  public SN1Model(int zSign) {
    this.zSign = zSign;
    oh = new Hydroxide(new Point3D(0, 0,  zSign * (Atom.SP3_SP3_BOND_LENGTH + (1.0 - getT()) * MAX_WITHDRAWAL)), 
                       AtomOrGroup.Charge.MINUS);
                       // zSign * (2 * Atom.BOND_LENGTH + Math.max(0, (0.5 - getT())))),
    if (zSign > 0) {
      oh.setRot(0, 180, 0);
    }
    
    carb = new SP3Atom();
    
    hydro = new Atom();
    ethyl = new Ethyl(new Point3D(), AtomOrGroup.Charge.NEUTRAL);
    ch3 = new Methyl(new Point3D(), AtomOrGroup.Charge.NEUTRAL);
    setHydrogenLocations();
    
    chlor = new SP3Atom(new Point3D(0, 0, Atom.SP3_SP3_BOND_LENGTH + getT() * MAX_WITHDRAWAL)); 
    chlor.setRot(0, 180, 0);
    
    // Create the Bonds
    carb_oh = new Bond(carb, oh, Bond.State.BROKEN);
    carb_hydro = new Bond(carb, hydro, Bond.State.FULL);
    carb_ethyl = new Bond(carb, ethyl, Bond.State.FULL);
    carb_methyl = new Bond(carb, ch3, Bond.State.FULL);
    carb_chlor = new Bond(carb, chlor, Bond.State.FULL);
  }
  
  private void setHydrogenLocations() {
    // Figure out the proper rotation for the hydrogens.  This depends on the inside-outness of the carbon.
    double rotation = 109.5 * (1.0 - carb.getInsideOutness()) + ((180.0 - 109.5) * carb.getInsideOutness());
    Matrix rotX = Matrix.makeRotationMatrix(rotation, Matrix.Axis.X);
    Point3D hLoc = new Point3D(0, 0, 2 * Atom.BOND_LENGTH);
    Point3D hLoc1 = hLoc.transform(rotX);
    ethyl.setLoc(hLoc1);
    ethyl.setRot(180 + rotation, 0, 0);

    Matrix rotZ = Matrix.makeRotationMatrix(120, Matrix.Axis.Z);
    Point3D hLoc2 = hLoc1.transform(rotZ);
    ch3.setLoc(hLoc2);
    ch3.setRot(0, 180 + rotation, -90 + 120); // (X up, Y right)
    
    rotZ =  Matrix.makeRotationMatrix(-120, Matrix.Axis.Z);
    Point3D hLoc3 = new Point3D(0, 0, Atom.BOND_LENGTH + SOrbitalView.RADIUS/2);
    hLoc3 = hLoc3.transform(rotX);
    hLoc3 = hLoc3.transform(rotZ);
    hydro.setLoc(hLoc3);   
  }

  public ArrayList<Drawable> createDrawList(boolean twoD) {
    ArrayList<Drawable> result = new ArrayList<Drawable>();
    if (twoD) { // Add the bonds as well
      result.add(new BondView(carb_oh));
      result.add(new BondView(carb_hydro));
      result.add(new BondView(carb_ethyl));
      result.add(new BondView(carb_methyl));
      result.add(new BondView(carb_chlor));
    }
    if (zSign < 0) {
      result.add(oh.createView(HydroxideView.TEXT_HO));
    }
    else {
      result.add(oh.createView(HydroxideView.TEXT_OH));
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
    //oh.setLoc(0, 0, zSign * (2 * Atom.BOND_LENGTH + Math.max(0, (0.5 - getT()))));
    if (getT() > 0.65) {
      oh.setLoc(0, 0, zSign * (Atom.SP3_SP3_BOND_LENGTH + Math.max(0, (0.95 - getT())) * (0.1/0.3)));
    }
    else {
      oh.setLoc(0, 0, zSign * (Atom.SP3_SP3_BOND_LENGTH + 0.1 + Math.pow(Math.max(0, (0.65 - getT())) * (MAX_WITHDRAWAL/0.65), 2)));
    }
    
    // Update the Bonds
    // PROBABLY NEEDS FIXING; BONDS FORM AND BREAK AT DIFFERENT VALUES OF T
    // Initial state
    if (getT() < 0.06) {
      carb_oh.setState(Bond.State.BROKEN);
      carb_chlor.setState(Bond.State.FULL);
      oh.setCharge(AtomOrGroup.Charge.MINUS);
      chlor.setCharge(AtomOrGroup.Charge.NEUTRAL);
      carb.setCharge(AtomOrGroup.Charge.NEUTRAL);
    }
    // Final state
    else if (getT() > 0.94) {
      carb_oh.setState(Bond.State.FULL);
      carb_chlor.setState(Bond.State.BROKEN);
      chlor.setCharge(AtomOrGroup.Charge.MINUS);
      oh.setCharge(AtomOrGroup.Charge.NEUTRAL);
      carb.setCharge(AtomOrGroup.Charge.NEUTRAL);
    }
    // Early intermediate state
    else if (getT() < 0.36) {
      carb_oh.setState(Bond.State.BROKEN);
      carb_chlor.setState(Bond.State.PARTIAL);
      oh.setCharge(AtomOrGroup.Charge.MINUS);
      chlor.setCharge(AtomOrGroup.Charge.PART_MINUS);
      carb.setCharge(AtomOrGroup.Charge.PART_PLUS);
    }
    // Late intermediate state
    else if (getT() > 0.64) {
      carb_oh.setState(Bond.State.PARTIAL);
      carb_chlor.setState(Bond.State.BROKEN);
      oh.setCharge(AtomOrGroup.Charge.PART_MINUS);
      chlor.setCharge(AtomOrGroup.Charge.MINUS);
      carb.setCharge(AtomOrGroup.Charge.PART_PLUS);
    }
    // Middle intermediate state
    else {
      carb_oh.setState(Bond.State.BROKEN);
      carb_chlor.setState(Bond.State.BROKEN);
      carb.setCharge(AtomOrGroup.Charge.PLUS);
      chlor.setCharge(AtomOrGroup.Charge.MINUS);
      oh.setCharge(AtomOrGroup.Charge.MINUS);
    }
  }
}
