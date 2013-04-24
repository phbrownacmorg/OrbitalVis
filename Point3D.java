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
  
  public String toString() {
	  return "("+this.x()+","+this.y()+","+this.z()+")";
  }
}