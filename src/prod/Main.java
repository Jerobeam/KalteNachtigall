package prod;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import controller.MainController;

public class Main {

	private static TrainCollection trainCollection = new TrainCollection();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createGUI();
			}
		});
	}
	
	public static void createGUI(){
		try {
			UIManager.setLookAndFeel ("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		JFrame frame = new JFrame("Lokomotiv Führer 2.0");
		// Layout-Manager setzen.
		// frame.setLayout (new GridBagLayout());
//		GridBagConstraints c;
		// Objekt für Abstände zwischen den GUI-Komponenten.
//		Insets set = new Insets(5, 5, 5, 5);

		// Eigenschaften für die Beschriftung "Name:".
//		c = new GridBagConstraints();
//		c.insets = set; // Abstände setzen.
//		c.gridx = 0; // Position auf X-Achse setzen.
//		c.gridy = 0; // Position auf Y-Achse setzen.
		// Ausrichtung setzen.
//		c.anchor = GridBagConstraints.LINE_START;
		// frame.add (new JLabel ("Name: "), c);

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createLineBorder(Color.red));
		panel.setBorder(BorderFactory.createTitledBorder("Deine Züge"));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setPreferredSize(new Dimension(350, 500));
		panel.setMaximumSize(new Dimension(350, 500));
		frame.add(panel, BorderLayout.WEST);

		JPanel panelTrains = new JPanel();
		panelTrains.setLayout(new BoxLayout(panelTrains, BoxLayout.Y_AXIS));

		final JScrollPane scroll = new JScrollPane(panelTrains);

		panel.add(scroll);

		JPanel train1 = new JPanel();
		train1.setName("Zug 1");
//		train1.setBorder(BorderFactory.createTitledBorder("Zug 1"));
		train1.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		train1.setPreferredSize(new Dimension(315, 100));
		train1.setMaximumSize(new Dimension(315,100));
		train1.setMinimumSize(new Dimension(315, 100));
		
		train1.setLayout(new GridLayout());
		
		train1.setLayout(new GridBagLayout());

		// Erstelle Regeln für GridBagLayout.
		GridBagConstraints c = new GridBagConstraints();
		// Regel: Objekt füllt Zelle in X- und Y-Richtung aus.
		c.fill = GridBagConstraints.BOTH;
		Insets set = new Insets(5, 5, 5, 5);
//		c.insets = set;
		// Regel: Ausdehnung erfolgt mit Gewichtung 1 in X-Richtung.
		c.weightx = 1.0;
		// Regel: Ausdehnung erfolgt mit Gewichtung 1 in Y-Richtung.
		c.weighty = 1.0;

		// Füge Zugname hinzu
		JLabel trainName = new JLabel("Zugname");
		c.gridx = 1;
		c.gridy = 0;
		train1.add(trainName, c);

		JLabel trainModelDesc = new JLabel("ModelDesc");
		c.gridx = 1;
		c.gridy = 1;
		train1.add(trainModelDesc, c);

		JLabel speedLabel = new JLabel("Geschwindigkeit: 57%");
		c.gridx = 2;
		c.gridy = 0;
		train1.add(speedLabel, c);

		JLabel lightLabel = new JLabel("Licht: Aus");
		c.gridx = 2;
		c.gridy = 1;
		train1.add(lightLabel, c);

		// Füge Zugbild hinzu
		JLabel trainImageLabel = new JLabel();
		ImageIcon icon = new ImageIcon();
		Image img;
		icon = new ImageIcon("D:/Bilder/Saved Pictures/Beautiful/2e24cab28226942d.png");
		img = icon.getImage();
		img = img.getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(img);
		trainImageLabel.setIcon(icon);
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.VERTICAL;
		train1.add(trainImageLabel, c);

		JButton editTrainButton = new JButton();
		BufferedImage buttonIcon;
		
		try {
			buttonIcon = ImageIO.read(new File("images/edit.png"));
			editTrainButton = new JButton(new ImageIcon(buttonIcon));
			editTrainButton.setBorder(BorderFactory.createEmptyBorder());
//			editTrainButton.setContentAreaFilled(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		JButton deleteTrainButton = new JButton();
		try {
			buttonIcon = ImageIO.read(new File("images/trash.png"));
			deleteTrainButton = new JButton(new ImageIcon(buttonIcon));
			deleteTrainButton.setBorder(BorderFactory.createEmptyBorder());
//			editTrainButton.setContentAreaFilled(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JPanel trainActionPanel = new JPanel();
		trainActionPanel.add(editTrainButton);
		trainActionPanel.add(deleteTrainButton);
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		train1.add(trainActionPanel, c);
		
		JLabel directionLabel = new JLabel("Fahrtrichtung: Rechts");
		c.gridx = 2;
		c.gridy = 3;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.BOTH;
		train1.add(directionLabel, c);
		
//		GridBagConstraints c = new GridBagConstraints();
		// Setze Ränder zwischen einzelnen Komponenten
//		Insets set = new Insets(5, 5, 5, 5);
//		c.insets = set;
//		
//		c.gridx = 0;
//		c.gridy = 0;
//		c.gridwidth = 1;
//		c.gridheight = 2;
//		c.anchor = GridBagConstraints.LINE_START;
//		c.fill = GridBagConstraints.HORIZONTAL;

//		JLabel trainImageLabel = new JLabel("Kein Bild gesetzt");
//		ImageIcon icon = new ImageIcon();
//		Image img;
//		icon = new ImageIcon("D:/Bilder/Saved Pictures/Beautiful/2e24cab28226942d.png");
//		img = icon.getImage();
//		img = img.getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH);
//		icon = new ImageIcon(img);
//		trainImageLabel.setText("");
//		trainImageLabel.setIcon(icon);
//		train1.add(trainImageLabel, c);
		
		// Setze Layout Einstellungen für 1. Textfeld
//		c = new GridBagConstraints();
//		c.insets = set;
//		c.gridx = 0;
//		c.gridy = 0;
//		c.gridwidth = GridBagConstraints.REMAINDER;
//		c.fill = GridBagConstraints.HORIZONTAL;
//		c.anchor = GridBagConstraints.LINE_START;
//		JLabel trainName = new JLabel("Zugname");
//		train1.add(trainName, c);
		
		// Setze Layout Einstellungen für 2. Textfeld
//		c.gridy = 1;
//		JLabel trainModelDesc = new JLabel("ModelDesc");
//		train1.add(trainModelDesc, c);
		
//		JButton button;
//	    train1.setLayout(new GridBagLayout());
//	    GridBagConstraints c = new GridBagConstraints();
//	    c.fill = GridBagConstraints.HORIZONTAL;
//	    Insets set = new Insets(5, 5, 5, 5);
//		c.insets = set;
//	 
//	    JLabel trainImageLabel = new JLabel();
//		ImageIcon icon = new ImageIcon();
//		Image img;
//		icon = new ImageIcon("D:/Bilder/Saved Pictures/Beautiful/2e24cab28226942d.png");
//		img = icon.getImage();
//		img = img.getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH);
//		icon = new ImageIcon(img);
//		trainImageLabel.setIcon(icon);
//	    c.gridheight = 2;
//	    c.fill = GridBagConstraints.HORIZONTAL;
//	    c.gridx = 0;
//	    c.gridy = 0;
//	    train1.add(trainImageLabel, c);
//	 
//	    JLabel trainName = new JLabel("Zugname");
//	    c.fill = GridBagConstraints.HORIZONTAL;
//	    c.weightx = 0.5;
//	    c.gridx = 1;
//	    c.gridy = 0;
//	    train1.add(trainName, c);
//	 
//	    button = new JButton("Button 3");
//	    c.fill = GridBagConstraints.HORIZONTAL;
//	    c.weightx = 0.5;
//	    c.gridx = 2;
//	    c.gridy = 1;
//	    train1.add(button, c);
//	 
//	    JLabel trainModelDesc = new JLabel("ModelDesc");
//	    c.fill = GridBagConstraints.HORIZONTAL;
////	    c.ipady = 40;      //make this component tall
//	    c.weightx = 0.0;
////	    c.gridwidth = 1;
//	    c.gridx = 1;
//	    c.gridy = 1;
//	    train1.add(trainModelDesc, c);
//	 
//	    button = new JButton("5");
//	    c.fill = GridBagConstraints.HORIZONTAL;
//	    c.ipady = 0;       //reset to default
//	    c.weighty = 1.0;   //request any extra vertical space
//	    c.anchor = GridBagConstraints.PAGE_END; //bottom of space
//	    c.insets = new Insets(10,0,0,0);  //top padding
//	    c.gridx = 1;       //aligned with button 2
//	    c.gridwidth = 2;   //2 columns wide
//	    c.gridy = 2;       //third row
//	    train1.add(button, c);
		
//		JButton select = new JButton("Select");
//		select.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent action) {
//				JButton source = (JButton) action.getSource();
//				source.getParent().setBackground(Color.LIGHT_GRAY);
//				source.getParent().revalidate();
//				System.out.println(source.getParent().getName());
//			}
//		});
//		train1.add(select);
		panelTrains.add(train1);

		JPanel train2 = new JPanel();
		train2.setBorder(BorderFactory.createTitledBorder("train2"));
		train2.setPreferredSize(new Dimension(250, 100));
		train2.setMaximumSize(new Dimension(250, 100));
		train2.setMinimumSize(new Dimension(250, 100));
		panelTrains.add(train2);

		
		JButton addTrainButton = new JButton("New Train");

		MainController mainController = new MainController(frame, panelTrains, trainCollection);
		addTrainButton.setActionCommand("addTrain");
		addTrainButton.addActionListener(mainController);

		panel.add(addTrainButton);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width/2-frame.getSize().width/2,
		dim.height/2-frame.getSize().height/2);
		frame.setVisible(true);
	}
}

//class ButtonController implements ActionListener {
//	private JPanel panel1;
//	private JFrame frame;
//
//	public ButtonController(JPanel panel1, JFrame frame) {
//		this.panel1 = panel1;
//		this.frame = frame;
//	}
//
//	public void actionPerformed(ActionEvent action) {
//		JPanel train3 = new JPanel();
//		train3.setBorder(BorderFactory.createTitledBorder("train3"));
//		train3.setPreferredSize(new Dimension(250, 100));
//		train3.setMaximumSize(new Dimension(250, 100));
//		train3.setMinimumSize(new Dimension(250, 100));
//		this.panel1.add(train3);
//		this.panel1.revalidate();
//	}
//}
