/**
 * Widget on which to draw a View.
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

public class Canvas2D extends java.awt.Canvas {
  private View2D view;
  
  public Canvas2D(View2D v) {
    super();
    view = v;
  }
  
  public void paint(java.awt.Graphics g) {
    view.draw(g);
  }
}