package prod;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
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

	public static void createGUI() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		JFrame frame = new JFrame("Lokomotiv Führer 2.0");

		// Erstelle MenuBar und füge sie dem Frame hinzu
		JMenuBar menuBar = new JMenuBar();
		JMenu menuEntry = new JMenu("Menü");

		// Erstellen des "Speichern"-Eintrags
		JMenuItem menuSave = new JMenuItem("Speichern");
		ImageIcon iconSave = new ImageIcon();
		Image imgSave;
		iconSave = new ImageIcon("images/save.png");
		imgSave = iconSave.getImage();
		imgSave = imgSave.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
		iconSave = new ImageIcon(imgSave);
		menuSave.setIcon(iconSave);
		menuEntry.add(menuSave);
		menuBar.add(menuEntry);
		frame.setJMenuBar(menuBar);

		JPanel leftPanel = new JPanel();
		leftPanel.setBorder(BorderFactory.createTitledBorder("Deine Züge"));
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setPreferredSize(new Dimension(350, 500));
		leftPanel.setMaximumSize(new Dimension(350, 500));
		frame.add(leftPanel, BorderLayout.WEST);

		JPanel trainsPanel = new JPanel();
		MainController mainController = new MainController(frame, trainsPanel, trainCollection);
		trainsPanel.setLayout(new BoxLayout(trainsPanel, BoxLayout.Y_AXIS));

		JScrollPane scrollPanel = new JScrollPane(trainsPanel);

		leftPanel.add(scrollPanel);

		JButton addTrainButton = new JButton("Neuer Zug");
		addTrainButton.setActionCommand("addTrain");
		addTrainButton.addActionListener(mainController);

		leftPanel.add(addTrainButton);

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		// rightPanel.setPreferredSize(new Dimension(350, 500));
		// rightPanel.setMaximumSize(new Dimension(350, 500));

		drawControllerArea(rightPanel);

		frame.add(rightPanel);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.setSize(800, 600);
		frame.pack();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

		JPanel stopAllPanel = new JPanel(new BorderLayout());

		JButton stopAllButton = new JButton("Alle Züge stoppen");
		stopAllButton.setBackground(Color.RED);
		stopAllButton.setForeground(Color.RED);
		ImageIcon iconStop = new ImageIcon();
		Image imgStop;
		iconStop = new ImageIcon("images/stop.png");
		imgStop = iconStop.getImage();
		imgStop = imgStop.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
		iconStop = new ImageIcon(imgStop);
		stopAllButton.setIcon(iconStop);

		stopAllPanel.setPreferredSize(new Dimension(frame.getWidth(), 30));
		stopAllPanel.setMaximumSize(new Dimension(frame.getWidth(), 30));
		stopAllPanel.add(stopAllButton, BorderLayout.CENTER);
		rightPanel.add(stopAllPanel);

		frame.setVisible(true);
	}

	public static void drawControllerArea(JPanel panel) {
		JPanel trainControlPanel = new JPanel();
		trainControlPanel.setLayout(new GridBagLayout());

		// Erstelle Regeln für GridBagLayout.
		GridBagConstraints c = new GridBagConstraints();
		Insets set = new Insets(10, 10, 10, 10);
		c.insets = set;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;

		// Füge Zugbild hinzu
		JLabel trainImageLabel = new JLabel();
		ImageIcon icon = new ImageIcon();
		Image img;
		icon = new ImageIcon("D:/Bilder/Saved Pictures/Beautiful/Background/stock-photo-154870507.jpg");
		img = icon.getImage();
		img = img.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(img);
		trainImageLabel.setIcon(icon);

		// JPanel panel1 = new JPanel();
		// panel1.setBorder(BorderFactory.createTitledBorder("Panel1"));
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 2;
		c.anchor = GridBagConstraints.PAGE_START;
		c.fill = GridBagConstraints.VERTICAL;
		trainControlPanel.add(trainImageLabel, c);

		JLabel trainName = new JLabel("Random Train Name Focker");
		Font myFont = new Font(trainName.getFont().getFontName(), Font.BOLD, 16);
		trainName.setFont(myFont);

		// JPanel panel2 = new JPanel();
		// panel2.setBorder(BorderFactory.createTitledBorder("Panel2"));
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		trainControlPanel.add(trainName, c);

		JLabel trainModelDesc = new JLabel("Dampflok Mampflol");
		myFont = new Font(trainModelDesc.getFont().getFontName(), Font.PLAIN, 14);
		trainModelDesc.setFont(myFont);
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.anchor = GridBagConstraints.PAGE_START;
		trainControlPanel.add(trainModelDesc, c);

		JLabel speedLabel = new JLabel("Geschwindigkeit:");
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		trainControlPanel.add(speedLabel, c);

		JPanel speedPanel = new JPanel();

		JSlider speedSlider = new JSlider();
		speedSlider.setMajorTickSpacing(100);
		speedSlider.setMinorTickSpacing(1);
		speedSlider.setPaintTicks(true);
		speedSlider.setPaintLabels(true);
		speedSlider.setValue(0);
		speedPanel.add(speedSlider);

		JButton stopTrainButton = new JButton("Stop");
		stopTrainButton.setBackground(Color.RED);
		stopTrainButton.setForeground(Color.RED);
		ImageIcon iconStop = new ImageIcon();
		Image imgStop;
		iconStop = new ImageIcon("images/stop.png");
		imgStop = iconStop.getImage();
		imgStop = imgStop.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
		iconStop = new ImageIcon(imgStop);
		stopTrainButton.setIcon(iconStop);

		speedPanel.add(stopTrainButton);

		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		trainControlPanel.add(speedPanel, c);

		JLabel directionLabel = new JLabel("Fahrtrichtung:");
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		trainControlPanel.add(directionLabel, c);

		// JPanel panel11 = new JPanel();
		// panel11.setBorder(BorderFactory.createTitledBorder("panel11"));
		// c.gridx = 1;
		// c.gridy = 3;
		// c.gridwidth = 1;
		// trainControlPanel.add(panel11, c);

		JPanel panel12 = new JPanel();
		panel12.setBorder(BorderFactory.createTitledBorder("panel12"));
		c.gridx = 2;
		c.gridy = 3;
		c.gridwidth = 1;
		trainControlPanel.add(panel12, c);

		JLabel lightLabel = new JLabel("Licht:");
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
		trainControlPanel.add(lightLabel, c);

		// JPanel panel14 = new JPanel();
		// panel14.setBorder(BorderFactory.createTitledBorder("panel14"));
		// c.gridx = 1;
		// c.gridy = 4;
		// c.gridwidth = 1;
		// trainControlPanel.add(panel14, c);

		JPanel panel15 = new JPanel();
		panel15.setBorder(BorderFactory.createTitledBorder("panel15"));
		c.gridx = 2;
		c.gridy = 4;
		c.gridwidth = 1;
		trainControlPanel.add(panel15, c);

		panel.add(trainControlPanel);
	}
}

