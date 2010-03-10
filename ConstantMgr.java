public interface ConstantMgr {
  /**
   * Distance to the far clipping plane.
   */
  public static final double FAR = 38.0;
  public static final double NEAR = 30.0;
  /**
   * Vertical field of view.
   */
  public static final double FOVY = 30.0;
 
  /**
   * From Bar
   */
  public static final double HOT = 50;
  public static final double AMBIENT = 25;
  public static final double COLD = 0;
  
  /**
   * Colors
   */
  public static final float ALPHA = 1.f;
  public static final float[] WHITE = {1.f, 1.f, 1.f, ALPHA};
  public static final float[] BLACK = {0.f, 0.f, 0.f, ALPHA};
  public static final float[] RED = {1.f, 0.f, 0.f, ALPHA};
  public static final float[] RED06 = {0.6f, 0.f, 0.f, ALPHA};
  public static final float[] GREEN = {0.f, 1.f, 0.f, ALPHA};
  public static final float[] BLUE = {0.f, 0.f, 1.f, ALPHA};
  public static final float[] BLUE06 = {0.f, 0.f, 0.6f, ALPHA};
  public static final float[] GRAY06 = {0.6f, 0.6f, 0.6f, ALPHA};
  public static final float[] GRAY02 = {0.2f, 0.2f, 0.2f, ALPHA};
  public static final float[] BROWN = {1.0f, 0.5f, 0.f, ALPHA};

}