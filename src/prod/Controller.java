package prod;

import java.awt.event.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import sun.awt.image.ImageWatched.Link;

/**
 * Controllerklasse mit Logik
 * 
 * @author Sebastian Röhling
 *
 * @version 1.0
 */
public class Controller implements ActionListener, MouseListener, ChangeListener, WindowListener {

	/**
	 * Frame der Anwendung
	 */
	private JFrame mainFrame;
	/**
	 * Panel mit der Zugliste
	 */
	private JPanel trainsPanel;
	/**
	 * Panel des Controller-Bereichs
	 */
	private JPanel controllerAreaPanel;
	/**
	 * Button zum stoppen aller Züge
	 */
	private JButton stopAllButton;
	/**
	 * Sammlung an Zugobjekten
	 */
	private TrainCollection trainCollection;
	/**
	 * Dialog zum Erstellen und Bearbeiten von Zugobjekten
	 */
	private JDialog trainDialog;
	/**
	 * Label mit Zugbild des zu erstellenden/ bearbeitenden Zuges
	 */
	private JLabel trainImageLabel;
	/**
	 * Textfeld mit Zugname des zu erstellenden/ bearbeitenden Zuges
	 */
	private JTextField trainNameTextField;
	/**
	 * Textfeld mit Zugmodellbeschreibung des zu erstellenden/ bearbeitenden
	 * Zuges
	 */
	private JTextField trainModelDescTextField;
	/**
	 * Bildpfad des zu erstellenden/ bearbeitenden Zuges
	 */
	private String imagePath;
	/**
	 * Panel, welches das zu erstellende/ bearbeitende Zugobjekt darstellt
	 */
	private JPanel affectedPanel;
	/**
	 * Zu erstellendes/ bearbeitenden Zugobjekt
	 */
	private Train affectedTrain;
	/**
	 * Panel, welches das ausgewählte Zugobjekt in der Zugliste darstellt
	 */
	private JPanel selectedTrainPanel;
	/**
	 * Ausgewähltes Zugobjekt
	 */
	private Train selectedTrain;
	/**
	 * Geschwindigkeitslabel des ausgewählten Zugobjekt in der Zugliste
	 */
	private JLabel selectedSpeedLabel;
	/**
	 * Richtungslabel des ausgewählten Zugobjekt in der Zugliste
	 */
	private JLabel selectedDirectionsLabel;
	/**
	 * Lichtlabel des ausgewählten Zugobjekt in der Zugliste
	 */
	private JLabel selectedLightLabel;
	/**
	 * ButtonGroup zur Auswahl des Richtung im Controller-Bereich
	 */
	private ButtonGroup directionButtonGroup;
	/**
	 * ToggleButton zur Richtungsauswahl links
	 */
	private JToggleButton toggleLeft;
	/**
	 * ToggleButton zur Richtungsauswahl rechts
	 */
	private JToggleButton toggleRight;
	/**
	 * Schalter zu Licht ein-/ausschalten im Controller-Bereich
	 */
	private JButton switchLightButton;
	/**
	 * Label zur Visualisierung der Lichtstatus des Zuges im Controller-Bereich
	 */
	private JLabel lightBulb;
	/**
	 * Slider zum Einstellen der Zuggeschwindigkeit im Controller-Bereich
	 */
	private JSlider speedSlider;
	/**
	 * Bildlabel des ausgewählten Zuges im Controller-Bereich
	 */
	private JLabel controllerImageLabel;
	/**
	 * Zugnamen-Label des ausgewählten Zuges im Controller-Bereich
	 */
	private JLabel controllerTrainName;
	/**
	 * Zugbeschreibung-Label des ausgewählten Zuges im Controller-Bereich
	 */
	private JLabel controllerTrainModelDesc;
	/**
	 * Button zum Löschen des Zugbildes
	 */
	private JButton deleteImageButton;
	/**
	 * Flag zum Überprüfen, ob sich Zugeigenschaften geändert haben bzw.
	 * gespeichert wurden
	 */
	private boolean isDirty;

	/**
	 * Konstruktor
	 * 
	 * @param mainFrame
	 *            Frame
	 * @param trainsPanel
	 *            Panel mit der Zugliste
	 * @param controllerAreaPanel
	 *            Panel des Controller-Bereichs
	 * @param trainCollection
	 *            Sammlung an Zugobjekten
	 * @param stopAllButton
	 *            Button zum stoppen aller Züge
	 */
	public Controller(JFrame mainFrame, JPanel trainsPanel, JPanel controllerAreaPanel, TrainCollection trainCollection,
			JButton stopAllButton) {
		this.mainFrame = mainFrame;
		this.trainsPanel = trainsPanel;
		this.controllerAreaPanel = controllerAreaPanel;
		this.trainCollection = trainCollection;
		this.stopAllButton = stopAllButton;
	}

	/**
	 * Öffnet einen Dialog zum Erstellen eines neuen Zuges. Dieser erlaubt es
	 * dem zu erstellenden Zugobjekt einen Namen, eine Beschreibung und ein Bild
	 * zu geben.
	 */
	public void openNewDialog() {
		// Erstelle neuen Dialog
		this.trainDialog = new JDialog(this.mainFrame, "Neuer Zug", true);
		trainDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// Setze Layout für Dialog
		trainDialog.setLayout(new GridBagLayout());
		GridBagConstraints c;
		// Setze Ränder zwischen einzelnen Komponenten
		Insets set = new Insets(10, 10, 10, 10);

		// Setze Layout Einstellungen für 1.Label
		c = new GridBagConstraints();
		c.insets = set;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		trainDialog.add(new JLabel("Zugname: "), c);
		// Setze Layout Einstellungen für 2.Label
		c.gridy = 1;
		trainDialog.add(new JLabel("Modell (optional): "), c);
		// Setze Layout Einstellungen für 3.Label
		c.gridy = 3;
		trainDialog.add(new JLabel("Bild (optional): "), c);

		// Setze Layout Einstellungen für 1. Textfeld
		c = new GridBagConstraints();
		c.insets = set;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		trainNameTextField = new JTextField();
		trainDialog.add(trainNameTextField, c);

		// Setze Layout Einstellungen für 2. Textfeld
		c.gridy = 1;
		trainModelDescTextField = new JTextField();
		trainDialog.add(trainModelDescTextField, c);

		// Setze Layout Einstellungen für Panel, welches den "Durchsuchen" und
		// "Bild löschen"-Button enthält
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 2;
		// Erstelle neues Panel mit FlowLayout
		JPanel imageButtonsPanel = new JPanel();
		imageButtonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		// Erstelle "Durchsuchen"-Button
		JButton searchButton = new JButton("Durchsuchen");
		searchButton.setActionCommand("searchForImage");
		searchButton.addActionListener(this);

		// Erstelle "Bild löschen"-Button
		this.deleteImageButton = new JButton("Bild löschen");
		// Deaktiviere Button (wird erst aktiviert, wenn ein Bild ausgewählt wurde)
		deleteImageButton.setEnabled(false);
		this.deleteImageButton.setActionCommand("deleteImage");
		this.deleteImageButton.addActionListener(this);

		// Füge Buttons dem Panel hinzu
		imageButtonsPanel.add(searchButton);
		imageButtonsPanel.add(this.deleteImageButton);
		// Füge Panel dem Dialog hinzu
		trainDialog.add(imageButtonsPanel, c);

		// Setze Layout Einstellungen für Zugbild
		c = new GridBagConstraints();
		c.insets = set;
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.weightx = 1.0;
		c.weighty = 1.0;

		trainImageLabel = new JLabel("Kein Bild gesetzt");
		trainDialog.add(trainImageLabel, c);

		// Setze Layout Einstellungen Action-Buttons
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 5;
		c.anchor = GridBagConstraints.EAST;
		// Benutze Hilfspanel für rechtsbündige Ausrichung
		JPanel panelAction = new JPanel();
		// Setze Layout in Hilfspanel zu "FlowLayout.RIGHT"
		panelAction.setLayout(new FlowLayout(FlowLayout.RIGHT));
		// Erstelle Buttons zum Abbrechen oder Erstellen
		JButton cancelDialogButton = new JButton("Abbrechen");
		cancelDialogButton.setActionCommand("cancelDialog");
		cancelDialogButton.addActionListener(this);
		JButton createButton = new JButton("Erstellen");
		createButton.setActionCommand("createTrain");
		createButton.addActionListener(this);
		// Setze "Erstelle"-Button als Default Button, damit er durch
		// Enter-Drücken ausgelöst wird und blau umrandet ist
		trainDialog.getRootPane().setDefaultButton(createButton);

		// Füge Buttons dem Hilfspanel hinzu
		panelAction.add(cancelDialogButton);
		panelAction.add(createButton);
		// Füge Hilfspanel zum Dialog dazu
		trainDialog.add(panelAction, c);

		trainDialog.setSize(400, 250);
		// Platziere Dialog in Mitte des Bildschirmes
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		trainDialog.setLocation(dim.width / 2 - trainDialog.getSize().width / 2,
				dim.height / 2 - trainDialog.getSize().height / 2);
		trainDialog.setVisible(true);
	}

