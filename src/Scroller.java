import java.awt.*;

import javax.swing.*;

public class Scroller extends JFrame {

	public Scroller() throws HeadlessException {
		final JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createLineBorder(Color.red));
		panel.setBorder(BorderFactory.createTitledBorder("Deine Züge"));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//		panel.setPreferredSize(new Dimension(800, 600));

		// Erstelle internes Panel
		JPanel panel1 = new JPanel();
		// panel.setLayout (new BoxLayout (panel, BoxLayout.X_AXIS));
		panel1.setBorder(BorderFactory.createTitledBorder("Zug1"));
		panel1.setPreferredSize(new Dimension(50, 400));
		panel1.setMinimumSize(new Dimension(300, 400));
		panel1.setMaximumSize(new Dimension(300, 400));
		panel.add(panel1);

		// Erstelle internes Panel
		JPanel panel2 = new JPanel();
		// panel.setLayout (new BoxLayout (panel, BoxLayout.X_AXIS));
		panel2.setBorder(BorderFactory.createTitledBorder("Zug2"));
		panel2.setPreferredSize(new Dimension(50, 400));
		panel2.setMinimumSize(new Dimension(300, 400));
		panel2.setMaximumSize(new Dimension(300, 400));
		panel.add(panel2);

		// Erstelle internes Panel
		JPanel panel3 = new JPanel();
		// panel.setLayout (new BoxLayout (panel, BoxLayout.X_AXIS));
		panel3.setBorder(BorderFactory.createTitledBorder("Zug3"));
		panel3.setPreferredSize(new Dimension(50, 400));
		panel3.setMinimumSize(new Dimension(300, 400));
		panel3.setMaximumSize(new Dimension(300, 400));
		panel.add(panel3);
		
		final JScrollPane scroll = new JScrollPane(panel);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		add(scroll, BorderLayout.CENTER);
		setSize(1600, 900);
		setVisible(true);
	}

	public static void main(final String[] args) throws Exception {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Scroller().setVisible(true);
			}
		});
	}
}