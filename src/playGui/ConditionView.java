package playGui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;

import effects.AfflictionInstance;
import main.*;

public class ConditionView extends JPanel {
	private Player player;
	private JTextArea conditionSummary;
	private JToggleButton dummyButton;
	private JToggleButton resistButton;
	private static int MARGIN = 5;
	private JButton endTurn;
	
	public ConditionView() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createVerticalStrut(MARGIN));
		ButtonGroup buttonGroup = new ButtonGroup();
		dummyButton = new JToggleButton();
		buttonGroup.add(dummyButton);
		resistButton = new JToggleButton("Resist Conditions");
		resistButton.setAlignmentX(CENTER_ALIGNMENT);
		resistButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				for(AfflictionInstance a : player.afflictions.values()) {
					a.passiveResist();
				}
			}
		});
		add(resistButton);
		buttonGroup.add(resistButton);
		add(Box.createVerticalStrut(MARGIN));
		conditionSummary = new JTextArea();
		conditionSummary.setBackground(getBackground());
		conditionSummary.setMaximumSize(new Dimension(100, Integer.MAX_VALUE));
		
		add(conditionSummary);
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
	
	public void setPlayer(Player player) {
		this.player = player;
		conditionSummary.setText(player.getConditionString());
		dummyButton.doClick();
	}
}
