import javax.media.opengl.*;

/**
 * Class to draw a RefFrame (atom, group of atoms, bond, or orbital).  
 * This class contains (stub) methods to display the object both in 2D and in 3D.
 */
public class Drawable {
  private RefFrame frame;
  
  public Drawable(RefFrame frame) {
    this.frame = frame;
  }
  
  protected RefFrame frame() { return frame; }
  
  public void initXfm(GL gl) {
    gl.glPushMatrix();
    gl.glTranslated(frame.getX(), frame.getY(), frame.getZ());
    gl.glRotated(frame.getRotZ(), 0.0, 0.0, 1.0);
    gl.glRotated(frame.getRotY(), 0.0, 1.0, 0.0);
    gl.glRotated(frame.getRotX(), 1.0, 0.0, 0.0);
  }
  
  public void endXfm(GL gl) {
    gl.glPopMatrix();
  }
  
  public void initDraw(GL gl) {
    initXfm(gl);
  }
  
  public void endDraw(GL gl) {
    endXfm(gl);
  }
  
  public void draw(GL gl) { }
  
  public void draw2D(java.awt.Graphics2D g) { }
}