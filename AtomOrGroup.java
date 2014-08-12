/**
 * Class to represent an atom or a group of atoms.  Basically, this is a RefFrame
 * with added charge information.
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
public abstract class AtomOrGroup extends RefFrame {
  public enum Charge { MINUS, PART_MINUS, NEUTRAL, PART_PLUS, PLUS };
  private Charge charge;
  
  protected AtomOrGroup(Point3D pt, Charge initialCharge, RefFrame parent, double tx, 
		  double ty) {
    super(pt, parent, tx, ty);
    charge = initialCharge;
  }
    
  protected AtomOrGroup(Point3D pt, Charge initialCharge) {
    this(pt, initialCharge, null, 0, 0);
  }
    
  protected AtomOrGroup(Point3D pt, Charge initialCharge, double tx, double ty) {
	  this(pt, initialCharge, null, tx, ty);
  }

  public Charge getCharge() { return charge; }
  public void setCharge(Charge newCharge) { charge = newCharge; }
}