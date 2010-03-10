public class Matrix {
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
  
  public Matrix mult(Matrix m) {
    Matrix result = new Matrix(this.rows(), m.cols());
    for (int i = 0; i < this.rows(); i++) {
      for (int j = 0; j < m.cols(); j++) {
        for (int k = 0; k < this.cols(); k++) {
          result.elt[i][j] += elt[i][k] * m.elt[k][i];
        }
      }
    }
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