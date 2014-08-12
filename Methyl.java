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
  
  public Methyl(Point3D pt, AtomOrGroup.Charge charge, RefFrame parent, 
		  		double tx, double ty) {
    super(pt, charge, parent, tx, ty);
    makeMethylStuff();
  }

  public Methyl(Point3D pt, AtomOrGroup.Charge charge, double tx, double ty) {
    this(pt, charge, null, tx, ty);
  }
  
  private void makeMethylStuff() {
	  carb = new SP3Atom();
	  Point3D hLoc = carb.getOrbitalVector(1).scale(Atom.S_TO_SP3_BOND_LENGTH);
	  hydro1 = new Atom(hLoc);
	  
	  hLoc = carb.getOrbitalVector(2).scale(Atom.S_TO_SP3_BOND_LENGTH);
	  hydro2 = new Atom(hLoc);
	  
	  hLoc = carb.getOrbitalVector(3).scale(Atom.S_TO_SP3_BOND_LENGTH);
	  hydro3 = new Atom(hLoc);	  
  }
  
  public MethylView createView() {
    SP3AtomView carbView = carb.createView("", AtomView.C_BLACK);
    HydrogenView hydro1View = (HydrogenView)(hydro1.createView());
    HydrogenView hydro2View = (HydrogenView)(hydro2.createView());
    HydrogenView hydro3View = (HydrogenView)(hydro3.createView());
    return new MethylView(this, carbView, hydro1View, hydro2View, hydro3View);
  }
}