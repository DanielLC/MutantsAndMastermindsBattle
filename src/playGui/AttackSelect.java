package playGui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import main.*;
import effects.Effect;

public class AttackSelect extends JPanel {
	public Effect selected = null;
	private ButtonGroup bg = new ButtonGroup();
	
	public AttackSelect() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	
	public void setPlayer(Player player) {
		AttackButton.notReady(this);
		removeAll();
		bg.clearSelection();
		for(Effect effect : player.effects) {
			JToggleButton button = new JToggleButton(effect.name);
			button.addActionListener(new SetSelected(effect));
			button.setAlignmentX(CENTER_ALIGNMENT);
			bg.add(button);
			add(button);
		}
	}
	
	private class SetSelected implements ActionListener {
		Effect effect;
		public SetSelected(Effect effect) {
			this.effect = effect;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			selected = effect;
			GUI.gui.targetSelect.setMultipleSelection(effect.multiattack || effect.selectiveArea);
			AttackButton.ready(GUI.gui.attackSelect);
		}
	}
}
