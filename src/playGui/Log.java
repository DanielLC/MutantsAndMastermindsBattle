package playGui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Log extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea textArea;
	private boolean first = true;
	public Log() {
		setLayout(new BorderLayout());		//The layout doesn't matter, as long as it's not a flow layout.
		textArea = new JTextArea();
		textArea.setEnabled(false);
		textArea.setDisabledTextColor(Color.BLACK);
		JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public void log(String s) {
		if(!first) {
			textArea.append("\n");
		}
		first = false;
		textArea.append(s);
	}
}
