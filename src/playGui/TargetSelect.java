package playGui;


import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import main.Player;
import main.Team;

public class TargetSelect extends JPanel implements ItemListener {
	private ArrayList<JToggleButton> buttons = new ArrayList<>();
	private ArrayList<Player> players = new ArrayList<>();
	private ButtonGroup group;
	public TargetSelect(Team red, Team blue) {
		group = new ButtonGroup();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		//JLabel selectTarget = new JLabel("Select target(s):");
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
		//top.add(Box.createHorizontalGlue());
		top.add(new JLabel("Select target(s):"));
		//top.add(Box.createHorizontalGlue());
		add(top);

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		//bottomPanel.setBorder(BorderFactory.createBevelBorder(1));
		add(bottomPanel);
		
		JPanel redPanel = new JPanel();
		redPanel.setLayout(new BoxLayout(redPanel, BoxLayout.Y_AXIS));
		for(Player p : red.getAllTargets()) {
			JToggleButton b = new JToggleButton(p.name);
			b.addItemListener(this);
			b.setAlignmentX(Component.RIGHT_ALIGNMENT);
			buttons.add(b);
			players.add(p);
			redPanel.add(b);
		}
		bottomPanel.add(redPanel);

		JPanel bluePanel = new JPanel();
		bluePanel.setLayout(new BoxLayout(bluePanel, BoxLayout.Y_AXIS));
		for(Player p : blue.getAllTargets()) {
			JToggleButton b = new JToggleButton(p.name);
			b.setAlignmentX(Component.LEFT_ALIGNMENT);
			b.addItemListener(this);
			buttons.add(b);
			players.add(p);
			bluePanel.add(b);
		}
		bottomPanel.add(bluePanel);
		
		add(AttackButton.getSingleton());
		
		setMultipleSelection(false);
	}
	
	public List<Player> getTargets() {
		ArrayList<Player> targets = new ArrayList<>();
		for(int i=0; i<buttons.size(); ++i) {
			if(buttons.get(i).isSelected()) {
				targets.add(players.get(i));
			}
		}
		return targets;
	}
	
	public void clear() {
		if(group.getButtonCount() > 0)
			group.clearSelection();
		else
			for(JToggleButton b : buttons) {
				b.setSelected(false);
			}
		AttackButton.notReady(this);
	}
	
	public void setMultipleSelection(boolean bool) {
		if(bool) {
			if(group.getButtonCount() > 0) {
				for(JToggleButton b : buttons) {
					group.remove(b);
				}
			}
		} else {
			if(group.getButtonCount() == 0) {	//If there's already buttons in the group, then it's already set for single target.
				for(JToggleButton b : buttons) {
					group.add(b);
				}
			}
		}
	}
	
	public void setEnabled(boolean enabled) {
		for(int i=0; i<buttons.size(); ++i) {
			buttons.get(i).setEnabled(enabled && !players.get(i).incapacitated);
		}
		if(enabled) {
			if(getTargets().size() == 0) {
				AttackButton.notReady(this);
			}
		} else {
			AttackButton.ready(this);
		}
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		if(arg0.getStateChange() == ItemEvent.SELECTED) {
			AttackButton.ready(this);
		} else {
			if(getTargets().size() == 0) {
				AttackButton.notReady(this);
			}
		}
	}
	
	public void updateIncapacitated() {
		for(int i=0; i<players.size(); ++i) {
			if(players.get(i).incapacitated) {
				buttons.get(i).setEnabled(false);
			}
		}
	}
}
