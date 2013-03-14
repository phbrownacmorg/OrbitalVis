
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

public class SN2Model extends Model {
  // Atoms/groups
  private Hydroxide oh;
  private SP3Atom carb;
  private Atom hydro1, hydro2, hydro3;
  private SP3Atom chlor;
  
  // bonds
  private Bond carb_oh;
  private Bond carb_hydro1, carb_hydro2, carb_hydro3;
  private Bond carb_chlor;

  public SN2Model(boolean markH) {
    oh = new Hydroxide(new Point3D(0, 0, -(2 * Atom.BOND_LENGTH + Math.max(0, (0.5 - getT())))),
                         AtomOrGroup.Charge.MINUS);
    
    carb = new SP3Atom();
    
    hydro1 = new Atom();
    hydro2 = new Atom();
    hydro3 = new Atom();
    setHydrogenLocations();
    
    chlor = new SP3Atom(new Point3D(0, 0, 2 * Atom.BOND_LENGTH + Math.max(0, getT() - 0.5))); 
    chlor.setRot(0, 180, 0);
    //chlor.setRot(90, 90, 0); // Rotates about Y, then about X
    
    // Create the Bonds
    carb_oh = new Bond(carb, oh, Bond.State.BROKEN);
    carb_hydro1 = new Bond(carb, hydro1, Bond.State.FULL);
    carb_hydro2 = new Bond(carb, hydro2, Bond.State.FULL);
    carb_hydro3 = new Bond(carb, hydro3, Bond.State.FULL);
    carb_chlor = new Bond(carb, chlor, Bond.State.FULL);
  }
  
  private void setHydrogenLocations() {
    Point3D orb1Vec = carb.getOrbitalVector(1).scale(Atom.S_TO_SP3_BOND_LENGTH);
    hydro1.setLoc(carb.getX() + orb1Vec.x(), carb.getY() + orb1Vec.y(), carb.getZ() + orb1Vec.z());
    
    Point3D orb2Vec = carb.getOrbitalVector(2).scale(Atom.S_TO_SP3_BOND_LENGTH);
    hydro2.setLoc(carb.getX() + orb2Vec.x(), carb.getY() + orb2Vec.y(), carb.getZ() + orb2Vec.z());
    
    Point3D orb3Vec = carb.getOrbitalVector(3).scale(Atom.S_TO_SP3_BOND_LENGTH);
    hydro3.setLoc(carb.getX() + orb3Vec.x(), carb.getY() + orb3Vec.y(), carb.getZ() + orb3Vec.z());
  }

  public ArrayList<Drawable> createDrawList(boolean twoD) {
    ArrayList<Drawable> result = new ArrayList<Drawable>();
    if (twoD) { // Add the bonds as well
      result.add(new BondView(carb_oh));
      result.add(new BondView(carb_hydro1));
      result.add(new BondView(carb_hydro2));
      result.add(new BondView(carb_hydro3));
      result.add(new BondView(carb_chlor));
    }
    result.add(oh.createView(HydroxideView.TEXT_HO));
    result.add(carb.createView("C", AtomView.C_BLACK));
    result.add(hydro1.createView());
    result.add(hydro2.createView());
    result.add(hydro3.createView());
    result.add(chlor.createView("Cl", AtomView.CL_GREEN));
    return result;
  }
  
  public void setT(double newT) {
    super.setT(newT);
    
    // Set the angles betwen the carbon's orbitals, and the proportion of its center orbital
    carb.setInsideOutness(Math.min(1.0, Math.max(0, getT() - 0.2)/0.6));

    // Set the corresponding locations of the hydrogens
    setHydrogenLocations();
    
    // The hydroxyl moves in as t is in [0, 0.5]
    oh.setLoc(0, 0, -(Atom.SP3_SP3_BOND_LENGTH + Math.max(0, (0.5 - getT()))));

    // The chlorine moves away as t goes 0.5 - 1
    chlor.setLoc(0, 0, Atom.SP3_SP3_BOND_LENGTH + Math.max(0, getT() - 0.5));
    
    // Update the Bonds
    if (getT() < 0.4) {
      carb_oh.setState(Bond.State.BROKEN);
      carb_chlor.setState(Bond.State.FULL);
      oh.setCharge(AtomOrGroup.Charge.MINUS);
      chlor.setCharge(AtomOrGroup.Charge.NEUTRAL);
    }
    else if (getT() > 0.6) {
      carb_oh.setState(Bond.State.FULL);
      carb_chlor.setState(Bond.State.BROKEN);
      chlor.setCharge(AtomOrGroup.Charge.MINUS);
      oh.setCharge(AtomOrGroup.Charge.NEUTRAL);
    }
    else {
      carb_oh.setState(Bond.State.PARTIAL);
      carb_chlor.setState(Bond.State.PARTIAL);
      chlor.setCharge(AtomOrGroup.Charge.PART_MINUS);
      oh.setCharge(AtomOrGroup.Charge.PART_MINUS);
    }
  }
}
