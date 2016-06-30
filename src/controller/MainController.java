package controller;

import java.awt.event.ActionEvent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import prod.Train;
import prod.TrainCollection;

public class MainController implements ActionListener, MouseListener, ChangeListener {

	private JFrame mainFrame;
	private JPanel trainsPanel;
	private JPanel controllerAreaPanel;
	private TrainCollection trainCollection;
	private JDialog trainDialog;
	private JLabel trainImageLabel;
	private JTextField trainNameTextField;
	private JTextField trainModelDescTextField;
	private String imagePath;
	private JPanel affectedPanel;
	private Train affectedTrain;
	private JPanel selectedTrainPanel;
	private Train selectedTrain;
	private JLabel selectedSpeedLabel;
	private JLabel selectedDirectionsLabel;
	private JLabel selectedLightLabel;
	private ButtonGroup directionButtonGroup;
	private JToggleButton toggleLeft;
	private JToggleButton toggleRight;
	private JButton switchLightButton;
	private JLabel lightBulb;
	private JSlider speedSlider;
	private JLabel controllerImageLabel;
	private JLabel controllerTrainName;
	private JLabel controllerTrainModelDesc;
	private JButton deleteImageButton;

	public MainController(JFrame mainFrame, JPanel trainsPanel, JPanel controllerAreaPanel,
			TrainCollection trainCollection) {
		this.mainFrame = mainFrame;
		this.trainsPanel = trainsPanel;
		this.controllerAreaPanel = controllerAreaPanel;
		this.trainCollection = trainCollection;
	}

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

