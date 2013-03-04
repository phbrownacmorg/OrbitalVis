import javax.media.opengl.*;

/**
 * A class to represent an s-orbital.
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

public class SOrbitalView {
  public static final double RADIUS = 0.2; // Was 0.3

  public void draw(GL2 gl) {
    gl.glPushMatrix();
    gl.glScaled(RADIUS, RADIUS, RADIUS);
    ShapeBuilder.sphere(gl);
    gl.glPopMatrix();
  }
  
}
