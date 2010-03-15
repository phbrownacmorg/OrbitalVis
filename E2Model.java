import java.util.ArrayList;
import javax.media.opengl.*;

/**
 * The Model class for the E2 reaction
 * Madonna King
 */

public class E2Model extends Model {
  public static final double MAX_WITHDRAWAL = 5.0 * Atom.SP3_SP3_BOND_LENGTH;
  
  private double carbonXRot = -109.5;

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
    
   // oh = new Hydroxide(new Point3D(0, 0, -(2 * Atom.BOND_LENGTH + Math.max(0, (0.5 - getT())))),
                     //    AtomOrGroup.Charge.MINUS);
    
    oh = new Hydroxide(new Point3D(0, 0, -(2*Atom.SP3_SP3_BOND_LENGTH + (1.0 - getT()) * MAX_WITHDRAWAL)), 
                     AtomOrGroup.Charge.MINUS);
    
        
    hydro1 = new Atom();
    hydro2 = new Atom();
    hydro3 = new Atom();
    
    chlor = new SP3Atom(new Point3D(0, 0, 2 * Atom.BOND_LENGTH + Math.max(0, getT() - 0.5))); 
    //chlor.setRot(0, 180, 0);
    
    
    meth1 = new Methyl(new Point3D(), AtomOrGroup.Charge.NEUTRAL);
    meth2 = new Methyl(new Point3D(), AtomOrGroup.Charge.NEUTRAL);
    
    setHydrogenLocations();
    