	/**
	 * Mit dieser Methode werden neue Zuglisteneinträge mit neu erstellten Zügen
	 * erzeugt
	 * 
	 * @param train
	 *            Neu erstellter Zug
	 */
	public void drawTrainPanel(Train train) {
		// Lösche das Platzhalter-Label aus dem Panel, wenn es keinen Zug gibt
		if (this.trainCollection.getTrains().isEmpty()) {
			this.trainsPanel.removeAll();
			this.trainsPanel.repaint();

			// Aktiviere stopAllButton sobald ein Zugeintrag vorliegt
			stopAllButton.setEnabled(true);
		}

		// Erstelle neues Panel für den Listeneintrag
		JPanel newTrainPanel = new JPanel();
		// Gebe panel einen eindeutigen Bezeichner
		newTrainPanel.setName(train.getName());
		newTrainPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		newTrainPanel.setPreferredSize(new Dimension(315, 100));
		newTrainPanel.setMaximumSize(new Dimension(315, 100));
		newTrainPanel.setMinimumSize(new Dimension(315, 100));
		newTrainPanel.setLayout(new GridBagLayout());

		// Erstelle Regeln für GridBagLayout.
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;

		// Füge Zugname hinzu
		JLabel trainName = new JLabel(train.getName());
		// Modifiziere Schriftart
		Font boldFont = new Font(trainName.getFont().getFontName(), Font.BOLD, trainName.getFont().getSize());
		trainName.setFont(boldFont);
		c.gridx = 1;
		c.gridy = 0;
		newTrainPanel.add(trainName, c);

		// Füge Modelbeschreibung hinzu
		JLabel trainModelDesc = new JLabel(train.getModelDesc());
		c.gridx = 1;
		c.gridy = 1;
		newTrainPanel.add(trainModelDesc, c);

		// Füge Geschwindigkeitslabel hinzu
		JLabel speedLabel = new JLabel("Geschwindigkeit: " + train.getSpeed() + "%");
		speedLabel.setName("speedLabel");
		c.gridx = 2;
		c.gridy = 0;
		newTrainPanel.add(speedLabel, c);

		// Füge Richtunglabel hinzu
		JLabel directionLabel = new JLabel();
		directionLabel.setName("directionLabel");
		if (train.isDirectionRight()) {
			directionLabel.setText("Fahrtrichtung: Rechts");
		} else {
			directionLabel.setText("Fahrtrichtung: Links");
		}
		c.gridx = 2;
		c.gridy = 1;
		newTrainPanel.add(directionLabel, c);

		// Füge Zugbild hinzu
		JLabel trainListImageLabel = new JLabel();
		ImageIcon icon = new ImageIcon();
		Image img;
		// Wurde kein Bild für den Zug gesetzt, so benutze das default Zugbild
		if (train.getImagePath() != null) {
			icon = new ImageIcon(train.getImagePath());
		} else {
			icon = new ImageIcon("images/default_train.png");
		}
		img = icon.getImage();
		img = img.getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(img);
		trainListImageLabel.setIcon(icon);
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.VERTICAL;
		newTrainPanel.add(trainListImageLabel, c);

		// Erstellen des "Edit"-Buttons
		JButton editTrainButton = new JButton();
		ImageIcon iconEdit = new ImageIcon();
		Image imgEdit;
		iconEdit = new ImageIcon("images/edit.png");
		imgEdit = iconEdit.getImage();
		imgEdit = imgEdit.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
		iconEdit = new ImageIcon(imgEdit);
		editTrainButton = new JButton(iconEdit);
		editTrainButton.setActionCommand("editTrain");
		editTrainButton.addActionListener(this);
		editTrainButton.addMouseListener(this);
		editTrainButton.setBorder(BorderFactory.createEmptyBorder());

		// Erstellen des "Delete"-Buttons
		JButton deleteTrainButton = new JButton();
		ImageIcon iconDelete = new ImageIcon();
		Image imgDelete;
		iconDelete = new ImageIcon("images/trash.png");
		imgDelete = iconDelete.getImage();
		imgDelete = imgDelete.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
		iconDelete = new ImageIcon(imgDelete);
		deleteTrainButton = new JButton(iconDelete);
		deleteTrainButton.setActionCommand("deleteTrain");
		deleteTrainButton.addActionListener(this);
		deleteTrainButton.addMouseListener(this);
		deleteTrainButton.setBorder(BorderFactory.createEmptyBorder());

		// Füge "Edit"- und "Delete"-Button  einem Hilfpanel hinzu
		JPanel trainActionPanel = new JPanel();
		// Mache dieses Panel durchsichtig, für den hover-over Effekt der Elternpanels
		trainActionPanel.setOpaque(false);
		trainActionPanel.add(editTrainButton);
		trainActionPanel.add(deleteTrainButton);
		// Füge Hilfspanel dem Listeneintragpanel hinzu
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		newTrainPanel.add(trainActionPanel, c);

		// Füge Label für den Lichtstatus des Zuges hinzu
		JLabel lightLabel = new JLabel();
		lightLabel.setName("lightLabel");
		if (train.isLightActive()) {
			lightLabel.setText("Licht: An");
		} else {
			lightLabel.setText("Licht: Aus");
		}
		c.gridx = 2;
		c.gridy = 3;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.BOTH;
		newTrainPanel.add(lightLabel, c);

		// Melde MouseListener an, um Hover-Over Effekt über das Panel zu realisieren und um abzufangen, wenn das Panel angeklickt wurde
		newTrainPanel.addMouseListener(this);

		// Füge den Listeneintrag dem Listen-Panel hinzu
		this.trainsPanel.add(newTrainPanel);
		this.trainsPanel.revalidate();
	}

