import javax.swing.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.GLCapabilities;
//import javax.media.opengl.GLProfile;
import com.jogamp.opengl.util.Animator;

//import java.util.ArrayList;
import java.util.Properties;

//import java.awt.Component;
//import java.awt.Dimension;
//import java.awt.Canvas;
//import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Controller class to start the whole thing going.
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
public class Controller extends Animator implements ActionListener, ChangeListener
{
  private int reactionSteps;
  private View view;
  private View2D view2d;
  private Model model;
  private java.util.ListIterator<Model> iterator;
  private int cineStep; // Number of steps per cine step in the automatic loop
  
  private JFrame testFrame;   // Top-level window
  //private Canvas2D canvas2d;
  
  //private int indx = 0;
  //private ArrayList <ImageIcon> images;
  //private ImageIcon picture;
  //private JLabel picLabel;
  
  private JButton rockButton;
  private JButton fwdButton;
  private JSlider slider; //private JSlider rockSlider;
  private JButton bkwdButton;
  private JButton attackButton;
  //private JButton goButton;
  private JButton settings;
  //private JComboBox chooser;
  
  //private JDialog dialog;
  private SettingsDialog dialog;
  
  private double userAngle; // Rotation angle set by the user with the mouse
  
  private boolean rocking; // Enable rocking
  private double animT; // For rocking
  private double rockingAngle; // Angle through which the scene rocks
  private int animSteps; // Number of times the scene is drawn to progress through a complete cycle of rocking
  
  private Properties props;
  public static Controller controller;
  
  /**
   * Create a Controller.
   */
  private Controller(Properties properties) {
    super();
    
    this.props = properties;
    // Must be parsed before the models are created
    try {
    	String defaultSpec = String.format("%f %f %f", View2D.X_SCALE, View2D.Y_SCALE, View2D.Z_SCALE);
      String spec[] = props.getProperty("2DScales", defaultSpec).split(" ");
      double sx = Double.parseDouble(spec[0]);
      double sy = Double.parseDouble(spec[1]);
      double sz = Double.parseDouble(spec[2]);
      View2D.setScales(sx, sy, sz);
    } catch (NumberFormatException e) {
      System.out.println("Ignoring 2DScales specification, due to the following:");
      System.out.println(e);
    }

    if (props.containsKey("fontSize")) {
      try {
        int newFontSize = Integer.parseInt(props.getProperty("fontSize"));
        View2D.setFontSize(newFontSize);
      } catch (NumberFormatException e) {
        System.out.println("Ignoring fontSize specification, due to the following:");
        System.out.println(e);
      }
    }

    cineStep = 0;

    reactionSteps = 100;
    if (props.containsKey("reactionSteps")) {
      try {
        this.reactionSteps = Integer.parseInt(props.getProperty("reactionSteps"));
      } catch (NumberFormatException e) {
        System.out.println("Ignoring reactionSteps specification, due to the following:");
        System.out.println(e);
      }
    }
    
    userAngle = 0;
    animT = 0;
    rocking = false;
    
    rockingAngle = 10;
    if (props.containsKey("rockingAngle")) {
      try {
        this.rockingAngle = Double.parseDouble(props.getProperty("rockingAngle"));
      } catch (NumberFormatException e) {
        System.out.println("Ignoring rockingAngle specification, due to the following:");
        System.out.println(e);
      }
    }
    
    animSteps = 50;
    if (props.containsKey("rockingSteps")) {
      try {
        this.animSteps = Integer.parseInt(props.getProperty("rockingSteps"));
      } catch (NumberFormatException e) {
        System.out.println("Ignoring animSteps specification, due to the following:");
        System.out.println(e);
      }
    }

    String modelName = props.getProperty("model", "SN2");
    testFrame = new JFrame(props.getProperty("title", modelName
    		+ " Visualization"));
    testFrame.setSize( 950, 730 );
    testFrame.setResizable(false);
    
    updateModel(modelName);
    
    testFrame.addWindowListener(makeWindowListener());

    dialog = new SettingsDialog(testFrame, "OrbitalVis", true);
      
  }
  
  
  private Box makeControlBox() {
    Box controlBox = Box.createHorizontalBox();
    
    java.net.URL iconURL = getClass().getResource("001_27.gif");
    ImageIcon leftArrowIcon = new ImageIcon(iconURL);
    iconURL = getClass().getResource("001_25.gif");
    ImageIcon rightArrowIcon = new ImageIcon(iconURL);
    iconURL = getClass().getResource("settings.jpg");
    ImageIcon settingsImage = new ImageIcon(iconURL);
    
    // Make button to toggle rocking
    rockButton = new JButton("Rock");
    rockButton.addActionListener(this);
      
    // Buttons and slider for user control over animation
    fwdButton = new JButton(rightArrowIcon);
    bkwdButton = new JButton(leftArrowIcon);
    slider = new JSlider(JSlider.HORIZONTAL, 0, reactionSteps, 0);
    slider.setMajorTickSpacing(reactionSteps/2);
    slider.setPaintTicks(true);
    
    MouseAdapter ma = makeButtonListener();
    fwdButton.addMouseListener(ma);
    bkwdButton.addMouseListener(ma);
    slider.addChangeListener(this);
    
    settings = new JButton (settingsImage);
    settings.addActionListener(this);

    // Make button to change attack side
    attackButton = new JButton("Attack other side");
    attackButton.addActionListener(this);
    attackButton.setEnabled(iterator.hasNext());
    
    // add the buttons and the slider to the control box
    controlBox.add(settings);
    controlBox.add( rockButton );
    controlBox.add( bkwdButton);
    controlBox.add(slider);
    controlBox.add(fwdButton);
    controlBox.add(attackButton);

    return controlBox;
  }
  
