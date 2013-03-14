import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
//import com.jogamp.opengl.util.*;

import java.util.ArrayList;
import java.util.Properties;

/**
 * A View class, in the terms of the Model-View-Controller paradigm.
 * This class borrows from code originally written by Gregory Pierce (freely
 * available on the Web).
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
public class View implements GLEventListener, ConstantMgr
{
  // The attributes have default values, so that we don't have to specify everything.
  private boolean perspective = true;  // Use perspective projection if true
  private double far = FAR;            // Distance to the far clipping plane
  private double aspect = -1;          // Aspect ratio of the viewing frustum
  private double near = NEAR;          // Distance to the near clipping plane
  private double fovy = FOVY;          // Vertical field of view, in degrees
  private float vp = 30.0f;            // Constant used for setting the eye point
  private float[] eye = {vp/2.f, vp/4.f, vp, 1};  // Eye point, in model coordinates
  private float[] lookAt = {0, 0, 0};  // Look-at point, in model coordinates
  private float[] up = {0, 1, 0};      // Up vector, in model coordinates
   
  private double hAngle = 0;  // Angle of rotation of the model about 
                             //   the up vector, in degrees
  private double hRotateBase = 180; // Static amount of rotation about the up vector,
                                    // in degrees
  
  private int canvasWidth = 1;
  //private int canvasHeight = 1;
  
  private ArrayList<Drawable> drawList;
  private GL2              gl;
  private GLDrawable      glDrawable;
  
  /**
   * Create a View.  The Properties object allows one to override the defaults
   * as necessary.
   */
  public View(Model model, Properties props) {
    drawList = model.createDrawList(false);

    props.list(System.out);
    
    if (props.containsKey("orthogonal")) {
      this.perspective = false;
    }
    
    if (props.containsKey("eye")) {
      String[] eyeStrings = props.getProperty("eye").split("\\s");
      if (eyeStrings.length != 3) {
        System.out.println("Wrong number of coordinates in eye property: ignoring it");
      }
      else {
        float[] eyePt = new float[4];
        eyePt[3] = 0; // Default
        try {
          for (int i = 0; i < eyeStrings.length; i++) {
            eyePt[i] = Float.parseFloat(eyeStrings[i]);
          }
          this.eye = eyePt;
        } catch (NumberFormatException e) {
          System.out.println("Ignoring eyepoint specification, due to the following:");
          System.out.println(e);
        }
      }
    }
    
    if (props.containsKey("fov")) {
      try {
        this.fovy = Double.parseDouble(props.getProperty("fov"));
      } catch (NumberFormatException e) {
        System.out.println("Ignoring FOV specification, due to the following:");
        System.out.println(e);
      }
    }
    
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
    
    if (props.containsKey("aspect")) {
      try {
        this.aspect = Double.parseDouble(props.getProperty("aspect"));
      } catch (NumberFormatException e) {
        System.out.println("Ignoring aspect-ratio specification, due to the following:");
        System.out.println(e);
      }
    }
    
    if (props.containsKey("baseRotation")) {
      try {
        this.hRotateBase = Double.parseDouble(props.getProperty("baseRotation"));
      } catch (NumberFormatException e) {
        System.out.println("Ignoring base-rotation specification, due to the following:");
        System.out.println(e);
      }
    }
  }
    
  /**
   * Initialize the View.  This should be called from the event-handling thread.
   */
  public void init(GLAutoDrawable drawable)
  {
	GLCapabilitiesImmutable glCapsActual = drawable.getChosenGLCapabilities();
	System.out.println("Alpha bits: " + glCapsActual.getAccumAlphaBits());
	System.out.println("Profile: " + glCapsActual.getGLProfile());

    this.gl = (GL2)(drawable.getGL());
    this.glDrawable = drawable;
    
    //gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glClearColor(0.4f, 0.4f, 0.4f, 1.0f);
    gl.glClearDepth(FAR);
    
    gl.glDisable(GL.GL_DEPTH_TEST);
    gl.glEnable(GL.GL_BLEND);
    gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        
    gl.glEnable(GL2.GL_POLYGON_SMOOTH);
    gl.glHint(GL2.GL_POLYGON_SMOOTH_HINT, GL.GL_NICEST);
    
    // Smooth shading
    //gl.glShadeModel(GL.GL_SMOOTH);
    
    // Keep normals normalized!
    gl.glEnable(GL2.GL_NORMALIZE);
    
    drawable.setGL( new DebugGL2((GL2)(drawable.getGL()) ));
    
    System.out.println("Init GL is " + gl.getClass().getName());
  }
  
  /**
   * Dispose of the GL stuff if the drawable is destroyed for any reason.
   */
  public void dispose(GLAutoDrawable drawable) {
	  if (drawable == this.glDrawable) {
		  this.gl = null;
		  this.glDrawable = null;
		  this.drawList = null;
	  }
  }
  /**
   * Take an X coordinate of a point on the canvas, and transform it to the the interval [-1, 1].
   * -1 is at the left of the canvas, 1 is at the right of the canvas, and 0 is in the center.
   */
  public double normX(int rawX) {
    return (rawX - canvasWidth/2) / (canvasWidth/2.0);
  }
   
  /**
   * Sets a rotation for the model about the up-vector.
   */
  public void setRotH(double degrees) {
    hAngle = degrees;
  }
  
  /**
   * Reset the model.
   */
  public void setModel(Model newModel) {
    drawList = newModel.createDrawList(false);
  }
  
  /**
   * Actually draw the picture.  Again, this should be called from
   * the event-handling thread.
   */
  public void display(GLAutoDrawable drawable)
  {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
    gl.glMatrixMode(GL2.GL_MODELVIEW);
    gl.glLoadIdentity();
    
    // Set up the projection
    GLU glu = new GLU();
    //           eye point      center of view       up
    glu.gluLookAt(eye[0], eye[1], eye[2], lookAt[0], lookAt[1], lookAt[2], 
                  up[0], up[1], up[2]);
    
    // Draw stuff.
    gl.glPushMatrix();
    gl.glRotated(hRotateBase + hAngle, up[0], up[1], up[2]);
    
    gl.glDisable(GL2.GL_LIGHTING);
    
    ShapeBuilder.axes(gl);
    for (Drawable d:drawList) {
      d.draw(gl);
    }
    gl.glPopMatrix();
    
  
  }
  
  /**
   * Handle the necessary changes to the projection if the user resizes
   * or reshapes the frame.
   */
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
  {
    canvasWidth = width;
    //canvasHeight = height;
    gl.glViewport(x, y, width, height);
    gl.glMatrixMode(GL2.GL_PROJECTION);
    gl.glLoadIdentity();
    if (perspective) {
      double aspectRatio = this.aspect;
      if (aspectRatio < 0) {
        aspectRatio = (double)width/(double)height;
      }
      GLU glu = new GLU();
      glu.gluPerspective(fovy, aspectRatio, near, far);
    }
    else {
      gl.glOrtho(-5, 5, -5, 5, near, far);
    }
  }
  
  /**
   * Don't do anything if the display itself changes.
   */
  public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged)
  {
  }
}
