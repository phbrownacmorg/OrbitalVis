//import java.awt.geom.Point2D;

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
  private double rx = 0;
  private double ry = 0;
  private double rz = 0;
  private double tx2D = 0;
  private double ty2D = 0;
  //private Point2D pt2d;
  private RefFrame parent;  // Frame within which this one is nested
  
  /**
   * Holds the frame's own rotation matrix (not that of its ancestors).
   */  
  protected Matrix rotMatrix;
  
  public RefFrame() {
    this(new Point3D());
  }
  
  public RefFrame(RefFrame parent) {
	  this(new Point3D(), parent);
  }
  
  public RefFrame(Point3D pt3d) {
	  pt = pt3d;
	  this.parent = null;
	  rotMatrix = Matrix.makeRotationMatrix(0, Matrix.Axis.X); // Identity matrix
  }
  
  public RefFrame(Point3D pt3d, RefFrame parent) {
	  this(pt3d);
	  this.parent = parent;
  }
  
  public RefFrame(Point3D pt3d, RefFrame parent, double tx, double ty) {
	  this(pt3d, parent);
	  this.tx2D = tx;
	  this.ty2D = ty;
  }
  
  public RefFrame(double x, double y, double z) {
      this(new Point3D(x, y, z));
  }
  
  public RefFrame(double x, double y, double z, RefFrame parent) {
    this(new Point3D(x, y, z), parent);
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
//  private void updatePt(Point3D newPt) {
//    pt = newPt;
//    //pt2d = View2D.to2D(pt);
//  }
  
  public double getX()        { return pt.x(); }
  public double getY()        { return pt.y(); }
  public double getZ()        { return pt.z(); }
  public Point3D getPt3D()	  { return pt; }
  
  // The following four methods apply (and return) the 2D offsets *unrotated*; that is, 
  // assuming that they will be applied in a context where the X and Y axes align with
  // those of world space.  If you don't have such a context, use getPtFor2D(), below.
  public double getX2D()      { return pt.x() + tx2D; }
  public double getY2D()      { return pt.y() + ty2D; }
  public double getTx2D()     { return tx2D; }
  public double getTy2D()     { return ty2D; }

  
  public void setLoc(double newX, double newY, double newZ) {
	  setLoc(new Point3D(newX, newY, newZ));
  }
  public void setLoc(Point3D newPt)                         { pt = newPt; }
  
  public double getRotX() { return rx; }
  public double getRotY() { return ry; }
  public double getRotZ() { return rz; }
  public void setRot(double newRX, double newRY, double newRZ) {
    rx = newRX;
    ry = newRY;
    rz = newRZ;
    Matrix mx = Matrix.makeRotationMatrix(rx, Matrix.Axis.X);
    Matrix my = Matrix.makeRotationMatrix(ry, Matrix.Axis.Y);
    Matrix mz = Matrix.makeRotationMatrix(rz, Matrix.Axis.Z);
    Matrix myz = my.mult(mz);
    rotMatrix = mx.mult(myz);
  }
  
  //public Point2D getPt2D() { return pt2d; }
  
  /*
   * Return the frame's location for use in 2D drawing--that is, translated by the x and y
   * offsets.  Since, in the case of a frame nested inside another one, the translations are
   * actually applied relative to the parent's frame, they are rotated first before being
   * applied in that case.  
   */
  public Point3D getPtFor2D() {
	  double tx = tx2D;
	  double ty = ty2D;
	  double tz = 0;
	  if (this.parent != null) {
		  // What about if this frame's parent is itself a child?  Do we need to multiply the
		  // rotation matrices together, right up the ancestry tree?  And if so, should it be
		  // right-multiplication or left-multiplication by each succeeding matrix?
		  Point3D translation = new Point3D(tx, ty, tz);
		  Matrix mx = Matrix.makeRotationMatrix(-parent.rx, Matrix.Axis.X);
		  Matrix my = Matrix.makeRotationMatrix(-parent.ry, Matrix.Axis.Y);
		  Matrix mz = Matrix.makeRotationMatrix(-parent.rz, Matrix.Axis.Z);
		  Matrix myz = my.mult(mz);
		  Matrix pRev = mx.mult(myz);
		  Point3D transRev = translation.transform(pRev);
		  tx = transRev.x(); ty = transRev.y(); tz = transRev.z();
	  }
	  return pt.translate(tx, ty, tz); 
  }

  public RefFrame getParent() { return parent; }
  
  public double distanceTo(RefFrame f) {
    return pt.distanceTo(f.pt);
  }
  
  public String toString() {
	  return String.format("%s r: %f %f %f t2D: %f %f", 
			  				pt.toString(), rx, ry, rz, tx2D, ty2D);
  }
}