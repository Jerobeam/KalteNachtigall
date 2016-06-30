package prod;

import java.awt.*;
import javax.swing.*;

/**
 * Klasse zum Starten der Anwendung
 * 
 * @author Sebastian Röhling
 *
 * @version 1.0
 */
public class Main {

	/**
	 * Zugliste
	 */
	private static TrainCollection trainCollection = new TrainCollection();

	/**
	 * Controller
	 */
	private static Controller controller;

	/**
	 * Main Methode zum Start der Anwendung
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// Übergebe das Aufbauen der GUI an den Event-Dispatcher Thread
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createGUI();
			}
		});
	}

	/**
	 * Baut initiales GUI auf
	 */
	public static void createGUI() {
		// Setze Windows Look and Feel
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		// Erstelle Hauptframe
		JFrame frame = new JFrame("Lokomotivführer 2.0");

		// Erstelle MenuBar
		JMenuBar menuBar = new JMenuBar();
		JMenu menuEntry = new JMenu("Menü");

		// Erstellen des "Fenster schließen"-Eintrags mit Icon
		JMenuItem menuNewTrain = new JMenuItem("Neuer Zug");
		// Belege Eintrag mit Shortcut "Strg-N"
		menuNewTrain.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		ImageIcon iconAdd = new ImageIcon();
		Image imgAdd;
		iconAdd = new ImageIcon("images/add.png");
		imgAdd = iconAdd.getImage();
		imgAdd = imgAdd.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
		iconAdd = new ImageIcon(imgAdd);
		menuNewTrain.setIcon(iconAdd);
		menuEntry.add(menuNewTrain);

		// Erstellen des "Speichern"-Eintrags mit Icon
		JMenuItem menuSave = new JMenuItem("Speichern");
		// Belege Eintrag mit Shortcut "Strg-S"
		menuSave.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		ImageIcon iconSave = new ImageIcon();
		Image imgSave;
		iconSave = new ImageIcon("images/save.png");
		imgSave = iconSave.getImage();
		imgSave = imgSave.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
		iconSave = new ImageIcon(imgSave);
		menuSave.setIcon(iconSave);
		menuEntry.add(menuSave);

		// Erstellen des "Fenster schließen"-Eintrags mit Icon
		JMenuItem menuClose = new JMenuItem("Fenster schließen");
		// Belege Eintrag mit Shortcut "Strg-W"
		menuClose.setAccelerator(KeyStroke.getKeyStroke('W', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		ImageIcon iconClose = new ImageIcon();
		Image imgClose;
		iconClose = new ImageIcon("images/close.png");
		imgClose = iconClose.getImage();
		imgClose = imgClose.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
		iconClose = new ImageIcon(imgClose);
		menuClose.setIcon(iconClose);
		menuEntry.add(menuClose);

		// Füge Menueintrag der Menubar hinzu
		menuBar.add(menuEntry);
		// Setze MenuBar in den Frame		
		frame.setJMenuBar(menuBar);

		// Erstelle Panel für den linken Teilbereich der Anwendung
		JPanel leftPanel = new JPanel();
		leftPanel.setBorder(BorderFactory.createTitledBorder("Deine Züge"));
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setPreferredSize(new Dimension(350, 500));
		leftPanel.setMaximumSize(new Dimension(350, 500));
		// Setze leftPanel in den Frame
		frame.add(leftPanel, BorderLayout.WEST);

		// Erstelle Panel für die Zugliste
		JPanel trainsPanel = new JPanel();
		trainsPanel.setLayout(new BoxLayout(trainsPanel, BoxLayout.Y_AXIS));
		// Erstelle Panel für die linke Seite, wenn Zugliste leer ist
		JPanel noTrainPanel = new JPanel(new GridBagLayout());
		// Erstelle dazugehöriges Label
		JLabel noTrainLabel = new JLabel("Noch keinen Zug erstellt");
		noTrainPanel.add(noTrainLabel);
		trainsPanel.add(noTrainPanel);

		// Erstelle Scroll Bereich, der die Zugliste beinhaltet
		JScrollPane scrollPanel = new JScrollPane(trainsPanel);
		// Setze Scroll Bereich in den linken Teilbereich der Anwendung
		leftPanel.add(scrollPanel);
		// Erstelle addTrainButton
		JButton addTrainButton = new JButton("Neuer Zug");
		// Button rechtsbündig ausrichten
		addTrainButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
		// Setze icon für addTrainButton
		imgAdd = imgAdd.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
		iconAdd = new ImageIcon(imgAdd);
		addTrainButton.setIcon(iconAdd);
		// Setze addTrainButton in den linken Teilbereich der Anwendung
		leftPanel.add(addTrainButton);
		
		// Panel für rechten Bereich der Anwendung erstellen
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		
		// Erstelle Panel für Controller-Bereich der Anwendung
		JPanel trainControlPanel = new JPanel(new GridBagLayout());
		// Füge initial ein Platzhalterlabel hinzu
		trainControlPanel.add(new JLabel("Kein Zug ausgewählt"));
		// Controller-Bereich rechts Einfügen
		rightPanel.add(trainControlPanel);
		
		// Erstelle Button zum Stoppen aller Züge
		JButton stopAllButton = new JButton("Alle Züge stoppen");

		frame.add(rightPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(850, 550);
		// Zentriere frame auf dem Bildschirm
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);frame.setVisible(true);
		
		// Erstelle Panel als Container des stoppAllButton
		JPanel stopAllPanel = new JPanel(new BorderLayout());
		// Deaktiviere den stopAllButton initial
		stopAllButton.setEnabled(false);
		// Bearbeite Design des Button
		stopAllButton.setBackground(Color.RED);
		stopAllButton.setForeground(Color.RED);
		ImageIcon iconStop = new ImageIcon();
		Image imgStop;
		iconStop = new ImageIcon("images/stop.png");
		imgStop = iconStop.getImage();
		imgStop = imgStop.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
		iconStop = new ImageIcon(imgStop);
		stopAllButton.setIcon(iconStop);
		// Setze die Größe des Panels in Abhängigkeit zu der frame-Größe
		stopAllPanel.setPreferredSize(new Dimension(frame.getWidth(), 30));
		stopAllPanel.setMaximumSize(new Dimension(frame.getWidth(), 30));
		stopAllPanel.add(stopAllButton, BorderLayout.CENTER);
		rightPanel.add(stopAllPanel);
		
		// Initialisiere Controller
		controller = new Controller(frame, trainsPanel, trainControlPanel, trainCollection, stopAllButton);
		// Menueintrag "Speichern" Eintrag beim Controller anmelden
		menuSave.addActionListener(controller);
		menuSave.setActionCommand("saveData");
		// Menueintrag "Schließen" Eintrag beim Controller anmelden
		menuClose.addActionListener(controller);
		menuClose.setActionCommand("close");
		// Menueintrag "Neuer Zug" Eintrag beim Controller anmelden
		menuNewTrain.addActionListener(controller);
		menuNewTrain.setActionCommand("addTrain");
		// addTrainButton beim Controller anmelden
		addTrainButton.addActionListener(controller);
		addTrainButton.setActionCommand("addTrain");
		// stopAllButton beim Controller anmelden
		stopAllButton.addActionListener(controller);
		stopAllButton.setActionCommand("stopAllTrains");
		// frame bei Controller anmelden
		frame.addWindowListener(controller);

		// Lade vorhandene Züge in die Anwendung
		controller.initializeFromJSON();
	}
}
