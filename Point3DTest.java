import junit.framework.TestCase;

/**
 * A JUnit test case class to test Point3D.
 * Every method starting with the word "test" will be called when running
 * the test with JUnit.
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
