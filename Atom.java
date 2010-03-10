import javax.media.opengl.*;

/**
 * A class to represent an atom.  It can also set the bond length.
 */
public class Atom extends AtomOrGroup {
  public static final double BOND_LENGTH = 1.0 - POrbital.SP3_PROP;
  public static final double S_TO_SP3_BOND_LENGTH = BOND_LENGTH + SOrbitalView.RADIUS; // Was (SOrbitalView.RADIUS/2);
  public static final double SP3_SP3_BOND_LENGTH = 2 * BOND_LENGTH;
  
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
