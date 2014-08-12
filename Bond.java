//import java.awt.geom.Point2D;

/**
 * Class to represent a Bond.  It turns out that changing bond state purely by a distance rule
 * doesn't work very well.  This class was therefore introduced to allow the Model to set the
 * state explicitly.
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
public class Bond {
  public static final double DEPTH_EPSILON = 0.01;
  public enum State { BROKEN, PARTIAL, FULL, FULL_PARTIAL, DOUBLE };
  private AtomOrGroup frame1;
  private AtomOrGroup frame2;
  private State state;
  
  public Bond(AtomOrGroup f1, AtomOrGroup f2, State state) {
    frame1 = f1;
    frame2 = f2;
    this.state = state;
  }
  
  public AtomOrGroup getStart3D() { return frame1; }
  public AtomOrGroup getEnd3D()   { return frame2; }
  public Point3D getVectorFor2D() {
	  Point3D result = null;
	  if (frame2.getParent() == frame1) {
		  result = frame2.getPtFor2D();
//		  System.out.printf("frame2: %s%nvecFor2D: %s%n", frame2, result);
	  }
	  else {
		  // This can be expected to fail when (a) one end of this Bond is nested inside
		  // another frame, but (b) the end isn't nested in the beginning.
//	  System.out.println("start: " + frame1);
//	  System.out.println("end: " + frame2);
		  result = new Point3D(frame2.getX2D() - frame1.getX2D(),
				  frame2.getY2D() - frame1.getY2D(), frame2.getZ() - frame1.getZ());
	  }
//	  System.out.println("diff: " + result);
	  return result;
  }
//  public Point2D getStart() { return frame1.getPt2D(); }
//  public Point2D getEnd()   { return frame2.getPt2D(); }
  
  public State getState()       { return state; }
  public void setState(State s) { state = s; }
  
  public int getDepthDir() {
    int result = 0;
    double diff = frame2.getZ() - frame1.getZ();
    if (Math.abs(diff) > DEPTH_EPSILON) {
      result = (int)(Math.signum(diff));
    }
    return result;
  }
  
}