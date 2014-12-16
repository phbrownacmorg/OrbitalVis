import java.util.ArrayList;
//import javax.media.opengl.*;

/**
 * The Model class for syn-addition of a peroxyacid to an alkene 
 *
 * Copyright 2011 Peter Brown <phbrown@acm.org>, Megan Dobbins<megan.dobbins@converse.edu>, 
 * and Ashleigh Geldenhuys<ashleigh.geldenhuys@converse.edu>
 * Copyright 2014 Peter Brown <phbrown@acm.org> and Ashish Nicodemus <anicodemus737@spart6students.org>
 *
 * This code is distributed under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the
 * license or (at your option) any later version.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

public class SAPAModel extends Model {
  // Peroxyacid attacks from the left or the right?
  private int xSign;
  
  // Atoms/groups
  private SP3Atom bottom_carb;  // Bottom carbon in double bond
  private SP3Atom top_carb;     // Top carbon in double bond
  private Atom bottom_H;
  private Atom top_H;
  private Methyl ch3a;
  private Methyl ch3b;
  
//bonds
 /**
  * Remember to add the new bonds here please!!!!
  */
 private Bond bottom_carb_H, top_carb_H, bottom_carb_ch3, carb_carb, top_carb_ch3;

 
  // Pieces of the peroxyacid
  private Atom r_H; // Hydrogen that takes the R position--not involved in the reaction
  private SP3Atom carbonyl_C; // Carbon in the COOH chain
  private SP3Atom double_bond_O; // Oxygen that starts out double-bonded to the carboxyl's C
  private SP3Atom resonance_O; // Oxygen that ends up double-bonded to the carboxyl's C
  private SP3Atom reactive_O; // Oxygen that ends up in the C-O-C ring
  private Atom end_H; // Hydrogen at the end of the COOH chain
  private Bond bottom_carb_O, top_carb_O, reactive_O_H, double_bond_O_H, resonance_O_reactive_O, resonance_O_carbonyl_C, carbonyl_C_R, carbonyl_C_double_bond_O;
  

  
  
  private static final double RESONANCE_O_Z_FUDGE = -6.022362;  // Found empirically
  private static final double CARBONYL_C_Y_TWEAK = 7;           // Found empirically by eye
  
  
  public SAPAModel(int xSign) {
    this.xSign = xSign;
    
    // Alkene to start
    
    top_carb = new SP3Atom(new Point3D(0, Atom.SP3_SP3_BOND_LENGTH/2.0, 0));
    top_carb.setInsideOutness(0.5);
    top_carb.setP0Divergence(1);
    
    bottom_carb = new SP3Atom(new Point3D(0, -Atom.SP3_SP3_BOND_LENGTH/2.0, 0)); //(top_carb.getOrbitalVector(1).scale(Atom.SP3_SP3_BOND_LENGTH/2.0));
    bottom_carb.setInsideOutness(0.5);
    bottom_carb.setP0Divergence(1);
    bottom_carb.setRot(0, 0, top_carb.getZRotation() + (180 - bottom_carb.getZRotation()));
    
    bottom_H = new Atom(new Point3D(), AtomOrGroup.Charge.NEUTRAL, bottom_carb, -0.6, -0.4);
    ch3a = new Methyl(new Point3D(), AtomOrGroup.Charge.NEUTRAL, top_carb, -0.3, 0.55);
    top_H = new Atom(new Point3D(), AtomOrGroup.Charge.NEUTRAL, top_carb, 0.3, 0.3);
    ch3b = new Methyl(new Point3D(), AtomOrGroup.Charge.NEUTRAL, bottom_carb, 0.4, -0.8);
    
    // Peroxyacid parts
    Point3D reactive_O_start = new Point3D(xSign * 2 * Atom.SP3_SP3_BOND_LENGTH, 0, 0);
    reactive_O = new SP3Atom(reactive_O_start);
    reactive_O.setRot(0, 0, SP3Atom.RELAXED_ANGLE/2.0 + (90 + (xSign * 90)));
    
    end_H = new Atom(reactive_O.getAbsOrbitalLoc(3, Atom.S_TO_SP3_BOND_LENGTH), 0, -0.6);
    Point3D resonance_O_pt = reactive_O.getAbsOrbitalLoc(2, Atom.SP3_SP3_BOND_LENGTH);
    resonance_O = new SP3Atom(resonance_O_pt, null, 0.2, 0.6);
    resonance_O.setRot(30, 
    		           90 + xSign * 90 - xSign * (120 - SP3Atom.RELAXED_ANGLE), 
    				   180 - resonance_O.getZRotation() + RESONANCE_O_Z_FUDGE);
    
    //System.out.println("Resonance O: " + resonance_O_pt);
    carbonyl_C = new SP3Atom(resonance_O.getAbsOrbitalLoc(1, Atom.SP3_SP3_BOND_LENGTH), null, 0.2, 0.6);
    //Point3D carbonyl_C_pt = carbonyl_C.getPt3D(); 
    //System.out.println("Carbonyl C: " + carbonyl_C_pt);
    //System.out.println("Z diff: " + (carbonyl_C_pt.z() - resonance_O_pt.z()));
    carbonyl_C.setInsideOutness(0.5);
    carbonyl_C.setP0Divergence(1);
    carbonyl_C.setRot(0, -xSign * (90 - (120 - SP3Atom.RELAXED_ANGLE)) + xSign * CARBONYL_C_Y_TWEAK, xSign * 90);
    
    double_bond_O = new SP3Atom(carbonyl_C.getAbsOrbitalLoc(1, Atom.SP3_SP3_BOND_LENGTH), null, 0, -0.3);
    double_bond_O.setInsideOutness(0.5);
    double_bond_O.setP0Divergence(1);
    double_bond_O.setRot(0, 90 + xSign * (120 - SP3Atom.RELAXED_ANGLE) + xSign * CARBONYL_C_Y_TWEAK, 
    		-double_bond_O.getZRotation());
    
    r_H = new Atom(carbonyl_C.getAbsOrbitalLoc(2, Atom.S_TO_SP3_BOND_LENGTH), 0, 0.8);
    
    setHydrogenLocations();
    
    // Create the Bonds
    bottom_carb_H = new Bond(bottom_carb, bottom_H, Bond.State.FULL);
    top_carb_H = new Bond(top_carb, top_H, Bond.State.FULL);
    bottom_carb_ch3 = new Bond(bottom_carb, ch3b, Bond.State.FULL);
    top_carb_ch3 = new Bond(top_carb, ch3a, Bond.State.FULL);            // creating the top methyl group
    carb_carb = new Bond(top_carb, bottom_carb, Bond.State.DOUBLE);
    bottom_carb_O = new Bond( bottom_carb, reactive_O, Bond.State.BROKEN); 
    top_carb_O = new Bond( top_carb, reactive_O, Bond.State.BROKEN);
    reactive_O_H = new Bond(reactive_O, end_H, Bond.State.FULL);
    double_bond_O_H = new Bond(double_bond_O, end_H, Bond.State.BROKEN);
    resonance_O_reactive_O = new Bond(reactive_O, resonance_O, Bond.State.FULL);
    resonance_O_carbonyl_C = new Bond(resonance_O, carbonyl_C, Bond.State.FULL);
    carbonyl_C_R = new Bond(carbonyl_C, r_H, Bond.State.FULL);
    carbonyl_C_double_bond_O = new Bond(carbonyl_C, double_bond_O, Bond.State.DOUBLE);
    
    
  }
  
  private void setHydrogenLocations() {
    double rotation = -top_carb.getZRotation(1);
    
    top_carb.setRot(0, 0, rotation - 90.0);

    // Bottom carbon on top carbon's orbital 1 
    //Point3D orb1Vec = top_carb.getOrbitalVector(1).scale(Atom.SP3_SP3_BOND_LENGTH);
    //bottom_carb.setLoc(orb1Vec.x(), orb1Vec.y(), orb1Vec.z());
    bottom_carb.setRot(0, 0, 90 - bottom_carb.getZRotation(1));
    
    // Methyl on top carbon's orbital 2
    Point3D orb2Vec = top_carb.getOrbitalVector(2).scale(Atom.SP3_SP3_BOND_LENGTH);
    ch3a.setLoc(orb2Vec.x(), orb2Vec.y(), orb2Vec.z());
    ch3a.setRot(120, 0, 180 + top_carb.getZRotation(2));
    
    // Methyl on bottom carbon's orbital 3
    Point3D orb3Vec = bottom_carb.getOrbitalVector(3).scale(Atom.SP3_SP3_BOND_LENGTH);
    ch3b.setLoc(orb3Vec.x(), orb3Vec.y(), orb3Vec.z());
    ch3b.setRot(-120, 0, 180 + bottom_carb.getZRotation(3));
    
    // Hydrogen on orbital 2 (orbital 3 on the top carbon)
    orb2Vec = bottom_carb.getOrbitalVector(2).scale(Atom.S_TO_SP3_BOND_LENGTH);
    bottom_H.setLoc(orb2Vec.x(), orb2Vec.y(), orb2Vec.z());
    orb3Vec = top_carb.getOrbitalVector(3).scale(Atom.S_TO_SP3_BOND_LENGTH);
    top_H.setLoc(orb3Vec.x(), orb3Vec.y(), orb3Vec.z());
  }

  public ArrayList<Drawable> createDrawList(boolean twoD) {
    ArrayList<Drawable> result = new ArrayList<Drawable>();
    if (twoD) { // Add the bonds as well
      //result.add(new BondView(top_carb_new_H));
      result.add(new BondView(bottom_carb_H));
      result.add(new BondView(top_carb_H));
      result.add(new BondView(bottom_carb_ch3));
      result.add(new BondView(top_carb_ch3));
      result.add(new BondView(carb_carb));
      result.add(new BondView(bottom_carb_O));
      result.add(new BondView(top_carb_O));
      result.add(new BondView(reactive_O_H));
      result.add(new BondView(double_bond_O_H));
      result.add(new BondView(resonance_O_reactive_O));
      result.add(new BondView(resonance_O_carbonyl_C));
      result.add(new BondView(carbonyl_C_R));
      result.add(new BondView(carbonyl_C_double_bond_O));
      
      //result.add(new BondView(new_H_cl));
    }

    // Draw the 3-D view back to front
    result.add(bottom_H.createView());
    result.add(ch3a.createView());
    result.add(ch3b.createView());
    result.add(bottom_carb.createView("C", AtomView.C_BLACK));
    result.add(top_carb.createView("C", AtomView.C_BLACK));
    result.add(top_H.createView());
    
    result.add(r_H.createView());
    result.add(resonance_O.createView("O", AtomView.O_RED));
    result.add(carbonyl_C.createView("C", AtomView.C_BLACK));
    result.add(reactive_O.createView("O", AtomView.O_RED));
    result.add(double_bond_O.createView("O", AtomView.O_RED));
    result.add(end_H.createView());
    
    return result;
  }
  
  public void setT(double newT) {
    super.setT(newT);
    
    // The reactive oxygen moves in along the X axis
    double t_O = Math.min(1.0, Math.max(0, (getT() - 0.1)/0.8));
    double startX = xSign * Atom.SP3_SP3_BOND_LENGTH * 2;
    double endX = xSign * Atom.SP3_SP3_BOND_LENGTH * (Math.sqrt(3.0) / 2);
    reactive_O.setLoc(t_O * endX + (1.0 - t_O) * startX, 0, 0);

    // Really simple-minded approach; fix later
    double zeroOneAngle = SP3Atom.RELAXED_ANGLE * (1.0 - t_O) + 60.0 * t_O;
    reactive_O.setZeroOneAngle(zeroOneAngle);  // Same angle as the carbons from the alkene
    reactive_O.setRot(0, 0, zeroOneAngle/2.0 + (90 + (xSign * 90)));

    // Set the angles between the carbon's orbitals, and the proportion of its center orbital
    double insideOutnessOffset = -xSign * Math.max(0, Math.min(0.5, ((0.5/0.6) * (getT() - 0.3))));
    double insideOutness = 0.5 + insideOutnessOffset;
    double divergence = 1.0 - (4 * (insideOutness - 0.5) * (insideOutness - 0.5));
    bottom_carb.setZeroOneAngle(zeroOneAngle);
    bottom_carb.setInsideOutness(0.5 - insideOutnessOffset);
    bottom_carb.setP0Divergence(divergence);
    top_carb.setInsideOutness(insideOutness);
    top_carb.setZeroOneAngle(zeroOneAngle);
    top_carb.setP0Divergence(divergence);
    
    // The double-bond oxygen loses its double bond with the carbonyl C
    double_bond_O.setInsideOutness((1.0 - t_O)/2);
    double_bond_O.setP0Divergence(1.0 - t_O);
    double_bond_O.setRot(0, 90 + xSign * (120 - SP3Atom.RELAXED_ANGLE) + xSign * CARBONYL_C_Y_TWEAK, 
    		-double_bond_O.getZRotation());
    
    // The resonance oxygen forms a double bond with the carbonyl C
    resonance_O.setInsideOutness(t_O/2);
    resonance_O.setP0Divergence(t_O);
    resonance_O.setRot((1.0 - t_O) * 30, 
    			        90 + xSign * 90 - xSign * ((120 - SP3Atom.RELAXED_ANGLE)),
    					180 - resonance_O.getZRotation() + (1.0 - t_O) * RESONANCE_O_Z_FUDGE);
    
    // The double bond (i.e., orbital 0) of the corbonyl C turns from the double-bond O to the resonance O
    carbonyl_C.setOrbital0XRotation(t_O * 60);
    
    // Move the end_H from the peroxyacid
    end_H.setLoc(reactive_O.getAbsOrbitalLoc(3, Atom.S_TO_SP3_BOND_LENGTH).interpolate(
    		double_bond_O.getAbsOrbitalLoc((int)(2.5-xSign*0.5), Atom.S_TO_SP3_BOND_LENGTH), t_O));
    
    // Set the corresponding locations of the hydrogens
    setHydrogenLocations();

    // Update the Bonds
    if (getT() < 0.23) { // Beginning state
    	end_H.setCharge(AtomOrGroup.Charge.NEUTRAL);
    	reactive_O_H.setState(Bond.State.FULL);
    	resonance_O_reactive_O.setState(Bond.State.FULL);
    	resonance_O_carbonyl_C.setState(Bond.State.FULL);
    	carbonyl_C_double_bond_O.setState(Bond.State.DOUBLE);
    	bottom_carb_O.setState(Bond.State.BROKEN);
    	top_carb_O.setState(Bond.State.BROKEN);
    	double_bond_O_H.setState(Bond.State.BROKEN);

    	top_carb.setCharge(AtomOrGroup.Charge.NEUTRAL);
    	carb_carb.setState(Bond.State.DOUBLE);
    	bottom_carb_ch3.setState(Bond.State.FULL);
    	top_carb_ch3.setState(Bond.State.FULL);
    }
    else if (getT() < 0.45) { // .23 < t < .45
        reactive_O_H.setState(Bond.State.PARTIAL);
        resonance_O_reactive_O.setState(Bond.State.PARTIAL);
        resonance_O_carbonyl_C.setState(Bond.State.FULL);
        carbonyl_C_double_bond_O.setState(Bond.State.FULL_PARTIAL);
        bottom_carb_O.setState(Bond.State.BROKEN);
        top_carb_O.setState(Bond.State.BROKEN);
        double_bond_O_H.setState(Bond.State.BROKEN);

    	//top_carb.setCharge(AtomOrGroup.Charge.MINUS);
    	carb_carb.setState(Bond.State.DOUBLE);
    	bottom_carb_ch3.setState(Bond.State.FULL);
    	top_carb_ch3.setState(Bond.State.FULL);    	
    }
    else if (getT() < 0.7) { // .45 < t < .7
        reactive_O_H.setState(Bond.State.BROKEN);
        resonance_O_reactive_O.setState(Bond.State.BROKEN);
        resonance_O_carbonyl_C.setState(Bond.State.FULL);
        carbonyl_C_double_bond_O.setState(Bond.State.FULL);
        bottom_carb_O.setState(Bond.State.PARTIAL);
        top_carb_O.setState(Bond.State.PARTIAL);
        double_bond_O_H.setState(Bond.State.PARTIAL);
    	
    	//top_carb.setCharge(AtomOrGroup.Charge.PART_MINUS);
    	carb_carb.setState(Bond.State.FULL_PARTIAL);
    	bottom_carb_ch3.setState(Bond.State.FULL);
    	top_carb_ch3.setState(Bond.State.FULL);
    }
    else if (getT() < 0.8) { // .7 < t < .8
        reactive_O_H.setState(Bond.State.BROKEN);
        resonance_O_reactive_O.setState(Bond.State.BROKEN);
        resonance_O_carbonyl_C.setState(Bond.State.FULL_PARTIAL);
        carbonyl_C_double_bond_O.setState(Bond.State.FULL);
        bottom_carb_O.setState(Bond.State.PARTIAL);
        top_carb_O.setState(Bond.State.PARTIAL);
        double_bond_O_H.setState(Bond.State.PARTIAL);
    	
    	//top_carb.setCharge(AtomOrGroup.Charge.MINUS);
    	carb_carb.setState(Bond.State.FULL);
    	bottom_carb_ch3.setState(Bond.State.FULL);
    	top_carb_ch3.setState(Bond.State.FULL);
    }
    else { // Final state; t > .8
        reactive_O_H.setState(Bond.State.BROKEN);
        resonance_O_reactive_O.setState(Bond.State.BROKEN);
        resonance_O_carbonyl_C.setState(Bond.State.DOUBLE);
        carbonyl_C_double_bond_O.setState(Bond.State.FULL);
        bottom_carb_O.setState(Bond.State.FULL);
        top_carb_O.setState(Bond.State.FULL);
        double_bond_O_H.setState(Bond.State.FULL);
    	
    	//top_carb.setCharge(AtomOrGroup.Charge.MINUS);
    	carb_carb.setState(Bond.State.FULL);
    	bottom_carb_ch3.setState(Bond.State.FULL);
    	top_carb_ch3.setState(Bond.State.FULL);
    }
  }
}
