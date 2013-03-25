import javax.media.opengl.*;
import com.jogamp.opengl.util.awt.TextRenderer;

/**
 * Class to draw a RefFrame (atom, group of atoms, bond, or orbital).  
 * This class contains (stub) methods to display the object both in 2D and in 3D.
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
public class Drawable {
  private RefFrame frame;
  
  public Drawable(RefFrame frame) {
    this.frame = frame;
  }
  
  protected RefFrame frame() { return frame; }
  
  public void initXfm(GL2 gl) {
    gl.glPushMatrix();
    initXfmForFrame(gl, frame);
  }
  
  protected void initXfmForFrame(GL2 gl, RefFrame thisFrame) {
    if (thisFrame.getParent() != null) {
      initXfmForFrame(gl, thisFrame.getParent());
    }
    gl.glTranslated(thisFrame.getX(), thisFrame.getY(), thisFrame.getZ());
    gl.glRotated(thisFrame.getRotX(), 1.0, 0.0, 0.0);
    gl.glRotated(thisFrame.getRotY(), 0.0, 1.0, 0.0);
    gl.glRotated(thisFrame.getRotZ(), 0.0, 0.0, 1.0);
  }
  
  public void endXfm(GL2 gl) {
    gl.glPopMatrix();
  }
  
  public void initDraw(GL2 gl) {
    initXfm(gl);
  }
  
  public void endDraw(GL2 gl) {
    endXfm(gl);
  }
  
  public void initDraw2D(GL2 gl, TextRenderer tr) {
	  initXfm(gl);
	  System.out.println("initDraw2D called for"+this);
	  gl.glRotated(90.0, 0.0, 1.0, 0.0);
	  gl.glTranslated(0,  0,  0);
  }
  
  public void endDraw2D(GL2 gl, TextRenderer tr) {
	  endXfm(gl);
  }
  
  public void draw(GL2 gl) { }
  
  public void draw2D(java.awt.Graphics2D g) { }
  
  public void draw2D(GL2 gl, TextRenderer tr) { }
}