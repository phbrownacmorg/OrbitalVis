import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class View2D {
  private static int Z_SCALE = 100;
  private static int Y_SCALE = 100;
  private static int X_SCALE = 25;
  private static int X_OFF = 365;
  private static int Y_OFF = 170;
  
  public static float FONT_SIZE = 24.0f;
  
  private ArrayList<Drawable> drawList;
 
  public View2D(Model m) {
    drawList = m.createDrawList(true);
  }
  
  /**
   * Reset the model.
   */
  public void setModel(Model newModel) {
    drawList = newModel.createDrawList(true);
  }
  
  public void draw(Graphics g) {
    // If blanking, just blank the canvas
    // Else, iterate over all the Atoms in the Model and tell them to draw themselves using g
    if (!(g instanceof Graphics2D)) {
      g.drawLine(500, 0, 0, 500);  // OK, you can cast it to Graphics2D and do Graphics2D things with it
    }
    else {
      for (Drawable d:drawList) {
        d.draw2D((Graphics2D)g);
      }
//      g.drawLine(250, 0, 250, 500);
    }
    
  }
  
  public static void setFontSize(float newFontSize) {
    FONT_SIZE = newFontSize;
  }
  
  public static void setOffsets(int offX, int offY) {
    X_OFF = offX;
    Y_OFF = offY;
    //System.out.println("2D offsets set to " + X_OFF + " and " + Y_OFF);
  }
  
  public static void setScales(int sx, int sy, int sz) {
    X_SCALE = sx;
    Y_SCALE = sy;
    Z_SCALE = sz;
  }
  
  public static Point2D to2D(Point3D pt) {
    double x = pt.z() * Z_SCALE + pt.x() * X_SCALE + X_OFF;
    double y = -pt.y() * Y_SCALE + Y_OFF;
    return new Point2D.Double(x, y);
  }
}