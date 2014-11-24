

/**
 * Class to represent a point in 3D.  This class is immutable.
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
public class Point3D {
  double elt[] = new double[3];
  
  Point3D() { 
    this(0, 0, 0);
  }
  
  Point3D(double x, double y, double z) {
    elt[0] = x;
    elt[1] = y;
    elt[2] = z;
  }
  
  public double x() { return elt[0]; }
  public double y() { return elt[1]; }
  public double z() { return elt[2]; }
  
  public double distanceTo(Point3D p) {
    double dx = elt[0] - p.elt[0];
    double dy = elt[1] - p.elt[1];
    double dz = elt[2] - p.elt[2];
    return Math.sqrt(dx*dx + dy*dy + dz*dz);
  }
  
  /*
   * Returns a Point3D which is the result of left-multiplying this Point3D by the given Matrix
   * (i.e., the Matrix is on the left).
   */
  public Point3D transform(Matrix m) {
    Point3D result = new Point3D();
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        result.elt[i] += m.elt(i, j) * this.elt[j];
      }
    }
    return result;
  }
  
  public Point3D scale(double scalar) {
    Point3D result = new Point3D(elt[0] * scalar, elt[1] * scalar, elt[2] * scalar);
    return result;
  }
  
  public Point3D translate(double tx, double ty, double tz) {
	  Point3D result = new Point3D(elt[0] + tx, elt[1] + ty, elt[2] + tz);
	  return result;
  }
  
  public Point3D interpolate(Point3D p, double prop) {
	  double newX = (1.0 - prop) * this.x() + prop * p.x();
	  double newY = (1.0 - prop) * this.y() + prop * p.y();
	  double newZ = (1.0 - prop) * this.z() + prop * p.z();
	  return new Point3D(newX, newY, newZ);
  }
  
  /*
   * Return the normalized right-hand cross product of the current point (considered
   * as a vector) with the +Z basis vector.  If the current point is in fact on the
   * Z-axis, return the zero vector instead.
   */
  public Point3D crossWithZ() {
	  Point3D result;
	  double x = this.y();
	  double y = -this.x();
	  double len = Math.sqrt(x*x + y*y);
	  if (len > 0) {
		  result = new Point3D(x/len, y/len, 0);
	  }
	  else {
		  result = new Point3D();
	  }
	  return result;
  }
  
  public String toString() {
	  return "("+this.x()+","+this.y()+","+this.z()+")";
  }
  
}