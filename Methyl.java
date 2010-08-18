/**
 * Class to model a methyl group.  The origin of the carbon atom
 * is the origin of the group.
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

public class Methyl extends AtomOrGroup {
  private Atom hydro1, hydro2, hydro3;
  private SP3Atom carb;
  
  public Methyl(Point3D pt, AtomOrGroup.Charge charge) {
    super(pt, charge);
    
    Point3D hLoc = new Point3D(0, 0, Atom.S_TO_SP3_BOND_LENGTH);
    Matrix rotMatrix = Matrix.makeRotationMatrix(109.5, Matrix.Axis.X);
    hLoc = hLoc.transform(rotMatrix);
    hydro1 = new Atom(hLoc);

    Matrix rotZ = Matrix.makeRotationMatrix(120, Matrix.Axis.Z);
    Point3D hLoc2 = hLoc.transform(rotZ);
    hydro2 = new Atom(hLoc2);
    
    rotZ = Matrix.makeRotationMatrix(-120, Matrix.Axis.Z);
    Point3D hLoc3 = hLoc.transform(rotZ);
    hydro3 = new Atom(hLoc3);   
    
    carb = new SP3Atom();
  }
  
  public MethylView createView() {
    SP3AtomView carbView = carb.createView("", AtomView.C_BLACK);
    HydrogenView hydro1View = (HydrogenView)(hydro1.createView());
    HydrogenView hydro2View = (HydrogenView)(hydro2.createView());
    HydrogenView hydro3View = (HydrogenView)(hydro3.createView());
    return new MethylView(this, carbView, hydro1View, hydro2View, hydro3View);
  }
}