package playGui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;

import effects.AfflictionInstance;
import effects.Condition;
import main.*;

public class ConditionView extends JPanel {
	private JButton endTurn;
	
	public ConditionView() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		endTurn = new JButton("End Turn");
		endTurn.setAlignmentX(CENTER_ALIGNMENT);
		endTurn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				GUI.gui.turnSelect.nextPlayer();
			}
		});
		add(endTurn);
		add(Box.createVerticalGlue());
	}
	
	private String getRegenText(Player player) {
		int turnsRemaining = player.regenTurnsRemaining();
		if(turnsRemaining > 0)
			return "Await regen (" + turnsRemaining + " turns remaining)";
		else
			return "Regenerate";
	}
	
	public void setPlayer(Player player) {
		removeAll();
		if(player.regenRank > 0 && (player.bruises > 0 || player.staggered)) {
			JToggleButton regen = new JToggleButton(getRegenText(player));
			regen.setAlignmentX(CENTER_ALIGNMENT);
			regen.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED) {
						player.regen();
						regen.setText(getRegenText(player));
					}
				}
			});
			add(regen);
		}
		for(AfflictionInstance afflictionInstance : player.afflictions.values()) {
			if(afflictionInstance.currentCondition == Condition.NULL)
				break;
			JToggleButton resist = new JToggleButton("Resist " + afflictionInstance.affliction.name);
			resist.setAlignmentX(CENTER_ALIGNMENT);
			resist.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED) {
						afflictionInstance.passiveResist();
						if(afflictionInstance.currentCondition == Condition.NULL) {
							GUI.gui.conditionView.remove(resist);
							GUI.gui.conditionView.revalidate();
						}
					}
				}
			});
			add(resist);
		}
		add(endTurn);
	}
}