  public void updateModel(String modelName) {
	  System.out.println("Updating model: " + modelName);
	  //System.out.flush();
	  java.util.List<Model> modelList = makeModels(modelName);
	  iterator = modelList.listIterator();

	  try {
	      
	      props.load(new java.io.FileReader(modelName + "-props.txt"));
	    } catch (java.io.IOException e) {
	      System.out.println(e);}
	  // Nuke anything that was there before
	  testFrame.getContentPane().removeAll();

	  model = iterator.next();

	  System.out.println("Model: " + model.toString());

	  Box vbox = Box.createVerticalBox();
	  testFrame.add(vbox);

	  // Control box
	  vbox.add(makeControlBox());

	  view = new View(model, props);

	  //GLProfile profile = ;
	  GLCapabilities glCaps = new GLCapabilities(null);
	  System.out.println(glCaps);

	  GLCanvas canvas = new GLCanvas( glCaps );

	  canvas.addGLEventListener(view);
	  canvas.addMouseMotionListener(makeMouseListener());
	  canvas.addKeyListener(makeKeyListener());
	  this.add(canvas);

	  view2d = new View2D(model, props);
	  //canvas2d = new Canvas2D(view2d);
	  GLCanvas canvas2D = new GLCanvas(glCaps);
	  canvas2D.addGLEventListener(view2d);
	  this.add(canvas2D);
	  // add the canvases
	  if (props.getProperty("canvasLayout", "horizontal").equals("vertical")) {
		  Box canvasBox = Box.createHorizontalBox();
		  canvasBox.add(canvas);
		  canvasBox.add(canvas2D);
		  vbox.add(canvasBox);
	  }
	  else {
		  vbox.add(canvas);
		  vbox.add(canvas2D);
	  }


  }
  
 
  
  private java.util.List<Model> makeModels(Properties props) {
    java.util.List<Model> result = new java.util.ArrayList<Model>();
    String modelName = props.getProperty("model", "SN2");
    if (modelName.equals("Acyl")) {
      result.add(new AcylModel(Model.LEFT_SIDE_ATTACK));
      result.add(new AcylModel(Model.RIGHT_SIDE_ATTACK));
    }
    if (modelName.equals("EA2A")) {
      result.add(new EA2AModel(Model.LEFT_SIDE_ATTACK));
      result.add(new EA2AModel(Model.RIGHT_SIDE_ATTACK));
    }
    else if (modelName.equals("SN1")) {
      result.add(new SN1Model(Model.LEFT_SIDE_ATTACK));
      result.add(new SN1Model(Model.RIGHT_SIDE_ATTACK));
    }
    else if (modelName.equals("E2")) {
      result.add(new E2Model());
    }
    else if (modelName.equals("E1")) {
      result.add(new E1Model());
    }
    else {
      result.add(new SN2Model());
    }
    return result;
  }
  
  private java.util.List<Model> makeModels(String modelName) {
	    java.util.List<Model> result = new java.util.ArrayList<Model>();
	    if (modelName.equals("Acyl")) {
	      result.add(new AcylModel(Model.LEFT_SIDE_ATTACK));
	      result.add(new AcylModel(Model.RIGHT_SIDE_ATTACK));
	    }
	    if (modelName.equals("EA2A")) {
	      result.add(new EA2AModel(Model.LEFT_SIDE_ATTACK));
	      result.add(new EA2AModel(Model.RIGHT_SIDE_ATTACK));
	    }
	    else if (modelName.equals("SN1")) {
	      result.add(new SN1Model(Model.LEFT_SIDE_ATTACK));
	      result.add(new SN1Model(Model.RIGHT_SIDE_ATTACK));
	    }
	    else if (modelName.equals("E2")) {
	      result.add(new E2Model());
	    }
	    else if (modelName.equals("E1")) {
	      result.add(new E1Model());
	    }
	    else {
	      result.add(new SN2Model());
	    }
	    return result;
	  }
  
