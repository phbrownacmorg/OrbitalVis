import java.util.ArrayList;
//import javax.media.opengl.*;

/**
 * ABC for the model classes representing the various reactions.
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
  
  public String toString(){
	  String model = new String();
	  if (this instanceof SN2Model)
		  model = "SN2";
	  else if (this instanceof SN1Model)
		  model = "SN1";
	  else if (this instanceof E1Model)
		  model = "E1";
	  else if (this instanceof E2Model)
		  model = "E2";
	  else if (this instanceof AcylModel)
		  model = "Acyl";
	  else if (this instanceof EA2AModel)
		  model = "EA2A";
	  else
		  model = "Model not recognized";
	  return model;
  }
  
  /**
   * Set t, clamping the parameter to the interval [0, 1].
   */
  public void setT(double newT) {
    t = Math.max(0.0, Math.min(1.0, newT));
  }
  
  public abstract ArrayList<Drawable> createDrawList(boolean twoD); 
}
