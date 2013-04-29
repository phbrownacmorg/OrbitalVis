import javax.media.opengl.GL2;

import com.jogamp.opengl.util.awt.TextRenderer;


public class SP3BondView extends BondView {

	public SP3BondView(SP3Bond bond) {
		super(bond);
		// TODO Auto-generated constructor stub
	}

	  public void draw2D(GL2 gl, TextRenderer tr, TextRenderer superTR) {
		  double colors[] = new double[4];
		  gl.glGetDoublev(GL2.GL_CURRENT_COLOR, colors, 0);
		  
		  this.initDraw2D(gl); // Frame1's transformation
		  this.apply2DOffsets(gl);
		  RefFrame start = bond.getStart3D();
		  RefFrame end = bond.getEnd3D();
		  
//		  if (end.getParent() == start) {
//			  this.applyRotationsForFrame(gl, end);
//		  } 
		  
		  gl.glBegin(GL2.GL_LINES);
		  gl.glColor3i(BOND_COLOR.getRed(), BOND_COLOR.getGreen(), BOND_COLOR.getBlue());
		  Point3D endPt = ((SP3Bond)(bond)).getBondVector();
		  
		  if (bond.getState() == Bond.State.FULL) {
			// Draw a regular bond
			  int dir = bond.getDepthDir();
			  switch (dir) {
			  	case 0: // Draw a line
//			  		ShapeBuilder.axes(gl);
			  		gl.glVertex3d(0, 0, 0); // Starting frame; we have made the 3D transform handle this one
//			  		gl.glVertex3d(0, 1, 0);
			  	    gl.glVertex3d(endPt.x(), endPt.y(), endPt.z());
			  		break;
			  	case 1: // Draw a dashed wedge
			  		break;
			  	case -1: // Draw a wedge
			  		break;
			  }
		  }
		  else if (bond.getState() == Bond.State.PARTIAL) {
			  // Draw a dashed line for the bond
	          // All partial bonds are assumed to lie in the plane of the screen.
		      //    This is mainly because I don't really know how to handle a partial
		      //    bond that isn't in the plane of the screen.
		  }
		  else if ((bond.getState() == Bond.State.DOUBLE) || (bond.getState() == Bond.State.FULL_PARTIAL)) {
			  // Draw a double line for the bond
			  // Double lines are assumed to lie in the plane of the screen.
		  }
		  // If the bond's state is BROKEN, naturally, do nothing
		  gl.glEnd();
		  this.endDraw(gl);
		  // Restore the previous color
		  gl.glColor4dv(colors, 0);
	  }

}
