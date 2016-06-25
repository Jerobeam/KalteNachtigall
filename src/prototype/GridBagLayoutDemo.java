package prototype;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GridBagLayoutDemo {
	private static void createGUI() {
		// Fenster definieren.
		JFrame frame = new JFrame("GridBagLayout-Demo (komplex)");
		// Definieren, was beim Schließen des Fensters geschehen soll.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Referenz auf Content-Pane des Hauptfensters abfragen.
		Container pane = frame.getContentPane();
		// Layout des Fensters setzen.
		pane.setLayout(new GridBagLayout());

		// Erstelle Regeln für GridBagLayout.
		GridBagConstraints c = new GridBagConstraints();
		// Regel: Objekt füllt Zelle in X- und Y-Richtung aus.
		c.fill = GridBagConstraints.BOTH;
		Insets set = new Insets(5, 5, 5, 5);
		c.insets = set;
		// Regel: Ausdehnung erfolgt mit Gewichtung 1 in X-Richtung.
		c.weightx = 1.0;
		// Regel: Ausdehnung erfolgt mit Gewichtung 1 in Y-Richtung.
		c.weighty = 1.0;

		// Füge Zugname hinzu
		JButton button = new JButton("BTN1", new ImageIcon("icons/tango/16x16/actions/edit-undo.png"));
		JLabel trainName = new JLabel("Zugname");
		c.gridx = 1;
		c.gridy = 0;
		pane.add(trainName, c);

		JLabel trainModelDesc = new JLabel("ModelDesc");
		c.gridx = 1;
		c.gridy = 1;
		pane.add(trainModelDesc, c);

		JLabel speedLabel = new JLabel("Geschwindigkeit: 57%");
		c.gridx = 2;
		c.gridy = 0;
		pane.add(speedLabel, c);

		JLabel lightLabel = new JLabel("Licht: Aus");
		c.gridx = 2;
		c.gridy = 1;
		pane.add(lightLabel, c);

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
		pane.add(trainImageLabel, c);

		JButton editTrainButton = new JButton("E");
		JButton deleteTrainButton = new JButton("D");
		JPanel trainActionPanel = new JPanel();
		trainActionPanel.add(editTrainButton);
		trainActionPanel.add(deleteTrainButton);
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		pane.add(trainActionPanel, c);
		
		JLabel directionLabel = new JLabel("Fahrtrichtung: Rechts");
		c.gridx = 2;
		c.gridy = 3;
		c.gridwidth = 1;
		pane.add(directionLabel, c);

		// Fenstergröße setzen.
		frame.setSize(410, 200);
		// Fenster anzeigen.
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// GUI im Event-Dispatch-Thread erzeugen.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createGUI();
			}
		});
	}
}