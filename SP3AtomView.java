import javax.media.opengl.*;

/**
 * A class to view an atom with SP3 hybridization
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

public class SP3AtomView extends AtomView {
  private POrbitalView porb[] = new POrbitalView[4];
  
  public SP3AtomView(Atom frame, String text, java.awt.Color color, POrbitalView[] orbitals) {
    super(frame, text, color);
    porb = orbitals;
  }

  public void draw (GL gl) {
    initDraw(gl);
    ShapeBuilder.axes(gl);
    for (int i = 0; i < 4; i++) {
      porb[i].draw(gl);
    }
    endDraw(gl);
  }
}
