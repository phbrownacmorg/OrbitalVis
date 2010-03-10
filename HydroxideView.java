import javax.media.opengl.*;
 
/** 
 * A class to represent a hydroxide molecule.  The oxygen is treated as the origin of the group.
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
