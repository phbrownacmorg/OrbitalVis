
public class SP3Bond extends Bond {
	
	private int orbital;

	public SP3Bond(SP3Atom f1, int orb, AtomOrGroup f2, State state) {
		super(f1, f2, state);
		this.orbital = orb;
	}

	public Point3D getBondVector() {
		Point3D vec = ((SP3Atom)(this.getStart3D())).getOrbitalVector(orbital);
		return vec.scale(this.getLength());
	}
	
}
