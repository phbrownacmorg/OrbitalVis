import java.util.ArrayList;
//import javax.media.opengl.*;

/**
 * The Model class for the SN2 reaction
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

public class AcylModel extends Model {
  // OH attacks from the left or the right?
  private int zSign;
  
  // Atoms/groups
  private Atom nucleophile;
  private SP3Atom carb;  // Central carbon
  private Ethyl ethyl;
  private Methyl ch3;
  private SP3Atom oxy;
  
  // bonds
  private Bond nucleophile_carb;
  private Bond carb_ethyl, carb_ch3, carb_oxy;

  public AcylModel(int zSign) {
    this.zSign = -zSign;
    
    nucleophile = new Atom(new Point3D(zSign * (Atom.S_TO_SP3_BOND_LENGTH + Math.max(0, (0.8 - getT()))), 
    									0, 0),
                           AtomOrGroup.Charge.MINUS);
    
    carb = new SP3Atom();
    carb.setInsideOutness(0.5);
    carb.setP0Divergence(1);
    carb.setRot(0, 0, 180);
    
    ethyl = new Ethyl(new Point3D(), AtomOrGroup.Charge.NEUTRAL, carb, -0.7, -0.6);
    ch3 = new Methyl(new Point3D(), AtomOrGroup.Charge.NEUTRAL, carb, 0.5, -0.1);
    oxy = new SP3Atom(new Point3D(), carb);
    oxy.setInsideOutness(0.5);
    oxy.setP0Divergence(1);
    setHydrogenLocations();
    
    // Create the Bonds
    nucleophile_carb = new Bond(nucleophile, carb, Bond.State.BROKEN);
    carb_ethyl = new Bond(carb, ethyl, Bond.State.FULL);
    carb_ch3 = new Bond(carb, ch3, Bond.State.FULL);
    carb_oxy = new Bond(carb, oxy, Bond.State.DOUBLE);
  }
  
  private void setHydrogenLocations() {
//    // Figure out the proper rotation for the hydrogens.  This depends on the inside-outness of the carbon.
    double rotation = -carb.getZRotation();

    // Oxygen on orbital 1
    Point3D orb1Vec = carb.getOrbitalVector(1).scale(Atom.SP3_SP3_BOND_LENGTH);
    oxy.setLoc(orb1Vec.x(), orb1Vec.y(), orb1Vec.z());
    oxy.setRot(0, 0, 180);
    
    // Ethyl on orbital 2
    Point3D orb2Vec = carb.getOrbitalVector(2).scale(Atom.SP3_SP3_BOND_LENGTH);
    ethyl.setLoc(orb2Vec.x(), orb2Vec.y(), orb2Vec.z());
    ethyl.setRot(-60, 0, 180 + rotation); // (X up, Y right)
    
    // Methyl on orbital 3
    Point3D orb3Vec = carb.getOrbitalVector(3).scale(Atom.SP3_SP3_BOND_LENGTH);
    ch3.setLoc(orb3Vec.x(), orb3Vec.y(), orb3Vec.z());
    ch3.setRot(-120, 0, 180 - rotation);
  }

  public ArrayList<Drawable> createDrawList(boolean twoD) {
    ArrayList<Drawable> result = new ArrayList<Drawable>();
    if (twoD) { // Add the bonds as well
    	result.add(new BondView(carb_ethyl));
        result.add(new BondView(nucleophile_carb));
        result.add(new BondView(carb_oxy));
        result.add(new BondView(carb_ch3));
    }

    // Draw the 3-D view back to front
    result.add(ethyl.createView());
    result.add(nucleophile.createView());
    result.add(carb.createView("C", AtomView.C_BLACK));
    result.add(oxy.createView("O", AtomView.O_RED));
    result.add(ch3.createView());

    return result;
  }
  
  public void setT(double newT) {
    super.setT(newT);
    
    // Set the angles betwen the carbon's orbitals, and the proportion of its center orbital
    double insideOutnessOffset = -zSign * Math.max(0, Math.min(0.5, ((0.5/0.6) * (getT() - 0.3))));
    double insideOutness = 0.5 + insideOutnessOffset;
    //double insideOutness = Math.min(1.0, Math.max(0.5, ((0.5/0.6) * (getT() - 0.3)) + 0.5));
    double divergence = 1.0 - (4 * (insideOutness - 0.5) * (insideOutness - 0.5));
    carb.setInsideOutness(insideOutness);
    carb.setP0Divergence(divergence);
    oxy.setInsideOutness(insideOutness);
    oxy.setP0Divergence(divergence);
    
    // Set the corresponding locations of the hydrogens
    setHydrogenLocations();
    
    // The nucleophile moves in
    nucleophile.setLoc(-zSign * (Atom.S_TO_SP3_BOND_LENGTH + Math.max(0, (0.8 - getT()))), 0, 0);
    
    // Update the Bonds
    if (getT() < 0.3) {
      nucleophile_carb.setState(Bond.State.BROKEN);
      nucleophile.setCharge(AtomOrGroup.Charge.MINUS);
      oxy.setCharge(AtomOrGroup.Charge.NEUTRAL);
      carb_oxy.setState(Bond.State.DOUBLE);
    }
    else if (getT() > 0.5) {
      nucleophile_carb.setState(Bond.State.FULL);
      nucleophile.setCharge(AtomOrGroup.Charge.NEUTRAL);
      oxy.setCharge(AtomOrGroup.Charge.MINUS);
      carb_oxy.setState(Bond.State.FULL);
    }
    else {
      nucleophile_carb.setState(Bond.State.PARTIAL);
      nucleophile.setCharge(AtomOrGroup.Charge.PART_MINUS);
      oxy.setCharge(AtomOrGroup.Charge.PART_MINUS);
      carb_oxy.setState(Bond.State.FULL_PARTIAL);
    }
  }
}
