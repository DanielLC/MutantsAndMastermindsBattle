package playGui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;

import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;

public class AttackButton extends JToggleButton implements ActionListener {
	
	private static JToggleButton dummy;
	private static AttackButton singleton;
	private static HashSet<Object> notReady = new HashSet<Object>();
	private static boolean isPressed = true;
	
	public static AttackButton getSingleton() {
		if(singleton == null)
			singleton = new AttackButton();
		return singleton;
	}
	
	private AttackButton() {
		super("Attack");
		singleton = this;
		setAlignmentX(CENTER_ALIGNMENT);
		dummy = new JToggleButton();
		ButtonGroup g = new ButtonGroup();
		g.add(this);
		g.add(dummy);
		dummy.doClick();
		addActionListener(this);
	}
	
	public static void reset() {
		dummy.doClick();
		isPressed = false;
	}
	
	public static void notReady(Object o) {
		notReady.add(o);
		singleton.setEnabled(false);
		//System.out.println("Not ready");
	}
	
	public static void ready(Object o) {
		notReady.remove(o);
		if(notReady.isEmpty()) {
			singleton.setEnabled(true);
			//System.out.println("Ready");
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(isPressed)
			return;
		GUI.gui.attackSelect.selected.attack(GUI.gui.targetSelect.getTargets());
		GUI.gui.updateIncapacitated();
		isPressed = true;
	}
}
