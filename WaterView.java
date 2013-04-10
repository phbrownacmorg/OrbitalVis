import javax.media.opengl.*;
 
/** 
 * A class to view a hydroxide group.  The oxygen is 
 * treated as the origin of the group.
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

public class WaterView extends AtomOrGroupView {
  public static final String TEXT_OH2 = "OH2";
  public static final String TEXT_H2O = "H2O";
  
  private HydrogenView h1View;
  private HydrogenView h2View;
  private SP3AtomView oxyView;
  
  public WaterView(Water frame, HydrogenView h1view, HydrogenView h2view, SP3AtomView oview, String text) {
    super(frame, text, AtomView.O_RED);
    this.h1View = h1view;
    this.h2View = h2view;
    oxyView = oview;
  }
  
  public void draw(GL2 gl) {
    initDraw(gl);
    //ShapeBuilder.axes(gl);
    oxyView.draw(gl);
    h1View.draw(gl);
    h2View.draw(gl);
    endDraw(gl);
  }
}
