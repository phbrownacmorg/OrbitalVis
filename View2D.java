import java.util.Properties;
import javax.media.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.awt.TextRenderer;

/**
 * A View class for the 2D part of the visualization.
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

public class View2D extends View {
	private static double X_SCALE = 1.0/6.0;
	private static double Y_SCALE = X_SCALE;
	private static double Z_SCALE = X_SCALE;
	private static int X_OFF = 365;
	private static int Y_OFF = 170;
  
	public static int FONT_SIZE = 12; // 24.0f
  
//  private double near = NEAR;          // Distance to the near clipping plane
//  private double far = FAR;            // Distance to the far clipping plane
//  private double aspect = -1;          // Aspect ratio of the viewing frustum
//  private double fovy = FOVY;          // Vertical field of view, in degrees  
//  private float[] eye = {VP/2.f, VP/4.f, VP, 1};  // Eye point, in model coordinates

//  private ArrayList<Drawable> drawList;
  private TextRenderer tr;
  
  public View2D(Model m, Properties props) {
	  super(m, props);
	  this.drawList = m.createDrawList(true);
	  this.perspective = false;
  }

  public void init(GLAutoDrawable drawable) {
	  super.init(drawable);
	  gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
	  this.tr = new TextRenderer(new java.awt.Font("SansSerif", java.awt.Font.BOLD, FONT_SIZE));
  }
  
  /**
   * Reset the model.
   */
  public void setModel(Model newModel) {
    drawList = newModel.createDrawList(true);
  }
  
  public void display(GLAutoDrawable canvas2D) {
	  //System.out.println("View2D.display called");
	  this.startDisplay();

	  // Draw stuff.
	  gl.glPushMatrix();
	  gl.glScaled(X_SCALE, Y_SCALE, Z_SCALE);
	  gl.glRotated(H_ROTATE_BASE, UP[0], UP[1], UP[2]);
	  

	  ShapeBuilder.axes(gl);
	  //System.out.println("Drawing " + drawList.size() + " Drawables");
	  for (Drawable d:drawList) {
		  d.draw2D(gl, this.tr);
	  }	
	  gl.glPopMatrix();
  }
    
  public static void setFontSize(int newFontSize) {
    FONT_SIZE = newFontSize;
  }
  
  public static void setOffsets(int offX, int offY) {
    X_OFF = offX;
    Y_OFF = offY;
    //System.out.println("2D offsets set to " + X_OFF + " and " + Y_OFF);
  }
  
  public static void setScales(double sx, double sy, double sz) {
    X_SCALE = sx;
    Y_SCALE = sy;
    Z_SCALE = sz;
  }
  
}