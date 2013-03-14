//import javax.media.opengl.*;

/**
 * Model a P-orbital or hybridized orbital along the Z-axis.  The proportion
 * controls the level of hybridization.
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