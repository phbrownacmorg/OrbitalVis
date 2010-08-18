import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import com.sun.opengl.util.GLUT;

/**
 * Class to build a set of generally-useful shapes.
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

public class ShapeBuilder implements ConstantMgr {
  private static int WEDGES = 15;
  private static int SLICES = 15;
  private static int LOOPS = 1;
  
  private static double v[][] = {
    {0.0, 0.0, 0.0}, {0.0, 0.0, 1.0},
    {0.0, 1.0, 0.0}, {0.0, 1.0, 1.0}, 
    {1.0, 0.0, 0.0}, {1.0, 0.0, 1.0},
    {1.0, 1.0, 0.0}, {1.0, 1.0, 1.0} };
  
  private static double n[][] = {
    {0.0, 0.0, 1.0}, {-1.0, 0.0, 0.0},
    {0.0, 0.0, -1.0}, {1.0, 0.0, 0.0},
    {0.0, -1.0, 0.0}, {0.0, 1.0, 0.0} };
  
  private static int edges[][] = {
    {0, 1}, {1, 3}, {3, 2}, {2, 0},
    {0, 4}, {1, 5}, {3, 7}, {2, 6},
    {4, 5}, {5, 7}, {7, 6}, {6, 4},
    {1, 0}, {3, 1}, {2, 3}, {0, 2},
    {4, 0}, {5, 1}, {7, 3}, {6, 2},
    {5, 4}, {7, 5}, {6, 7}, {4, 6}};
  
  private static int cubeface[][] = {
    {0, 1, 2, 3}, {5, 9, 18, 13},
    {14, 6, 10, 19}, {7, 11, 16, 15},
    {4, 8, 17, 12}, {22, 21, 20, 23}};

  /**
   * Make a unit cube in the first octant.  This method does assign normals to the faces.
   */
  public static void cube(GL gl) {
    // unit cube in the first octant
    gl.glBegin(GL.GL_QUADS);
    for (int face = 0; face < 6; face++) {
      gl.glNormal3dv(n[face], 0);
      for (int edge = 0; edge < 4; edge++) {
        gl.glVertex3dv(v[edges[cubeface[face][edge]][0]], 0);
      }
    }
    gl.glEnd();
  }
  
  /**
   * Make a unit square in the first quadrant of the XY plane.
   */
  public static void square(GL gl) {
    gl.glBegin(GL.GL_QUADS);
    gl.glNormal3dv(n[0], 0);
    gl.glVertex3dv(v[0], 0);
    gl.glNormal3dv(n[0], 0);
    gl.glVertex3dv(v[2], 0);
    gl.glNormal3dv(n[0], 0);
    gl.glVertex3dv(v[6], 0);
    gl.glNormal3dv(n[0], 0);
    gl.glVertex3dv(v[4], 0);
    gl.glEnd();
  }
  
  public static void yzSquare(GL gl) {
    gl.glBegin(GL.GL_QUADS);
    gl.glNormal3dv(n[3], 0);
    gl.glVertex3dv(v[0], 0);
    gl.glNormal3dv(n[3], 0);
    gl.glVertex3dv(v[2], 0);
    gl.glNormal3dv(n[3], 0);
    gl.glVertex3dv(v[3], 0);
    gl.glNormal3dv(n[3], 0);
    gl.glVertex3dv(v[1], 0);
    gl.glEnd();
//    gl.glRotated(90, 0, 1, 0);
//    square(gl);
  }
  
  /**
   * Make a unit square centered on the origin.
   */
  public static void centeredSquare(GL gl) {
    gl.glTranslated(-0.5, -0.5, 0);
    square(gl);
  }
  
  /**
   * Make a 45-45-90 right triangle in the XY plane, with the right angle at the origin.
   */
  public static void triangle(GL gl) {
    gl.glBegin(GL.GL_TRIANGLES);
    gl.glNormal3dv(n[0], 0);
    gl.glVertex3dv(v[0], 0);
    gl.glNormal3dv(n[0], 0);
    gl.glVertex3dv(v[2], 0);
    gl.glNormal3dv(n[0], 0);
    gl.glVertex3dv(v[4], 0);
    gl.glEnd();
  }

  /**
   * Make a unit set of axes.  The X axis is in red, the Y axis is in green, and the Z axis is in blue.
   */
  public static void axes(GL gl) {
    gl.glDisable(GL.GL_LIGHTING);
    double colors[] = new double[4];
    gl.glGetDoublev(GL.GL_CURRENT_COLOR, colors, 0);
    
    gl.glBegin(GL.GL_LINES);
    gl.glColor4fv(RED, 0);
    gl.glVertex3dv(v[0], 0);
    gl.glVertex3dv(v[4], 0);
    gl.glColor4fv(GREEN, 0);
    gl.glVertex3dv(v[0], 0);
    gl.glVertex3dv(v[2], 0);
    gl.glColor4fv(BLUE, 0);
    gl.glVertex3dv(v[0], 0);
    gl.glVertex3dv(v[1], 0);
    gl.glEnd();
    
    // Restore the previous color
    gl.glColor4dv(colors, 0);
    //gl.glEnable(GL.GL_LIGHTING);
  }
  
  /**
   * Make a unit sphere, centered on the origin.
   */
  public static void sphere(GL gl) {
    GLU glu = new GLU();
    GLUquadric gluq = glu.gluNewQuadric();
    glu.gluQuadricNormals(gluq, GLU.GLU_SMOOTH);
    glu.gluSphere(gluq, 1.0, WEDGES, SLICES);
  }
  
  /**
   * Make a 30-degree cone, base in the xy plane, tip at z = 1.
   */
  public static void cone(GL gl) {
    GLU glu = new GLU();
    GLUquadric gluq = glu.gluNewQuadric();
    glu.gluQuadricNormals(gluq, GLU.GLU_SMOOTH);
    glu.gluCylinder(gluq, 1.0, 0.0, 1.0, SLICES, WEDGES);
  }
  
  public static void quadrisphere(GL gl) {
    // Make the polar cap
    gl.glBegin(GL.GL_TRIANGLE_FAN);
    gl.glNormal3d(0, 0, 1);
    gl.glVertex3d(0.0, 0.0, 1.0);
    for (int i = 0; i <= WEDGES; i++) {
      double theta = i * Math.PI/WEDGES;
      //double nextTheta = (i+1) * Math.PI/WEDGES;
      double phi = Math.PI/(2 * SLICES);
      gl.glNormal3d(Math.sin(phi) * Math.cos(theta), Math.sin(phi) * Math.sin(theta), Math.cos(phi));
      gl.glVertex3d(Math.sin(phi) * Math.cos(theta), Math.sin(phi) * Math.sin(theta), Math.cos(phi));
    }
    gl.glEnd();
  
    // Make the rest of the strips
    for (int j = 1; j < SLICES; j++) {
      double phi = j * Math.PI/(2 * SLICES);
      double nextphi = (j+1) * Math.PI/(2 * SLICES);
      gl.glBegin(GL.GL_TRIANGLE_STRIP);
      for (int i = 0; i <= WEDGES; i++) {
        double theta = i * Math.PI/WEDGES;
        gl.glVertex3d(Math.sin(phi) * Math.cos(theta), Math.sin(phi) * Math.sin(theta), Math.cos(phi));
        gl.glNormal3d(Math.sin(phi) * Math.cos(theta), Math.sin(phi) * Math.sin(theta), Math.cos(phi));
        gl.glVertex3d(Math.sin(nextphi) * Math.cos(theta), Math.sin(nextphi) * Math.sin(theta), Math.cos(nextphi));
        gl.glNormal3d(Math.sin(nextphi) * Math.cos(theta), Math.sin(nextphi) * Math.sin(theta), Math.cos(nextphi));
      }
      gl.glEnd();
    }
  }
  
  public static void disk(GL gl, double inner) {
    GLU glu = new GLU();
    GLUquadric gluq = glu.gluNewQuadric();
    glu.gluQuadricNormals(gluq, GLU.GLU_SMOOTH);
    glu.gluDisk(gluq, inner, 1.0, WEDGES, LOOPS);
  }
  
  /**
   * Draw a ring of dots in the yz plane.
   */
  public static void ringOfDots(GL gl, int numDots, double dotSize) {
    GLU glu = new GLU();
    GLUquadric gluq = glu.gluNewQuadric();
    glu.gluQuadricNormals(gluq, GLU.GLU_SMOOTH);
    for (int i = 0; i < numDots; i++) {
      double angle = i * 2 * Math.PI / numDots;
      gl.glPushMatrix();
      gl.glTranslated(0, Math.sin(angle), Math.cos(angle));
      glu.gluSphere(gluq, dotSize, WEDGES, SLICES);
      gl.glPopMatrix();
    }
  }
  
  public static void hemisphere(GL gl) {
    quadrisphere(gl);
    gl.glRotated(180.0, 0.0, 1.0, 0.0);
    quadrisphere(gl);
  }
  
  public static void setMaterial(GL gl, int face, float[] rgba) {
    if (face == GL.GL_FRONT_AND_BACK) {
      setMaterial(gl, GL.GL_FRONT, rgba);
      setMaterial(gl, GL.GL_BACK, rgba);
    }
    else {
      gl.glMaterialfv(face, GL.GL_AMBIENT, rgba, 0);
      gl.glMaterialfv(face, GL.GL_DIFFUSE, rgba, 0);
      gl.glMaterialfv(face, GL.GL_SPECULAR, rgba, 0);
      gl.glMateriali(face, GL.GL_SHININESS, 4);
      gl.glMaterialfv(face, GL.GL_EMISSION, BLACK, 0);
    }
  }
}
