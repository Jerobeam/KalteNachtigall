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
import javax.swing.filechooser.FileNameExtensionFilter;

import prod.Train;
import prod.TrainCollection;

public class MainController implements ActionListener, MouseListener {

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

	public MainController(JFrame mainFrame, JPanel trainsPanel, JPanel controllerAreaPanel, TrainCollection trainCollection) {
		this.mainFrame = mainFrame;
		this.trainsPanel = trainsPanel;
		this.controllerAreaPanel = controllerAreaPanel;
		this.trainCollection = trainCollection;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("addTrain")) {
			// imagePath zur�cksetzen
			this.imagePath = null;

			// �ffne Dialog f�r die Zugerstellung
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

			// �berpr�fen, ob Zugname eingegeben wurde
			if (trainNameTextField.getText().equals("")) {
				JOptionPane.showMessageDialog(trainDialog, "Bitte gebe dem Zug einen Namen", "Zugname leer",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			// �berpr�fen, ob Zug bereits existiert
			if (trainCollection.trainIsAlreadyExisting(trainName)) {
				JOptionPane.showMessageDialog(trainDialog, "Zug existiert bereits, bitte gebe einen anderen Namen ein",
						"Zug existiert bereits", JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Wenn beide F�lle nicht eintreten, f�ge Zug der trainCollection
			// hinzu
			Train newTrain = new Train(trainName, trainModelDesc, imagePath);
			this.trainCollection.addTrain(newTrain);
			trainDialog.dispose();

			// Nach schlie�en des Dialoges muss der neue Zug auch im UI erstellt
			// werden
			this.drawTrainPanel(newTrain);

		} else if (e.getActionCommand().equals("editTrain")) {
			// imagePath zur�cksetzen
			this.imagePath = null;

			// Hole Button des getriggerten Events
			JButton actionButton = (JButton) e.getSource();

			// Speichere affectedPanel f�r sp�ter
			this.affectedPanel = (JPanel) actionButton.getParent().getParent();

			// Hole Zugname �ber Parent des Buttons
			String trainName = affectedPanel.getName();
			// Hole Zugobjekt
			this.affectedTrain = this.trainCollection.getTrainByName(trainName);

			this.openEditDialog(affectedTrain);

		} else if (e.getActionCommand().equals("deleteTrain")) {
			// Hole Button des getriggerten Events
			JButton actionButton = (JButton) e.getSource();

			// Speichere affectedPanel f�r sp�ter
			this.affectedPanel = (JPanel) actionButton.getParent().getParent();

			// Hole Zugname �ber Parent des Buttons
			String trainName = affectedPanel.getName();
			// Hole Zugobjekt
			this.affectedTrain = this.trainCollection.getTrainByName(trainName);

			// Frage nach, onb Zug wirklich gel�scht werden soll
			int confirmResult = JOptionPane.showConfirmDialog(this.trainsPanel,
					"Soll Zug " + trainName + " wirklich gel�scht werden?", "Zug " + trainName + " l�schen?",
					JOptionPane.YES_NO_OPTION);

			if (confirmResult == JOptionPane.YES_OPTION) {
				// Entferne Zugobjekt von trainCollection
				this.trainCollection.removeTrainFromCollection(affectedTrain);
				// Entferne Panel und refreshe trainsPanel
				this.trainsPanel.remove(this.affectedPanel);
				this.trainsPanel.revalidate();
				this.trainsPanel.repaint();
			} else {
				return;
			}

		} else if (e.getActionCommand().equals("saveTrain")) {
			String trainName = trainNameTextField.getText();
			String trainModelDesc = trainModelDescTextField.getText();

			// �berpr�fen, ob Zugname eingegeben wurde
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
			// Wenn beide F�lle nicht eintreten, update den Zug
			affectedTrain.setName(trainName);
			affectedTrain.setModelDesc(trainModelDesc);
			affectedTrain.setImagePath(imagePath);
			trainDialog.dispose();

			// Nach schlie�en des Dialoges muss der neue Zug auch im UI
			// aktualisiert werden
			this.redrawTrainPanel(affectedTrain, affectedPanel);
		}
	}

	public void openNewDialog() {
		// Erstelle neuen Dialog
		this.trainDialog = new JDialog(this.mainFrame, "Neuer Zug", true);
		trainDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// Setze Layout f�r Dialog
		trainDialog.setLayout(new GridBagLayout());
		GridBagConstraints c;
		// Setze R�nder zwischen einzelnen Komponenten
		Insets set = new Insets(10, 10, 10, 10);

		// Setze Layout Einstellungen f�r 1.Label
		c = new GridBagConstraints();
		c.insets = set;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		trainDialog.add(new JLabel("Zugname: "), c);
		// Setze Layout Einstellungen f�r 2.Label
		c.gridy = 1;
		trainDialog.add(new JLabel("Modell (optional): "), c);
		// Setze Layout Einstellungen f�r 3.Label
		c.gridy = 3;
		trainDialog.add(new JLabel("Bild (optional): "), c);

		// Setze Layout Einstellungen f�r 1. Textfeld
		c = new GridBagConstraints();
		c.insets = set;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		trainNameTextField = new JTextField();
		trainDialog.add(trainNameTextField, c);

		// Setze Layout Einstellungen f�r 2. Textfeld
		c.gridy = 1;
		trainModelDescTextField = new JTextField();
		trainDialog.add(trainModelDescTextField, c);

		// Setze Layout Einstellungen f�r Panel, welches den "Durchsuchen" und
		// "Bild l�schen"-Button enth�lt
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
		// Erstelle "Bild l�schen"-Button
		JButton deleteImageButton = new JButton("Bild l�schen");
		deleteImageButton.setActionCommand("deleteImage");
		deleteImageButton.addActionListener(this);
		// F�ge Buttons dem Panel hinzu
		imageButtonsPanel.add(searchButton);
		imageButtonsPanel.add(deleteImageButton);
		// F�ge Panel dem Dialog hinzu
		trainDialog.add(imageButtonsPanel, c);

		// Setze Layout Einstellungen f�r Zugbild
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
		// Benutze Hilfspanel f�r rechtsb�ndige Ausrichung
		JPanel panelAction = new JPanel();
		// Setze Layout in Hilfs�ane zu "FlowLayout.RIGHT"
		panelAction.setLayout(new FlowLayout(FlowLayout.RIGHT));
		// Erstelle Buttons zum Abbrechen oder Erstellen
		JButton cancelDialogButton = new JButton("Abbrechen");
		cancelDialogButton.setActionCommand("cancelDialog");
		cancelDialogButton.addActionListener(this);
		JButton createButton = new JButton("Erstellen");
		createButton.setActionCommand("createTrain");
		createButton.addActionListener(this);
		// Setze "Erstelle"-Button als Default Button, damit er durch
		// Enter-Dr�cken ausgel�st wird und blau umrandet ist
		trainDialog.getRootPane().setDefaultButton(createButton);

		// F�ge Buttons dem Hilfspanel hinzu
		panelAction.add(cancelDialogButton);
		panelAction.add(createButton);
		// F�ge Hilfspanel zum Dialog dazu
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

		// Erstelle Regeln f�r GridBagLayout.
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		// Insets set = new Insets(5, 5, 5, 5);
		// c.insets = set;
		c.weightx = 1.0;
		c.weighty = 1.0;

		// F�ge Zugname hinzu
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

		// F�ge Zugbild hinzu
		JLabel trainImageLabel = new JLabel();
		ImageIcon icon = new ImageIcon();
		Image img;
		if(train.getImagePath() != null){
			icon = new ImageIcon(train.getImagePath());
		}else{
			icon = new ImageIcon("images/default_train.png");
		}
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
		// Setze Layout f�r Dialog
		trainDialog.setLayout(new GridBagLayout());
		GridBagConstraints c;
		// Setze R�nder zwischen einzelnen Komponenten
		Insets set = new Insets(10, 10, 10, 10);

		// Setze Layout Einstellungen f�r 1.Label
		c = new GridBagConstraints();
		c.insets = set;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		trainDialog.add(new JLabel("Zugname: "), c);
		// Setze Layout Einstellungen f�r 2.Label
		c.gridy = 1;
		trainDialog.add(new JLabel("Modell (optional): "), c);
		// Setze Layout Einstellungen f�r 3.Label
		c.gridy = 3;
		trainDialog.add(new JLabel("Bild (optional): "), c);

		// Setze Layout Einstellungen f�r 1. Textfeld
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

		// Setze Layout Einstellungen f�r 2. Textfeld
		c.gridy = 1;
		trainModelDescTextField = new JTextField();
		trainModelDescTextField.setText(train.getModelDesc());
		trainDialog.add(trainModelDescTextField, c);

		// Setze Layout Einstellungen f�r Panel, welches den "Durchsuchen" und
		// "Bild l�schen"-Button enth�lt
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
		// Erstelle "Bild l�schen"-Button
		JButton deleteImageButton = new JButton("Bild l�schen");
		deleteImageButton.setActionCommand("deleteImage");
		deleteImageButton.addActionListener(this);
		// F�ge Buttons dem Panel hinzu
		imageButtonsPanel.add(searchButton);
		imageButtonsPanel.add(deleteImageButton);
		// F�ge Panel dem Dialog hinzu
		trainDialog.add(imageButtonsPanel, c);

		// Setze Layout Einstellungen f�r Zugbild
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
		// Benutze Hilfspanel f�r rechtsb�ndige Ausrichung
		JPanel panelAction = new JPanel();
		// Setze Layout in Hilfs�ane zu "FlowLayout.RIGHT"
		panelAction.setLayout(new FlowLayout(FlowLayout.RIGHT));
		// Erstelle Buttons zum Abbrechen oder Erstellen
		JButton cancelDialogButton = new JButton("Abbrechen");
		cancelDialogButton.setActionCommand("cancelDialog");
		cancelDialogButton.addActionListener(this);
		JButton saveButton = new JButton("Speichern");
		saveButton.setActionCommand("saveTrain");
		saveButton.addActionListener(this);
		// Setze "Erstelle"-Button als Default Button, damit er durch
		// Enter-Dr�cken ausgel�st wird und blaum umrandet ist
		trainDialog.getRootPane().setDefaultButton(saveButton);

		// F�ge Buttons dem Hilfspanel hinzu
		panelAction.add(cancelDialogButton);
		panelAction.add(saveButton);
		// F�ge Hilfspanel zum Dialog dazu
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
		// Setze Hintergrund des Zugpanels zur�ck
		panel.setBackground(trainsPanel.getBackground());
		panel.removeAll();
		panel.setName(train.getName());
		// newTrainPanel.setBorder(BorderFactory.createTitledBorder(" "));
		panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		panel.setPreferredSize(new Dimension(315, 100));
		panel.setMaximumSize(new Dimension(315, 100));
		panel.setMinimumSize(new Dimension(315, 100));

		panel.setLayout(new GridLayout());

		panel.setLayout(new GridBagLayout());

		// Erstelle Regeln f�r GridBagLayout.
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;

		// F�ge Zugname hinzu
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

		// F�ge Zugbild hinzu
		JLabel trainImageLabel = new JLabel();
		ImageIcon icon = new ImageIcon();
		Image img;
		if(train.getImagePath() != null){
			icon = new ImageIcon(train.getImagePath());
		}else{
			icon = new ImageIcon("images/default_train.png");
		}
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
			// Setze Hintergrund nur zur�ck, wenn das Panel nicht gerade
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
			if (selectedTrainPanel == null) {
				this.selectedTrainPanel = activePanel;
				activePanel.setBackground(new Color(210, 210, 210));
			} else {
				selectedTrainPanel.setBackground(trainsPanel.getBackground());
				this.selectedTrainPanel = activePanel;
				activePanel.setBackground(new Color(210, 210, 210));
			}
			// Hole den ausgew�hlten Zug �ber den Panel Namen
			this.selectedTrain = this.trainCollection.getTrainByName(activePanel.getName());

			// Hole sich alle relevanten Labels aus dem ausgew�hlten Panel, um ihre Werte sp�ter manipulieren zu k�nnen
			for (Component component : activePanel.getComponents()) {
				if(("speedLabel").equals(component.getName())){
					this.selectedSpeedLabel = (JLabel)component;
				}
				if(("directionLabel").equals(component.getName())){
					this.selectedDirectionsLabel = (JLabel)component;
				}
				if(("lightLabel").equals(component.getName())){
					this.selectedLightLabel = (JLabel)component;
				}
			}
			this.drawControllerArea();
		}
	}
	
	public void drawControllerArea() {
		this.controllerAreaPanel.removeAll();
		JPanel trainControlPanel = new JPanel();
		trainControlPanel.setLayout(new GridBagLayout());

		// Erstelle Regeln f�r GridBagLayout.
		GridBagConstraints c = new GridBagConstraints();
		Insets set = new Insets(10, 10, 10, 10);
		c.insets = set;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;

		// F�ge Zugbild hinzu
		JLabel trainImageLabel = new JLabel();
		ImageIcon icon = new ImageIcon();
		Image img;
		icon = new ImageIcon("D:/Bilder/Saved Pictures/Beautiful/Background/stock-photo-154870507.jpg");
		img = icon.getImage();
		img = img.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(img);
		trainImageLabel.setIcon(icon);

		// JPanel panel1 = new JPanel();
		// panel1.setBorder(BorderFactory.createTitledBorder("Panel1"));
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 2;
		c.anchor = GridBagConstraints.PAGE_START;
		c.fill = GridBagConstraints.VERTICAL;
		trainControlPanel.add(trainImageLabel, c);

		JLabel trainName = new JLabel("Random Train Name Focker");
		Font myFont = new Font(trainName.getFont().getFontName(), Font.BOLD, 16);
		trainName.setFont(myFont);

		// JPanel panel2 = new JPanel();
		// panel2.setBorder(BorderFactory.createTitledBorder("Panel2"));
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		trainControlPanel.add(trainName, c);

		JLabel trainModelDesc = new JLabel("Dampflok Mampflol");
		myFont = new Font(trainModelDesc.getFont().getFontName(), Font.PLAIN, 14);
		trainModelDesc.setFont(myFont);
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.anchor = GridBagConstraints.PAGE_START;
		trainControlPanel.add(trainModelDesc, c);

		JLabel speedLabel = new JLabel("Geschwindigkeit:");
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.CENTER;
		trainControlPanel.add(speedLabel, c);

		JPanel speedPanel = new JPanel();

		JSlider speedSlider = new JSlider();
		speedSlider.setMajorTickSpacing(100);
		speedSlider.setMinorTickSpacing(1);
		speedSlider.setPaintTicks(true);
		speedSlider.setPaintLabels(true);
		speedSlider.setValue(0);
		speedPanel.add(speedSlider);

		JButton stopTrainButton = new JButton("Stop");
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
		
		ButtonGroup directionButtonGroup = new ButtonGroup();
		
		JToggleButton toggleLeft = new JToggleButton("Links");
		toggleLeft.setPreferredSize(new Dimension(85, 25));
		ImageIcon iconTurnLeft = new ImageIcon();
		Image imgTurnLeft;
		iconTurnLeft = new ImageIcon("images/turn_left.png");
		imgTurnLeft = iconTurnLeft.getImage();
		imgTurnLeft = imgTurnLeft.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
		iconTurnLeft = new ImageIcon(imgTurnLeft);
		toggleLeft.setIcon(iconTurnLeft);
		
		JToggleButton toggleRight = new JToggleButton("Rechts");
		toggleRight.setPreferredSize(new Dimension(85, 25));
		ImageIcon iconTurnRight = new ImageIcon();
		Image imgTurnRight;
		iconTurnRight = new ImageIcon("images/turn_right.png");
		imgTurnRight = iconTurnRight.getImage();
		imgTurnRight = imgTurnRight.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
		iconTurnRight = new ImageIcon(imgTurnRight);
		toggleRight.setIcon(iconTurnRight);
		
		directionButtonGroup.add(toggleLeft);
		directionButtonGroup.add(toggleRight);
	
		directionButtonGroup.setSelected(toggleLeft.getModel(), true);
	
		directionPanel.add(toggleLeft);
		directionPanel.add(toggleRight);
		
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
		
		JButton switchLightButton = new JButton();
		switchLightButton.setBorder(BorderFactory.createEmptyBorder());
		switchLightButton.setContentAreaFilled(false);
		ImageIcon iconSwitch = new ImageIcon();
		Image imgSwitch;
		iconSwitch = new ImageIcon("images/switch_right_green.png");
		imgSwitch = iconSwitch.getImage();
		imgSwitch = imgSwitch.getScaledInstance(35, 35, java.awt.Image.SCALE_SMOOTH);
		iconSwitch = new ImageIcon(imgSwitch);
		switchLightButton.setIcon(iconSwitch);
		
		JLabel lightBulb = new JLabel();
		ImageIcon iconLight = new ImageIcon();
		Image imgLight;
		iconLight = new ImageIcon("images/lightbulb_on.png");
		imgLight = iconLight.getImage();
		imgLight = imgLight.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
		iconLight = new ImageIcon(imgLight);
		lightBulb.setIcon(iconLight);
		
		lightPanel.add(switchLightButton);
		lightPanel.add(lightBulb);

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

}