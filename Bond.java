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
//  public Point2D getStart() { return frame1.getPt2D(); }
//  public Point2D getEnd()   { return frame2.getPt2D(); }
  
  public State getState()       { return state; }
  public void setState(State s) { state = s; }
  
  public int getDepthDir() {
    int result = 0;
    double diff = frame2.getX() - frame1.getX();
    if (Math.abs(diff) > DEPTH_EPSILON) {
      result = (int)(Math.signum(diff));
    }
    return result;
  }
  
  public double getLength() {
	  return frame1.getLoc().distanceTo(frame2.getLoc());
//	  System.out.println(frame1.getLoc() + String.format(" ?= (%f, %f, %f)", frame1.getX(), frame1.getY(), frame1.getZ())); //" ?= (" ?= "')
//	  System.out.println(frame2.getLoc() + String.format(" ?= (%f, %f, %f)", frame2.getX(), frame2.getY(), frame2.getZ())); //" ?= (" ?= "')
//	  double dx = frame1.getX() - frame2.getX();
//	  double dy = frame1.getY() - frame2.getY();
//	  double dz = frame1.getZ() - frame2.getZ();
//	  return Math.sqrt(dx*dx + dy*dy + dz*dz);
  }
  
}