	/**
	 * Öffnet einen Dialog zum Bearbeiten eines bereits bestehenden Zuges.
	 * Dieser erlaubt es den Namen, die Beschreibung und das Bild des
	 * Zugobjektes zu ändern.
	 * 
	 * @param train
	 *            Zu bearbeitender Zug
	 */
	public void openEditDialog(Train train) {
		// Erstelle neuen Dialog
		this.trainDialog = new JDialog(this.mainFrame, train.getName() + " bearbeiten", true);
		trainDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// Setze Layout für Dialog
		trainDialog.setLayout(new GridBagLayout());
		GridBagConstraints c;
		// Setze Ränder zwischen einzelnen Komponenten
		Insets set = new Insets(10, 10, 10, 10);

		// Setze Layout Einstellungen für 1.Label
		c = new GridBagConstraints();
		c.insets = set;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		trainDialog.add(new JLabel("Zugname: "), c);
		// Setze Layout Einstellungen für 2.Label
		c.gridy = 1;
		trainDialog.add(new JLabel("Modell (optional): "), c);
		// Setze Layout Einstellungen für 3.Label
		c.gridy = 3;
		trainDialog.add(new JLabel("Bild (optional): "), c);

		// Setze Layout Einstellungen für 1. Textfeld
		c = new GridBagConstraints();
		c.insets = set;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		trainNameTextField = new JTextField();
		// Hole Zugname von Zugobjekt
		trainNameTextField.setText(train.getName());
		trainDialog.add(trainNameTextField, c);

		// Setze Layout Einstellungen für 2. Textfeld
		c.gridy = 1;
		trainModelDescTextField = new JTextField();
		// Hole Zugbeschreibung von Zugobjekt
		trainModelDescTextField.setText(train.getModelDesc());
		trainDialog.add(trainModelDescTextField, c);

		// Setze Layout Einstellungen für Panel, welches den "Durchsuchen" und
		// "Bild löschen"-Button enthält
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 2;
		// Erstelle neues Panel mit FlowLayout
		JPanel imageButtonsPanel = new JPanel();
		imageButtonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		// Erstelle "Durchsuchen"-Button
		JButton searchButton = new JButton("Durchsuchen");
		searchButton.setActionCommand("searchForImage");
		searchButton.addActionListener(this);
		// Erstelle "Bild löschen"-Button
		this.deleteImageButton = new JButton("Bild löschen");

		//Aktiviere Button nur, wenn Zug ein Bild besitzt
		if (train.getImagePath() == null) {
			deleteImageButton.setEnabled(false);
		} else {
			deleteImageButton.setEnabled(true);
		}
		this.deleteImageButton.setActionCommand("deleteImage");
		this.deleteImageButton.addActionListener(this);
		// Füge Buttons dem Panel hinzu
		imageButtonsPanel.add(searchButton);
		imageButtonsPanel.add(this.deleteImageButton);
		// Füge Panel dem Dialog hinzu
		trainDialog.add(imageButtonsPanel, c);

		// Setze Layout Einstellungen für Zugbild
		c = new GridBagConstraints();
		c.insets = set;
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.weightx = 1.0;
		c.weighty = 1.0;

		// Besitzt der zu editierende Zug ein Bild, so zeige dieses an
		if (train.getImagePath() == null) {
			trainImageLabel = new JLabel("Kein Bild gesetzt");
		} else {
			imagePath = train.getImagePath();

			ImageIcon icon = new ImageIcon();
			// Erstelle Image zum skalieren des Bildes
			Image img;
			icon = new ImageIcon(imagePath);
			img = icon.getImage();
			// Skaliere Bild
			img = img.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH);
			icon = new ImageIcon(img);
			trainImageLabel.setText("");
			trainImageLabel.setIcon(icon);
			// Passe Dialoggröße an
			trainDialog.pack();
		}
		trainDialog.add(trainImageLabel, c);

		// Setze Layout Einstellungen Action-Buttons
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 5;
		c.anchor = GridBagConstraints.EAST;
		// Benutze Hilfspanel für rechtsbündige Ausrichung
		JPanel panelAction = new JPanel();
		// Setze Layout in Hilfspanel zu "FlowLayout.RIGHT"
		panelAction.setLayout(new FlowLayout(FlowLayout.RIGHT));
		// Erstelle Buttons zum Abbrechen oder Erstellen
		JButton cancelDialogButton = new JButton("Abbrechen");
		cancelDialogButton.setActionCommand("cancelDialog");
		cancelDialogButton.addActionListener(this);
		JButton saveButton = new JButton("Speichern");
		saveButton.setActionCommand("saveTrain");
		saveButton.addActionListener(this);
		// Setze "Erstelle"-Button als Default Button, damit er durch
		// Enter-Drücken ausgelöst wird und blaum umrandet ist
		trainDialog.getRootPane().setDefaultButton(saveButton);

		// Füge Buttons dem Hilfspanel hinzu
		panelAction.add(cancelDialogButton);
		panelAction.add(saveButton);
		// Füge Hilfspanel zum Dialog dazu
		trainDialog.add(panelAction, c);

		// Vergrößere Dialog, wenn er das Zugbild anzeigt
		if (train.getImagePath() == null) {
			trainDialog.setSize(400, 250);
		} else {
			trainDialog.setSize(400, 380);
		}

