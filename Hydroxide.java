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
public class Hydroxide extends AtomOrGroup {
  private Atom hydrogen;
  private SP3Atom oxygen;
  
  public Hydroxide(Point3D pt, AtomOrGroup.Charge charge) {
    super(pt, charge);
    
    Point3D hydroLoc = new Point3D(0, 0, Atom.S_TO_SP3_BOND_LENGTH);
    Matrix rotMatrix = Matrix.makeRotationMatrix(109.5, Matrix.Axis.X);
    hydroLoc = hydroLoc.transform(rotMatrix);
    hydrogen = new Atom(hydroLoc);

    oxygen = new SP3Atom();
  }
  
  public HydroxideView createView(String text) {
    HydrogenView hydroView = (HydrogenView)(hydrogen.createView());
    SP3AtomView oxyView = oxygen.createView("", AtomView.O_RED);
    return new HydroxideView(this, hydroView, oxyView, text);
  }
}