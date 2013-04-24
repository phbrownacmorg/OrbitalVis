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
	public static double X_SCALE = 1.0;
	public static double Y_SCALE = X_SCALE;
	public static double Z_SCALE = X_SCALE;
	public static float Z_OFFSET_FACTOR[] = {0.3f, 0f};
	public static float Y_OFFSET_FACTOR[] = {0f, 0f};;
  
	public static int FONT_SIZE = 96; // 24.0f
	public static double FONT_SCALING_FACTOR = 1.0/FONT_SIZE;
  
//  private double near = NEAR;          // Distance to the near clipping plane
//  private double far = FAR;            // Distance to the far clipping plane
//  private double aspect = -1;          // Aspect ratio of the viewing frustum
//  private double fovy = FOVY;          // Vertical field of view, in degrees  
//  private float[] eye = {VP/2.f, VP/4.f, VP, 1};  // Eye point, in model coordinates

//  private ArrayList<Drawable> drawList;
  private TextRenderer tr;
  private TextRenderer superTR;
  
  public View2D(Model m, Properties props) {
	  super(m, props);

	  try {
		  Z_OFFSET_FACTOR = readMultiDimProp(props, "ZOff", 2);
		  System.out.println(Z_OFFSET_FACTOR);
	  } catch (NumberFormatException e) {
		  System.out.println("Ignoring ZOff specification, due to the following:");
		  System.out.println(e);
	  } catch (Exception e) {
		  System.out.println(e);
	  }
	  
	  try {
		  Y_OFFSET_FACTOR = readMultiDimProp(props, "YOff", 2);
	  } catch (NumberFormatException e) {
		  System.out.println("Ignoring YOff specification, due to the following:");
		  System.out.println(e);
	  } catch (Exception e) {
		  System.out.println(e);
	  }
	  	  
	  // Intrinsic to a View2D that the eyepoint is on the negative X-axis, looking towards the origin
	  eye[0] = (float)(Math.sqrt(eye[0]*eye[0] + eye[1]*eye[1] + eye[2]*eye[2]));
	  eye[1] = 0; eye[2] = 0;
	  
	  this.drawList = m.createDrawList(true);
	  this.perspective = false;
  }

  public float[] readMultiDimProp(Properties props, String propName, int dims) throws Exception {
	  float[] result = new float[2];
	  if (props.containsKey(propName)) {
		  String[] offsetStrings = props.getProperty(propName).split("\\s");
		  if (offsetStrings.length != dims) {
			  throw new Exception("Wrong number of coordinates: ignoring spec" + propName);
		  }
		  for (int i = 0; i < dims; i++) {
			  result[i] = Float.parseFloat(offsetStrings[i]);
		  }
	  }
	  return result;
  }
  
  
  public void init(GLAutoDrawable drawable) {
	  super.init(drawable);
	  gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
	  this.tr = new TextRenderer(new java.awt.Font("SansSerif", java.awt.Font.BOLD, FONT_SIZE));
	  this.superTR = new TextRenderer(new java.awt.Font("SansSerif", java.awt.Font.BOLD, Math.round(FONT_SIZE * .6f)));
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
		  d.draw2D(gl, this.tr, this.superTR);
	  }	
	  gl.glPopMatrix();
  }
  
  public static Point3D ptFor2D(RefFrame frame) {
	  Point3D result = new Point3D(frame.getLoc());

	  result = result.translate(frame.getX() * -View2D.Z_OFFSET_FACTOR[0] + frame.getY() * View2D.Y_OFFSET_FACTOR[0], 
				frame.getX() * View2D.Z_OFFSET_FACTOR[1] + frame.getY() * View2D.Y_OFFSET_FACTOR[1], 0);
	  
//	  float xLoc;
//	  
//	  if(frame.getParent()==null)
//		  xLoc=(float)(frame.getLoc().z());
//	  else
//		  xLoc=(float)(frame.getParent().getLoc().z());
//	  //System.out.println(text+" at: "+xLoc);
//
//	  if(xLoc>0) {
//		  result = result.translate(frame.getX() * (-View2D.Z_OFFSET_FACTOR[0] + View2D.Y_OFFSET_FACTOR[0]) * View2D.X_SCALE, 
//				  frame.getY() * (View2D.Z_OFFSET_FACTOR[1] + View2D.Y_OFFSET_FACTOR[1]) * View2D.Y_SCALE, 0);
//	  }
//	  else {
//		  result = result.translate(frame.getX() * (-View2D.Z_OFFSET_FACTOR[0] + View2D.Y_OFFSET_FACTOR[0]) * View2D.X_SCALE, 
//				  -frame.getY() * (View2D.Z_OFFSET_FACTOR[1] + View2D.Y_OFFSET_FACTOR[1]) * View2D.Y_SCALE, 0);
//	  }
	  
	  System.out.println("View2D.ptFor3D("+frame+") => "+result);
	  
	  return result;
  }
  
  public static void setFontSize(int newFontSize) {
    FONT_SIZE = newFontSize;
  }
  
  public static void setScales(double sx, double sy, double sz) {
    X_SCALE = sx;
    Y_SCALE = sy;
    Z_SCALE = sz;
  }
  
}