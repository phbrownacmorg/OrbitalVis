
public class SP3Bond extends Bond {
	
	private int orbital;

	public SP3Bond(SP3Atom f1, int orb, AtomOrGroup f2, State state) {
		super(f1, f2, state);
		this.orbital = orb;
	}

	public Point3D getBondVector() {
		Point3D vec = ((SP3Atom)(this.getStart3D())).getOrbitalVector(orbital);
//		if (this.getEnd3D() instanceof SP3Atom) {
//			double length = this.getLength();
//			if (!Matrix.fpEquals(length, Atom.SP3_SP3_BOND_LENGTH)) {
//				System.out.println(String.format("LENGTH ERROR: %f != %f", length, Atom.SP3_SP3_BOND_LENGTH));
//				System.out.println(this.getStart3D());
//				System.out.println(this.getEnd3D());
//			}
//		}
		return vec.scale(this.getLength());
	}
	
}
