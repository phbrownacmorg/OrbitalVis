import javax.media.opengl.*;

/**
 * A class to view an atom with SP3 hybridization
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
