/**
 * Class to model an ethyl group.
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
public class Ethyl extends AtomOrGroup {
  private Methyl methyl;
  private SP3Atom carbon;
  private Atom hydro1;
  private Atom hydro2;
  
  public Ethyl(Point3D pt, AtomOrGroup.Charge charge, RefFrame parent) {
    super(pt, charge, parent);
    
    carbon = new SP3Atom();
    
    Point3D methLoc = new Point3D(0, 0, Atom.SP3_SP3_BOND_LENGTH);
    Matrix rotMatrix = Matrix.makeRotationMatrix(109.5, Matrix.Axis.X);
    methLoc = methLoc.transform(rotMatrix);
    methyl = new Methyl(methLoc, AtomOrGroup.Charge.NEUTRAL);
    methyl.setRot(180 - 109.5, 0, 180);
    
    Point3D hLoc = new Point3D(0, 0, Atom.S_TO_SP3_BOND_LENGTH);
    hLoc = hLoc.transform(rotMatrix);

    Matrix rotZ = Matrix.makeRotationMatrix(120, Matrix.Axis.Z);
    Point3D hLoc2 = hLoc.transform(rotZ);
    hydro1 = new Atom(hLoc2);
    
    rotZ = Matrix.makeRotationMatrix(-120, Matrix.Axis.Z);
    Point3D hLoc3 = hLoc.transform(rotZ);
    hydro2 = new Atom(hLoc3);   
  }
  
  public Ethyl(Point3D pt, AtomOrGroup.Charge charge) {
    this(pt, charge, null);
  }
  
  public EthylView createView() {
    SP3AtomView carbView = carbon.createView("", AtomView.C_BLACK);
    HydrogenView hydro1View = (HydrogenView)(hydro1.createView());
    HydrogenView hydro2View = (HydrogenView)(hydro2.createView());
    MethylView methylView = (MethylView)(methyl.createView());
    return new EthylView(this, carbView, hydro1View, hydro2View, methylView);
  }

}