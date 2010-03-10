import javax.media.opengl.*;

/**
 * A class to represent a atom with SP3 hybridization
 */

public class SP3Atom extends Atom {
  private POrbital porb[] = new POrbital[4];
  public double insideOutness; // In range [0, 1]
  
  public SP3Atom(Point3D pt) {
    super(pt);
    insideOutness = 0;
    porb[0] = new POrbital(0, 0, 0);
    porb[1] = new POrbital(109.5, 0, 0);
    porb[2] = new POrbital(109.5, 0, 120);
    porb[3] = new POrbital(109.5, 0, -120);
  }

  public SP3Atom() {
    this(new Point3D());
  }
  
  public void setInsideOutness(double d) {
    insideOutness = d;
    porb[0].setProportion(POrbital.SP3_PROP * (1.0 - d) + (1.0 - POrbital.SP3_PROP) * d);
    double rotation = 109.5*(1.0 - insideOutness) + ((180.0 - 109.5) * insideOutness);
    porb[1].setRot(rotation, 0, 0);
    porb[2].setRot(rotation, 0, 120);
    porb[3].setRot(rotation, 0, -120);
  }
  
  public double getInsideOutness() {
    return insideOutness;
  }
  
  public void setP0Divergence(double d) {
    porb[0].setDivergence(d);
  }
  
  public SP3AtomView createView(String text, java.awt.Color color) {
    POrbitalView orbViews[] = new POrbitalView[4];
    for (int i = 0; i < 4; i++) {
      orbViews[i] = new POrbitalView(porb[i]);
    }
    return new SP3AtomView(this, text, color, orbViews);
  }
}
