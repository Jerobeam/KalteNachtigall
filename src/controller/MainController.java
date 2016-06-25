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
	private JDialog newTrainDialog;
	private JLabel trainImageLabel;
	private JTextField newTrainNameTextField;
	private JTextField newTrainModelDescTextField;
	private String imagePath;

	public MainController(JFrame mainFrame, JPanel trainsPanel, TrainCollection trainCollection) {
		this.mainFrame = mainFrame;
		this.trainsPanel = trainsPanel;
		this.trainCollection = trainCollection;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("addTrain")) {
			// Erstelle neuen Dialog
			this.newTrainDialog = new JDialog(this.mainFrame, "Neuer Zug", true);
			newTrainDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			// Setze Layout für Dialog
			newTrainDialog.setLayout(new GridBagLayout());
			GridBagConstraints c;
			// Setze Ränder zwischen einzelnen Komponenten
			Insets set = new Insets(5, 5, 5, 5);

			// Setze Layout Einstellungen für 1.Label
			c = new GridBagConstraints();
			c.insets = set;
			c.gridx = 0;
			c.gridy = 0;
			c.anchor = GridBagConstraints.LINE_START;
			newTrainDialog.add(new JLabel("Zugname: "), c);
			// Setze Layout Einstellungen für 2.Label
			c.gridy = 1;
			newTrainDialog.add(new JLabel("Modell (optional): "), c);
			// Setze Layout Einstellungen für 3.Label
			c.gridy = 3;
			newTrainDialog.add(new JLabel("Bild (optional): "), c);

			// Setze Layout Einstellungen für 1. Textfeld
			c = new GridBagConstraints();
			c.insets = set;
			c.gridx = 1;
			c.gridy = 0;
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.LINE_START;
			newTrainNameTextField = new JTextField();
			newTrainDialog.add(newTrainNameTextField, c);
			// Setze Layout Einstellungen für 2. Textfeld
			c.gridy = 1;
			
			newTrainModelDescTextField = new JTextField();
			newTrainDialog.add(newTrainModelDescTextField, c);
			// Setze Layout Einstellungen für "Durchsuchen"-Button
			c.gridx = 1;
			c.gridy = 4;
			c.gridwidth = 1;
			JButton searchButton = new JButton("Durchsuchen");
			searchButton.setActionCommand("searchForImage");
			searchButton.addActionListener(this);
			newTrainDialog.add(searchButton, c);

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
			newTrainDialog.add(trainImageLabel, c);

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
			JButton cancelCreateButton = new JButton("Abbrechen");
			cancelCreateButton.setActionCommand("cancelCreate");
			cancelCreateButton.addActionListener(this);
			JButton createButton = new JButton("Erstellen");
			createButton.setActionCommand("createTrain");
			createButton.addActionListener(this);
			// Setze "Erstelle"-Button als Default Button, damit er durch Enter-Drücken ausgelöst wird und blaum umrandet ist
			newTrainDialog.getRootPane().setDefaultButton(createButton);
			
			// Füge Buttons dem Hilfspanel hinzu
			panelAction.add(cancelCreateButton);
			panelAction.add(createButton);
			// Füge Hilfspanel zum Dialog dazu
			newTrainDialog.add(panelAction, c);

			newTrainDialog.pack();
			// newTrainDialog.setSize(400,300);
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			newTrainDialog.setLocation(dim.width / 2 - newTrainDialog.getSize().width / 2,
					dim.height / 2 - newTrainDialog.getSize().height / 2);
			newTrainDialog.setVisible(true);

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

			if (fileChooser.showOpenDialog(newTrainDialog) == JFileChooser.APPROVE_OPTION) {
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
				newTrainDialog.pack();
			}
		}else if (e.getActionCommand().equals("cancelCreate")){
			newTrainDialog.dispose();
		}else if (e.getActionCommand().equals("createTrain")){
			String trainName = newTrainNameTextField.getText();
			String trainModelDesc = newTrainModelDescTextField.getText();
			
			// Überprüfen, ob Zugname eingegeben wurde
			if(newTrainNameTextField.getText().equals("")){
				JOptionPane.showMessageDialog (newTrainDialog, "Bitte geben Sie dem Zug einen Namen", 
			            "Zugname leer", JOptionPane.WARNING_MESSAGE);
				return;
			}
			// Überprüfen, ob Zug bereits existiert
			if(trainCollection.trainIsAlreadyExisting(trainName)){
				JOptionPane.showMessageDialog (newTrainDialog, "Zug existiert bereits, bitte geben Sie einen anderen Namen ein", 
			            "Zug existiert bereits", JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Wenn beide Fälle nicht eintreten, füge Zug der trainCollection hinzu
			Train newTrain = new Train(trainName, trainModelDesc);
			newTrain.setImagePath(imagePath);
			System.out.println(imagePath);
			this.trainCollection.addTrain(newTrain);
			System.out.println("Train added: " + newTrain.toString());
			System.out.println(trainCollection.getTrainByName(trainName).toString());
		}
	}

}