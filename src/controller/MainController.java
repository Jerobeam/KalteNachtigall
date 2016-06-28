package controller;

import java.awt.event.ActionEvent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import prod.Train;
import prod.TrainCollection;

public class MainController implements ActionListener {

	private JFrame mainFrame;
	private JPanel trainsPanel;
	private TrainCollection trainCollection;
	private JDialog trainDialog;
	private JLabel trainImageLabel;
	private JTextField trainNameTextField;
	private JTextField trainModelDescTextField;
	private String imagePath;
	private JPanel affectedPanel;
	private Train affectedTrain;

	public MainController(JFrame mainFrame, JPanel trainsPanel, TrainCollection trainCollection) {
		this.mainFrame = mainFrame;
		this.trainsPanel = trainsPanel;
		this.trainCollection = trainCollection;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("addTrain")) {
			// imagePath zurücksetzen
			this.imagePath = null;

			// Öffne Dialog für die Zugerstellung
			this.openNewDialog();
			// Erstelle neues Panel in der Liste für neuen Zug
			// JPanel train3 = new JPanel();
			// train3.setBorder(BorderFactory.createTitledBorder("train3"));
			// train3.setPreferredSize(new Dimension(250, 100));
			// train3.setMaximumSize(new Dimension(250, 100));
			// train3.setMinimumSize(new Dimension(250, 100));
			// this.trainsPanel.add(train3);
			// this.trainsPanel.revalidate();

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
				// trainDialog.pack();
				trainDialog.setSize(400, 380);
			}

		} else if (e.getActionCommand().equals("deleteImage")) {
			this.imagePath = null;
			trainImageLabel.setIcon(null);
			trainImageLabel.setText("Kein Bild gesetzt");
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

			// Wenn beide Fälle nicht eintreten, füge Zug der trainCollection
			// hinzu
			Train newTrain = new Train(trainName, trainModelDesc, imagePath);
			this.trainCollection.addTrain(newTrain);
			trainDialog.dispose();

			// Nach schließen des Dialoges muss der neue Zug auch im UI erstellt
			// werden
			this.drawTrainPanel(newTrain);

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

			this.openEditDialog(affectedTrain);

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
				// Entferne Zugobjekt von trainCollection
				this.trainCollection.removeTrainFromCollection(affectedTrain);
				// Entferne Panel und refreshe trainsPanel
				this.trainsPanel.remove(this.affectedPanel);
				this.trainsPanel.repaint();
			} else {
				return;
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
			if (trainCollection.trainIsAlreadyExisting(trainName) && !(affectedTrain.getName().equals(trainName))) {
				JOptionPane.showMessageDialog(trainDialog, "Zug existiert bereits, bitte gebe einen anderen Namen ein",
						"Zug existiert bereits", JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Wenn beide Fälle nicht eintreten, update den Zug
			affectedTrain.setName(trainName);
			affectedTrain.setModelDesc(trainModelDesc);
			affectedTrain.setImagePath(imagePath);
			trainDialog.dispose();

			// Nach schließen des Dialoges muss der neue Zug auch im UI
			// aktualisiert
			// werden
			this.redrawTrainPanel(affectedTrain, affectedPanel);
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
		JButton deleteImageButton = new JButton("Bild löschen");
		deleteImageButton.setActionCommand("deleteImage");
		deleteImageButton.addActionListener(this);
		// Füge Buttons dem Panel hinzu
		imageButtonsPanel.add(searchButton);
		imageButtonsPanel.add(deleteImageButton);
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
		// Enter-Drücken ausgelöst wird und blaum umrandet ist
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
		c.gridx = 2;
		c.gridy = 0;
		newTrainPanel.add(speedLabel, c);

		JLabel lightLabel = new JLabel();
		if (train.isLightActive()) {
			lightLabel.setText("Licht: An");
		} else {
			lightLabel.setText("Licht: Aus");
		}
		c.gridx = 2;
		c.gridy = 1;
		newTrainPanel.add(lightLabel, c);

		// Füge Zugbild hinzu
		JLabel trainImageLabel = new JLabel();
		ImageIcon icon = new ImageIcon();
		Image img;
		icon = new ImageIcon(train.getImagePath());
		img = icon.getImage();
		img = img.getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(img);
		trainImageLabel.setIcon(icon);
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.VERTICAL;
		newTrainPanel.add(trainImageLabel, c);

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
		deleteTrainButton.setBorder(BorderFactory.createEmptyBorder());

		JPanel trainActionPanel = new JPanel();

		trainActionPanel.add(editTrainButton);

		trainActionPanel.add(deleteTrainButton);

		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		newTrainPanel.add(trainActionPanel, c);

		JLabel directionLabel = new JLabel();
		if (train.isDirectionRight()) {
			directionLabel.setText("Fahrtrichtung: Rechts");
		} else {
			directionLabel.setText("Fahrtrichtung: Links");
		}
		c.gridx = 2;
		c.gridy = 3;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.BOTH;
		newTrainPanel.add(directionLabel, c);

		this.trainsPanel.add(newTrainPanel);
		this.trainsPanel.revalidate();

		// JPanel train2 = new JPanel();
		// train2.setBorder(BorderFactory.createTitledBorder("train2"));
		// train2.setPreferredSize(new Dimension(250, 100));
		// train2.setMaximumSize(new Dimension(250, 100));
		// train2.setMinimumSize(new Dimension(250, 100));
		// this.trainsPanel.add(train2);
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
		JButton deleteImageButton = new JButton("Bild löschen");
		deleteImageButton.setActionCommand("deleteImage");
		deleteImageButton.addActionListener(this);
		// Füge Buttons dem Panel hinzu
		imageButtonsPanel.add(searchButton);
		imageButtonsPanel.add(deleteImageButton);
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
		panel.removeAll();
		panel.setName(train.getName());
		// newTrainPanel.setBorder(BorderFactory.createTitledBorder(" "));
		panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		panel.setPreferredSize(new Dimension(315, 100));
		panel.setMaximumSize(new Dimension(315, 100));
		panel.setMinimumSize(new Dimension(315, 100));

		panel.setLayout(new GridLayout());

		panel.setLayout(new GridBagLayout());

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
		panel.add(trainName, c);

		JLabel trainModelDesc = new JLabel(train.getModelDesc());
		c.gridx = 1;
		c.gridy = 1;
		panel.add(trainModelDesc, c);

		JLabel speedLabel = new JLabel("Geschwindigkeit: " + train.getSpeed() + "%");
		c.gridx = 2;
		c.gridy = 0;
		panel.add(speedLabel, c);

		JLabel lightLabel = new JLabel();
		if (train.isLightActive()) {
			lightLabel.setText("Licht: An");
		} else {
			lightLabel.setText("Licht: Aus");
		}
		c.gridx = 2;
		c.gridy = 1;
		panel.add(lightLabel, c);

		// Füge Zugbild hinzu
		JLabel trainImageLabel = new JLabel();
		ImageIcon icon = new ImageIcon();
		Image img;
		icon = new ImageIcon(train.getImagePath());
		img = icon.getImage();
		img = img.getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(img);
		trainImageLabel.setIcon(icon);
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.VERTICAL;
		panel.add(trainImageLabel, c);

		// Erstellen des "Edit"-Buttons
		JButton editTrainButton = new JButton();
		ImageIcon iconEdit = new ImageIcon();
		Image imgEdit;
		iconEdit = new ImageIcon("images/edit.png");
		imgEdit = iconEdit.getImage();
		imgEdit = imgEdit.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
		iconEdit = new ImageIcon(imgEdit);
		editTrainButton = new JButton(iconEdit);
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
		deleteTrainButton.setBorder(BorderFactory.createEmptyBorder());

		JPanel trainActionPanel = new JPanel();

		trainActionPanel.add(editTrainButton);
		editTrainButton.setActionCommand("editTrain");
		editTrainButton.addActionListener(this);

		trainActionPanel.add(deleteTrainButton);
		deleteTrainButton.setActionCommand("deleteTrain");
		deleteTrainButton.addActionListener(this);
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		panel.add(trainActionPanel, c);

		JLabel directionLabel = new JLabel();
		if (train.isDirectionRight()) {
			directionLabel.setText("Fahrtrichtung: Rechts");
		} else {
			directionLabel.setText("Fahrtrichtung: Links");
		}
		c.gridx = 2;
		c.gridy = 3;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.BOTH;
		panel.add(directionLabel, c);

		this.trainsPanel.revalidate();
	}

}