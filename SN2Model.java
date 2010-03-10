import java.util.ArrayList;
import javax.media.opengl.*;

/**
 * The Model class for the SN2 reaction
 */

public class SN2Model extends Model {
  // Atoms/groups
  private Hydroxide oh;
  private SP3Atom carb;
  private Atom hydro1, hydro2, hydro3;
  private SP3Atom chlor;
  
  // bonds
  private Bond carb_oh;
  private Bond carb_hydro1, carb_hydro2, carb_hydro3;
  private Bond carb_chlor;

  public SN2Model(boolean markH) {
    oh = new Hydroxide(new Point3D(0, 0, -(2 * Atom.BOND_LENGTH + Math.max(0, (0.5 - getT())))),
                         AtomOrGroup.Charge.MINUS);
    
    carb = new SP3Atom();
    
    hydro1 = new Atom();
    hydro2 = new Atom();
    hydro3 = new Atom();
    setHydrogenLocations();
    
    chlor = new SP3Atom(new Point3D(0, 0, 2 * Atom.BOND_LENGTH + Math.max(0, getT() - 0.5))); 
    chlor.setRot(0, 180, 0);
    
    // Create the Bonds
    carb_oh = new Bond(carb, oh, Bond.State.BROKEN);
    carb_hydro1 = new Bond(carb, hydro1, Bond.State.FULL);
    carb_hydro2 = new Bond(carb, hydro2, Bond.State.FULL);
    carb_hydro3 = new Bond(carb, hydro3, Bond.State.FULL);
    carb_chlor = new Bond(carb, chlor, Bond.State.FULL);
  }
  
  private void setHydrogenLocations() {
    // Figure out the proper rotation for the hydrogens.  This depends on the inside-outness of the carbon.
    double rotation = 109.5 * (1.0 - carb.getInsideOutness()) + ((180.0 - 109.5) * carb.getInsideOutness());
    Matrix rotX = Matrix.makeRotationMatrix(rotation, Matrix.Axis.X);
    Point3D hLoc = new Point3D(0, 0, Atom.BOND_LENGTH + SOrbitalView.RADIUS/2);
    Point3D hLoc1 = hLoc.transform(rotX);
    hydro1.setLoc(hLoc1);
    
    Matrix rotZ = Matrix.makeRotationMatrix(120, Matrix.Axis.Z);
    Point3D hLoc2 = hLoc1.transform(rotZ);
    hydro2.setLoc(hLoc2);
    
    rotZ =  Matrix.makeRotationMatrix(-120, Matrix.Axis.Z);
    Point3D hLoc3 = hLoc1.transform(rotZ);
    hydro3.setLoc(hLoc3);   
  }

  public ArrayList<Drawable> createDrawList(boolean twoD) {
    ArrayList<Drawable> result = new ArrayList<Drawable>();
    if (twoD) { // Add the bonds as well
      result.add(new BondView(carb_oh));
      result.add(new BondView(carb_hydro1));
      result.add(new BondView(carb_hydro2));
      result.add(new BondView(carb_hydro3));
      result.add(new BondView(carb_chlor));
    }
    result.add(oh.createView(HydroxideView.TEXT_HO));
    result.add(carb.createView("C", AtomView.C_BLACK));
    result.add(hydro1.createView());
    result.add(hydro2.createView());
    result.add(hydro3.createView());
    result.add(chlor.createView("Cl", AtomView.CL_GREEN));
    return result;
  }
  
  public void setT(double newT) {
    super.setT(newT);
    
    // Set the angles betwen the carbon's orbitals, and the proportion of its center orbital
    carb.setInsideOutness(Math.min(1.0, Math.max(0, getT() - 0.2)/0.6));

    // Set the corresponding locations of the hydrogens
    setHydrogenLocations();
    
    // The hydroxyl moves in as t is in [0, 0.5]
    oh.setLoc(0, 0, -(2 * Atom.BOND_LENGTH + Math.max(0, (0.5 - getT()))));

    // The chlorine moves away as t goes 0.5 - 1
    chlor.setLoc(0, 0, 2 * Atom.BOND_LENGTH + Math.max(0, getT() - 0.5));
    
    // Update the Bonds
    if (getT() < 0.4) {
      carb_oh.setState(Bond.State.BROKEN);
      carb_chlor.setState(Bond.State.FULL);
      oh.setCharge(AtomOrGroup.Charge.MINUS);
      chlor.setCharge(AtomOrGroup.Charge.NEUTRAL);
    }
    else if (getT() > 0.6) {
      carb_oh.setState(Bond.State.FULL);
      carb_chlor.setState(Bond.State.BROKEN);
      chlor.setCharge(AtomOrGroup.Charge.MINUS);
      oh.setCharge(AtomOrGroup.Charge.NEUTRAL);
    }
    else {
      carb_oh.setState(Bond.State.PARTIAL);
      carb_chlor.setState(Bond.State.PARTIAL);
      chlor.setCharge(AtomOrGroup.Charge.PART_MINUS);
      oh.setCharge(AtomOrGroup.Charge.PART_MINUS);
    }
  }
}
