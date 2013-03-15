import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Properties;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

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

public class View2D implements GLEventListener, ConstantMgr {
  private static int Z_SCALE = 100;
  private static int Y_SCALE = 100;
  private static int X_SCALE = 25;
  private static int X_OFF = 365;
  private static int Y_OFF = 170;
  
  public static float FONT_SIZE = 24.0f;
  
  private double near = NEAR;          // Distance to the near clipping plane
  private double far = FAR;            // Distance to the far clipping plane
  
  private ArrayList<Drawable> drawList;
 
  public View2D(Model m, Properties props) {
    drawList = m.createDrawList(true);
    
    if (props.containsKey("near")) {
        try {
          this.near = Double.parseDouble(props.getProperty("near"));
        } catch (NumberFormatException e) {
          System.out.println("Ignoring near specification, due to the following:");
          System.out.println(e);
        }
    }
      
    if (props.containsKey("far")) {
        try {
          this.far = Double.parseDouble(props.getProperty("far"));
        } catch (NumberFormatException e) {
          System.out.println("Ignoring far specification, due to the following:");
          System.out.println(e);
        }
    }

  }

  public void init(GLAutoDrawable drawable) {
	  
  }
  
  
  /**
   * Reset the model.
   */
  public void setModel(Model newModel) {
    drawList = newModel.createDrawList(true);
  }
  
  public void draw(Graphics g) {
    // If blanking, just blank the canvas
    // Else, iterate over all the Atoms in the Model and tell them to draw themselves using g
    if (!(g instanceof Graphics2D)) {
      g.drawLine(500, 0, 0, 500);  // OK, you can cast it to Graphics2D and do Graphics2D things with it
    }
    else {
      for (Drawable d:drawList) {
        d.draw2D((Graphics2D)g);
      }
//      g.drawLine(250, 0, 250, 500);
    }
    
  }
  
  public void display(GLAutoDrawable canvas2D) {
	  GL2 gl = (GL2)(canvas2D.getGL());
	  
	  gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
	  gl.glMatrixMode(GL2.GL_MODELVIEW);
	  gl.glLoadIdentity();

	  // Set up the projection

	  // Draw stuff.
	  gl.glPushMatrix();
	  gl.glDisable(GL2.GL_LIGHTING);
	  
	  for (Drawable d:drawList) {
		  d.draw2D(gl);
	  }	
	  gl.glPopMatrix();
  }
  
  public void dispose(GLAutoDrawable drawable) { }
  
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
  {
	  GL2 gl = (GL2)(drawable.getGL());
	  
	  gl.glViewport(x, y, width, height);
	  gl.glMatrixMode(GL2.GL_PROJECTION);
	  gl.glLoadIdentity();
	  gl.glOrtho(-5, 5, -5, 5, near, far);
  }
  
  public static void setFontSize(float newFontSize) {
    FONT_SIZE = newFontSize;
  }
  
  public static void setOffsets(int offX, int offY) {
    X_OFF = offX;
    Y_OFF = offY;
    //System.out.println("2D offsets set to " + X_OFF + " and " + Y_OFF);
  }
  
  public static void setScales(int sx, int sy, int sz) {
    X_SCALE = sx;
    Y_SCALE = sy;
    Z_SCALE = sz;
  }
  
  public static Point2D to2D(Point3D pt) {
    double x = pt.z() * Z_SCALE + pt.x() * X_SCALE + X_OFF;
    double y = -pt.y() * Y_SCALE + Y_OFF;
    return new Point2D.Double(x, y);
  }
}