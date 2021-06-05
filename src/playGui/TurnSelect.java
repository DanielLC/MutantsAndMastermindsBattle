package playGui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import main.*;

public class TurnSelect extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ButtonGroup playerGroup;
	private Player currentPlayer;
	
	public TurnSelect(TurnOrder turnOrder) {//For testing only
		playerGroup = new ButtonGroup();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		for(Player p : turnOrder.players) {
			add(new TurnPane(this, p));
		}
	}
	
	private void move(TurnPane pane, boolean down) {
		Component[] components = getComponents();
		int i;
		for(i=0; i<components.length; ++i) {
			if(components[i] == pane)
				break;
		}
		int newPosition;
		if(down) {
			newPosition = i+1;
			if(newPosition >= components.length)
				newPosition = 0;
		} else {
			newPosition = i-1;
			if(newPosition < 0)
				newPosition = components.length-1;
		}
		add(pane, newPosition);
		revalidate();
	}
	
	private TurnPane getTurnPane(int n) {
		return (TurnPane)getComponent(n);
	}
	
	public void firstPlayer() {
		getTurnPane(0).playerButton.doClick();
	}
	
	public void nextPlayer() {
		int i;
		//First skip to the current player
		for(i = 0; i<getComponents().length; ++i) {
			if(getTurnPane(i).player == currentPlayer)
				break;
		}
		//Then go ahead one, and keep doing so if players are incapacitated
		do {
			i = (i+1)%getComponentCount();
		} while(getTurnPane(i).player.incapacitated);
		
		getTurnPane(i).playerButton.doClick();
	}
	
	public void updateIncapacitated() {
		for(int i = 0; i<getComponents().length; ++i) {
			TurnPane turnPane = getTurnPane(i);
			if(turnPane.player.incapacitated)
				turnPane.playerButton.setEnabled(false);
		}
	}
	
	private class TurnPane extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Player player;
		public JToggleButton playerButton;
		private TurnSelect parent;
		
		public TurnPane(TurnSelect parent, Player player) {
			this.parent = parent;
			this.player = player;
			setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
			playerButton = new JToggleButton(player.name);
			playerButton.addActionListener(new SetPlayer(player));
			add(playerButton);
			parent.playerGroup.add(playerButton);
			JPanel upDownPane = new JPanel();
			upDownPane.setLayout(new BoxLayout(upDownPane, BoxLayout.PAGE_AXIS));
			JButton up = new JButton("\u25B2");
			up.addActionListener(new Move(this, false));
			JButton down = new JButton("\u25BC");
			down.addActionListener(new Move(this, true));
			upDownPane.add(up);
			upDownPane.add(down);
			add(upDownPane);
		}
		
		public class SetPlayer implements ActionListener {
			private Player player;
			public SetPlayer(Player player) {
				this.player = player;
			}
			@Override
			public void actionPerformed(ActionEvent arg0) {
				currentPlayer = player;
				GUI.gui.setPlayer(player);
			}
		}
		
		private class Move implements ActionListener {
			private TurnPane parent;
			private boolean down;
			public Move(TurnPane parent, boolean down) {
				this.parent = parent;
				this.down = down;
			}

			@Override
			public void actionPerformed(ActionEvent arg0) {
				parent.parent.move(parent, down);
				((Component)arg0.getSource()).requestFocus();
			}
		}
	}
}
