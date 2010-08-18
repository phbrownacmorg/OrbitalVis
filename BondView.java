import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

/**
 * Class to display a Bond.  Since bonds aren't represented explicitly in the 3D view,
 * this class only overrides the draw2D() method of Drawable.
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
public class BondView extends Drawable {
  public static final Color BOND_COLOR = Color.GRAY;

  /**
   * Wedges only cover a part of the distance between the things they bond, so as to avoid colliding
   * in ugly ways with the text at each end.
   */
  public static final double WEDGE_SCALE_FACTOR = 0.5;
  
  /**
   * Lines also cover only a part of the distance between the things they bond, for exactly the
   * same reason.
   */
  public static final double LINE_SCALE_FACTOR = 0.8;
  public static final double ONE_MINUS_LINE_SCALE_FACTOR = 1.0 - LINE_SCALE_FACTOR;
  public static final double LINE_OFFSET = 0;
  
  /**
   * Stroke pattern for partial bonds
   */
  private static final float[] dashPattern = { 5, 5 };
  
  /**
   * Pattern to draw rear-facing wedges.
   */
  private static Paint pattern = new java.awt.GradientPaint(0, 0, BOND_COLOR, 0.05f, 0, Color.WHITE, true);

  private Bond bond;
  
  /**
   * This is the actual stroke object for drawing partial bonds.
   */
  private Stroke dashedStroke;
  
  /**
   * This is the path used for drawing wedges.
   */
  private Path2D wedge;

  public BondView(Bond bond) {
    super(bond.getStart3D());
    this.bond = bond;
    dashedStroke = new BasicStroke(1, BasicStroke.CAP_SQUARE, 
                                   BasicStroke.JOIN_MITER, 10, dashPattern, 0);
    wedge = new Path2D.Double();
    wedge.moveTo(0, 0);
    wedge.lineTo(1, -.1);
    wedge.lineTo(1, .1);
    wedge.lineTo(0, 0);
  }
    
  private void drawLine(Graphics2D g, Point2D start, Point2D end) {
    double startX = start.getX();
    double startY = start.getY();
    double endX = end.getX();
    double endY = end.getY();
    g.draw(new Line2D.Double(startX + (endX - startX)*ONE_MINUS_LINE_SCALE_FACTOR,
                             startY + (endY - startY)*ONE_MINUS_LINE_SCALE_FACTOR,
                             startX + (endX - startX)*LINE_SCALE_FACTOR,
                             startY + (endY - startY)*LINE_SCALE_FACTOR));
  }

  private void drawWedge(Graphics2D g, Point2D start, Point2D end) {
    double dist = start.distance(end);
    double scale = dist * WEDGE_SCALE_FACTOR;
    AffineTransform xfm = new AffineTransform();
    xfm.setToIdentity();
    xfm.rotate(end.getX() - start.getX(), end.getY() - start.getY(), start.getX(), start.getY());
    xfm.translate(start.getX() + (dist * (1.0 - WEDGE_SCALE_FACTOR)/2.0), start.getY());
    xfm.scale(scale, scale);
    g.setTransform(xfm);
    g.fill(wedge);
  }
  
  private void drawDoubleLine(Graphics2D g, Point2D start, Point2D end, boolean dotOneLine) {
    double dist = start.distance(end);
    AffineTransform xfm = new AffineTransform();
    xfm.setToIdentity();
    xfm.rotate(end.getX() - start.getX(), end.getY() - start.getY(), start.getX(), start.getY());
    xfm.translate(start.getX(), start.getY());
    g.setTransform(xfm);
    g.draw(new Line2D.Double(dist * ONE_MINUS_LINE_SCALE_FACTOR, -2, 
                             dist * LINE_SCALE_FACTOR, -2));
    if (dotOneLine) {
      g.setStroke(dashedStroke);
    }
    g.draw(new Line2D.Double(dist * ONE_MINUS_LINE_SCALE_FACTOR, 2, 
                             dist * LINE_SCALE_FACTOR, 2));
  }
  
  public void draw2D(Graphics2D g) {
    Paint oldPaint = g.getPaint();
    g.setPaint(BOND_COLOR);
    Point2D start = bond.getStart();
    Point2D end = bond.getEnd();
    if (bond.getState() == Bond.State.FULL) {
      // Draw a regular bond
      int dir = bond.getDepthDir();
      switch (dir) {
        case 0:
          drawLine(g, start, end);
//          g.draw(new java.awt.geom.Line2D.Double(bond.getStart(), bond.getEnd()));
          break;
        case 1:
          g.setPaint(pattern);
        case -1:
          AffineTransform oldXfm = g.getTransform();
          drawWedge(g, start, end);
          g.setTransform(oldXfm);
          break;
      }
    }
    else if (bond.getState() == Bond.State.PARTIAL) {
      // Draw a dashed line for the bond
      // All partial bonds are asumed to lie in the plane of the screen.
      //    This is mainly because I don't really know how to handle a partial
      //    bond that isn't in the plane of the screen.
      Stroke oldStroke = g.getStroke();
      g.setStroke(dashedStroke);
      drawLine(g, start, end);
      g.setStroke(oldStroke);
    }
    else if ((bond.getState() == Bond.State.DOUBLE) || (bond.getState() == Bond.State.FULL_PARTIAL)) {
      // Draw a double line for the bond
      // Double lines are assumed to lie in the plane of the screen.
      AffineTransform oldXfm = g.getTransform();
      Stroke oldStroke = g.getStroke();
      drawDoubleLine(g, start, end, (bond.getState() == Bond.State.FULL_PARTIAL));
      g.setStroke(oldStroke);
      g.setTransform(oldXfm);
    }
    // If the bond's state is BROKEN, don't draw anything (naturally)
    g.setPaint(oldPaint);
  }
}