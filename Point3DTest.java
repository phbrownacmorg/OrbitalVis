import junit.framework.TestCase;

/**
 * A JUnit test case class.
 * Every method starting with the word "test" will be called when running
 * the test with JUnit.
 */
public class Point3DTest extends TestCase {
  
  /**
   * A test method.
   * (Replace "X" with a name describing the test.  You may write as
   * many "testSomething" methods in this class as you wish, and each
   * one will be called when running JUnit over this class.)
   */
  public void testXfm() {
    Point3D z = new Point3D(0, 0, 1);
    Matrix rotX = Matrix.makeRotationMatrix(90, Matrix.Axis.X);
    Matrix rotY = Matrix.makeRotationMatrix(90, Matrix.Axis.Y);
    Matrix m = rotY.mult(rotX);
    Point3D pt = z.transform(m);
    assertTrue(Matrix.fpEquals(pt.x(), 0) && Matrix.fpEquals(pt.y(), -1) 
                 && Matrix.fpEquals(pt.z(), 0));
  }
  
}
