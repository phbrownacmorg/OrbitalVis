/**
 * Class to represent a matrix.  Objects of this class are immutable.
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

public class Matrix {
  public static final double EPSILON = 0.0000001;
  public static boolean fpEquals(double num1, double num2) {
    return (Math.abs(num1 - num2) < EPSILON);
  }
  
  public enum Axis { X, Y, Z };
  private double elt[][];
  
  public Matrix(int rows, int cols) {
    elt = new double[rows][];
    for (int i = 0; i < rows; i++) {
      elt[i] = new double[cols];
    }
  }
  
  public int rows() { return elt.length; }
  public int cols() { return elt[0].length; }
  public double elt(int i, int j) { return elt[i][j]; }
  
  /**
   * Create and return a new Matrix that is the product of this Matrix 
   * right-multiplied by the given Matrix. This method does not change
   * either of the operand matrices.
   * @param m Matrix to right-multiply this matrix by
   * @return new Matrix set to this * m
   */
  public Matrix mult(Matrix m) {
    Matrix result = new Matrix(this.rows(), m.cols());
    for (int i = 0; i < this.rows(); i++) {
      for (int j = 0; j < m.cols(); j++) {
//        System.out.print(i + "," + j + ": ");
        for (int k = 0; k < this.cols(); k++) {
//          System.out.print(elt[i][k] + "*" + m.elt[k][j] + " + ");
          result.elt[i][j] += elt[i][k] * m.elt[k][j];
        }
//        System.out.println(" = " + result.elt[i][j]);
      }
    }
    return result;
  }
  
  public String toString() {
    String result = "[\t" + elt[0][0] + "\t" + elt[0][1] + "\t" + elt[0][2] + "\t]";
    result = result + "\n" + "[\t" + elt[1][0] + "\t" + elt[1][1] + "\t" + elt[1][2] + "\t]";
    result = result + "\n" + "[\t" + elt[2][0] + "\t" + elt[2][1] + "\t" + elt[2][2] + "\t]";
    return result;
  }
  
  public static Matrix makeRotationMatrix(double degrees, Axis a) {
    Matrix result = new Matrix(3, 3);
    double theta = Math.toRadians(degrees);
    double sinTheta = Math.sin(theta);
    double cosTheta = Math.cos(theta);
    if (a == Axis.X) {
      result.elt[0][0] = 1;
      result.elt[1][1] = result.elt[2][2] = cosTheta;
      result.elt[2][1] = sinTheta;
      result.elt[1][2] = -sinTheta;
    }
    else if (a == Axis.Y) {
      result.elt[1][1] = 1;
      result.elt[0][0] = result.elt[2][2] = cosTheta;
      result.elt[2][0] = -sinTheta;
      result.elt[0][2] = sinTheta;
    }
    else if (a == Axis.Z) {
      result.elt[2][2] = 1;
      result.elt[0][0] = result.elt[1][1] = cosTheta;
      result.elt[1][0] = sinTheta;
      result.elt[0][1] = -sinTheta;
    }
    return result;
  }
}