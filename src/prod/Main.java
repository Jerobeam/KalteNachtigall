package prod;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
		GridBagConstraints c;
		// Objekt für Abstände zwischen den GUI-Komponenten.
		Insets set = new Insets(5, 5, 5, 5);

		// Eigenschaften für die Beschriftung "Name:".
		c = new GridBagConstraints();
		c.insets = set; // Abstände setzen.
		c.gridx = 0; // Position auf X-Achse setzen.
		c.gridy = 0; // Position auf Y-Achse setzen.
		// Ausrichtung setzen.
		c.anchor = GridBagConstraints.LINE_START;
		// frame.add (new JLabel ("Name: "), c);

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createLineBorder(Color.red));
		panel.setBorder(BorderFactory.createTitledBorder("panel"));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setPreferredSize(new Dimension(300, 500));
		panel.setMaximumSize(new Dimension(300, 500));
		frame.add(panel, BorderLayout.WEST);

		JPanel panelTrains = new JPanel();
		panelTrains.setBorder(BorderFactory.createTitledBorder("panelTrains"));
		// panelTrains.setPreferredSize (new Dimension (250, 5000));
		panelTrains.setLayout(new BoxLayout(panelTrains, BoxLayout.Y_AXIS));

		final JScrollPane scroll = new JScrollPane(panelTrains);

		panel.add(scroll);

		JPanel train1 = new JPanel();
		train1.setName("Train1");
		train1.setBorder(BorderFactory.createTitledBorder("train1"));
		train1.setPreferredSize(new Dimension(250, 100));
		train1.setMaximumSize(new Dimension(250, 100));
		train1.setMinimumSize(new Dimension(250, 100));

		JButton select = new JButton("Select");
		select.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent action) {
				JButton source = (JButton) action.getSource();
				source.getParent().setBackground(Color.LIGHT_GRAY);
				source.getParent().revalidate();
				System.out.println(source.getParent().getName());
			}
		});
		train1.add(select);
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
