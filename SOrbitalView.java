import javax.media.opengl.*;

/**
 * A class to represent an s-orbital.
 */

public class SOrbitalView {
  public static final double RADIUS = 0.2; // Was 0.3

  public void draw(GL gl) {
    gl.glPushMatrix();
    gl.glScaled(RADIUS, RADIUS, RADIUS);
    ShapeBuilder.sphere(gl);
    gl.glPopMatrix();
  }
  
}
