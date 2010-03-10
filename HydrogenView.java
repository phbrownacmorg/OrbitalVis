import javax.media.opengl.*;

/**
 * A class to represent a white Hydrogen atom
 */

public class HydrogenView extends AtomView {
  private SOrbitalView sorb;
  private int numDots;
  
  public HydrogenView(Atom atom) {
    super(atom, "H", H_WHITE);
    sorb = new SOrbitalView();
    numDots = 0;
  }
  
  public void draw(GL gl) {
    initDraw(gl);
    sorb.draw(gl);
    endDraw(gl);
  }
}
