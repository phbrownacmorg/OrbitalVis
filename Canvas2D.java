public class Canvas2D extends java.awt.Canvas {
  private View2D view;
  
  public Canvas2D(View2D v) {
    super();
    view = v;
  }
  
  public void paint(java.awt.Graphics g) {
    view.draw(g);
  }
}