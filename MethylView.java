import javax.media.opengl.*;
 
/** 
 * A class to view a methyl group.  The carbon is treated as the origin
 * of the group.
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

public class MethylView extends AtomOrGroupView {
  private SP3AtomView carbView;
  private HydrogenView hydro1View;
  private HydrogenView hydro2View;
  private HydrogenView hydro3View;
  
  public MethylView(Methyl frame, SP3AtomView cview, HydrogenView h1view, 
                    HydrogenView h2view, HydrogenView h3view) {
    super(frame, "CH3");
    carbView = cview;
    hydro1View = h1view;
    hydro2View = h2view;
    hydro3View = h3view;
  }
  
  public void draw(GL2 gl) {
    initDraw(gl);
    //ShapeBuilder.axes(gl);
    carbView.draw(gl);
    hydro1View.draw(gl);
    hydro2View.draw(gl);
    hydro3View.draw(gl);
    endDraw(gl);
  }
  
}
