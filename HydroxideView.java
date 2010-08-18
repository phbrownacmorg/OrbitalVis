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

public class HydroxideView extends AtomOrGroupView {
  public static final String TEXT_OH = "OH";
  public static final String TEXT_HO = "HO";
  
  private HydrogenView hydroView;
  private SP3AtomView oxyView;
  
  public HydroxideView(Hydroxide frame, HydrogenView hview, SP3AtomView oview, String text) {
    super(frame, text);
    hydroView = hview;
    oxyView = oview;
  }
  
  public void draw(GL gl) {
    initDraw(gl);
    //ShapeBuilder.axes(gl);
    oxyView.draw(gl);
    hydroView.draw(gl);
    endDraw(gl);
  }
}
