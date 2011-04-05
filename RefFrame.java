import java.awt.geom.Point2D;

/**
 * Represents a frame of reference (coordinate system), which may be nested
 * within a larger frame. It is mutable.
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
public class RefFrame {
  private Point3D pt;
  private double rx;
  private double ry;
  private double rz;
  private Point2D pt2d;
  private RefFrame parent;  // Frame within which this one is nested
  
  public RefFrame() {
    updatePt(new Point3D());
    this.parent = null;
  }
  
  public RefFrame(RefFrame parent) {
    updatePt(new Point3D());
    this.parent = parent;
  }
  
  public RefFrame(Point3D pt3d) {
    updatePt(pt3d);
    this.parent = null;
  }
  
  public RefFrame(Point3D pt3d, RefFrame parent) {
    updatePt(pt3d);
    this.parent = parent;
  }
  
  public RefFrame(double x, double y, double z) {
    updatePt(new Point3D(x, y, z));
    this.parent = null;
  }
  
  public RefFrame(double x, double y, double z, RefFrame parent) {
    updatePt(new Point3D(x, y, z));
    this.parent = parent;
  }
  
  /**
   * Update both the Point3D and Point2D members, which need to be kept in sync.
   * 
   * Because this is used in the constructors, it is important that this method
   * not be virtual (that is, that it not be protected or public).  If it were virtual,
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
  
  public RefFrame getParent() { return parent; }
  
  public double distanceTo(RefFrame f) {
    return pt.distanceTo(f.pt);
  }
}