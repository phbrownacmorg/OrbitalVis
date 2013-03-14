//import javax.media.opengl.*;

/**
 * A class to represent an atom.  It can also set the bond length.
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
public class Atom extends AtomOrGroup {
  public static final double BOND_LENGTH = 1.0 - POrbital.SP3_PROP;
  public static final double S_TO_SP3_BOND_LENGTH = BOND_LENGTH + SOrbitalView.RADIUS; // Was (SOrbitalView.RADIUS/2);
  public static final double SP3_SP3_BOND_LENGTH = 2 * BOND_LENGTH;
  
  public Atom(Point3D pt, AtomOrGroup.Charge initialCharge, RefFrame parent) {
    super(pt, initialCharge, parent);
  }
  
  public Atom(Point3D pt, RefFrame parent) {
    this(pt, AtomOrGroup.Charge.NEUTRAL, parent);
  }
  
  public Atom() {
    this(new Point3D());
  }
  
  public Atom(Point3D pt) {
    super(pt, AtomOrGroup.Charge.NEUTRAL);
  }
  
  public Atom(Point3D pt, AtomOrGroup.Charge initialCharge) {
    super(pt, initialCharge);
  }

  /**
   * Create a HydrogenView object to view this Atom as a hydrogen atom.
   * This isn't very clean from a design point of view, since not every
   * atom should be viewable as if it were hydrogen.  But a Hydrogen class
   * would be so tiny that this seems like a reasonable tradeoff.
   */
  public AtomView createView() {
    return new HydrogenView(this);
  }
}