			if (fileChooser.showOpenDialog(trainDialog) == JFileChooser.APPROVE_OPTION) {
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
				deleteImageButton.setEnabled(true);
				// trainDialog.pack();
				trainDialog.setSize(400, 380);
			}

		} else if (e.getActionCommand().equals("deleteImage")) {
			this.imagePath = null;
			trainImageLabel.setIcon(null);
			trainImageLabel.setText("Kein Bild gesetzt");
			deleteImageButton.setEnabled(false);
			trainDialog.setSize(400, 250);

		} else if (e.getActionCommand().equals("cancelDialog")) {
			trainDialog.dispose();

		} else if (e.getActionCommand().equals("createTrain")) {

			String trainName = trainNameTextField.getText();
			String trainModelDesc = trainModelDescTextField.getText();

			// Überprüfen, ob Zugname eingegeben wurde
			if (trainNameTextField.getText().equals("")) {
				JOptionPane.showMessageDialog(trainDialog, "Bitte gebe dem Zug einen Namen", "Zugname leer",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			// Überprüfen, ob Zug bereits existiert
			if (trainCollection.trainIsAlreadyExisting(trainName)) {
				JOptionPane.showMessageDialog(trainDialog, "Zug existiert bereits, bitte gebe einen anderen Namen ein",
						"Zug existiert bereits", JOptionPane.WARNING_MESSAGE);
				return;
			}

			trainDialog.dispose();
			Train newTrain = new Train(trainName, trainModelDesc, imagePath);

			// Nach schließen des Dialoges muss der neue Zug im UI erstellt werden
			this.drawTrainPanel(newTrain);
			
			// Wenn beide Fälle nicht eintreten, füge Zug der trainCollection hinzu		
			this.trainCollection.addTrain(newTrain);

		} else if (e.getActionCommand().equals("editTrain")) {
			// imagePath zurücksetzen
			this.imagePath = null;

			// Hole Button des getriggerten Events
			JButton actionButton = (JButton) e.getSource();

			// Speichere affectedPanel für später
			this.affectedPanel = (JPanel) actionButton.getParent().getParent();

			// Hole Zugname über Parent des Buttons
			String trainName = affectedPanel.getName();
			// Hole Zugobjekt
			this.affectedTrain = this.trainCollection.getTrainByName(trainName);

			this.openEditDialog(this.affectedTrain);

		} else if (e.getActionCommand().equals("deleteTrain")) {
			// Hole Button des getriggerten Events
			JButton actionButton = (JButton) e.getSource();

			// Speichere affectedPanel für später
			this.affectedPanel = (JPanel) actionButton.getParent().getParent();

			// Hole Zugname über Parent des Buttons
			String trainName = affectedPanel.getName();
			// Hole Zugobjekt
			this.affectedTrain = this.trainCollection.getTrainByName(trainName);

			// Frage nach, onb Zug wirklich gelöscht werden soll
			int confirmResult = JOptionPane.showConfirmDialog(this.trainsPanel,
					"Soll Zug " + trainName + " wirklich gelöscht werden?", "Zug " + trainName + " löschen?",
					JOptionPane.YES_NO_OPTION);

			if (confirmResult == JOptionPane.YES_OPTION) {
				// Setze Controller Bereich zurück, falls der gelöschte Zug der zuletzt ausgewählt war
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

			} else {
				return;
			}
			
			// Wenn alle Züge gelöscht wurden, blende Platzhalter-Label wieder ein
			if(this.trainCollection.getTrains().isEmpty()){
				JPanel noTrainPanel = new JPanel(new GridBagLayout());
				JLabel noTrainLabel = new JLabel("Noch kein Zug erstellt");
				noTrainPanel.add(noTrainLabel);
				this.trainsPanel.add(noTrainPanel);
			}
			
		} else if (e.getActionCommand().equals("saveTrain")) {
			String trainName = trainNameTextField.getText();
			String trainModelDesc = trainModelDescTextField.getText();

			// Überprüfen, ob Zugname eingegeben wurde
			if (trainNameTextField.getText().equals("")) {
				JOptionPane.showMessageDialog(trainDialog, "Bitte gebe dem Zug einen Namen", "Zugname leer",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			// Werfe Fehler, wenn Zugname bereits von einem anderen Zug belegt
			// ist
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

			// Falls der editierte Zug gleichzeitig auch der zuletzt ausgewählte Zug ist, aktualisiere auch den Controller-Bereiech
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

			// Nach schließen des Dialoges muss der neue Zug auch im UI
			// aktualisiert werden
			this.redrawTrainPanel(this.affectedTrain, affectedPanel);

		} else if (e.getActionCommand().equals("stopTrain")) {
			if (this.selectedTrain.getSpeed() != 0) {
				this.selectedTrain.setSpeed(0);
				this.selectedSpeedLabel.setText("Geschwindigkeit: " + this.selectedTrain.getSpeed() + "%");
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
			this.selectedTrain.setLightActive(!this.selectedTrain.isLightActive());

			ImageIcon iconSwitch = new ImageIcon();
			ImageIcon iconLight = new ImageIcon();
			Image imgSwitch;

			if (this.selectedTrain.isLightActive()) {
				iconSwitch = new ImageIcon("images/switch_right_green.png");
				iconLight = new ImageIcon("images/lightbulb_on.png");
				this.selectedLightLabel.setText("Licht: An");
			} else {
				iconSwitch = new ImageIcon("images/switch_left.png");
				iconLight = new ImageIcon("images/lightbulb_off.png");
				this.selectedLightLabel.setText("Licht: Aus");
			}

			imgSwitch = iconSwitch.getImage();
			imgSwitch = imgSwitch.getScaledInstance(35, 35, java.awt.Image.SCALE_SMOOTH);
			iconSwitch = new ImageIcon(imgSwitch);
			this.switchLightButton.setIcon(iconSwitch);

			Image imgLight;
			imgLight = iconLight.getImage();
			imgLight = imgLight.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
			iconLight = new ImageIcon(imgLight);
			this.lightBulb.setIcon(iconLight);

		} else if (e.getActionCommand().equals("stopAllTrains")) {
			this.trainCollection.stopAllTrains();
			if (this.selectedTrainPanel != null) {
				this.speedSlider.setValue(this.selectedTrain.getSpeed());
			}

			// Gehe über jedes Zugpanel in der Zugliste
			JPanel trainEntry;
			JLabel speedLabel;
			for (Component c1 : this.trainsPanel.getComponents()) {
				// Überprüfe sicherheitshalber, ob Komponenten ein Panel ist
				if (c1 instanceof JPanel) {
					trainEntry = (JPanel) c1;
					// Gehe über jede Komponente innerhalb eines Listeneintrages um das Geschwindigkeitslabel zu manipulieren
					for (Component c2 : trainEntry.getComponents()) {
						// Ist das aktuelle Objekt das Geschwindigkeitslabel, so verändere es
						if (("speedLabel").equals(c2.getName())) {
							speedLabel = (JLabel) c2;
							speedLabel.setText("Geschwindigkeit: 0%");
						}
					}
				}
			}
		}
	}

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
		// Deaktiviere Button und aktiviere ihn erst, wenn ein Bild ausgewählt wurde
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
		// Setze Layout in Hilfsüane zu "FlowLayout.RIGHT"
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

		// trainDialog.pack();
		trainDialog.setSize(400, 250);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		trainDialog.setLocation(dim.width / 2 - trainDialog.getSize().width / 2,
				dim.height / 2 - trainDialog.getSize().height / 2);
		trainDialog.setVisible(true);
	}

	public void drawTrainPanel(Train train) {
		// Lösche das Platzhalter-Label aus dem Panel, wenn es keinen Zug gibt
		if (this.trainCollection.getTrains().isEmpty()) {
			this.trainsPanel.removeAll();
			this.trainsPanel.repaint();
		}

		JPanel newTrainPanel = new JPanel();
		newTrainPanel.setName(train.getName());
		// newTrainPanel.setBorder(BorderFactory.createTitledBorder(" "));
		newTrainPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		newTrainPanel.setPreferredSize(new Dimension(315, 100));
		newTrainPanel.setMaximumSize(new Dimension(315, 100));
		newTrainPanel.setMinimumSize(new Dimension(315, 100));

		newTrainPanel.setLayout(new GridLayout());

		newTrainPanel.setLayout(new GridBagLayout());

		// Erstelle Regeln für GridBagLayout.
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		// Insets set = new Insets(5, 5, 5, 5);
		// c.insets = set;
		c.weightx = 1.0;
		c.weighty = 1.0;

		// Füge Zugname hinzu
		JLabel trainName = new JLabel(train.getName());
		Font boldFont = new Font(trainName.getFont().getFontName(), Font.BOLD, trainName.getFont().getSize());
		trainName.setFont(boldFont);

		c.gridx = 1;
		c.gridy = 0;
		newTrainPanel.add(trainName, c);

		JLabel trainModelDesc = new JLabel(train.getModelDesc());
		c.gridx = 1;
		c.gridy = 1;
		newTrainPanel.add(trainModelDesc, c);

		JLabel speedLabel = new JLabel("Geschwindigkeit: " + train.getSpeed() + "%");
		speedLabel.setName("speedLabel");
		c.gridx = 2;
		c.gridy = 0;
		newTrainPanel.add(speedLabel, c);

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

		JPanel trainActionPanel = new JPanel();
		trainActionPanel.setOpaque(false);
		trainActionPanel.add(editTrainButton);
		trainActionPanel.add(deleteTrainButton);

		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		newTrainPanel.add(trainActionPanel, c);

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

		newTrainPanel.addMouseListener(this);

		this.trainsPanel.add(newTrainPanel);
		this.trainsPanel.revalidate();
	}

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
		trainNameTextField.setText(train.getName());
		trainDialog.add(trainNameTextField, c);

		// Setze Layout Einstellungen für 2. Textfeld
		c.gridy = 1;
		trainModelDescTextField = new JTextField();
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
		// Setze Layout in Hilfsüane zu "FlowLayout.RIGHT"
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

		// trainDialog.pack();
		if (train.getImagePath() == null) {
			trainDialog.setSize(400, 250);
		} else {
			trainDialog.setSize(400, 380);
		}

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		trainDialog.setLocation(dim.width / 2 - trainDialog.getSize().width / 2,
				dim.height / 2 - trainDialog.getSize().height / 2);
		trainDialog.setVisible(true);
	}

	public void redrawTrainPanel(Train train, JPanel panel) {
		// Setze Hintergrund des Zugpanels zurück
		panel.setBackground(trainsPanel.getBackground());
		panel.removeAll();
		panel.setName(train.getName());
		panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		panel.setPreferredSize(new Dimension(315, 100));
		panel.setMaximumSize(new Dimension(315, 100));
		panel.setMinimumSize(new Dimension(315, 100));

		panel.setLayout(new GridLayout());

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

		JLabel trainModelDesc = new JLabel(train.getModelDesc());
		c.gridx = 1;
		c.gridy = 1;
		panel.add(trainModelDesc, c);

		JLabel speedLabel = new JLabel("Geschwindigkeit: " + train.getSpeed() + "%");
		speedLabel.setName("speedLabel");
		c.gridx = 2;
		c.gridy = 0;
		panel.add(speedLabel, c);

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

		JPanel trainActionPanel = new JPanel();
		trainActionPanel.setOpaque(false);
		trainActionPanel.add(editTrainButton);
		trainActionPanel.add(deleteTrainButton);

		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;

		panel.add(trainActionPanel, c);

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

		this.trainsPanel.revalidate();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() instanceof JPanel) {
			JPanel activePanel = (JPanel) (e.getSource());
			// Setze Hintergrund nur, wenn das Panel nicht gerade angeklickt
			// wurde
			if (!(activePanel == selectedTrainPanel)) {
				activePanel.setBackground(new Color(230, 230, 230));
			}
		} else if (e.getSource() instanceof JButton) {
			JButton activeButton = (JButton) (e.getSource());
			JPanel activePanel = (JPanel) activeButton.getParent().getParent();
			// Setze Hintergrund nur, wenn das Panel nicht gerade angeklickt
			// wurde
			if (!(activePanel == selectedTrainPanel)) {
				activePanel.setBackground(new Color(230, 230, 230));
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getSource() instanceof JPanel) {
			JPanel activePanel = (JPanel) (e.getSource());
			// Setze Hintergrund nur zurück, wenn das Panel nicht gerade
			// angeklickt wurde
			if (!(activePanel == selectedTrainPanel)) {
				activePanel.setBackground(trainsPanel.getBackground());
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getSource() instanceof JPanel) {
			JPanel activePanel = (JPanel) (e.getSource());
			if (this.selectedTrainPanel == null) {
				this.selectedTrainPanel = activePanel;
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
				this.drawControllerArea();

			} else if (this.selectedTrainPanel != activePanel) {
				this.selectedTrainPanel.setBackground(trainsPanel.getBackground());
				this.selectedTrainPanel = activePanel;
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
				this.drawControllerArea();
			}

		}
	}

	public void drawControllerArea() {
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

		this.controllerTrainModelDesc = new JLabel(this.selectedTrain.getModelDesc());
		myFont = new Font(this.controllerTrainModelDesc.getFont().getFontName(), Font.PLAIN, 14);
		this.controllerTrainModelDesc.setFont(myFont);
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.anchor = GridBagConstraints.PAGE_START;
		trainControlPanel.add(this.controllerTrainModelDesc, c);

		JLabel speedLabel = new JLabel("Geschwindigkeit:");
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.CENTER;
		trainControlPanel.add(speedLabel, c);

		JPanel speedPanel = new JPanel();

		this.speedSlider = new JSlider();
		this.speedSlider.setName("speedSlider");
		this.speedSlider.addChangeListener(this);
		this.speedSlider.setMajorTickSpacing(100);
		this.speedSlider.setMinorTickSpacing(1);
		this.speedSlider.setPaintTicks(true);
		this.speedSlider.setPaintLabels(true);
		this.speedSlider.setValue(this.selectedTrain.getSpeed());
		speedPanel.add(this.speedSlider);

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

		JPanel directionPanel = new JPanel(new FlowLayout());

		this.directionButtonGroup = new ButtonGroup();

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

		this.directionButtonGroup.add(this.toggleLeft);
		this.directionButtonGroup.add(this.toggleRight);

		if (this.selectedTrain.isDirectionRight()) {
			this.directionButtonGroup.setSelected(this.toggleRight.getModel(), true);
		} else {
			this.directionButtonGroup.setSelected(this.toggleLeft.getModel(), true);
		}

		directionPanel.add(this.toggleLeft);
		directionPanel.add(this.toggleRight);

		c.gridx = 2;
		c.gridy = 3;
		c.gridwidth = 1;
		trainControlPanel.add(directionPanel, c);

		JLabel lightLabel = new JLabel("Licht:");
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
		trainControlPanel.add(lightLabel, c);

		JPanel lightPanel = new JPanel();

		this.switchLightButton = new JButton();
		this.switchLightButton.addActionListener(this);
		this.switchLightButton.setActionCommand("switchLight");
		this.switchLightButton.setBorder(BorderFactory.createEmptyBorder());
		this.switchLightButton.setContentAreaFilled(false);
		ImageIcon iconSwitch = new ImageIcon();
		ImageIcon iconLight = new ImageIcon();
		Image imgSwitch;

		if (this.selectedTrain.isLightActive()) {
			iconSwitch = new ImageIcon("images/switch_right_green.png");
			iconLight = new ImageIcon("images/lightbulb_on.png");
		} else {
			iconSwitch = new ImageIcon("images/switch_left.png");
			iconLight = new ImageIcon("images/lightbulb_off.png");
		}

		imgSwitch = iconSwitch.getImage();
		imgSwitch = imgSwitch.getScaledInstance(35, 35, java.awt.Image.SCALE_SMOOTH);
		iconSwitch = new ImageIcon(imgSwitch);
		this.switchLightButton.setIcon(iconSwitch);

		this.lightBulb = new JLabel();
		Image imgLight;
		imgLight = iconLight.getImage();
		imgLight = imgLight.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
		iconLight = new ImageIcon(imgLight);
		this.lightBulb.setIcon(iconLight);

		lightPanel.add(this.switchLightButton);
		lightPanel.add(this.lightBulb);

		JPanel flowPanel = new JPanel(new FlowLayout());
		flowPanel.add(lightPanel);

		c.gridx = 2;
		c.gridy = 4;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.CENTER;
		trainControlPanel.add(lightPanel, c);

		this.controllerAreaPanel.add(trainControlPanel);
		this.controllerAreaPanel.revalidate();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider activeSlider = (JSlider) e.getSource();

		// Überprüfe, welcher Slider das Ereignis ausgelöst hat
		if (("speedSlider".equals(activeSlider.getName()))) {
			int speed = activeSlider.getValue();
			this.selectedTrain.setSpeed(speed);
			this.selectedSpeedLabel.setText("Geschwindigkeit: " + this.selectedTrain.getSpeed() + "%");
		}
	}
}