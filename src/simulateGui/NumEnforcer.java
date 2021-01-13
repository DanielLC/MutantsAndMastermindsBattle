package simulateGui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

public class NumEnforcer implements FocusListener {
	private JTextField field;
	public NumEnforcer(JTextField field) {
		this.field = field;
	}
	@Override
	public void focusGained(FocusEvent arg0) {
		// Does nothing
	}
	@Override
	public void focusLost(FocusEvent arg0) {
		String newText = field.getText().replaceAll("\\D", "");
		if(newText == "")
			newText = "0";
		field.setText(newText);
	}
	
}