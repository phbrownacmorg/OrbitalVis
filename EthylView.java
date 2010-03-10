import javax.media.opengl.*;

/** 
 * A class to draw an ethyl group.  The carbon with only two hydrogens is treated as the origin of the group.
 */

public class EthylView extends AtomOrGroupView {
  private SP3AtomView carbView;
  private HydrogenView hydro1View;
  private HydrogenView hydro2View;
  private MethylView methylView;
  
  public EthylView(Ethyl frame, SP3AtomView cview, HydrogenView h1view, 
                    HydrogenView h2view, MethylView mview) {
    super(frame, "CH2CH3");
    carbView = cview;
    hydro1View = h1view;
    hydro2View = h2view;
    methylView = mview;
  }
  
  public void draw(GL gl) {
    initDraw(gl);
    //ShapeBuilder.axes(gl);
    carbView.draw(gl);
    hydro1View.draw(gl);
    hydro2View.draw(gl);
    methylView.draw(gl);
    endDraw(gl);
  }
  
}