		// Platziere Dialog in Mitte des Bildschirmes
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		trainDialog.setLocation(dim.width / 2 - trainDialog.getSize().width / 2,
				dim.height / 2 - trainDialog.getSize().height / 2);
		trainDialog.setVisible(true);
	}

	/**
	 * Mit dieser Methode werden bestehende Zuglisteneinträge nach Bearbeitung
	 * eines Zuges aktualisierts
	 * 
	 * @param train
	 *            Bearbeiteter Zug
	 * @param panel
	 *            Zu aktualisierendes Panel
	 */
	public void redrawTrainPanel(Train train, JPanel panel) {
		// Setze Hintergrund des Zugpanels zurück
		panel.setBackground(trainsPanel.getBackground());
		// Entferne alle bisherigen Komponenten
		panel.removeAll();
		// Gebe panel einen eindeutigen Bezeichner
		panel.setName(train.getName());
		panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		panel.setPreferredSize(new Dimension(315, 100));
		panel.setMaximumSize(new Dimension(315, 100));
		panel.setMinimumSize(new Dimension(315, 100));
		panel.setLayout(new GridBagLayout());

		// Erstelle Regeln für GridBagLayout.
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;

		// Füge Zugname hinzu
		JLabel trainName = new JLabel(train.getName());
		Font boldFont = new Font(trainName.getFont().getFontName(), Font.BOLD, trainName.getFont().getSize());
		trainName.setFont(boldFont);
		c.gridx = 1;
		c.gridy = 0;
		panel.add(trainName, c);

		// Füge Zugbeschreibung hinzu
		JLabel trainModelDesc = new JLabel(train.getModelDesc());
		c.gridx = 1;
		c.gridy = 1;
		panel.add(trainModelDesc, c);

		// Füge Label für Zuggeschwindigkeit hinzu
		JLabel speedLabel = new JLabel("Geschwindigkeit: " + train.getSpeed() + "%");
		speedLabel.setName("speedLabel");
		c.gridx = 2;
		c.gridy = 0;
		panel.add(speedLabel, c);

		// Füge Label für Fahrtrichtung hinzu
		JLabel directionLabel = new JLabel();
		directionLabel.setName("directionLabel");
		if (train.isDirectionRight()) {
			directionLabel.setText("Fahrtrichtung: Rechts");
		} else {
			directionLabel.setText("Fahrtrichtung: Links");
		}
		c.gridx = 2;
		c.gridy = 1;
		panel.add(directionLabel, c);

		// Füge Zugbild hinzu
		JLabel trainListImageLabel = new JLabel();
		ImageIcon icon = new ImageIcon();
		Image img;
		// Wurde kein Bild für den Zug gesetzt, so benutze das default Zugbild
		if (train.getImagePath() != null) {
			icon = new ImageIcon(train.getImagePath());
		} else {
			icon = new ImageIcon("images/default_train.png");
		}
		img = icon.getImage();
		img = img.getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(img);
		trainListImageLabel.setIcon(icon);
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.VERTICAL;
		panel.add(trainListImageLabel, c);

		// Erstellen des "Edit"-Buttons
		JButton editTrainButton = new JButton();
		ImageIcon iconEdit = new ImageIcon();
		Image imgEdit;
		iconEdit = new ImageIcon("images/edit.png");
		imgEdit = iconEdit.getImage();
		imgEdit = imgEdit.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
		iconEdit = new ImageIcon(imgEdit);
		editTrainButton = new JButton(iconEdit);
		editTrainButton.setActionCommand("editTrain");
		editTrainButton.addActionListener(this);
		editTrainButton.addMouseListener(this);
		editTrainButton.setBorder(BorderFactory.createEmptyBorder());

		// Erstellen des "Delete"-Buttons
		JButton deleteTrainButton = new JButton();
		ImageIcon iconDelete = new ImageIcon();
		Image imgDelete;
		iconDelete = new ImageIcon("images/trash.png");
		imgDelete = iconDelete.getImage();
		imgDelete = imgDelete.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
		iconDelete = new ImageIcon(imgDelete);
		deleteTrainButton = new JButton(iconDelete);
		deleteTrainButton.setActionCommand("deleteTrain");
		deleteTrainButton.addActionListener(this);
		deleteTrainButton.addMouseListener(this);
		deleteTrainButton.setBorder(BorderFactory.createEmptyBorder());

		// Füge "Edit"- und "Delete"-Button einem Hilfpanel hinzu
		JPanel trainActionPanel = new JPanel();
		// Mache dieses Panel durchsichtig, für den hover-over Effekt der Elternpanels
		trainActionPanel.setOpaque(false);
		trainActionPanel.add(editTrainButton);
		trainActionPanel.add(deleteTrainButton);
		// Füge Hilfspanel dem Listeneintragpanel hinzu
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		panel.add(trainActionPanel, c);

		// Füge Label für den Lichtstatus des Zuges hinzu
		JLabel lightLabel = new JLabel();
		lightLabel.setName("lightLabel");
		if (train.isLightActive()) {
			lightLabel.setText("Licht: An");
		} else {
			lightLabel.setText("Licht: Aus");
		}
		c.gridx = 2;
		c.gridy = 3;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.BOTH;
		panel.add(lightLabel, c);

		// Revalidiere das Panel zum Aktualisieren der Anzeige
		this.trainsPanel.revalidate();
	}

	/**
	 * Methode zum Zeichnen des Controller-Bereiches, nachdem ein Zug ausgewählt
	 * wurde. Hier können Geschwindigkeit, Licht und Fahrtrichtung des Zuges
	 * verstellt werden.
	 */
	public void drawControllerArea() {
		// Entferne alle bisherigen Inhalte und damit auch das Platzhalterlabel
		this.controllerAreaPanel.removeAll();
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
		this.controllerImageLabel = new JLabel();
		ImageIcon icon = new ImageIcon();
		Image img;
		// Wurde kein Bild für den Zug gesetzt, so benutze das default Zugbild
		if (this.selectedTrain.getImagePath() != null) {
			icon = new ImageIcon(this.selectedTrain.getImagePath());
		} else {
			icon = new ImageIcon("images/default_train.png");
		}
		img = icon.getImage();
		img = img.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(img);
		controllerImageLabel.setIcon(icon);
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 2;
		c.anchor = GridBagConstraints.PAGE_START;
		c.fill = GridBagConstraints.VERTICAL;
		trainControlPanel.add(controllerImageLabel, c);

		// Füge Zugname hinzu
		this.controllerTrainName = new JLabel(this.selectedTrain.getName());
		Font myFont = new Font(this.controllerTrainName.getFont().getFontName(), Font.BOLD, 16);
		this.controllerTrainName.setFont(myFont);
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		trainControlPanel.add(this.controllerTrainName, c);

		// Füge Zugbeschreibung hinzu
		this.controllerTrainModelDesc = new JLabel(this.selectedTrain.getModelDesc());
		myFont = new Font(this.controllerTrainModelDesc.getFont().getFontName(), Font.PLAIN, 14);
		this.controllerTrainModelDesc.setFont(myFont);
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.anchor = GridBagConstraints.PAGE_START;
		trainControlPanel.add(this.controllerTrainModelDesc, c);

		// Füge Geschwindigkeitslabel hinzu
		JLabel speedLabel = new JLabel("Geschwindigkeit:");
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.CENTER;
		trainControlPanel.add(speedLabel, c);

		// Packe den Slider zur Geschwindigkeitanpassung und den Stop Knopf in ein Hilfspanel
		JPanel speedPanel = new JPanel();
		// Initialisiere den Slider und füge ihn dem Hilfspanel hinzu
		this.speedSlider = new JSlider();
		this.speedSlider.setName("speedSlider");
		this.speedSlider.addChangeListener(this);
		this.speedSlider.setMajorTickSpacing(100);
		this.speedSlider.setMinorTickSpacing(1);
		this.speedSlider.setPaintTicks(true);
		this.speedSlider.setPaintLabels(true);
		this.speedSlider.setValue(this.selectedTrain.getSpeed());
		speedPanel.add(this.speedSlider);
		// Füge den StopButton dem Hilfspanel hinzu
		JButton stopTrainButton = new JButton("Stop");
		stopTrainButton.addActionListener(this);
		stopTrainButton.setActionCommand("stopTrain");
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
		// Füge Hilfspanel dem Control-Panel hinzu
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		trainControlPanel.add(speedPanel, c);

		// Füge Label für die Fahrtrichtung hinzu
		JLabel directionLabel = new JLabel("Fahrtrichtung:");
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		trainControlPanel.add(directionLabel, c);

		// Erstelle Hilfspanel für die Verstellung der Fahrtrichtung
		JPanel directionPanel = new JPanel(new FlowLayout());
		// Erstelle ButtonGroup
		this.directionButtonGroup = new ButtonGroup();
		// Erstelle ToggleButtons für beide Fahrtrichtungen
		this.toggleLeft = new JToggleButton("Links");
		this.toggleLeft.addActionListener(this);
		this.toggleLeft.setActionCommand("toggleLeft");
		this.toggleLeft.setPreferredSize(new Dimension(85, 25));
		ImageIcon iconTurnLeft = new ImageIcon();
		Image imgTurnLeft;
		iconTurnLeft = new ImageIcon("images/turn_left.png");
		imgTurnLeft = iconTurnLeft.getImage();
		imgTurnLeft = imgTurnLeft.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
		iconTurnLeft = new ImageIcon(imgTurnLeft);
		this.toggleLeft.setIcon(iconTurnLeft);

		this.toggleRight = new JToggleButton("Rechts");
		this.toggleRight.addActionListener(this);
		this.toggleRight.setActionCommand("toggleRight");
		this.toggleRight.setPreferredSize(new Dimension(85, 25));
		ImageIcon iconTurnRight = new ImageIcon();
		Image imgTurnRight;
		iconTurnRight = new ImageIcon("images/turn_right.png");
		imgTurnRight = iconTurnRight.getImage();
		imgTurnRight = imgTurnRight.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
		iconTurnRight = new ImageIcon(imgTurnRight);
		this.toggleRight.setIcon(iconTurnRight);
		// Füge ToggleButtons der ButtonGroup hinzu
		this.directionButtonGroup.add(this.toggleLeft);
		this.directionButtonGroup.add(this.toggleRight);
		// Bestimme, welcher der beiden ToggleButtons selektiert ist
		if (this.selectedTrain.isDirectionRight()) {
			this.directionButtonGroup.setSelected(this.toggleRight.getModel(), true);
		} else {
			this.directionButtonGroup.setSelected(this.toggleLeft.getModel(), true);
		}
		// Füge die ToggleButtons dem Hilfspanel hinzu
		directionPanel.add(this.toggleLeft);
		directionPanel.add(this.toggleRight);
		// Füge das Hilfspanel dem Control-Panel hinzu
		c.gridx = 2;
		c.gridy = 3;
		c.gridwidth = 1;
		trainControlPanel.add(directionPanel, c);

		// Füge Lichtlabel hinzu
		JLabel lightLabel = new JLabel("Licht:");
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
		trainControlPanel.add(lightLabel, c);

		// Erstelle Hilfspanel für die Verstellung der Lichts
		JPanel lightPanel = new JPanel();
		// Initialisiere den switchLightButton
		this.switchLightButton = new JButton();
		this.switchLightButton.addActionListener(this);
		this.switchLightButton.setActionCommand("switchLight");
		this.switchLightButton.setBorder(BorderFactory.createEmptyBorder());
		this.switchLightButton.setContentAreaFilled(false);
		ImageIcon iconSwitch = new ImageIcon();
		// Erstelle Icon für die Glühbirne, damit es in der If-Abfrage darunter gefüllt werden kann
		ImageIcon iconLight = new ImageIcon();
		Image imgSwitch;

		// Überprüfe welches Icon der Switch und die Glühbirne zugewiesen bekommen muss
		if (this.selectedTrain.isLightActive()) {
			iconSwitch = new ImageIcon("images/switch_right.png");
			iconLight = new ImageIcon("images/lightbulb_on.png");
		} else {
			iconSwitch = new ImageIcon("images/switch_left.png");
			iconLight = new ImageIcon("images/lightbulb_off.png");
		}
		imgSwitch = iconSwitch.getImage();
		imgSwitch = imgSwitch.getScaledInstance(35, 35, java.awt.Image.SCALE_SMOOTH);
		iconSwitch = new ImageIcon(imgSwitch);
		this.switchLightButton.setIcon(iconSwitch);

		// Visualisiere den Lichtstatus durch ein Glühbirnen-Label
		this.lightBulb = new JLabel();
		Image imgLight;
		imgLight = iconLight.getImage();
		imgLight = imgLight.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
		iconLight = new ImageIcon(imgLight);
		this.lightBulb.setIcon(iconLight);

		// Füge Lichtschalter und Glühbirnen-Label dem Hilfspanel hinzu
		lightPanel.add(this.switchLightButton);
		lightPanel.add(this.lightBulb);

		// Benutze erneutes Hilfspanel mit FlowLayout um gewünschte Darstellung zur erzielen
		JPanel flowPanel = new JPanel(new FlowLayout());
		// Füge Hilfspanel 1 dem 2. Hilfspanel hinzu
		flowPanel.add(lightPanel);
		c.gridx = 2;
		c.gridy = 4;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.CENTER;
		// Füge 2.Hilfspanel dem Control-Panel hinzu
		trainControlPanel.add(lightPanel, c);

		// Füge Control Panel dem Control-Bereich hinzu und revalidiere diesen
		this.controllerAreaPanel.add(trainControlPanel);
		this.controllerAreaPanel.revalidate();
	}

	/**
	 * Erstellt Zugobjekte aus einem JSON File und erstellt für diese Zugobjekte
	 * Listeneinträge
	 */
	public void initializeFromJSON() {
		JSONParser parser = new JSONParser();
		String trainName;
		String trainModelDesc;
		String trainImgPath;
		Boolean trainDirectionRight;
		Boolean trainLightActive;

		try {
			// Hole JSON File
			Object obj = parser.parse(new FileReader("savedata/trains.json"));

			// Lese JSON Array aus der Datei und loope darüber
			JSONArray trainsArray = (JSONArray) obj;
			Iterator<JSONObject> iterator = trainsArray.iterator();
			while (iterator.hasNext()) {
				JSONObject trainJSON = (JSONObject) iterator.next();
				trainName = (String) trainJSON.get("name");
				trainModelDesc = (String) trainJSON.get("modelDesc");
				trainImgPath = (String) trainJSON.get("imgPath");
				trainDirectionRight = (Boolean) trainJSON.get("directionRight");
				trainLightActive = (Boolean) trainJSON.get("lightActive");

				// Erstelle Zugobjekt mit Werten aus dem JSON File
				Train newTrain = new Train(trainName, trainModelDesc, trainImgPath);
				newTrain.setDirectionRight(trainDirectionRight);
				newTrain.setLightActive(trainLightActive);

				// Zeichne Zuglisteneintrag im UI
				this.drawTrainPanel(newTrain);

				// Füge neuen Zug der Collection hinzu	
				this.trainCollection.addTrain(newTrain);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Speichert alle Züge inklusive Name, Beschreibung, Bild, Fahrtrichtung und
	 * Lichstatus in einem JSON File ab
	 */
	public void saveDataToJSON() {
		// Erstelle JSON Array
		JSONArray trainsArray = new JSONArray();
		// Erstelle JSON Object für einzelne Züge
		JSONObject trainObject;

		// Erstelle JSON Objekte für jeden Zug in der Collection und hänge sie an das JSON Array
		for (Train t : this.trainCollection.getTrains()) {
			trainObject = new JSONObject();
			trainObject.put("name", t.getName());
			trainObject.put("modelDesc", t.getModelDesc());
			trainObject.put("imgPath", t.getImagePath());
			trainObject.put("directionRight", t.isDirectionRight());
			trainObject.put("lightActive", t.isLightActive());
			trainsArray.add(trainObject);
		}

		try {
			// Erstelle FileWriter zum Schreiben von Dateien
			FileWriter file = new FileWriter("savedata/trains.json");
			// Übergebe JSON Array zum Schreiben
			file.write(trainsArray.toJSONString());
			file.flush();
			file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		// Blende Erfolgsdialog ein
		JOptionPane.showMessageDialog(trainDialog, "Deine Zugdaten wurden erfolgreich gespeichert",
				"Speichern erfolgreich", JOptionPane.INFORMATION_MESSAGE);

		// Merke, dass Daten gespeichert wurden
		this.isDirty = false;
	}

	/*
	 * Event-Listener-Methoden
	 */

	/**
	 * Horcht auf ActionEvents von angemeldeten Ereignisquellen. Diese Methode
	 * wird benutzt um diverse Ereignisse von Buttons abzufangen, und zu
	 * entscheiden, welche Logik anzuwenden ist.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("addTrain")) {
			// imagePath zurücksetzen
			this.imagePath = null;

			// Öffne Dialog für die Zugerstellung
			this.openNewDialog();

		} else if (e.getActionCommand().equals("searchForImage")) {
			// FileChooser aufrufen
			JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.home")));
			FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("Image files",
					ImageIO.getReaderFileSuffixes());
			fileChooser.setFileFilter(imageFilter);

			// Überprüfe, ob gültige Datei ausgewählt wurde
			if (fileChooser.showOpenDialog(trainDialog) == JFileChooser.APPROVE_OPTION) {
				// Merke imagePath
				imagePath = fileChooser.getSelectedFile().getPath();

				ImageIcon icon = new ImageIcon();
				// Erstelle Image zum skalieren des Bildes
				Image img;
				icon = new ImageIcon(imagePath);
				img = icon.getImage();
				// Skaliere Bild
				img = img.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH);
				icon = new ImageIcon(img);
				trainImageLabel.setText("");
				trainImageLabel.setIcon(icon);

				// Aktiviere deleteImageButton
				deleteImageButton.setEnabled(true);
				// Passe Dialoggröße an
				trainDialog.setSize(400, 380);
			}

		} else if (e.getActionCommand().equals("deleteImage")) {
			// Setze imagePath und Zugbild im Dialog zurück
			this.imagePath = null;
			trainImageLabel.setIcon(null);
			trainImageLabel.setText("Kein Bild gesetzt");

			//	Deaktiviere deleteImageButton
			deleteImageButton.setEnabled(false);
			// Passe Dialoggröße an
			trainDialog.setSize(400, 250);

		} else if (e.getActionCommand().equals("cancelDialog")) {
			trainDialog.dispose();

		} else if (e.getActionCommand().equals("createTrain")) {

			String trainName = trainNameTextField.getText();
			String trainModelDesc = trainModelDescTextField.getText();

			// Überprüfen, ob Zugname eingegeben wurde
			if (trainNameTextField.getText().equals("")) {
				// Zeige Warnung 
				JOptionPane.showMessageDialog(trainDialog, "Bitte gebe dem Zug einen Namen", "Zugname leer",
						JOptionPane.WARNING_MESSAGE);
				// Kehre zum Dialog zurück
				return;
			}
			// Überprüfen, ob Zug bereits existiert
			if (trainCollection.trainIsAlreadyExisting(trainName)) {
				// Zeige Warnung
				JOptionPane.showMessageDialog(trainDialog, "Zug existiert bereits, bitte gebe einen anderen Namen ein",
						"Zug existiert bereits", JOptionPane.WARNING_MESSAGE);
				// Kehre zum Dialog zurück
				return;
			}

			trainDialog.dispose();
			// Erstelle neues Zugobjekt
			Train newTrain = new Train(trainName, trainModelDesc, imagePath);

			// Nach schließen des Dialoges ein neuer Listeneintrag für den Zug erstellt werden
			this.drawTrainPanel(newTrain);

			// Füge Zug der trainCollection hinzu		
			this.trainCollection.addTrain(newTrain);

			// Merke, dass trainCollection verändert wurde
			this.isDirty = true;

		} else if (e.getActionCommand().equals("editTrain")) {
			// imagePath zurücksetzen
			this.imagePath = null;

			// Hole Button des getriggerten Events
			JButton actionButton = (JButton) e.getSource();

			// Speichere affectedPanel (Listeneintragpanel des ausgewählten Zuges) für später
			this.affectedPanel = (JPanel) actionButton.getParent().getParent();

			// Hole Zugname über Parent des Buttons
			String trainName = affectedPanel.getName();
			// Hole Zugobjekt
			this.affectedTrain = this.trainCollection.getTrainByName(trainName);

			// Öffne EditDialog
			this.openEditDialog(this.affectedTrain);

		} else if (e.getActionCommand().equals("deleteTrain")) {
			// Hole Button des getriggerten Events
			JButton actionButton = (JButton) e.getSource();

			// Speichere affectedPanel (Listeneintragpanel des ausgewählten Zuges) für später
			this.affectedPanel = (JPanel) actionButton.getParent().getParent();

			// Hole Zugname über Parent des Buttons
			String trainName = affectedPanel.getName();
			// Hole Zugobjekt
			this.affectedTrain = this.trainCollection.getTrainByName(trainName);

			// Frage nach, onb Zug wirklich gelöscht werden soll
			int confirmResult = JOptionPane.showConfirmDialog(this.mainFrame,
					"Soll Zug " + trainName + " wirklich gelöscht werden?", "Zug " + trainName + " löschen?",
					JOptionPane.YES_NO_OPTION);

			if (confirmResult == JOptionPane.YES_OPTION) {
				// Setze Controller Bereich zurück und zeige wieder das Platzhalter-Label an, falls der gelöschte Zug der zuletzt ausgewählt war
				if (this.affectedTrain == this.selectedTrain) {
					controllerAreaPanel.removeAll();
					controllerAreaPanel.repaint();
					controllerAreaPanel.setLayout(new GridBagLayout());
					controllerAreaPanel.add(new JLabel("Kein Zug ausgewählt"));
					controllerAreaPanel.revalidate();
				}

				// Entferne Zugobjekt von trainCollection
				this.trainCollection.removeTrainFromCollection(this.affectedTrain);
				// Entferne Panel und refreshe trainsPanel
				this.trainsPanel.remove(this.affectedPanel);
				this.trainsPanel.revalidate();
				this.trainsPanel.repaint();

				// Merke, dass trainCollection verändert wurde
				this.isDirty = true;

			} else {
				return;
			}

			// Wenn alle Züge gelöscht wurden, blende Platzhalter-Label in der Liste wieder ein
			if (this.trainCollection.getTrains().isEmpty()) {
				JPanel noTrainPanel = new JPanel(new GridBagLayout());
				JLabel noTrainLabel = new JLabel("Noch kein Zug erstellt");
				noTrainPanel.add(noTrainLabel);
				this.trainsPanel.add(noTrainPanel);

				// Deaktiviere stopAllButton
				stopAllButton.setEnabled(false);
			}

		} else if (e.getActionCommand().equals("saveTrain")) {
			// Hole Zugeigenschaften
			String trainName = trainNameTextField.getText();
			String trainModelDesc = trainModelDescTextField.getText();

			// Überprüfen, ob Zugname eingegeben wurde
			if (trainNameTextField.getText().equals("")) {
				JOptionPane.showMessageDialog(trainDialog, "Bitte gebe dem Zug einen Namen", "Zugname leer",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			// Zeige Warnung, wenn Zugname bereits von einem anderen Zug belegt ist
			if (trainCollection.trainIsAlreadyExisting(trainName)
					&& !(this.affectedTrain.getName().equals(trainName))) {
				JOptionPane.showMessageDialog(trainDialog, "Zug existiert bereits, bitte gebe einen anderen Namen ein",
						"Zug existiert bereits", JOptionPane.WARNING_MESSAGE);
				return;
			}
			// Wenn beide Fälle nicht eintreten, update den Zug
			this.affectedTrain.setName(trainName);
			this.affectedTrain.setModelDesc(trainModelDesc);
			this.affectedTrain.setImagePath(imagePath);

			// Falls der editierte Zug gleichzeitig auch der zuletzt ausgewählte Zug ist, aktualisiere auch den Controller-Bereich
			if (this.affectedTrain == this.selectedTrain) {
				// Aktualisiere Bild
				ImageIcon icon = new ImageIcon();
				Image img;
				if (this.affectedTrain.getImagePath() != null) {
					icon = new ImageIcon(this.affectedTrain.getImagePath());
				} else {
					icon = new ImageIcon("images/default_train.png");
				}
				img = icon.getImage();
				img = img.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH);
				icon = new ImageIcon(img);
				controllerImageLabel.setIcon(icon);

				//Aktualisieren Namen
				this.controllerTrainName.setText(this.affectedTrain.getName());
				//Aktualisieren Beschreibung
				this.controllerTrainModelDesc.setText(this.affectedTrain.getModelDesc());
			}

			trainDialog.dispose();

			// Nach schließen des Dialoges muss der neue Zug auch in der Liste aktualisiert werden
			this.redrawTrainPanel(this.affectedTrain, affectedPanel);

			// Merke, dass trainCollection verändert wurde
			this.isDirty = true;

		} else if (e.getActionCommand().equals("stopTrain")) {
			if (this.selectedTrain.getSpeed() != 0) {
				// Setze Geschwindigkeit des Zuges auf 0
				this.selectedTrain.setSpeed(0);
				// Aktualisiere Label des dazugehörigen Listeneintrag
				this.selectedSpeedLabel.setText("Geschwindigkeit: " + this.selectedTrain.getSpeed() + "%");
				// Aktualisiere speedSlider im Control-Bereich
				this.speedSlider.setValue(0);
			}

		} else if (e.getActionCommand().equals("toggleLeft")) {
			if (this.selectedTrain.isDirectionRight()) {
				this.selectedTrain.setDirectionRight(false);
				this.selectedDirectionsLabel.setText("Fahrtrichtung: Links");
			}

		} else if (e.getActionCommand().equals("toggleRight")) {
			if (!this.selectedTrain.isDirectionRight()) {
				this.selectedTrain.setDirectionRight(true);
				this.selectedDirectionsLabel.setText("Fahrtrichtung: Rechts");
			}

		} else if (e.getActionCommand().equals("switchLight")) {
			// Kehre Lichtstatus um
			this.selectedTrain.setLightActive(!this.selectedTrain.isLightActive());

			ImageIcon iconSwitch = new ImageIcon();
			ImageIcon iconLight = new ImageIcon();
			Image imgSwitch;

			// Überprüfe, welche Bilder dem Lichtschalter und der Glühbirne zugewiesen werden muss
			if (this.selectedTrain.isLightActive()) {
				iconSwitch = new ImageIcon("images/switch_right.png");
				iconLight = new ImageIcon("images/lightbulb_on.png");
				// Aktualisiere Lichtlabel im Listeneintrag
				this.selectedLightLabel.setText("Licht: An");
			} else {
				iconSwitch = new ImageIcon("images/switch_left.png");
				iconLight = new ImageIcon("images/lightbulb_off.png");
				// Aktualisiere Lichtlabel im Listeneintrag
				this.selectedLightLabel.setText("Licht: Aus");
			}

			imgSwitch = iconSwitch.getImage();
			imgSwitch = imgSwitch.getScaledInstance(35, 35, java.awt.Image.SCALE_SMOOTH);
			iconSwitch = new ImageIcon(imgSwitch);
			// Setze das neue Bild
			this.switchLightButton.setIcon(iconSwitch);

			Image imgLight;
			imgLight = iconLight.getImage();
			imgLight = imgLight.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
			iconLight = new ImageIcon(imgLight);
			// Setze das neue Bild
			this.lightBulb.setIcon(iconLight);

		} else if (e.getActionCommand().equals("stopAllTrains")) {
			// Setze die Geschwindigkeit aller Züge auf 0
			this.trainCollection.stopAllTrains();
			if (this.selectedTrainPanel != null) {
				// Ist ein Zug gerade im Control-Bereich vertreten, so aktualisiere auch den Wert dieses sliders
				this.speedSlider.setValue(this.selectedTrain.getSpeed());
			}

			// Gehe über jedes Zugpanel in der Zugliste
			JPanel trainEntry;
			JLabel speedLabel;
			for (Component c1 : this.trainsPanel.getComponents()) {
				// Überprüfe sicherheitshalber, ob Komponente ein Panel ist
				if (c1 instanceof JPanel) {
					trainEntry = (JPanel) c1;
					// Gehe über jede Komponente innerhalb eines Listeneintrages um das Geschwindigkeitslabel aufzuspüren
					for (Component c2 : trainEntry.getComponents()) {
						// Ist das aktuelle Objekt das Geschwindigkeitslabel, so verändere es
						if (("speedLabel").equals(c2.getName())) {
							speedLabel = (JLabel) c2;
							speedLabel.setText("Geschwindigkeit: 0%");
						}
					}
				}
			}
		} else if (e.getActionCommand().equals("saveData")) {
			this.saveDataToJSON();
		} else if (e.getActionCommand().equals("close")) {
			// Schließe Frame
			this.mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	/**
	 * Diese Methode horcht, ob die Maus den Bereich einer angemeldeten
	 * Ereignisquelle betritt. Dies wird in Kombination mit der Methode
	 * {@link #mouseExited(MouseEvent)} benutzt, um einen hover-over Effekt zu
	 * erzeugen, indem der Hintegrund eines auslösenden Listeneintrag-Panels
	 * eingefärbt wird.
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		// Überprüfe, ob Auslöser ein Panel ist
		if (e.getSource() instanceof JPanel) {
			// Hole auslösendes Panel
			JPanel activePanel = (JPanel) (e.getSource());
			// Setze Hintergrund nur, wenn das Panel nicht gerade angeklickt wurde
			if (!(activePanel == selectedTrainPanel)) {
				activePanel.setBackground(new Color(230, 230, 230));
			}
			// Überprüfe, ob Auslöser ein Button ist
		} else if (e.getSource() instanceof JButton) {
			// Hole auslösender Button
			JButton activeButton = (JButton) (e.getSource());
			// Hole dazugehöriges Eltern-Panel
			JPanel activePanel = (JPanel) activeButton.getParent().getParent();
			// Setze Hintergrund nur, wenn das Panel nicht gerade angeklickt wurde
			if (!(activePanel == selectedTrainPanel)) {
				activePanel.setBackground(new Color(230, 230, 230));
			}
		}
	}

	/**
	 * Diese Methode horcht, ob die Maus den Bereich einer angemeldeten
	 * Ereignisquelle verlässt. Dies wird in Kombination mit der Methode
	 * {@link #mouseEntered(MouseEvent)} benutzt, um einen hover-over Effekt zu
	 * erzeugen, indem der Hintegrund eines auslösenden Listeneintrag-Panels
	 * eingefärbt wird.
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		// Überprüfe, ob Event von einem Panel ausgelöst wurde
		if (e.getSource() instanceof JPanel) {
			JPanel activePanel = (JPanel) (e.getSource());
			// Setze Hintergrund nur zurück, wenn das Panel nicht gerade angeklickt wurde
			if (!(activePanel == selectedTrainPanel)) {
				activePanel.setBackground(trainsPanel.getBackground());
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	/**
	 * Diese Methode horcht, ob die gedrückte Maustaste in dem Bereich einer
	 * angemeldeten Ereignisquelle losgelassen wird. Dadurch wird ein
	 * Listeneintrag-Panel ausgewählt. D.h. er wird dunkel eingefärbt und das
	 * Zugobjekt wird in den Control-Bereich geladen, wodurch der Zug sich
	 * steuern lässt.
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		// Überprüfe ob Auslöser ein Panel ist
		if (e.getSource() instanceof JPanel) {
			JPanel activePanel = (JPanel) (e.getSource());
			// Überprüfe, ob zum ersten Mal ein Panel ausgewählt wurde
			if (this.selectedTrainPanel == null) {
				// Belege das selectedTrainPanel mit dem angeklickten Panel
				this.selectedTrainPanel = activePanel;
				// Verdunkle den Hintergrund des angeklickten Panels
				activePanel.setBackground(new Color(210, 210, 210));

				// Hole den ausgewählten Zug über den Panel-Namen
				this.selectedTrain = this.trainCollection.getTrainByName(activePanel.getName());

				// Hole sich alle relevanten Labels aus dem ausgewählten Panel, um
				// ihre Werte später manipulieren zu können
				for (Component component : activePanel.getComponents()) {
					if (("speedLabel").equals(component.getName())) {
						this.selectedSpeedLabel = (JLabel) component;
					}
					if (("directionLabel").equals(component.getName())) {
						this.selectedDirectionsLabel = (JLabel) component;
					}
					if (("lightLabel").equals(component.getName())) {
						this.selectedLightLabel = (JLabel) component;
					}
				}

				// Zeichne Controler Area
				this.drawControllerArea();

				// Überprüfe, ob angeklicktes Panel davor nicht schon ausgewählt bzw. angeklickt wurde
			} else if (this.selectedTrainPanel != activePanel) {
				// Setze den Hintegrund des davor ausgewählten Panels zurück
				this.selectedTrainPanel.setBackground(trainsPanel.getBackground());
				// Belege das selectedTrainPanel mit dem angeklickten Panel
				this.selectedTrainPanel = activePanel;
				// Verdunkle den Hintergrund des angeklickten Panels
				activePanel.setBackground(new Color(210, 210, 210));

				// Hole den ausgewählten Zug über den Panel Namen
				this.selectedTrain = this.trainCollection.getTrainByName(activePanel.getName());

				// Hole sich alle relevanten Labels aus dem ausgewählten Panel, um
				// ihre Werte später manipulieren zu können
				for (Component component : activePanel.getComponents()) {
					if (("speedLabel").equals(component.getName())) {
						this.selectedSpeedLabel = (JLabel) component;
					}
					if (("directionLabel").equals(component.getName())) {
						this.selectedDirectionsLabel = (JLabel) component;
					}
					if (("lightLabel").equals(component.getName())) {
						this.selectedLightLabel = (JLabel) component;
					}
				}
				// Zeichne Controler Area
				this.drawControllerArea();
			}
		}
	}

	/**
	 * Horcht auf Änderungen von angemeldeten Ereignisquellen. Dies wird
	 * benutzt, um die Geschwindigkeit eines Zuges über einen Slider zu
	 * verändern.
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider activeSlider = (JSlider) e.getSource();

		// Überprüfe, welcher Slider das Ereignis ausgelöst hat
		if (("speedSlider".equals(activeSlider.getName()))) {
			int speed = activeSlider.getValue();
			// Aktualisiere Geschwindigkeit des Zugobjektes und Geschwindigkeitslabel in der Zugliste
			this.selectedTrain.setSpeed(speed);
			this.selectedSpeedLabel.setText("Geschwindigkeit: " + this.selectedTrain.getSpeed() + "%");
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	/**
	 * Horcht auf das Schließen einer angemeldeten Ereignisquelle. Damit wird
	 * beim Schließen des Frames überprüft, ob noch ungespeicherte Änderungen
	 * vorliegen. Ist dem so, so wird gefragt, ob diese gespeichert werden
	 * sollen.
	 */
	@Override
	public void windowClosing(WindowEvent arg0) {
		// Prüfe, ob noch ungespeicherte Änderungen vorhanden sind
		if (isDirty) {
			// Frage, ob Änderungen gespeichert werden sollen
			int confirmResult = JOptionPane.showConfirmDialog(this.mainFrame,
					"Ungespeicherte Zugänderungen vor dem Schließen speichern?", "Speichern vor dem Schließen?",
					JOptionPane.YES_NO_OPTION);

			// Wenn ja, speichere Daten
			if (confirmResult == JOptionPane.YES_OPTION) {
				this.saveDataToJSON();
			}
		}
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}
}