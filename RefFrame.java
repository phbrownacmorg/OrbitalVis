import java.awt.geom.Point2D;

/**
 * Represents a frame of reference (coordinate system), which may be nested within a larger frame.
 * It is mutable.
 */
public class RefFrame {
  private Point3D pt;
  private double rx;
  private double ry;
  private double rz;
  private Point2D pt2d;
  
  public RefFrame() {
    updatePt(new Point3D());
  }
  
  public RefFrame(Point3D pt3d) {
    updatePt(pt3d);
  }
  
  public RefFrame(double x, double y, double z) {
    updatePt(new Point3D(x, y, z));
  }
  
  /**
   * Update both the Point3D and Point2D members, which need to be kept in sync.
   * 
   * Because this is used in the constructors, it is important that this method
   * not be virtual (that is, that it not be not protected or public).  If it were virtual,
   * it could be overridden by a subclass.  Since it's called by this class's
   * constructors, it gets called at a time when a subclass object might not yet be
   * fully initialized as an instance of the subclass.
   */
  private void updatePt(Point3D newPt) {
    pt = newPt;
    pt2d = View2D.to2D(pt);
  }
  
  public double getX()                                      { return pt.x(); }
  public double getY()                                      { return pt.y(); }
  public double getZ()                                      { return pt.z(); }
  public void setLoc(double newX, double newY, double newZ) { updatePt(new Point3D(newX, newY, newZ)); }
  public void setLoc(Point3D newPt)                         { updatePt(newPt); }
  
  public double getRotX() { return rx; }
  public double getRotY() { return ry; }
  public double getRotZ() { return rz; }
  public void setRot(double newRX, double newRY, double newRZ) {
    rx = newRX;
    ry = newRY;
    rz = newRZ;
  }
  
  public Point2D getPt2D() { return pt2d; }
  
  public double distanceTo(RefFrame f) {
    return pt.distanceTo(f.pt);
  }
}