  /**
   * Start drawing.
   */
  public boolean start() {
    testFrame.setVisible(true);
    dialog.setVisible(true);
    return super.start();
  }
  
  private void quit() {
    this.stop();
    System.exit(0);
  }
  
  private void toggleRocking() {
    rocking = !rocking;
    if (rocking) {
      rockButton.setText("Stop");
    }
    else {
      rockButton.setText("Rock");
    }
  }
  
  private void switchAttack() {
    if (iterator.hasNext()) {
      nextModel();
    }
    else {
      prevModel();
    }
  }
  
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == rockButton) {
    	toggleRocking();
    }
    else if (e.getSource() == attackButton){
    	switchAttack();
    }
    else if (e.getSource() == settings){
    	dialog.setVisible(true);
    }
  }
  
  /**
   * If the window closes, shut down.
   */ 
  private WindowAdapter makeWindowListener() {
    return (new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        quit();
      }
      public void windowIconified(WindowEvent e) {
        //System.out.println("Stopping...");
        stop();
      }
      public void windowDeiconified(WindowEvent e) {
        start();
      }
    });
  }
  
  private void nextModel() {
    Model newModel = model;
    while (iterator.hasNext() && (model == newModel)) {
      newModel = iterator.next();
    }
    setModel(newModel);
  }
  
  private void prevModel() {
    Model newModel = model;
    while (iterator.hasPrevious() && (model == newModel)) {
      newModel = iterator.previous();
    }
    setModel(newModel);
  }
  
  private void setModel(Model newModel) {
    if (model != newModel) {
      model = newModel;
      slider.setValue(0);
      view.setModel(model);
      view2d.setModel(model);
      //canvas2d.repaint();
    }
  }
  
  /**
   * If the user presses the spacebar, go to the next model.
   */
  private KeyAdapter makeKeyListener() {
    return (new KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
          case KeyEvent.VK_ESCAPE:
            quit();
            break;
          case KeyEvent.VK_PAGE_DOWN:
          case KeyEvent.VK_SPACE:
            nextModel();
            break;
          case KeyEvent.VK_PAGE_UP:
          case KeyEvent.VK_BACK_SPACE:
            prevModel();
            break;
        }
      }
    });
  }
  
  private MouseMotionAdapter makeMouseListener() {
    return (new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        // Rotate horizontally
        double normX = view.normX(e.getX()); // [-1, 1]
        userAngle = 45 * normX;
      }
    });
  }
  
  private MouseAdapter makeButtonListener() {
    return (new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        if (e.getSource() == fwdButton) {
          cineStep = 1;
        }
        else if (e.getSource() == bkwdButton) {
          cineStep = -1;
        }
      }
      public void mouseReleased(MouseEvent e) {
        cineStep = 0;
      }
    });
  }
  
  public void stateChanged(ChangeEvent e) {
    if (e.getSource() == slider){
	  model.setT(slider.getValue()/((double)(reactionSteps)));
    }
    //canvas2d.repaint();
  }
  
  /**
   * Increase the value of t by the constant DT.
   */
  public void incrT() {
    if (rocking && (rockingAngle > 0)) {
      animT += 1.0/animSteps;
      if (animT > 1.0) {
        animT = 0;
      }
    }
    slider.setValue(slider.getValue() + cineStep);
  }
  
  /**
   * Draw the canvas, changing t as you go.
   */
  protected void display() {
    this.incrT();
    
    // Set the view rotation
    view.setRotH(userAngle + rockingAngle * Math.sin(2 * Math.PI * animT));
        
    // display
    super.display();
  }
  
    
  /**
   * Run the whole thing.  The Properties object is used to override defaults
   * without necessarily having to recompile.
   */
  public static void main( String[] args ) {
    Properties props = new Properties();
    try {
      props.load(new java.io.FileReader("generic-props.txt"));
      String modelFile = props.getProperty("model", "SN2");
      props.load(new java.io.FileReader(modelFile + "-props.txt"));
    } catch (java.io.IOException e) {
      System.out.println(e);
    }
    controller = new Controller(props);
    controller.start();
  }
}
