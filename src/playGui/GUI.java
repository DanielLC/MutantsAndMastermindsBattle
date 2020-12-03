package playGui;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import fileio.PlayerReader;
import main.*;

public class GUI extends JFrame {
	public TurnSelect turnSelect;
	public ModifierSelect modifierSelect;
	public TargetSelect targetSelect;
	public ConditionView conditionView;
	public AttackSelect attackSelect;
	public static GUI gui;
	
	public void setPlayer(Player player) {
		//turnSelect.setPlayer(player);
		modifierSelect.setPlayer(player);
		targetSelect.clear();
		conditionView.setPlayer(player);
		attackSelect.setPlayer(player);
		AttackButton.reset();
	}
	
	public void updateIncapacitated() {
		turnSelect.updateIncapacitated();
		targetSelect.updateIncapacitated();
	}
	
	public GUI() {
		gui = this;
		
		setSize(1000,400);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

		//Team red = new Team(new Player("Red"), 5);
		//Team blue = new Team(new Player("Blue"), 4);
		Team red = PlayerReader.read("team1.txt");
		Team blue = PlayerReader.read("team2.txt");
		TurnOrder turnOrder = new TurnOrder(red, blue);

		add(Box.createHorizontalGlue());
		
		turnSelect = new TurnSelect(turnOrder);
		JScrollPane turnOrderScroll = new JScrollPane(turnSelect, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		turnOrderScroll.setBorder(BorderFactory.createEmptyBorder());
		//turnOrderScroll.add(new TurnSelect(turnOrder));
		add(turnOrderScroll);
		
		add(Box.createHorizontalGlue());
		add(new JSeparator(JSeparator.VERTICAL));
		
		modifierSelect = new ModifierSelect();
		add(modifierSelect);
		modifierSelect.allOutAttack.setBounds(-2, 5);
		
		add(Box.createHorizontalGlue());
		add(new JSeparator(JSeparator.VERTICAL));
		
		attackSelect = new AttackSelect();
		add(attackSelect);
		
		add(Box.createHorizontalGlue());
		add(new JSeparator(JSeparator.VERTICAL));
		
		targetSelect = new TargetSelect(red, blue);
		add(targetSelect);
		
		add(Box.createHorizontalGlue());
		add(new JSeparator(JSeparator.VERTICAL));
		
		conditionView = new ConditionView();
		conditionView.setPlayer(red.members.get(0));
		add(conditionView);
		
		//add(Box.createHorizontalGlue());
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//turnSelect.firstPlayer();  //Crashes on this line. Sometimes. With unknown source.
	}
	
	public static void main(String[] args) {
		new GUI();
	}
}
