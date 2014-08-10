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
    
    // Hang the methyl on the carbon's orbital 1
    Point3D loc = carbon.getOrbitalVector(1).scale(Atom.SP3_SP3_BOND_LENGTH);
    
    //Point3D methLoc = new Point3D(Atom.SP3_SP3_BOND_LENGTH, 0, 0);
    //Matrix rotMatrix = Matrix.makeRotationMatrix(109.5, Matrix.Axis.X);
    //methLoc = methLoc.transform(rotMatrix);
    methyl = new Methyl(loc, AtomOrGroup.Charge.NEUTRAL);
    methyl.setRot(180, 0, -(180 - 109.5));
    
    // Hang hydrogens on the remaining orbitals
    loc = carbon.getOrbitalVector(2).scale(Atom.S_TO_SP3_BOND_LENGTH);
    hydro1 = new Atom(loc);
    
    loc = carbon.getOrbitalVector(3).scale(Atom.S_TO_SP3_BOND_LENGTH);
    hydro2 = new Atom(loc);   
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