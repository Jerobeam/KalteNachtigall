package prototype;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame frame = new JFrame("Layout-Manager: GridBag-Layout");
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
		train1.setBorder(BorderFactory.createTitledBorder("train1"));
		train1.setPreferredSize(new Dimension(250, 100));
		train1.setMaximumSize(new Dimension(250, 100));
		train1.setMinimumSize(new Dimension(250, 100));
		panelTrains.add(train1);

		JPanel train2 = new JPanel();
		train2.setBorder(BorderFactory.createTitledBorder("train2"));
		train2.setPreferredSize(new Dimension(250, 100));
		train2.setMaximumSize(new Dimension(250, 100));
		train2.setMinimumSize(new Dimension(250, 100));
		panelTrains.add(train2);

//		JPanel train3 = new JPanel();
//		train3.setBorder(BorderFactory.createTitledBorder("train3"));
//		train3.setPreferredSize(new Dimension(250, 300));
//		train3.setMaximumSize(new Dimension(250, 300));
//		train3.setMinimumSize(new Dimension(250, 300));
//		panelTrains.add(train3);
		JButton addTrainButton = new JButton();
		
		ButtonController controller = new ButtonController(panelTrains, panel, scroll, frame);
		addTrainButton.addActionListener(controller);

		panel.add(addTrainButton);
		panel.add (new JButton ("Abbrechen"));
//
//		// Eigenschaftsänderung für die Beschriftung "Vorname:".
//		c.gridy = 1;
//		// frame.add (new JLabel ("Vorname: "), c);
//
//		// Eigenschaftsänderung für die Beschriftung "Kommentar:".
//		c.gridy = 2;
//		// frame.add (new JLabel ("Kommentar: "), c);
//		// Eigenschaften Textfeld zur Beschriftung "Name:".
//		c = new GridBagConstraints();
//		c.insets = set;
//		c.gridx = 1;
//		c.gridy = 0;
//		// Setze die Breite der GUI-Komponente.
//		c.gridwidth = GridBagConstraints.REMAINDER;
//		// Setze die Ausbreitung der GUI-Komponente.
//		c.fill = GridBagConstraints.HORIZONTAL;
//		// Setze die Ausrichtung.
//		c.anchor = GridBagConstraints.LINE_START;
//		// frame.add (new JTextField (30), c);
//		// Eigenschaftsänderung für das Textfeld zur Beschriftung
//		// "Vorname:".
//		c.gridy = 1;
//		// frame.add (new JTextField (30), c);
//		// Eigenschaften mehrzeiliges Textfeld.
//		c = new GridBagConstraints();
//		c.insets = set;
//		c.gridx = 0;
//		c.gridy = 3;
//		// Setze die Breite.
//		c.gridwidth = GridBagConstraints.REMAINDER;
//		// Setze die Ausbreitung.
//		c.fill = GridBagConstraints.BOTH;
//		// Setze die Ausrichtung.
//		c.anchor = GridBagConstraints.FIRST_LINE_START;
//		// Setze Gewichtigkeit bei Größenänderung.
//		c.weightx = 1.0;
//		c.weighty = 1.0;
//		// frame.add (new JTextArea (5,5), c);
//		// Eigenschaften Schaltfläche "Abbrechen".
//		c = new GridBagConstraints();
//		c.insets = set;
//		c.gridx = 0;
//		c.gridy = 4;
//		// Setze die Ausrichtung
//		c.anchor = GridBagConstraints.LINE_START;
//		// frame.add (new JButton ("Abbrechen"), c);
//		// Eigenschaftsänderung für die Schaltfläche "Übernehmen".
//		c.gridx = 1;
		// frame.add (new JButton ("Übernehmen"), c);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1050, 590);
		frame.setVisible(true);
	}

}

class ButtonController implements ActionListener {
	private JPanel panel1;
	private JPanel panel2;
	private JScrollPane scroll;
	private JFrame frame;
	
	public ButtonController(JPanel panel1, JPanel panel2, JScrollPane scroll, JFrame frame){
		this.panel1 = panel1;
		this.panel2 = panel2;
		this.scroll = scroll;
		this.frame = frame;
	}
	
	public void actionPerformed(ActionEvent action) {
		JPanel train3 = new JPanel();
		train3.setBorder(BorderFactory.createTitledBorder("train3"));
		train3.setPreferredSize(new Dimension(250, 100));
		train3.setMaximumSize(new Dimension(250, 100));
		train3.setMinimumSize(new Dimension(250, 100));
		this.panel1.add(train3);
		this.panel1.revalidate();
	}
}
