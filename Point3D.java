public class Point3D {
  double elt[] = new double[3];
  
  Point3D() { 
    this(0, 0, 0);
  }
  
  Point3D(double x, double y, double z) {
    elt[0] = x;
    elt[1] = y;
    elt[2] = z;
  }
  
  public double x() { return elt[0]; }
  public double y() { return elt[1]; }
  public double z() { return elt[2]; }
  
  public double distanceTo(Point3D p) {
    double dx = elt[0] - p.elt[0];
    double dy = elt[1] - p.elt[1];
    double dz = elt[2] - p.elt[2];
    return Math.sqrt(dx*dx + dy*dy + dz*dz);
  }
  
  public Point3D transform(Matrix m) {
    Point3D result = new Point3D();
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        result.elt[i] += m.elt(i, j) * this.elt[j];
      }
    }
    return result;
  }
}