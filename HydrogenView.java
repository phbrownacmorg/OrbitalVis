import javax.media.opengl.*;

/**
 * A class to represent a white Hydrogen atom
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

public class HydrogenView extends AtomView {
  private SOrbitalView sorb;
  private int numDots;
  
  public HydrogenView(Atom atom) {
    super(atom, "H", H_WHITE);
    sorb = new SOrbitalView();
    numDots = 0;
  }
  
  public void draw(GL2 gl) {
    initDraw(gl);
    sorb.draw(gl);
    endDraw(gl);
  }
}
