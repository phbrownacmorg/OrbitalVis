import java.awt.Color;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
//import java.awt.image.BufferedImage;


import com.jogamp.opengl.util.awt.TextRenderer;

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
  public static final Color BOND_COLOR = Color.DARK_GRAY;
  public static final float[] BOND_COLOR_4V = BOND_COLOR.getRGBComponents(null);
  
  public static final short FULL_BOND = (short) 0xFFFF;
  public static final short PARTIAL_BOND = (short) 0xFF00;
  public static final float LINE_WIDTH = 2;
  public static final byte[] RECEDING_WEDGE = { 
//	  0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xF, 0xF, 0xF, 0xF, 0xF, 0xF, 0xF, 0xF,
//	  0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xF, 0xF, 0xF, 0xF, 0xF, 0xF, 0xF, 0xF,
//	  0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xF, 0xF, 0xF, 0xF, 0xF, 0xF, 0xF, 0xF,
//	  0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xF, 0xF, 0xF, 0xF, 0xF, 0xF, 0xF, 0xF,
//	  0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xF, 0xF, 0xF, 0xF, 0xF, 0xF, 0xF, 0xF,
//	  0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xF, 0xF, 0xF, 0xF, 0xF, 0xF, 0xF, 0xF,
//	  0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xF, 0xF, 0xF, 0xF, 0xF, 0xF, 0xF, 0xF,
//	  0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xF, 0xF, 0xF, 0xF, 0xF, 0xF, 0xF, 0xF };
	  0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
	  (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, 
	  (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, 
	  0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
	  (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, 
	  (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, 
	  0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
	  (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, 
	  (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, 
	  0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
	  (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, 
	  (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, 

	  0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
	  (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, 
	  (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, 
	  0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
	  (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, 
	  (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, 
	  0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
	  (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, 
	  (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, 
	  0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
	  (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, 
	  (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF };
	  
  public static final int WEDGE_WIDTH = 4;
  
  /**
   * Wedges only cover a part of the distance between the things they bond, so as to avoid colliding
   * in ugly ways with the text at each end.
   */
  public static final double WEDGE_SCALE_FACTOR = 0.75;
  
  /**
   * Lines also cover only a part of the distance between the things they bond, for exactly the
   * same reason.
   */
  public static final double LINE_SCALE_FACTOR = 0.8;
  public static final double ONE_MINUS_LINE_SCALE_FACTOR = 1.0 - LINE_SCALE_FACTOR;
  public static final double LINE_OFFSET = 0;
  
  private Bond bond;
  private double tx, ty;
  
  public BondView(Bond bond) {
    super(bond.getStart3D());
    this.bond = bond;
    this.tx = bond.getStart3D().getTx2D();
    this.ty = bond.getStart3D().getTy2D();
    bond.getEnd3D().getTx2D();
    bond.getEnd3D().getTy2D();
    
//    wedge = new Path2D.Double();
//    wedge.moveTo(0, 0);
//    wedge.lineTo(1, -.1);
//    wedge.lineTo(1, .1);
//    wedge.lineTo(0, 0);
  }
    
  private void drawSingleBondLine(GL2 gl, short stipple, double offX, double offY) {
	  Point3D vector = bond.getVectorFor2D().translate(offX+this.tx, offY+this.ty, 0);
	  gl.glLineWidth(LINE_WIDTH);
	  gl.glLineStipple(1, stipple);
	  gl.glEnable(GL2.GL_LINE_STIPPLE);
	  //ShapeBuilder.line(gl, vectorStart, vectorEnd);
	  ShapeBuilder.line(gl, new Point3D(offX+this.tx, offY+this.ty, 0), vector);
	  gl.glDisable(GL2.GL_LINE_STIPPLE);
  }

  private void drawDoubleBondLine(GL2 gl, short stipple) {
	  Point3D vector = bond.getVectorFor2D();
	  Point3D offset = vector.crossWithZ().scale(LINE_WIDTH*View2D.UNIT_TO_PIXEL_FUDGE);
	  drawSingleBondLine(gl, FULL_BOND, offset.x(), offset.y());
	  drawSingleBondLine(gl, stipple, -offset.x(), -offset.y());
  }
  
  private void drawWedge(GL2 gl, byte[] stipple) {
	  Point3D vector = bond.getVectorFor2D();
	  Point3D offset = vector.crossWithZ().scale(View2D.UNIT_TO_PIXEL_FUDGE);
//	  double startTx = WEDGE_SCALE_FACTOR * tx + (1.0 - WEDGE_SCALE_FACTOR) * endTx; 
//	  double startTy = WEDGE_SCALE_FACTOR * ty + (1.0 - WEDGE_SCALE_FACTOR) * endTy; 
	  Point3D wedgeStart = vector.scale(1.0 - WEDGE_SCALE_FACTOR); //.translate(startTx, startTy, 0);
//	  double wedgeEndTx = (1.0 - WEDGE_SCALE_FACTOR) * tx + WEDGE_SCALE_FACTOR * endTx; 
//	  double wedgeEndTy = (1.0 - WEDGE_SCALE_FACTOR) * ty + WEDGE_SCALE_FACTOR * endTy; 
	  Point3D wedgeEnd = vector.scale(WEDGE_SCALE_FACTOR); //.translate(wedgeEndTx, wedgeEndTy, 0);
//	  System.out.printf("start: %s%nend: %s%nvec: %s%nwedge: %s - %s%n", bond.getStart3D(),
//			  			bond.getEnd3D(), vector, wedgeStart, wedgeEnd);
	  
	  
	  if (stipple != null) {
		  gl.glEnable(GL2.GL_POLYGON_STIPPLE);
		  gl.glPolygonStipple(stipple, 0);
	  }
	  gl.glBegin(GL.GL_TRIANGLES);
	  gl.glVertex3d(wedgeStart.x(), wedgeStart.y(), wedgeStart.z());
	  gl.glVertex3d(wedgeEnd.x() + WEDGE_WIDTH*offset.x(), 
			  		wedgeEnd.y() + WEDGE_WIDTH*offset.y(), wedgeEnd.z());
	  gl.glVertex3d(wedgeEnd.x() - WEDGE_WIDTH*offset.x(), 
		  		wedgeEnd.y() - WEDGE_WIDTH*offset.y(), wedgeEnd.z());
	  gl.glEnd();
	  if (stipple != null) {
		  gl.glDisable(GL2.GL_POLYGON_STIPPLE);
	  }
  }
  
//  private void drawWedge(Graphics2D g, Point2D start, Point2D end) {
//    double dist = start.distance(end);
//    double scale = dist * WEDGE_SCALE_FACTOR;
//    AffineTransform xfm = new AffineTransform();
//    xfm.setToIdentity();
//    xfm.rotate(end.getX() - start.getX(), end.getY() - start.getY(), start.getX(), start.getY());
//    xfm.translate(start.getX() + (dist * (1.0 - WEDGE_SCALE_FACTOR)/2.0), start.getY());
//    xfm.scale(scale, scale);
//    g.setTransform(xfm);
//    g.fill(wedge);
//  }
  
  public void draw2D(GL2 gl, TextRenderer tr) {
	  this.initDraw(gl);
	  
	  AtomOrGroup start = bond.getStart3D();
	  AtomOrGroup end = bond.getEnd3D();
	  if (end.getParent() != start) {
		  this.negateRotationsForFrame(gl, start);
	  }
	  gl.glColor4fv(BOND_COLOR_4V, 0);
	  if (bond.getState() == Bond.State.FULL) {
//		  System.out.println("start:" + start);
//		  System.out.println("end:" + end);
//		  Point3D vec = bond.getVector3D();
//		  System.out.println("vec:" + vec);
//		  System.out.println(start.distanceTo(end) + " ?= " + vec.distanceTo(new Point3D(0, 0, 0)));
		  
		  switch (bond.getDepthDir()) {
		  case 0:
			  drawSingleBondLine(gl, FULL_BOND, 0, 0);
			  break;
		  case 1:
			  drawWedge(gl, null);
			  break;
		  case -1:
			  drawWedge(gl, RECEDING_WEDGE);
			  break;
		  }
	  }
	  else if (bond.getState() == Bond.State.PARTIAL) {
		  // Assumed to lie in the XY plane
		  drawSingleBondLine(gl, PARTIAL_BOND, 0, 0);
	  }
	  else if (bond.getState() == Bond.State.DOUBLE) {
		  drawDoubleBondLine(gl, FULL_BOND);
	  }
	  else if (bond.getState() == Bond.State.FULL_PARTIAL) {
		  drawDoubleBondLine(gl, PARTIAL_BOND);
	  }
	  // Otherwise, do nothing
	  
	  this.endDraw(gl);
  }
  
//  public void draw2D(Graphics2D g) {
//    Paint oldPaint = g.getPaint();
//    g.setPaint(BOND_COLOR);
//    Point2D start = bond.getStart();
//    Point2D end = bond.getEnd();
//    if (bond.getState() == Bond.State.FULL) {
//      // Draw a regular bond
//      int dir = bond.getDepthDir();
//      switch (dir) {
//        case 0:
//          drawLine(g, start, end);
////          g.draw(new java.awt.geom.Line2D.Double(bond.getStart(), bond.getEnd()));
//          break;
//        case 1:
//          g.setPaint(pattern);
//        case -1:
//          AffineTransform oldXfm = g.getTransform();
//          drawWedge(g, start, end);
//          g.setTransform(oldXfm);
//          break;
//      }
//    }
//    else if (bond.getState() == Bond.State.PARTIAL) {
//      // Draw a dashed line for the bond
//      // All partial bonds are assumed to lie in the plane of the screen.
//      //    This is mainly because I don't really know how to handle a partial
//      //    bond that isn't in the plane of the screen.
//      Stroke oldStroke = g.getStroke();
//      g.setStroke(dashedStroke);
//      drawLine(g, start, end);
//      g.setStroke(oldStroke);
//    }
//    else if ((bond.getState() == Bond.State.DOUBLE) || (bond.getState() == Bond.State.FULL_PARTIAL)) {
//      // Draw a double line for the bond
//      // Double lines are assumed to lie in the plane of the screen.
//      AffineTransform oldXfm = g.getTransform();
//      Stroke oldStroke = g.getStroke();
//      drawDoubleLine(g, start, end, (bond.getState() == Bond.State.FULL_PARTIAL));
//      g.setStroke(oldStroke);
//      g.setTransform(oldXfm);
//    }
//    // If the bond's state is BROKEN, don't draw anything (naturally)
//    g.setPaint(oldPaint);
//  }
}