package playGui;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import fileio.PlayerReader;
import main.*;

public class GUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public TurnSelect turnSelect;
	public ModifierSelect modifierSelect;
	public TargetSelect targetSelect;
	public ConditionView conditionView;
	public AttackSelect attackSelect;
	public static GUI gui;
	public Player player;
	
	public void setPlayer(Player player) {
		this.player = player;
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
		
		setSize(1000,800);
		setLayout(new GridLayout(2, 1));
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));

		//Team red = new Team(new Player("Red"), 5);
		//Team blue = new Team(new Player("Blue"), 4);
		Team red = PlayerReader.read("team1.txt");
		Team blue = PlayerReader.read("team2.txt");
		TurnOrder turnOrder = new TurnOrder(red, blue);

		top.add(Box.createHorizontalGlue());
		
		turnSelect = new TurnSelect(turnOrder);
		JScrollPane turnOrderScroll = new JScrollPane(turnSelect, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		turnOrderScroll.setBorder(BorderFactory.createEmptyBorder());
		//turnOrderScroll.add(new TurnSelect(turnOrder));
		top.add(turnOrderScroll);
		
		top.add(Box.createHorizontalGlue());
		top.add(new JSeparator(JSeparator.VERTICAL));
		
		modifierSelect = new ModifierSelect();
		top.add(modifierSelect);
		
		top.add(Box.createHorizontalGlue());
		top.add(new JSeparator(JSeparator.VERTICAL));
		
		attackSelect = new AttackSelect();
		top.add(attackSelect);
		
		top.add(Box.createHorizontalGlue());
		top.add(new JSeparator(JSeparator.VERTICAL));
		
		targetSelect = new TargetSelect(red, blue);
		top.add(targetSelect);
		
		top.add(Box.createHorizontalGlue());
		top.add(new JSeparator(JSeparator.VERTICAL));
		
		conditionView = new ConditionView();
		conditionView.setPlayer(red.members.get(0));
		top.add(conditionView);
		
		add(top);
		
		Log log = new Log();
		add(log);
		Main.log = log;
		Main.verbose = true;
		
		//add(Box.createHorizontalGlue());
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//turnSelect.firstPlayer();  //Crashes on this line. Sometimes. With unknown source. And even when it does work it doesn't seem to properly set the player in the other functions.
	}
	
	public static void main(String[] args) {
		Main.verbose = true;
		new GUI();
	}
}
