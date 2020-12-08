package gui;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import main.Player;

public class CharacterGUI extends JPanel implements ActionListener {
	public Player character;
	private TextField activeDefense;
	private TextField toughness;
	private TextField attack;
	private TextField damage;
	private TeamGUI team;
	
	public CharacterGUI(TeamGUI team) {
		this.team = team;
		setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		character = new Player("", 0);
		character.team = team.team;
		
		activeDefense = new TextField("0");
		toughness = new TextField("0");
		attack = new TextField("0");
		damage = new TextField("0");
		activeDefense.addFocusListener(new NumEnforcer());
		toughness.addFocusListener(new NumEnforcer());
		attack.addFocusListener(new NumEnforcer());
		damage.addFocusListener(new NumEnforcer());
		add(new Label("Acitve Defense:"));
		add(activeDefense);
		add(new Label("Toughness:"));
		add(toughness);
		add(new Label("Attack:"));
		add(attack);
		add(new Label("Damage:"));
		add(damage);
		Button remove = new Button("-");
		remove.addActionListener(this);
		add(remove);
	}
	
	private class NumEnforcer implements FocusListener {
		private double value;
		@Override
		public void focusGained(FocusEvent arg0) {
			// Does nothing
		}
		@Override
		public void focusLost(FocusEvent arg0) {
			TextField field = (TextField) arg0.getSource();
			try {
				value = Double.parseDouble(field.getText());
			} catch(NumberFormatException E) {
				//Do nothing. The importan thing is it leaves the previous value.
			}
			if(value % 1 == 0) {
				field.setText(Integer.toString((int)value));
			} else {
				field.setText(Double.toString(value));
			}
			
			if(field == activeDefense) {
				character.dodge = value;
				character.parry = value;
			} else if(field == toughness) {
				character.toughness = value;
			} else if(field == attack) {
				character.effects.get(0).attack = value;
			} else if(field == damage) {
				character.effects.get(0).effectRank = value;
			}
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		team.removeCharacter(this);
	}
}
