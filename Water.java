/**
 * Class to model a hydroxide group.  The origin of the oxygen atom is 
 * treated as the origin of the group.
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
public class Water extends AtomOrGroup {
  private Atom h1, h2;
  private SP3Atom oxygen;
  
  public Water(Point3D pt, AtomOrGroup.Charge charge) {
    super(pt, charge);
    
    // FIX: Hydrogens are colocated
    Point3D hydroLoc = new Point3D(0, 0, Atom.S_TO_SP3_BOND_LENGTH);
    Matrix rX = Matrix.makeRotationMatrix(109.5, Matrix.Axis.X);
    Matrix rZ = Matrix.makeRotationMatrix(120, Matrix.Axis.Z);
    Matrix rotMatrix = rX.mult(rZ);
    hydroLoc = hydroLoc.transform(rotMatrix);
    h1 = new Atom(hydroLoc);
    
    hydroLoc = new Point3D(0, 0, Atom.S_TO_SP3_BOND_LENGTH);
    //rotMatrix = Matrix.makeRotationMatrix(109.5, Matrix.Axis.X);
    //hydroLoc = hydroLoc.transform(rotMatrix);
    h2 = new Atom(hydroLoc);

    oxygen = new SP3Atom();
  }
  
  public WaterView createView(String text) {
    HydrogenView h1View = (HydrogenView)(h1.createView());
    HydrogenView h2View = (HydrogenView)(h2.createView());
    SP3AtomView oxyView = oxygen.createView("", AtomView.O_RED);
    return new WaterView(this, h1View, h2View, oxyView, text);
  }
}