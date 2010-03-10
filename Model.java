import java.util.ArrayList;
import javax.media.opengl.*;

/**
 * Model class.  
 */

public abstract class Model implements ConstantMgr {
  public static final int LEFT_SIDE_ATTACK = -1;
  public static final int RIGHT_SIDE_ATTACK = 1;
    
  private double t; // Tracks process through a reaction, going from 0 to 1
  
  protected Model() {
    t = 0;
  }
    
  /**
   * Get t.
   */
  public double getT() {
    return t;
  }
  
  /**
   * Set t, clamping the parameter to the interval [0, 1].
   */
  public void setT(double newT) {
    t = Math.max(0.0, Math.min(1.0, newT));
  }
  
  public abstract ArrayList<Drawable> createDrawList(boolean twoD); 
}
