package simulateGui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fileio.PlayerReader;
import main.*;

public class GUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Team team1;
	private Team team2;
	private JLabel results1;
	private JLabel results2;
	private static int MARGIN = 5;
	private JTextField textField;
	
	public GUI() {
		setSize(300,200);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		add(Box.createHorizontalStrut(MARGIN));
		add(mainPanel);
		add(Box.createHorizontalStrut(MARGIN));
		
		mainPanel.add(Box.createVerticalStrut(MARGIN));
		
		JButton refresh = new JButton("Refresh Teams");
		refresh.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(refresh);

		mainPanel.add(Box.createVerticalGlue());
		
		JPanel teams = new JPanel();
		teams.setLayout(new BoxLayout(teams, BoxLayout.X_AXIS));
		team1 = PlayerReader.read("team1.txt");
		team2 = PlayerReader.read("team2.txt");
		JPanel teamPane1 = getTeamPane(team1, LEFT_ALIGNMENT);
		teams.add(teamPane1);
		teams.add(Box.createHorizontalGlue());
		teams.add(new JLabel("vs"));
		teams.add(Box.createHorizontalGlue());
		JPanel teamPane2 = getTeamPane(team2, RIGHT_ALIGNMENT);
		teams.add(teamPane2);
		mainPanel.add(teams);
		
		refresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				team1 = PlayerReader.read("team1.txt");
				team2 = PlayerReader.read("team2.txt");
				refreshTeam(teamPane1, team1, LEFT_ALIGNMENT);
				refreshTeam(teamPane2, team2, RIGHT_ALIGNMENT);
				revalidate();
				repaint();
			}
		});
		
		mainPanel.add(Box.createVerticalGlue());
		
		JPanel resultsPanel = new JPanel();
		resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.X_AXIS));
		results1 = new JLabel();
		results2 = new JLabel();
		resultsPanel.add(results1);
		resultsPanel.add(Box.createHorizontalGlue());
		resultsPanel.add(results2);
		mainPanel.add(resultsPanel);
		
		mainPanel.add(Box.createVerticalGlue());
		
		JPanel simulatePanel = new JPanel();
		simulatePanel.setLayout(new BoxLayout(simulatePanel, BoxLayout.X_AXIS));
		textField = new JTextField("1000", 10);
		textField.setMaximumSize(new Dimension(150, textField.getPreferredSize().height));
		//textField.getDocument().addDocumentListener(new NumEnforcer(textField));
		textField.addFocusListener(new NumEnforcer(textField));
		simulatePanel.add(Box.createHorizontalGlue());
		simulatePanel.add(new JLabel("Battles:"));
		simulatePanel.add(Box.createHorizontalStrut(MARGIN));
		simulatePanel.add(textField);
		simulatePanel.add(Box.createHorizontalGlue());
		JButton simulate = new JButton("Simulate");
		simulatePanel.add(simulate);
		simulate.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int battles = Integer.parseInt(textField.getText());
				int wins = Main.simulate(team1, team2, battles);
				results1.setText(Main.getWinPercentString(wins, battles));
				results2.setText(Main.getWinPercentString(battles-wins, battles));
			}
		});
		simulatePanel.add(Box.createHorizontalGlue());
		mainPanel.add(simulatePanel);
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private JPanel getTeamPane(Team team, float alignment) {
		JPanel teamPane = new JPanel();
		teamPane.setLayout(new BoxLayout(teamPane, BoxLayout.Y_AXIS));
		refreshTeam(teamPane, team, alignment);
		return teamPane;
	}
	
	private void refreshTeam(JPanel teamPane, Team team, float alignment) {
		teamPane.removeAll();
		for(Player p : team.members) {
			JLabel label = new JLabel(p.name);
			label.setAlignmentX(alignment);
			teamPane.add(label);
		}
	}
	
	public static void main(String[] args) {
		new GUI();
	}

}
