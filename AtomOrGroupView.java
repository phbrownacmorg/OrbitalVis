//import java.awt.Font;
import java.awt.Color;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.util.Hashtable;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.awt.TextRenderer; 

/**
 * View for an AtomOrGroup.
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
public abstract class AtomOrGroupView extends Drawable {
  private AtomOrGroup frame;
  private String text;
  protected Color textColor;
  
  private Hashtable<TextAttribute, Object> map; // Is this even used?
  
  protected AtomOrGroupView(AtomOrGroup frame, String text, Color tColor) {
    super(frame);
    this.frame = frame;
    this.text = text;
    this.textColor = tColor;
    if (textColor.equals(Color.WHITE)) { // White text won't show up
    	textColor = Color.DARK_GRAY;
    }

    map = new Hashtable<TextAttribute, Object>();
    map.put(TextAttribute.SIZE, new Float(View2D.FONT_SIZE*1.1f));
    map.put(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER);
  }
  
  protected AtomOrGroupView(AtomOrGroup frame, String text) {
	  this(frame, text, Color.BLACK);
  }
  
  /**
   * Display the charge.  Note that this is intended to be called ONLY from draw2D().
   * In particular, this method relies on draw2D() to save the original font and 
   * restore it at the end.
   */
//  protected void displayCharge(Graphics2D g, float x, float y) {
//    AtomOrGroup.Charge charge = frame.getCharge();
//    
//    // Don't do anything for a neutral charge
//    if (charge != AtomOrGroup.Charge.NEUTRAL) {
//      String chargeString = "";
//      if (charge == AtomOrGroup.Charge.MINUS) {
//        chargeString = "-";
//      }
//      else if (charge == AtomOrGroup.Charge.PART_MINUS) {
//        chargeString = "\u03B4-";
//      }
//      else if (charge == AtomOrGroup.Charge.PART_PLUS) {
//        chargeString = "\u03B4+";
//      }
//      else if (charge == AtomOrGroup.Charge.PLUS) {
//        chargeString = "+";
//      }
//      
//      g.setFont(g.getFont().deriveFont(map));
//      g.drawString(chargeString, x, y);
//    }
//  }
  
//  public void draw2D(Graphics2D g) {
//    java.awt.Font f = g.getFont();
//    g.setFont(f.deriveFont(View2D.FONT_SIZE)); // font size is in points
////    java.awt.FontMetrics fm = g.getFontMetrics();
////    System.out.print(fm.getAscent() + " " + fm.getDescent() + " " + fm.getLeading() + " ");
//    Rectangle2D textRect = g.getFontMetrics().getStringBounds(text, g);
////    System.out.println(textRect);
//    java.awt.geom.Point2D pt2d = frame.getPt2D();
//    // Remember, this thing is specified from the baseline, not one of the corners
////    System.out.println(textRect);
//
//    // Find the left end of the baseline.  
//    // getCenterX() and getCenterY() work perfectly for drawString because the textRect is specified baseline-relative,
//    // and drawString wants to know where to start the baseline.
//    double startX = pt2d.getX() - textRect.getCenterX();
//    double startY = pt2d.getY() - textRect.getCenterY();
//    g.drawString(text, (float)startX, (float)startY);
//    
//    // Display the charge, if any.
//    if (frame.getCharge() != AtomOrGroup.Charge.NEUTRAL) { // Don't display a neutral charge
//      displayCharge(g, (float)(startX + textRect.getWidth()), (float)startY);
//    }
//    
//    g.setFont(f); // reset the font
//  }
  
 
  public String getChargeString() {
	  AtomOrGroup.Charge charge = frame.getCharge();
	  String chargeString = "";

	  // Don't do anything for a neutral charge
	  if (charge != AtomOrGroup.Charge.NEUTRAL) {
		  if (charge == AtomOrGroup.Charge.MINUS) {
			  chargeString = "-";
		  }
		  else if (charge == AtomOrGroup.Charge.PART_MINUS) {
			  chargeString = "\u03B4-";
		  }
		  else if (charge == AtomOrGroup.Charge.PART_PLUS) {
			  chargeString = "\u03B4+";
		  }
		  else if (charge == AtomOrGroup.Charge.PLUS) {
			  chargeString = "+";
		  }
	  }
	  return chargeString;
  }
  
  public void draw2D(GL2 gl, TextRenderer tr) {
	  this.initDraw2D(gl);
//	  ShapeBuilder.axes(gl);
	  
	  String textToDraw = text + this.getChargeString();
	  // Text-specific transformations
	  this.negateRotationsForFrame(gl, frame);
	  gl.glTranslated(frame.getTx2D(), frame.getTy2D(), 0);
	  gl.glScaled(View2D.FONT_SCALE / View2D.X_SCALE, View2D.FONT_SCALE / View2D.Y_SCALE, 
			  View2D.FONT_SCALE / View2D.Z_SCALE);

//	  System.out.printf("Drawing %s at size %f, scales %f %f %f%n", 
//			  			text + this.getChargeString(), View2D.FONT_SCALE,
//			  			View2D.X_SCALE, View2D.Y_SCALE, View2D.Z_SCALE);
	  
	  Rectangle2D box = tr.getBounds(textToDraw);
	  drawBackingRectangle(gl, box);

	  tr.begin3DRendering();
	  tr.setColor(textColor);
	  tr.draw3D(textToDraw, 
			  (float)(-box.getCenterX()), (float)(box.getCenterY()), 0.0f, 1.0f);
	  tr.end3DRendering();

	  this.endDraw2D(gl);
  }
  
  private void drawBackingRectangle(GL2 gl, Rectangle2D box) {
	  double colors[] = new double[4];
	  gl.glGetDoublev(GL2.GL_CURRENT_COLOR, colors, 0);
	  
	  double x = frame.getX2D();
	  double y = frame.getY2D();
	  double halfWidth = box.getWidth()/2 + 2;
	  double halfHeight = box.getHeight()/2 + 2;
	  double minX = x - halfWidth;
	  double minY = y - halfHeight;
	  double maxX = x + halfWidth;
	  double maxY = y + halfHeight;
	  double rectZ = frame.getZ();
	  
	  gl.glBegin(GL2.GL_QUADS);
	  gl.glColor4fv(ConstantMgr.WHITE, 0);
	  gl.glVertex3d(minX, minY, rectZ);
	  gl.glVertex3d(minX, maxY, rectZ);
	  gl.glVertex3d(maxX, maxY, rectZ);
	  gl.glVertex3d(maxX, minY, rectZ);
	  gl.glEnd();
	  
	  // Restore the previous color
	  gl.glColor4dv(colors, 0);
  }
  
}