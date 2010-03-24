import junit.framework.TestCase;

/**
 * A JUnit test case class.
 * Every method starting with the word "test" will be called when running
 * the test with JUnit.
 */
public class MatrixTest extends TestCase {
  
  /**
   * A test method.
   * (Replace "X" with a name describing the test.  You may write as
   * many "testSomething" methods in this class as you wish, and each
   * one will be called when running JUnit over this class.)
   */
  public void testRot0X() {
    Matrix m = Matrix.makeRotationMatrix(0, Matrix.Axis.X);
    //System.out.println(m);
    assertTrue(m.rows() == 3 && m.cols() == 3
                 && m.elt(0, 0) == 1 && m.elt(0, 1) == 0 && m.elt(0, 2) == 0
                 && m.elt(1, 0) == 0 && m.elt(1, 1) == 1 && m.elt(1, 2) == 0
                 && m.elt(2, 0) == 0 && m.elt(2, 1) == 0 && m.elt(2, 2) == 1);
  }
  
  public void testRot90X() {
    Matrix m = Matrix.makeRotationMatrix(90, Matrix.Axis.X);
    //System.out.println(m);
    assertTrue(m.rows() == 3 && m.cols() == 3
                 && Matrix.fpEquals(m.elt(0, 0), 1) && Matrix.fpEquals(m.elt(0, 1), 0) 
                 && Matrix.fpEquals(m.elt(0, 2), 0)
                 && Matrix.fpEquals(m.elt(1, 0), 0) && Matrix.fpEquals(m.elt(1, 1), 0) 
                 && Matrix.fpEquals(m.elt(1, 2), -1)
                 && Matrix.fpEquals(m.elt(2, 0), 0) && Matrix.fpEquals(m.elt(2, 1), 1) 
                 && Matrix.fpEquals(m.elt(2, 2), 0));
  }
  
  public void testRot90Y() {
    Matrix m = Matrix.makeRotationMatrix(90, Matrix.Axis.Y);
    //System.out.println(m);
    assertTrue(m.rows() == 3 && m.cols() == 3
                 && Matrix.fpEquals(m.elt(0, 0), 0) && Matrix.fpEquals(m.elt(0, 1), 0) 
                 && Matrix.fpEquals(m.elt(0, 2), 1)
                 && Matrix.fpEquals(m.elt(1, 0), 0) && Matrix.fpEquals(m.elt(1, 1), 1) 
                 && Matrix.fpEquals(m.elt(1, 2), 0)
                 && Matrix.fpEquals(m.elt(2, 0), -1) && Matrix.fpEquals(m.elt(2, 1), 0) 
                 && Matrix.fpEquals(m.elt(2, 2), 0));
  }
  
  public void testRot90XY() {
    Matrix rotX = Matrix.makeRotationMatrix(90, Matrix.Axis.X);
    Matrix rotY = Matrix.makeRotationMatrix(90, Matrix.Axis.Y);
    Matrix m = rotY.mult(rotX);
//    System.out.println("rotY:\n"+rotY);
//    System.out.println("rotX:\n"+rotX);
//    System.out.println("m:\n"+m);
    assertTrue(m.rows() == 3 && m.cols() == 3
                 && Matrix.fpEquals(m.elt(0, 0), 0) && Matrix.fpEquals(m.elt(0, 1), 1) 
                 && Matrix.fpEquals(m.elt(0, 2), 0)
                 && Matrix.fpEquals(m.elt(1, 0), 0) && Matrix.fpEquals(m.elt(1, 1), 0) 
                 && Matrix.fpEquals(m.elt(1, 2), -1)
                 && Matrix.fpEquals(m.elt(2, 0), -1) && Matrix.fpEquals(m.elt(2, 1), 0) 
                 && Matrix.fpEquals(m.elt(2, 2), 0));
  }
}
