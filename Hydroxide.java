public class Hydroxide extends AtomOrGroup {
  private Atom hydrogen;
  private SP3Atom oxygen;
  
  public Hydroxide(Point3D pt, AtomOrGroup.Charge charge) {
    super(pt, charge);
    
    Point3D hydroLoc = new Point3D(0, 0, Atom.BOND_LENGTH + SOrbitalView.RADIUS/2);
    Matrix rotMatrix = Matrix.makeRotationMatrix(109.5, Matrix.Axis.X);
    hydroLoc = hydroLoc.transform(rotMatrix);
    hydrogen = new Atom(hydroLoc);

    oxygen = new SP3Atom();
  }
  
  public HydroxideView createView(String text) {
    HydrogenView hydroView = (HydrogenView)(hydrogen.createView());
    SP3AtomView oxyView = oxygen.createView("", AtomView.O_RED);
    return new HydroxideView(this, hydroView, oxyView, text);
  }
}