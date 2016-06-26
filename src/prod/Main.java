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

	public static void createGUI() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		JFrame frame = new JFrame("Lokomotiv Führer 2.0");

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
		// train1.setBorder(BorderFactory.createTitledBorder("Zug 1"));
		train1.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		train1.setPreferredSize(new Dimension(315, 100));
		train1.setMaximumSize(new Dimension(315, 100));
		train1.setMinimumSize(new Dimension(315, 100));

		train1.setLayout(new GridLayout());

		train1.setLayout(new GridBagLayout());

		// Erstelle Regeln für GridBagLayout.
		GridBagConstraints c = new GridBagConstraints();
		// Regel: Objekt füllt Zelle in X- und Y-Richtung aus.
		c.fill = GridBagConstraints.BOTH;
		Insets set = new Insets(5, 5, 5, 5);
		// c.insets = set;
		// Regel: Ausdehnung erfolgt mit Gewichtung 1 in X-Richtung.
		c.weightx = 1.0;
		// Regel: Ausdehnung erfolgt mit Gewichtung 1 in Y-Richtung.
		c.weighty = 1.0;

		// Füge Zugname hinzu
		JLabel trainName = new JLabel("DL-TH2231");

		c.gridx = 1;
		c.gridy = 0;
		train1.add(trainName, c);

		JLabel trainModelDesc = new JLabel("Dampflok");
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
		ImageIcon iconEdit = new ImageIcon();
		Image imgEdit;
		iconEdit = new ImageIcon("images/edit.png");
		imgEdit = iconEdit.getImage();
		imgEdit = imgEdit.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
		iconEdit = new ImageIcon(imgEdit);
		editTrainButton = new JButton(iconEdit);
		editTrainButton.setBorder(BorderFactory.createEmptyBorder());

		JButton deleteTrainButton = new JButton();
		ImageIcon iconDelete = new ImageIcon();
		Image imgDelete;
		iconDelete = new ImageIcon("images/trash.png");
		imgDelete = iconDelete.getImage();
		imgDelete = imgDelete.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
		iconDelete = new ImageIcon(imgDelete);
		deleteTrainButton = new JButton(iconDelete);
		deleteTrainButton.setBorder(BorderFactory.createEmptyBorder());

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
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
		frame.setVisible(true);
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
