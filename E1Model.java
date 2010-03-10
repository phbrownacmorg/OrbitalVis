import java.util.ArrayList;
import javax.media.opengl.*;

/**
 * The Model class for the E1 reaction
 */

public class E1Model extends Model {
  // OH attacks from the left or the right?
  private int zSign;
  
  // Atoms/groups
  private Atom nucleophile;
  private SP3Atom carb;
  private Ethyl ethyl;
  private Methyl ch3;
  private SP3Atom oxy;
  
  // bonds
  private Bond carb_nucleophile;
  private Bond carb_ethyl, carb_ch3, carb_oxy;

  public E1Model(int zSign) {
    this.zSign = zSign;
    
    nucleophile = new Atom(new Point3D(0, 0, zSign * (Atom.S_TO_SP3_BOND_LENGTH + Math.max(0, (0.8 - getT())))),
                           AtomOrGroup.Charge.MINUS);
    
    carb = new SP3Atom();
    carb.setInsideOutness(0.5);
    carb.setP0Divergence(1);
    carb.setRot(0, 0, 180);
    
    ethyl = new Ethyl(new Point3D(), AtomOrGroup.Charge.NEUTRAL);
    ch3 = new Methyl(new Point3D(), AtomOrGroup.Charge.NEUTRAL);
    oxy = new SP3Atom();
    oxy.setInsideOutness(0.5);
    oxy.setP0Divergence(1);
    setHydrogenLocations();
    
    // Create the Bonds
    carb_nucleophile = new Bond(carb, nucleophile, Bond.State.BROKEN);
    carb_ethyl = new Bond(carb, ethyl, Bond.State.FULL);
    carb_ch3 = new Bond(carb, ch3, Bond.State.FULL);
    carb_oxy = new Bond(carb, oxy, Bond.State.DOUBLE);
  }
  
  private void setHydrogenLocations() {
    // Figure out the proper rotation for the hydrogens.  This depends on the inside-outness of the carbon.
    double rotation = -109.5 * (1.0 - carb.getInsideOutness()) 
      + ((-180.0 + 109.5) * carb.getInsideOutness());
    Matrix rotX = Matrix.makeRotationMatrix(rotation, Matrix.Axis.X);
    Point3D hLoc = new Point3D(0, 0, Atom.SP3_SP3_BOND_LENGTH);
    Point3D hLoc1 = hLoc.transform(rotX);
    oxy.setLoc(hLoc1);
    oxy.setRot(180 + 2 * rotation, 0, 0);
    
    Matrix rotZ = Matrix.makeRotationMatrix(120, Matrix.Axis.Z);
    Point3D hLoc2 = hLoc1.transform(rotZ);
    ethyl.setLoc(hLoc2);
    //ethyl.setRot(180 + rotation, 0, 120);
    //ethyl.setRot(180 + rotation, 90, 120); // X left, Y up
    ethyl.setRot(0, 180 + rotation, -90 + 120); // (X up, Y right)
    
    rotZ =  Matrix.makeRotationMatrix(-120, Matrix.Axis.Z);
    Point3D hLoc3 = hLoc.transform(rotX);
    hLoc3 = hLoc3.transform(rotZ);
    ch3.setLoc(hLoc3);
    //ch3.setRot(180 + rotation, 90, -120); // 0,0,0 gives Y up, Z right.  0,0,-120 points Y away, X down.  Want to rot X into Y.
    ch3.setRot(0, 180 - rotation, 90 - 120);
  }

  public ArrayList<Drawable> createDrawList(boolean twoD) {
    ArrayList<Drawable> result = new ArrayList<Drawable>();
    if (twoD) { // Add the bonds as well
      result.add(new BondView(carb_nucleophile));
      result.add(new BondView(carb_ethyl));
      result.add(new BondView(carb_ch3));
      result.add(new BondView(carb_oxy));
    }
    result.add(ch3.createView());
    result.add(nucleophile.createView());
    result.add(carb.createView("C", AtomView.C_BLACK));
    result.add(oxy.createView("O", AtomView.O_RED));
    result.add(ethyl.createView());
    return result;
  }
  
  public void setT(double newT) {
    super.setT(newT);
    
    // Set the angles betwen the carbon's orbitals, and the proportion of its center orbital
    double insideOutnessOffset = -zSign * Math.max(0, Math.min(0.5, ((0.5/0.6) * (getT() - 0.3))));
    double insideOutness = 0.5 + insideOutnessOffset;
    //double insideOutness = Math.min(1.0, Math.max(0.5, ((0.5/0.6) * (getT() - 0.3)) + 0.5));
    double divergence = 1.0 - (4 * (insideOutness - 0.5) * (insideOutness - 0.5));
    carb.setInsideOutness(insideOutness);
    carb.setP0Divergence(divergence);
    oxy.setInsideOutness(insideOutness);
    oxy.setP0Divergence(divergence);
    
    // Set the corresponding locations of the hydrogens
    setHydrogenLocations();
    
    // The nucleophile moves in
    nucleophile.setLoc(0, 0, zSign * (Atom.S_TO_SP3_BOND_LENGTH + Math.max(0, (0.8 - getT()))));
    
    // Update the Bonds
    if (getT() < 0.3) {
      carb_nucleophile.setState(Bond.State.BROKEN);
      nucleophile.setCharge(AtomOrGroup.Charge.MINUS);
      oxy.setCharge(AtomOrGroup.Charge.NEUTRAL);
      carb_oxy.setState(Bond.State.DOUBLE);
    }
    else if (getT() > 0.5) {
      carb_nucleophile.setState(Bond.State.FULL);
      nucleophile.setCharge(AtomOrGroup.Charge.NEUTRAL);
      oxy.setCharge(AtomOrGroup.Charge.MINUS);
      carb_oxy.setState(Bond.State.FULL);
    }
    else {
      carb_nucleophile.setState(Bond.State.PARTIAL);
      nucleophile.setCharge(AtomOrGroup.Charge.PART_MINUS);
      oxy.setCharge(AtomOrGroup.Charge.PART_MINUS);
      carb_oxy.setState(Bond.State.FULL_PARTIAL);
    }
  }
}
