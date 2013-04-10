//import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics2D;
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
    this.textColor = new java.awt.Color(textColor.getRed(), textColor.getGreen(), textColor.getBlue());

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
  protected void displayCharge(Graphics2D g, float x, float y) {
    AtomOrGroup.Charge charge = frame.getCharge();
    
    // Don't do anything for a neutral charge
    if (charge != AtomOrGroup.Charge.NEUTRAL) {
      String chargeString = "";
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
      
      g.setFont(g.getFont().deriveFont(map));
      g.drawString(chargeString, x, y);
    }
  }
  
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
  
   public void draw2D(GL2 gl, TextRenderer tr, TextRenderer superTR) {
	   this.initDraw(gl);

	   // Text-specific transformations
	   this.unwindRotationsForFrame(gl, frame);
	   gl.glRotated(-90, 0, 1, 0);
	   gl.glTranslated(-frame.getX() * View2D.Z_OFFSET_FACTOR, 0, 0);
	   gl.glScaled(View2D.FONT_SCALING_FACTOR / View2D.X_SCALE, View2D.FONT_SCALING_FACTOR / View2D.Y_SCALE, 
			   View2D.FONT_SCALING_FACTOR / View2D.Z_SCALE);
	   Rectangle2D bounds = tr.getBounds(text);
	   gl.glTranslated(-bounds.getWidth()/2.0, bounds.getY()/2.0, 0);
	   
	   tr.begin3DRendering();
	   tr.setColor(textColor);
	   // Eventually this is likely to be inadequate, as the geometry gets fancier
	   tr.draw3D(text, 0, 0, 0, 1.0f);
	   //System.out.println("AOGView "+this+" drawing text '"+text+"' at "+frame.toString());
	   tr.end3DRendering();

	   this.drawChargeString(gl, tr, superTR);
	    
	   this.endDraw(gl);
  }

   public void drawChargeString(GL2 gl, TextRenderer tr, TextRenderer supertr) {
	   //Renders charge 60% smaller than parent text, moved up 44% to emulate superscripting
	   AtomOrGroup.Charge charge = frame.getCharge();
	   String chargeString = "";
	    // Don't do anything for a neutral charge
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
	   // If neutral, leave charge string as empty
	   
	   if (chargeString.length() > 0) {
		   Rectangle2D bounds = tr.getBounds(text);
		   // 1.1 is an experimentally determined fudge factor
		   gl.glTranslated(bounds.getWidth() * 1.1, -bounds.getY() * .44, 0);
		   TextRenderer trToUse = tr;
		   if (chargeString.length() == 2) {
			   trToUse = supertr;
		   }
		   trToUse.begin3DRendering();
		   trToUse.setColor(textColor);
		   trToUse.draw3D(chargeString, 0, 0, 0, 1.0f);
		   trToUse.end3DRendering();
	   }
   }
}