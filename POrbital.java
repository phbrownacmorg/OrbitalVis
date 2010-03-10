import javax.media.opengl.*;

/**
 * Model a P-orbital or hybridized orbital along the Z-axis.  The proportion controls the
 * level of hybridization.
 */

public class POrbital extends RefFrame { 
  public static final double SP3_PROP = 0.3; // Fix this
  private double proportion = SP3_PROP; // Proportion of total orbital volume in the first lobe
  
  public static double MIN_CENTRAL_ANGLE = 30;
  public static double MAX_CENTRAL_ANGLE = 120;
  private double centralAngle = MIN_CENTRAL_ANGLE;
  
  public POrbital(double rx, double ry, double rz) {
    super();
    setRot(rx, ry, rz);
  }
  
  public void setProportion(double newProp) {
    proportion = newProp;
  }
  
  public double proportion() { return proportion; }
  
  public void setCentralAngle(double degrees) {
    centralAngle = degrees;
  }
  
  /**
   * Set the central angle according to a divergence parameter in the interval [0,1].
   * The resulting central angle interpolates linearly between MIN_CENTRAL_ANGLE and
   * MAX_CENTRAL_ANGLE.
   */
  public void setDivergence(double divergence) {
    centralAngle = MIN_CENTRAL_ANGLE * (1.0 - divergence) 
      + MAX_CENTRAL_ANGLE * divergence;
  }
  
  public double centralAngle() { return centralAngle; }
}