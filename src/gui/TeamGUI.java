package gui;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import main.*;

public class TeamGUI extends JPanel implements ActionListener {
	public Team team;
	private Label header;
	
	public TeamGUI() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		team = new Team();

		CharacterGUI first = new CharacterGUI(this);
		team.members.add(first.character);
		add(first);
		Button addCharacter = new Button("+");
		addCharacter.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
		addCharacter.addActionListener(this);
		add(addCharacter);
		add(Box.createVerticalGlue());
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		CharacterGUI newChar = new CharacterGUI(this);
		add(newChar,getComponentCount()-2);
		team.members.add(newChar.character);
		revalidate();
	}
	
	public void removeCharacter(CharacterGUI c) {
		team.members.remove(c.character);
		remove(c);
		revalidate();
	}
	
	public void setHeader(String s) {
		if(header == null) {
			header = new Label();
			header.setAlignment(Label.CENTER);
			header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));
			add(header, 0);
		}
		header.setText(s);
		revalidate();
	}
}
