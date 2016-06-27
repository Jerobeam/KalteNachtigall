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

		JPanel trainsPanel = new JPanel();
		trainsPanel.setLayout(new BoxLayout(trainsPanel, BoxLayout.Y_AXIS));

		final JScrollPane scroll = new JScrollPane(trainsPanel);

		panel.add(scroll);

		JButton addTrainButton = new JButton("New Train");

		MainController mainController = new MainController(frame, trainsPanel, trainCollection);
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
