import javax.media.opengl.*;

/**
 * A class to represent a atom with SP3 hybridization.  The class is composed of four POrbital objects.
 * Orbital 0 lies along the atom's Z axis.  
 * Orbital 1 is in the YZ plane.  When the atom's insideOutness == 0.5, it lies along the atom's +Y axis.  
 * Orbital 2 points on the +X side of the YZ plane.  When insideOutness == 0.5, it lies in the XY plane.
 * Orbital 3 points on the -X side of the YZ plane.  When insideOutness == 0.5, it lies in the XY plane.
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

public class SP3Atom extends Atom {
  private POrbital porb[] = new POrbital[4];
  public double insideOutness; // In range [0, 1]
  
  /**
   * Holds the atom's own rotation matrix.  Logically, this should live in RefFrame.  For reasons of efficiency, 
   * however, it doesn't.
   */
  private Matrix rotMatrix;
  
  public SP3Atom(Point3D pt) {
    super(pt);
    insideOutness = 0;
    porb[0] = new POrbital(0, 0, 0);
    porb[1] = new POrbital(109.5, 0, 0);
    porb[2] = new POrbital(109.5, 0, 120);
    porb[3] = new POrbital(109.5, 0, -120);
    rotMatrix = Matrix.makeRotationMatrix(0, Matrix.Axis.X); // Identity matrix
  }

  public SP3Atom() {
    this(new Point3D());
  }
  
  public void setInsideOutness(double d) {
    insideOutness = d;
    porb[0].setProportion(POrbital.SP3_PROP * (1.0 - d) + (1.0 - POrbital.SP3_PROP) * d);
    double rotation = this.getXRotation(); //109.5*(1.0 - insideOutness) + ((180.0 - 109.5) * insideOutness);
    porb[1].setRot(rotation, 0, 0);
    porb[2].setRot(rotation, 0, 120);
    porb[3].setRot(rotation, 0, -120);
  }
  
  public double getInsideOutness() {
    return insideOutness;
  }
  
  public double getXRotation() {
    return 109.5*(1.0 - insideOutness) + ((180.0 - 109.5) * insideOutness);
  }
  
  /**
   * Returns a Point3D representing the unit vector from the nucleus down the center of the given orbital.
   * For orbital 0, the unit vector always points towards the lobe that would be the large lobe if insideOutness < 0.5.
   * 
   * NOTE: this method takes no account of the fact that this atom's rotations may be nested inside other rotations.
   * It can therefore be expected to fail if called on an SP3 atom that isn't defined in world coordinates (as is likely
   * if the atom is part of a larger substituent group not centered on this atom).
   * 
   * @param i Index of the desired orbital
   * @return Unit vector as a Point3D
   */
  public Point3D getOrbitalVector(int i) {
    Point3D result = new Point3D(0, 0, 1);  // Unit vector along the z-axis
    
    // Transform it by the atom's own rotations
    // Then associate it with the appropriate orbital
    if (i == 0) { // Orbital 0 lies along the z-axis
      result = result.transform(rotMatrix);
    }
    else if (i == 1) {
      Matrix rotX = Matrix.makeRotationMatrix(getXRotation(), Matrix.Axis.X);
      Matrix m = rotMatrix.mult(rotX);
      result = result.transform(m);
    }
    else if (i == 2) {
      Matrix rotX = Matrix.makeRotationMatrix(getXRotation(), Matrix.Axis.X);
      Matrix rotZ = Matrix.makeRotationMatrix(120, Matrix.Axis.Z);
      Matrix rotZX = rotZ.mult(rotX);
      Matrix m = rotMatrix.mult(rotZX);
      result = result.transform(m);
    }
    else if (i == 3) {
      Matrix rotX = Matrix.makeRotationMatrix(getXRotation(), Matrix.Axis.X);
      Matrix rotZ = Matrix.makeRotationMatrix(-120, Matrix.Axis.Z);
      Matrix rotZX = rotZ.mult(rotX);
      Matrix m = rotMatrix.mult(rotZX);
      result = result.transform(m);
    }

    return result;
  }
  
  public void setRot(double rx, double ry, double rz) {
    super.setRot(rx, ry, rz);
    Matrix mx = Matrix.makeRotationMatrix(rx, Matrix.Axis.X);
    Matrix my = Matrix.makeRotationMatrix(ry, Matrix.Axis.Y);
    Matrix mz = Matrix.makeRotationMatrix(rz, Matrix.Axis.Z);
    Matrix myx = my.mult(mx);
    rotMatrix = mz.mult(myx);
    
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
