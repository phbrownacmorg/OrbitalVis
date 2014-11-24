//import javax.media.opengl.*;

/**
 * A class to represent a atom with SP3 hybridization.  The class is composed of four POrbital objects.
 * Orbital 0 lies along the atom's X axis.  
 * Orbital 1 is in the XY plane.  When the atom's insideOutness == 0.5, it lies along the atom's -Y axis.  
 * Orbital 2 points on the -Z side of the XY plane.  When insideOutness == 0.5, it lies in the YZ plane.
 * Orbital 3 points on the +Z side of the XY plane.  When insideOutness == 0.5, it lies in the YZ plane.
 *
 * Copyright 2010 Peter Brown <phbrown@acm.org> and Madonna King
 * Copyright 2014 Peter Brown and Ashish Nicodemus
 *
 * This code is distributed under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the
 * license or (at your option) any later version.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

public class SP3Atom extends Atom {
	/**
	 * The four electron orbitals of the SP3Atom. 
	 */
  private POrbital porb[] = new POrbital[4];
  
  /**
   * Controls the degree to which orbital 0 lies along the +X axis as opposed to the -X axis.
   * The value is in the range from 0 (orbital 0 has POrbital.SP3_PROP of its volume along the +X axis)
   * to 1 (orbital 0 has POrbital.SP3_PROP of its volume along the -X axis).  A value of 0.5 means that
   * orbital 0 is fully a p-orbital (not hybridized), evenly split between the +X and -X axes. 
   */
  private double insideOutness; // In range [0, 1]
  
  /**
   * Angle in degrees between orbitals 0 and 1.  This value is in the range [60, 180].  The relaxed value
   * (the value if the atom isn't strained) is the arccos(-1/3) ~= 109.5 degees.
   */
  private double zeroOneAngle;
  
  /**
   * Relaxed value of zeroOneAngle 109.5 degrees
   */
  public static final double RELAXED_ANGLE = Math.toDegrees(Math.acos(-1/3.0)); 
  
  /**
   * Create an SP3Atom at a given point, nested within a given frame of reference.
   * 
   * @param pt Point3D at which to create the SP3Atom
   * @param parent RefFrame within which the Point3D is nested
   */
  public SP3Atom(Point3D pt, RefFrame parent) {
    super(pt, parent);
    insideOutness = 0;
    // adjusts the position of the atom
    zeroOneAngle = RELAXED_ANGLE;
    // 109.5 degrees
    porb[0] = new POrbital(0, 0, 0);
    porb[1] = new POrbital(0, 0, -zeroOneAngle);
    porb[2] = new POrbital(120, 0, -zeroOneAngle);
    porb[3] = new POrbital(-120, 0, -zeroOneAngle);
    
  }

  /**
   * Create an SP3Atom at a given point, with no containing frame of reference.
   * 
   * @param pt Point3D at which to create the SP3Atom
   */
  public SP3Atom(Point3D pt) {
    this(pt, null);
  }
  
  
  /**
   * Create an SP3Atom at the origin with no containing frame of reference. 
   */
  public SP3Atom() {
    this(new Point3D());
  }
  
  public void setOrbital0XRotation(double d) {
	  porb[0].setRot(d, 0, 0);
  }
  
  /**
   * Takes the Z rotation from orbital 1 and maps it through to the rest of the orbitals.
   */
  protected void setOrbitalRotations() {
	  // Orbital 0 is assumed to not rotate
	  for (int i = 1; i <= 3; i++) {
		  porb[i].setRot(120 * (i-1), 0, this.getZRotation(i));
	  }
  }
  
  /**
   * Sets the insideOutness member to the given value.  Update the proportion member of orbital 0 and the rotations
   * of the other orbitals accordingly.
   * 
   * @param d New value for insideOutness. 0 <= d <= 1.
   */
  public void setInsideOutness(double d) {
	  // assert 0 <= d <= 1
    insideOutness = d;
    porb[0].setProportion(POrbital.SP3_PROP * (1.0 - d) + (1.0 - POrbital.SP3_PROP) * d);
    //double rotation = this.getZRotation(); //RELAXED_ANGLE*(1.0 - insideOutness) + ((180.0 - RELAXED_ANGLE) * insideOutness);
    this.setOrbitalRotations();
//    porb[1].setRot(0, 0, this.getZRotation(1));
//    porb[2].setRot(120, 0, this.getZRotation(2));
//    porb[3].setRot(-120, 0, this.getZRotation(3));
  }
  
  public void setZeroOneAngle(double theta) {
	  // assert 60 <= theta <= 180 (degrees)
	  zeroOneAngle = theta;
	  this.setOrbitalRotations();
  }
  
  public double getInsideOutness() {
    return insideOutness;
  }
  
  public double getZRotation() {
	  return getZRotation(1);
	  // of orbital 1
  }
  
  public double getZRotation(int orbitalNum) {
	  // assert 1 <= orbitalNum <= 3 
	  double angle = zeroOneAngle;
	  if ((orbitalNum > 1) && ((zeroOneAngle > 110) || zeroOneAngle < 109)) { // RELAXED_ANGLE)) {
		  // Filthy hack.  Chemists (and mathematicians), please avert your eyes.  
		  // But it seems to *look* OK, which is good enough for graphics.
		  double zeroOneProp = zeroOneAngle / RELAXED_ANGLE;
		  angle = zeroOneProp * RELAXED_ANGLE + (1.0 - zeroOneProp) * 120.0;
	  }
	  return -(angle*(1.0 - insideOutness) + ((180.0 - angle) * insideOutness)); 
  }
  
  /**
   * Returns a Point3D representing the unit vector IN WORLD COORDINATES from the nucleus down the center of the given
   * orbital. For orbital 0, the unit vector always points towards the lobe that would be the large lobe if 
   * insideOutness < 0.5.
   * 
   * NOTE: this method takes no account of the fact that this atom's rotations may be nested inside other rotations.
   * It can therefore be expected to fail if called on an SP3 atom that isn't defined in world coordinates (as is likely
   * if the atom is part of a larger substituent group not centered on this atom).
   * 
   * @param i Index of the desired orbital
   * @return Unit vector as a Point3D
   */
  public Point3D getAbsOrbitalLoc(int i, double scalingFactor) {
    Point3D result = new Point3D(scalingFactor, 0, 0);  // Unit vector along the +x-axis
    
    // Transform it by the atom's own rotations
    // Then associate it with the appropriate orbital
    if (i == 0) { // Orbital 0 lies along the x-axis
      result = result.transform(rotMatrix);
      // .transform is used to manipulate the position, rotation, or scale of an object
    }
    else if (i == 1) {
      Matrix rotZ = Matrix.makeRotationMatrix(getZRotation(1), Matrix.Axis.Z);
      Matrix m = rotMatrix.mult(rotZ);
      result = result.transform(m);
    }
    else if (i == 2) {
      Matrix rotZ = Matrix.makeRotationMatrix(getZRotation(2), Matrix.Axis.Z);
      Matrix rotX = Matrix.makeRotationMatrix(120, Matrix.Axis.X);
      Matrix rotXZ = rotX.mult(rotZ);
      Matrix m = rotMatrix.mult(rotXZ);
      result = result.transform(m);
    }
    else if (i == 3) {
      Matrix rotZ = Matrix.makeRotationMatrix(getZRotation(3), Matrix.Axis.Z);
      Matrix rotX = Matrix.makeRotationMatrix(-120, Matrix.Axis.X);
      Matrix rotXZ = rotX.mult(rotZ);
      Matrix m = rotMatrix.mult(rotXZ);
      result = result.transform(m);
    }

    result = result.translate(this.getX(), this.getY(), this.getZ());
    RefFrame parent = this.getParent();
    while (parent != null) {
        Matrix mx = Matrix.makeRotationMatrix(parent.getRotX(), Matrix.Axis.X);
        Matrix my = Matrix.makeRotationMatrix(parent.getRotY(), Matrix.Axis.Y);
        Matrix mz = Matrix.makeRotationMatrix(parent.getRotZ(), Matrix.Axis.Z);
        Matrix myz = my.mult(mz);
        Matrix m = mx.mult(myz);
        result = result.transform(m);
    	result = result.translate(parent.getX(), parent.getY(), parent.getZ());
    	parent = parent.getParent();
    }
    return result;
  }
  
  /**
   * Returns a Point3D representing the unit vector IN THE ATOM'S OWN FRAME from the nucleus down the center of the
   * given orbital. For orbital 0, the unit vector always points towards the lobe that would be the large lobe if 
   * insideOutness < 0.5.
   * 
   * @param i Index of the desired orbital
   * @return Unit vector as a Point3D
   */
  public Point3D getOrbitalVector(int i) {
    Point3D result = new Point3D(1, 0, 0);  // Unit vector along the x-axis
    
    // Transform it by the atom's own rotations
    // Then associate it with the appropriate orbital
    // [[FIX: FAILS IN CASE OF NESTED FRAMES]]
    if (i == 0) { // Orbital 0 lies along the x-axis
      //result = result.transform(rotMatrix);
    }
    else if (i == 1) {
      Matrix rotZ = Matrix.makeRotationMatrix(getZRotation(1), Matrix.Axis.Z);
      //Matrix m = rotMatrix.mult(rotX);
      result = result.transform(rotZ);
    }
    else if (i == 2) {
      Matrix rotZ = Matrix.makeRotationMatrix(getZRotation(2), Matrix.Axis.Z);
      Matrix rotX = Matrix.makeRotationMatrix(120, Matrix.Axis.X);
      Matrix rotXZ = rotX.mult(rotZ);
      //Matrix m = rotMatrix.mult(rotZX);
      result = result.transform(rotXZ);
    }
    else if (i == 3) {
      Matrix rotZ = Matrix.makeRotationMatrix(getZRotation(3), Matrix.Axis.Z);
      Matrix rotX = Matrix.makeRotationMatrix(-120, Matrix.Axis.X);
      Matrix rotXZ = rotX.mult(rotZ);
      //Matrix m = rotMatrix.mult(rotZX);
      result = result.transform(rotXZ);
    }

    return result;
  }
  
  public void setRot(double rx, double ry, double rz) {
    super.setRot(rx, ry, rz);
  }
  
  public double getP0DivergenceAngle() {
    return porb[0].centralAngle();
  }
  
  public void setP0Divergence(double d) {
    porb[0].setDivergence(d);
  }
  
  public SP3AtomView createView(String text, java.awt.Color color) {
    POrbitalView orbViews[] = new POrbitalView[4];
    for (int i = 0; i < 4; i++) {
      orbViews[i] = new POrbitalView(porb[i]);
    }
    return new SP3AtomView(this, text, color, orbViews);
  }
}
