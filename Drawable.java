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
  
  protected void unwindRotationsForFrame(GL2 gl, RefFrame thisFrame) {
	  gl.glRotated(-thisFrame.getRotX(), 1.0, 0.0, 0.0);
	  gl.glRotated(-thisFrame.getRotY(), 0.0, 1.0, 0.0);
	  gl.glRotated(-thisFrame.getRotZ(), 0.0, 0.0, 1.0);
	  if (thisFrame.getParent() != null) {
		  unwindRotationsForFrame(gl, thisFrame.getParent());
	  }
  }

  protected void applyRotationsForFrame(GL2 gl, RefFrame thisFrame) {
	  gl.glRotated(thisFrame.getRotZ(), 0.0, 0.0, 1.0);
	  gl.glRotated(thisFrame.getRotY(), 0.0, 1.0, 0.0);
	  gl.glRotated(thisFrame.getRotX(), 1.0, 0.0, 0.0);
  }
  
  protected void initXfmForFrame(GL2 gl, RefFrame thisFrame) {
    if (thisFrame.getParent() != null) {
      initXfmForFrame(gl, thisFrame.getParent());
    }
    gl.glTranslated(thisFrame.getX(), thisFrame.getY(), thisFrame.getZ());
    this.applyRotationsForFrame(gl, thisFrame);
  }
  
  public void endXfm(GL2 gl) {
    gl.glPopMatrix();
  }
  
  public void initDraw(GL2 gl) {
    initXfm(gl);
  }
  
  public void apply2DOffsets(GL2 gl) {
	  gl.glTranslated(0,
				this.frame.getX() * View2D.Z_OFFSET_FACTOR[1] + this.frame.getY() * View2D.Y_OFFSET_FACTOR[1],
				this.frame.getX() * -View2D.Z_OFFSET_FACTOR[0] + this.frame.getY() * View2D.Y_OFFSET_FACTOR[0]); 
  }
  
  public void initDraw2D(GL2 gl) {
	  //System.out.println(String.format("Drawable::initDraw2D(): offsets: (%f %f), (%f %f)", View2D.Z_OFFSET_FACTOR[0], View2D.Z_OFFSET_FACTOR[1], View2D.Y_OFFSET_FACTOR[0], View2D.Y_OFFSET_FACTOR[1]));
	  this.initXfm(gl);
  }
  
  public void endDraw(GL2 gl) {
    endXfm(gl);
  }
    
  public void draw(GL2 gl) { }
  
  public void draw2D(java.awt.Graphics2D g) { }
  
  public void draw2D(GL2 gl, TextRenderer tr, TextRenderer superTR) { }
}