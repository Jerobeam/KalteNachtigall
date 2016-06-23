import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// GUI im Event-Dispatch-Thread erzeugen.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createGUI();
			}
		});
	}

	private static void createGUI() {
		// //Hauptfenster erzeugen.
		// MyFrame frame = new MyFrame();
		// //Titel für das Hauptfenster setzen.
		// frame.setTitle ("Loco Controllo");
		//
		// JScrollPane scroll = new JScrollPane();
		// scroll.setBorder (BorderFactory.createTitledBorder ("Scroll"));
		// scroll.setPreferredSize (new Dimension (300, 50));
		// JPanel trains = new JPanel();
		//
		// trains.setBorder (BorderFactory.createTitledBorder ("Deine Züge"));
		// trains.setPreferredSize (new Dimension (300, 50));
		// trains.setLayout (new BoxLayout (trains, BoxLayout.Y_AXIS));
		//
		//
		// // Erstelle internes Panel
		// JPanel panel = new JPanel();
		//// panel.setLayout (new BoxLayout (panel, BoxLayout.X_AXIS));
		// panel.setBorder (BorderFactory.createTitledBorder ("Zug1"));
		// panel.setPreferredSize (new Dimension (50, 400));
		// panel.setMinimumSize(new Dimension (300, 400));
		// panel.setMaximumSize(new Dimension (300, 400));
		// scroll.add(panel);
		//
		// // Erstelle internes Panel
		// JPanel panel2 = new JPanel();
		//// panel.setLayout (new BoxLayout (panel, BoxLayout.X_AXIS));
		// panel2.setBorder (BorderFactory.createTitledBorder ("Zug1"));
		// panel2.setPreferredSize (new Dimension (50, 400));
		// panel2.setMinimumSize(new Dimension (300, 400));
		// panel2.setMaximumSize(new Dimension (300, 400));
		// scroll.add(panel2);
		//
		// scroll.add(trains);
		//
		// frame.add(scroll,BorderLayout.WEST);
		//
		// ArrayList<JPanel> panels = frame.getPanels();
		//
		// for (JPanel addPanel : panels) {
		// //frame.add(addPanel);
		// }
		//
		// //Definieren, was beim Schließen des Fensters geschehen soll.
		// frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE );
		//
		// //Größe setzen.
		// frame.setSize (1050, 590);
		// Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		// frame.setLocation(dim.width/2-frame.getSize().width/2,
		// dim.height/2-frame.getSize().height/2);
		// //Hauptfenster anzeigen.
		// frame.setVisible (true);
		
		JFrame frame = new JFrame();
		frame.setTitle ("Loco Controllo");
		final JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createLineBorder(Color.red));
		panel.setBorder(BorderFactory.createTitledBorder("Deine Züge"));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setMaximumSize(new Dimension(300, 400));

		// Erstelle internes Panel
		JPanel panel1 = new JPanel();
		// panel.setLayout (new BoxLayout (panel, BoxLayout.X_AXIS));
		panel1.setBorder(BorderFactory.createTitledBorder("Zug1"));
		panel1.setPreferredSize(new Dimension(50, 400));
		panel1.setMinimumSize(new Dimension(300, 400));
		panel1.setMaximumSize(new Dimension(300, 400));
		panel.add(panel1);

		// Erstelle internes Panel
		JPanel panel2 = new JPanel();
		// panel.setLayout (new BoxLayout (panel, BoxLayout.X_AXIS));
		panel2.setBorder(BorderFactory.createTitledBorder("Zug2"));
		panel2.setPreferredSize(new Dimension(50, 400));
		panel2.setMinimumSize(new Dimension(300, 400));
		panel2.setMaximumSize(new Dimension(300, 400));
		panel.add(panel2);

		// Erstelle internes Panel
		JPanel panel3 = new JPanel();
		// panel.setLayout (new BoxLayout (panel, BoxLayout.X_AXIS));
		panel3.setBorder(BorderFactory.createTitledBorder("Zug3"));
		panel3.setPreferredSize(new Dimension(50, 400));
		panel3.setMinimumSize(new Dimension(300, 400));
		panel3.setMaximumSize(new Dimension(300, 400));
		panel.add(panel3);
		
		final JScrollPane scroll = new JScrollPane(panel);

		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridLayout());
		
		GridBagConstraints gbConstraints;
		
		// Eigenschaften für die Beschriftung "Name:".
		gbConstraints = new GridBagConstraints();
		Insets set = new Insets (5, 5, 5, 5);
		gbConstraints.insets = set; // Abstände setzen.
		gbConstraints.gridx = 0; // Position auf X-Achse setzen.
		gbConstraints.gridy = 0; // Position auf Y-Achse setzen.
		// Ausrichtung setzen.
		gbConstraints.anchor = GridBagConstraints.LINE_START;
		
		frame.add(scroll);
		frame.setSize (1050, 590);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width/2-frame.getSize().width/2,
		dim.height/2-frame.getSize().height/2);
		frame.setVisible(true);
	}
}
