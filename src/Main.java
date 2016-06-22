import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// GUI im Event-Dispatch-Thread erzeugen.
	      SwingUtilities.invokeLater (new Runnable()
	      {
	         public void run()
	         {
	            createGUI();
	         }
	      });
	}

	private static void createGUI()
	   {
	      //Hauptfenster erzeugen.
	      MyFrame frame = new MyFrame();
	      //Titel für das Hauptfenster setzen.
	      frame.setTitle ("Loco Controllo");

	      JScrollPane scroll = new JScrollPane();
	      scroll.setBorder (BorderFactory.createTitledBorder ("Scroll"));
	      scroll.setPreferredSize (new Dimension (300, 50));
	      JPanel trains = new JPanel();
	      
	      trains.setBorder (BorderFactory.createTitledBorder ("Deine Züge"));
	      trains.setPreferredSize (new Dimension (300, 50));
	      trains.setLayout (new BoxLayout (trains, BoxLayout.Y_AXIS));
	      
	      
	      // Erstelle internes Panel
	      JPanel panel = new JPanel();
//	      panel.setLayout (new BoxLayout (panel, BoxLayout.X_AXIS));
	      panel.setBorder (BorderFactory.createTitledBorder ("Zug1"));
	      panel.setPreferredSize (new Dimension (50, 400));
	      panel.setMinimumSize(new Dimension (300, 400));
	      panel.setMaximumSize(new Dimension (300, 400));
	      scroll.add(panel);
	      
	   // Erstelle internes Panel
	      JPanel panel2 = new JPanel();
//	      panel.setLayout (new BoxLayout (panel, BoxLayout.X_AXIS));
	      panel2.setBorder (BorderFactory.createTitledBorder ("Zug1"));
	      panel2.setPreferredSize (new Dimension (50, 400));
	      panel2.setMinimumSize(new Dimension (300, 400));
	      panel2.setMaximumSize(new Dimension (300, 400));
	      scroll.add(panel2);
	      
	      scroll.add(trains);
	      
	      frame.add(scroll,BorderLayout.WEST);
	      
	      ArrayList<JPanel> panels = frame.getPanels();
	      
	      for (JPanel addPanel : panels) {
			//frame.add(addPanel);
	      }
	      
	      //Definieren, was beim Schließen des Fensters geschehen soll.
	      frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE );
	      
	      //Größe setzen.
	      frame.setSize (1050, 590);
	      Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	      frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
	      //Hauptfenster anzeigen.
	      frame.setVisible (true);
	   }
}
