import javax.media.opengl.*;
import java.awt.Color;

/**
 * A class to view an atom.  The basic difference between this and a bare Drawable
 * is that an AtomView has a notion of color.
 */
public class AtomView extends AtomOrGroupView {
  public static final float OPACITY = 0.3f;
  public static final Color C_BLACK = new Color(0, 0, 0, OPACITY);
  public static final Color CL_GREEN = new Color(0, 1, 0, OPACITY);
  public static final Color H_WHITE = new Color(1, 1, 1, OPACITY);
  public static final Color O_RED = new Color(1, 0, 0, OPACITY);

  private double colors[] = new double[4];
  private float atomColor[] = {0, 0, 0, OPACITY};
  private Color textColor;

  public AtomView(Atom frame, String text, Color col) {
    super(frame, text);
    atomColor = col.getComponents(atomColor);
    textColor = new Color(col.getRed(), col.getGreen(), col.getBlue(), 255);
    if (textColor.equals(Color.WHITE)) { // White text won't show up
      textColor = Color.DARK_GRAY;
    }
  }
  
  protected void setColor(GL gl) {
    gl.glGetDoublev(GL.GL_CURRENT_COLOR, colors, 0); 
    gl.glColor4fv(atomColor, 0);
  }
  
  protected void restoreColor(GL gl) {
    gl.glColor4dv(colors, 0);
  }
  
  public void initDraw(GL gl) {
    super.initDraw(gl);
    setColor(gl);
  }
  
  public void endDraw(GL gl) {
    restoreColor(gl);
    super.endDraw(gl);
  }
  
  public void draw2D(java.awt.Graphics2D g) {
    java.awt.Paint paint = g.getPaint();
    g.setPaint(textColor);
    super.draw2D(g);
    g.setPaint(paint);
  }
}