    // Create the Bonds
    //hydro2_oh = new Bond(hydro2, oh, Bond.State.BROKEN);
    carb1_carb2 = new Bond(carb1, carb2, Bond.State.FULL);
   // carb1_meth1 = new Bond(carb1, meth1, Bond.State.FULL);
    carb1_hydro1 = new Bond(carb1, hydro1, Bond.State.FULL);
    carb1_hydro2 = new Bond(carb1, hydro2, Bond.State.FULL);
    //carb2_chlor = new Bond(carb2, chlor, Bond.State.FULL);
    carb2_hydro3 = new Bond(carb2, hydro3, Bond.State.FULL);
    //carb2_meth2 = new Bond(carb2, meth2, Bond.State.FULL);
    
   
  }
  
  private void setHydrogenLocations() {
    // Figure out the proper rotation for the hydrogens.  This depends on the inside-outness of the carbon.
    double rotation = -109.5 * (1.0 - carb1.getInsideOutness()) - ((180.0 - 109.5) * carb1.getInsideOutness());
    Matrix rotX = Matrix.makeRotationMatrix(rotation, Matrix.Axis.X);
    Point3D hLoc = new Point3D(0, 0, Atom.S_TO_SP3_BOND_LENGTH);
    Point3D hLoc1 = hLoc.transform(rotX);
    hLoc1 = new Point3D(hLoc1.x(), hLoc1.y(), hLoc1.z() - Atom.BOND_LENGTH);
    hydro1.setLoc(hLoc1);
    
    Matrix rot180LessX = Matrix.makeRotationMatrix(-180.0 + rotation, Matrix.Axis.X);
    final double BOND_LENGTH_FUZZ_FACTOR = 1.05; // Arbitrary
    final double WITHDRAWAL_DISTANCE_FACTOR = 2; // Arbitrary
    Point3D hLocClPreRot = new Point3D(0, 0, Atom.SP3_SP3_BOND_LENGTH * BOND_LENGTH_FUZZ_FACTOR 
                                         + WITHDRAWAL_DISTANCE_FACTOR * Math.max(0, getT() - 0.4)
                                         + 8 * Math.max(0, getT() - 0.6));
    Point3D hLocCl = hLocClPreRot.transform(rot180LessX);
    chlor.setLoc(hLocCl);
    chlor.setRot(rotation, 0, 0);
    chlor.setLoc(chlor.getX(), chlor.getY(), chlor.getZ() + Atom.BOND_LENGTH);
    
   // meth1.setLoc(hLoc1);
   // meth1.setRot(180 + rotation, 0, 0);

    Matrix rotZ = Matrix.makeRotationMatrix(120, Matrix.Axis.Z);
    Point3D hLoc2 = hLoc1.transform(rotZ);
    //ch3.setLoc(hLoc2);
    //ch3.setRot(0, 180 + rotation, -90 + 120); // (X up, Y right)
    
    rotZ =  Matrix.makeRotationMatrix(-120, Matrix.Axis.Z);
    Point3D hLoc3 = new Point3D(0, 0, Atom.BOND_LENGTH + SOrbitalView.RADIUS/2);
    hLoc3 = hLoc3.transform(rotX);
    hLoc3 = hLoc3.transform(rotZ);
    //hydro.setLoc(hLoc3);   
  }

  public ArrayList<Drawable> createDrawList(boolean twoD) {
    ArrayList<Drawable> result = new ArrayList<Drawable>();
    if (twoD) { // Add the bonds as well
      //result.add(new BondView(hydro2_oh));
      result.add(new BondView(carb1_carb2));
//      result.add(new BondView(carb1_meth1));
//      result.add(new BondView(carb1_hydro1));
      result.add(new BondView(carb1_hydro2));
//      result.add(new BondView(carb2_chlor));
//      result.add(new BondView(carb2_hydro3));
//      result.add(new BondView(carb2_meth2));
    }
 //   result.add(meth1.createView());
//    result.add(meth2.createView());
//    result.add(oh.createView(HydroxideView.TEXT_HO));
    result.add(carb1.createView("C", AtomView.C_BLACK));
    result.add(carb2.createView("C", AtomView.C_BLACK));
    result.add(hydro1.createView());
    //result.add(hydro2.createView());
    //result.add(hydro3.createView());
    result.add(chlor.createView("Cl", AtomView.CL_GREEN));
    return result;
  }
  
  public void setT(double newT) {
    super.setT(newT);
    
    // Set the angles betwen the carbon's orbitals, and the proportion of its center orbital
    double insideOutness = Math.min(1.0, Math.max(0, (getT() - 0.05) * (0.5/0.9)));
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
    
    // Set the corresponding locations of the hydrogens
    setHydrogenLocations();
    
    // The oh moves in
    //oh.setLoc(0, 0,  (Atom.S_TO_SP3_BOND_LENGTH + Math.max(0, (0.8 - getT()))));
    //oh.setLoc(0, 0,  (Atom.SP3_SP3_BOND_LENGTH + (1.0 - getT()) * MAX_WITHDRAWAL));
    
    // Update the Bonds
//    if (getT() < 0.3) {
//      hydro2_oh = new Bond(hydro2, oh, Bond.State.BROKEN);
//      carb1_carb2 = new Bond(carb1, carb2, Bond.State.FULL);
//      carb1_meth1 = new Bond(carb1, meth1, Bond.State.FULL);
//      carb1_hydro1 = new Bond(carb1, hydro1, Bond.State.FULL);
//      carb1_hydro2 = new Bond(carb1, hydro2, Bond.State.FULL);
//      carb2_chlor = new Bond(carb2, chlor, Bond.State.FULL);
//      carb2_hydro3 = new Bond(carb2, hydro3, Bond.State.FULL);
//      carb2_meth2 = new Bond(carb2, meth2, Bond.State.FULL);
//    }
//    else if (getT() > 0.5) {
//      hydro2_oh = new Bond(hydro2, oh, Bond.State.BROKEN);
//      carb1_carb2 = new Bond(carb1, carb2, Bond.State.FULL);
//      carb1_meth1 = new Bond(carb1, meth1, Bond.State.FULL);
//      carb1_hydro1 = new Bond(carb1, hydro1, Bond.State.FULL);
//      carb1_hydro2 = new Bond(carb1, hydro2, Bond.State.FULL);
//      carb2_chlor = new Bond(carb2, chlor, Bond.State.FULL);
//      carb2_hydro3 = new Bond(carb2, hydro3, Bond.State.FULL);
//      carb2_meth2 = new Bond(carb2, meth2, Bond.State.FULL);
//    }
//    else {
//      hydro2_oh = new Bond(hydro2, oh, Bond.State.BROKEN);
//      carb1_carb2 = new Bond(carb1, carb2, Bond.State.FULL);
//      carb1_meth1 = new Bond(carb1, meth1, Bond.State.FULL);
//      carb1_hydro1 = new Bond(carb1, hydro1, Bond.State.FULL);
//      carb1_hydro2 = new Bond(carb1, hydro2, Bond.State.FULL);
//      carb2_chlor = new Bond(carb2, chlor, Bond.State.FULL);
//      carb2_hydro3 = new Bond(carb2, hydro3, Bond.State.FULL);
//      carb2_meth2 = new Bond(carb2, meth2, Bond.State.FULL);
//    }
  }
}
