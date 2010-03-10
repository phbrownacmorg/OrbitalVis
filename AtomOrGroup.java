/**
 * Class to represent an atom or a group of atoms.  Basically, this is a RefFrame
 * with added charge information.
 */
public abstract class AtomOrGroup extends RefFrame {
  public enum Charge { MINUS, PART_MINUS, NEUTRAL, PART_PLUS, PLUS };
  private Charge charge;
  
  protected AtomOrGroup(Point3D pt, Charge initialCharge) {
    super(pt);
    charge = initialCharge;
  }
    
  public Charge getCharge() { return charge; }
  public void setCharge(Charge newCharge) { charge = newCharge; }
}