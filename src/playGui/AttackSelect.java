package playGui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import main.*;
import effects.Effect;
import effects.RecoverAction;

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
		if(player.staggered) {
			JToggleButton button = new JToggleButton("Recover");
			button.addActionListener(new SetSelected(new RecoverAction(player)));
			button.setAlignmentX(CENTER_ALIGNMENT);
			bg.add(button);
			add(button);
		}
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
			GUI.gui.targetSelect.setEnabled(!(effect instanceof RecoverAction));
			GUI.gui.targetSelect.setMultipleSelection(effect.multiattack || effect.selectiveArea);
			AttackButton.ready(GUI.gui.attackSelect);
		}
	}
}
