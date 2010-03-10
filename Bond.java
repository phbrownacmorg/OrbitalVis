import java.awt.geom.Point2D;

/**
 * Class to represent a Bond.  It turns out that changing bond state purely by a distance rule
 * doesn't work very well.  This class was therefore introduced to allow the Model to set the
 * state explicitly.
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
  public Point2D getStart() { return frame1.getPt2D(); }
  public Point2D getEnd()   { return frame2.getPt2D(); }
  
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
  
}