import java.awt.*;
import java.util.*;
import javax.swing.*;

public class MyFrame extends JFrame {
	
	private ArrayList<JPanel> panels = new ArrayList<JPanel>();
	
	public void addPanel(JPanel panel){
		this.panels.add(panel);
	}
	
	public ArrayList<JPanel> getPanels(){
		return this.panels;
	}
	
}


