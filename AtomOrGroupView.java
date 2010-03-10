import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.util.Hashtable;

public abstract class AtomOrGroupView extends Drawable {
  private AtomOrGroup frame;
  private String text;
  private Hashtable<TextAttribute, Object> map;
  
  protected AtomOrGroupView(AtomOrGroup frame, String text) {
    super(frame);
    this.frame = frame;
    this.text = text;
    map = new Hashtable<TextAttribute, Object>();
    map.put(TextAttribute.SIZE, new Float(View2D.FONT_SIZE*1.1f));
    map.put(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER);
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
  
  public void draw2D(Graphics2D g) {
    java.awt.Font f = g.getFont();
    g.setFont(f.deriveFont(View2D.FONT_SIZE)); // font size is in points
//    java.awt.FontMetrics fm = g.getFontMetrics();
//    System.out.print(fm.getAscent() + " " + fm.getDescent() + " " + fm.getLeading() + " ");
    Rectangle2D textRect = g.getFontMetrics().getStringBounds(text, g);
//    System.out.println(textRect);
    java.awt.geom.Point2D pt2d = frame.getPt2D();
    // Remember, this thing is specified from the baseline, not one of the corners
//    System.out.println(textRect);

    // Find the left end of the baseline.  
    // getCenterX() and getCenterY() work perfectly for drawString because the textRect is specified baseline-relative,
    // and drawString wants to know where to start the baseline.
    double startX = pt2d.getX() - textRect.getCenterX();
    double startY = pt2d.getY() - textRect.getCenterY();
    g.drawString(text, (float)startX, (float)startY);
    
    // Display the charge, if any.
    if (frame.getCharge() != AtomOrGroup.Charge.NEUTRAL) { // Don't display a neutral charge
      displayCharge(g, (float)(startX + textRect.getWidth()), (float)startY);
    }
    
    g.setFont(f); // reset the font
  }
  
}