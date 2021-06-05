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
import javax.swing.JFrame;
import javax.swing.JPanel;

import main.Main;

public class GUI extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TeamGUI team1;
	private TeamGUI team2;
	private TextField battles;
	public GUI() {
		setSize(1000,500);
		
		JPanel options = new JPanel();
		battles = new TextField("100000");
		battles.addFocusListener(new NumEnforcer());
		Button begin = new Button("Begin");
		begin.addActionListener(this);
		
		options.add(new Label("Battles:"));
		options.add(battles);
		options.add(begin);
		options.setMaximumSize(new Dimension(Integer.MAX_VALUE, options.getPreferredSize().height));
		
		JPanel teams = new JPanel();
		teams.setLayout(new BoxLayout(teams, BoxLayout.LINE_AXIS));
		team1 = new TeamGUI();
		team2 = new TeamGUI();
		teams.add(team1);
		teams.add(team2);
		
		setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		add(options);
		add(teams);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		new GUI();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		int n = Integer.parseInt(battles.getText());
		int team1Wins = Main.simulate(team1.team, team2.team, n);
		team1.setHeader("Wins: " + Main.getWinPercentString(team1Wins, n));
		team2.setHeader("Wins: " + Main.getWinPercentString(n-team1Wins, n));
	}
	
	private class NumEnforcer implements FocusListener {
		private int value;
		@Override
		public void focusGained(FocusEvent arg0) {
			// Does nothing
		}
		@Override
		public void focusLost(FocusEvent arg0) {
			TextField field = (TextField) arg0.getSource();
			try {
				value = Integer.parseInt(field.getText());
			} catch(NumberFormatException E) {
				//Do nothing. The importan thing is it leaves the previous value.
			}
			field.setText(Integer.toString((int)value));
		}
	}
}