// class ButtonController implements ActionListener {
// private JPanel panel1;
// private JFrame frame;
//
// public ButtonController(JPanel panel1, JFrame frame) {
// this.panel1 = panel1;
// this.frame = frame;
// }
//
// public void actionPerformed(ActionEvent action) {
// JPanel train3 = new JPanel();
// train3.setBorder(BorderFactory.createTitledBorder("train3"));
// train3.setPreferredSize(new Dimension(250, 100));
// train3.setMaximumSize(new Dimension(250, 100));
// train3.setMinimumSize(new Dimension(250, 100));
// this.panel1.add(train3);
// this.panel1.revalidate();
// }
// }

// public static void drawControllerArea(JFrame frame) {
// JPanel trainControlPanel = new JPanel();
// trainControlPanel.setBorder(BorderFactory.createTitledBorder("Zugkontroller"));
// trainControlPanel.setLayout(new GridBagLayout());
//
// // Erstelle Regeln für GridBagLayout.
// GridBagConstraints c = new GridBagConstraints();
// // Regel: Objekt füllt Zelle in X- und Y-Richtung aus.
// c.fill = GridBagConstraints.BOTH;
// // Regel: Ausdehnung erfolgt mit Gewichtung 1 in X-Richtung.
// c.weightx = 1.0;
// // Regel: Ausdehnung erfolgt mit Gewichtung 1 in Y-Richtung.
// c.weighty = 1.0;
//
// // Erstelle Button für eine Zelle.
// JPanel panel1 = new JPanel();
// panel1.setBorder(BorderFactory.createTitledBorder("Panel1"));
// // Regel: Spalte 0.
// c.gridx = 0;
// // Regel: Zeile 0.
// c.gridy = 0;
// // Button zur Pane des Fensters hinzufügen.
// trainControlPanel.add(panel1, c);
//
// // Erstelle Button für eine Zelle.
// JPanel panel2 = new JPanel();
// panel2.setBorder(BorderFactory.createTitledBorder("Panel2"));
// // Regel: Spalte 1.
// c.gridx = 1;
// // Regel: Zeile 0.
// c.gridy = 0;
// // Button zur Pane des Fensters hinzufügen.
// trainControlPanel.add(panel2, c);
//
// // Erstelle Button für eine Zelle.
// JPanel panel3 = new JPanel();
// panel3.setBorder(BorderFactory.createTitledBorder("Panel3"));
// // Regel: Spalte 0.
// c.gridx = 0;
// // Regel: Zeile 1.
// c.gridy = 1;
// // Button zur Pane des Fensters hinzufügen.
// trainControlPanel.add(panel3, c);
//
// // Erstelle Button für eine Zelle.
// JPanel panel4 = new JPanel();
// panel4.setBorder(BorderFactory.createTitledBorder("Panel4"));
// // Regel: Spalte 1.
// c.gridx = 1;
// // Regel: Zeile 1.
// c.gridy = 1;
// // Button zur Pane des Fensters hinzufügen.
// trainControlPanel.add(panel4, c);
//
// // Erstelle Button für zwei Zellen (vertikal).
// JPanel panel5 = new JPanel();
// panel5.setBorder(BorderFactory.createTitledBorder("Panel5"));
// // Regel: Spalte 2.
// c.gridx = 2;
// // Regel: Zeile 0.
// c.gridy = 0;
// // Regel: Button erstreckt sich vertiktal über zwei Zellen.
// c.gridheight = 2;
// // Button zur Pane des Fensters hinzufügen.
// trainControlPanel.add(panel5, c);
//
// // Erstelle Button für drei Zellen (horizontal).
// JPanel panel6 = new JPanel();
// panel6.setBorder(BorderFactory.createTitledBorder("Panel6"));
// // Regel: Spalte 0.
// c.gridx = 0;
// // Regel: Zeile 3.
// c.gridy = 3;
// // Regel: Button erstreckt sich horizontal über drei Zellen.
// c.gridwidth = 3;
// // Button zur Pane des Fensters hinzufügen.
// trainControlPanel.add(panel6, c);
// frame.add(trainControlPanel);
// }