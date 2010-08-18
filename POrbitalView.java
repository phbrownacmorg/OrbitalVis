import javax.media.opengl.*;

/**
 * View of a P-orbital or hybridized orbital along the Z-axis.  The proportion
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

public class POrbitalView extends Drawable { 
  //private static final double HALF_ANGLE = 15.0;
  
  private POrbital orbital;
  
  public POrbitalView(POrbital orbital) {
    super(orbital);
    this.orbital = orbital;
  }
  
  public void draw (GL gl) {
    initDraw(gl);
    // Scale the thing correctly
    gl.glPushMatrix();
    
    // Make one cone
    gl.glPushMatrix();
    double proportion = orbital.proportion();
    gl.glScaled(proportion, proportion, proportion);
    makeHalfOrbital(gl);
    gl.glPopMatrix();
    
    // Make the other cone
    gl.glPushMatrix();
    gl.glRotated(180.0, 0.0, 0.0, 0.0);
    gl.glScaled(1.0 - proportion, 1.0 - proportion, 1.0 - proportion);
    makeHalfOrbital(gl);
    gl.glPopMatrix();

    gl.glPopMatrix();
    endDraw(gl);
  }
  
  private void makeHalfOrbital(GL gl) {
    gl.glPushMatrix();
    double tanAngleX = Math.tan(Math.toRadians(POrbital.MIN_CENTRAL_ANGLE/2));
    double tanAngleY = Math.tan(Math.toRadians(orbital.centralAngle()/2));
//    if (orbital.centralAngle() > 30) {
//      System.out.println(orbital.centralAngle() + " " +
//                         (orbital.centralAngle()/2) + " " +
//                         Math.toRadians(orbital.centralAngle()/2) + " " +
//                         tanAngle);
//    }
    //gl.glScaled(tanAngle, tanAngle, 1.0);
    gl.glScaled(tanAngleX, tanAngleY, 1.0);
    gl.glTranslated(0, 0, -1);
    ShapeBuilder.cone(gl);
    gl.glScaled(1, 1, (tanAngleY + 0.27)/2);
    gl.glRotated(-90, 1.0, 0.0, 0.0);
    ShapeBuilder.hemisphere(gl);
    gl.glPopMatrix();
  }
  
}