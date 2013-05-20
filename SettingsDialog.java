import java.awt.Component;
//import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
//import java.awt.GraphicsConfiguration;
//import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class SettingsDialog extends JDialog implements ActionListener,
		ChangeListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 426836938364724110L;
	private ArrayList <ImageIcon> images;
	private JLabel picLabel;
	private JSlider rockSlider;
	private JButton goButton;
	private JComboBox chooser;
	private String model;
	
	public SettingsDialog(Frame arg0, String arg1, boolean arg2) {
		super(arg0, arg1, arg2);
		this.setResizable(false);
		this.setSize(700, 500);
		this.setLocation(arg0.getWidth()/2 - this.getWidth()/2, arg0.getHeight()/2 - this.getHeight()/2);
		Box dBox = Box.createVerticalBox();
		JLabel words = new JLabel ("Welcome to OrbitalVis!"); words.setAlignmentX(Component.CENTER_ALIGNMENT);
		//		  JLabel space = new JLabel (" ");
		//		  JLabel space1 = new JLabel (" ");
		JLabel sliderText = new JLabel ("Smallest                                                                   Default                                                                   Greatest"); sliderText.setAlignmentX(Component.CENTER_ALIGNMENT);
		JLabel moreWords = new JLabel ("To begin, please choose a reaction:"); moreWords.setAlignmentX(Component.CENTER_ALIGNMENT);
		JLabel choose = new JLabel ("Now, set a rock angle:"); choose.setAlignmentX(Component.CENTER_ALIGNMENT);

		String [] reactions = {"SN2", "SN1", "Acyl", "E1", "E2", "EA2A"}; //These really should be brought in from somewhere else
		chooser = new JComboBox (reactions);
		chooser.addActionListener(this);

		model = (String)chooser.getItemAt(0);
		rockSlider = new JSlider();
		rockSlider.addChangeListener(this);

		goButton = new JButton("GO!"); goButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		goButton.addActionListener(this);

		images = getReactionImages();
		picLabel = new JLabel (images.get(0));
		picLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		picLabel.setAlignmentY(Component.TOP_ALIGNMENT);

		//		  dBox.add(space1);
		dBox.add(Box.createRigidArea(new Dimension(0,20)));
		dBox.add(words);
		dBox.add(Box.createRigidArea(new Dimension(0,20)));
		//		  dBox.add(space);
		dBox.add(moreWords);
		dBox.add(Box.createRigidArea(new Dimension(0,20)));
		dBox.add(chooser);
		dBox.add(Box.createRigidArea(new Dimension(0,20)));
		dBox.add(picLabel);
		dBox.add(Box.createRigidArea(new Dimension(0,20)));
		dBox.add(choose);
		dBox.add(Box.createRigidArea(new Dimension(0,20)));
		dBox.add(rockSlider);
		dBox.add(sliderText);
		dBox.add(Box.createRigidArea(new Dimension(0,30)));
		dBox.add(goButton);
		dBox.add(Box.createRigidArea(new Dimension(0,50)));
		this.add(dBox);

	}

	private ArrayList<ImageIcon> getReactionImages() {
		ArrayList<ImageIcon> image = new ArrayList<ImageIcon>();

		java.net.URL rxnURL = getClass().getResource("SN2.png");
		ImageIcon SN2 = new ImageIcon(rxnURL);
		rxnURL = getClass().getResource("SN1.jpg");
		ImageIcon SN1 = new ImageIcon(rxnURL);
		rxnURL = getClass().getResource("Acyl.jpg");
		ImageIcon Acyl = new ImageIcon(rxnURL);

		image.add(SN2);
		image.add(SN1);
		image.add(Acyl);
		return image;
	}

	 private void updateLabel(int x){
		  picLabel.setIcon(images.get(x));
//		  System.out.println("Chosen: " + chooser.getSelectedItem());
	  }
	
	public void stateChanged(ChangeEvent e) {
		

	}


	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == chooser) {
			model = (String)chooser.getSelectedItem();
			System.out.println(model + " selected");
	    	updateLabel(chooser.getSelectedIndex());
	    
		}

	    else if (e.getSource() == goButton) {
	    	System.out.println("Responding to GO");
	    	//Controller.controller.updateModel(model);
	    	this.setVisible(false);
	    }
	}

	
	
}
