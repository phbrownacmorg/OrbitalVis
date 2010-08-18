/**
 * Dumping ground for commonly-used constants.
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
public interface ConstantMgr {
  /**
   * Distance to the far clipping plane.
   */
  public static final double FAR = 38.0;
  public static final double NEAR = 30.0;
  /**
   * Vertical field of view.
   */
  public static final double FOVY = 30.0;
 
  /**
   * From Bar
   */
  public static final double HOT = 50;
  public static final double AMBIENT = 25;
  public static final double COLD = 0;
  
  /**
   * Colors
   */
  public static final float ALPHA = 1.f;
  public static final float[] WHITE = {1.f, 1.f, 1.f, ALPHA};
  public static final float[] BLACK = {0.f, 0.f, 0.f, ALPHA};
  public static final float[] RED = {1.f, 0.f, 0.f, ALPHA};
  public static final float[] RED06 = {0.6f, 0.f, 0.f, ALPHA};
  public static final float[] GREEN = {0.f, 1.f, 0.f, ALPHA};
  public static final float[] BLUE = {0.f, 0.f, 1.f, ALPHA};
  public static final float[] BLUE06 = {0.f, 0.f, 0.6f, ALPHA};
  public static final float[] GRAY06 = {0.6f, 0.6f, 0.6f, ALPHA};
  public static final float[] GRAY02 = {0.2f, 0.2f, 0.2f, ALPHA};
  public static final float[] BROWN = {1.0f, 0.5f, 0.f, ALPHA};

}