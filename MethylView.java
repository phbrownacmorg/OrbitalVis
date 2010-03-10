import javax.media.opengl.*;
 
/** 
 * A class to represent a methyl group.  The carbon is treated as the origin of the group.
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
  
  public void draw(GL gl) {
    initDraw(gl);
    //ShapeBuilder.axes(gl);
    carbView.draw(gl);
    hydro1View.draw(gl);
    hydro2View.draw(gl);
    hydro3View.draw(gl);
    endDraw(gl);
  }
}
