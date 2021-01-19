package main;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

//Simple convenience class
public class TurnOrder implements Iterable<Player> {
	public List<Player> players;
	public TurnOrder(Team...teams) {
		players = new ArrayList<Player>();
		for(Team team : teams) {
			players.addAll(team.members);
		}
	}
	public TurnOrder(Player...Players) {
		this.players = new ArrayList<Player>(Arrays.asList(Players));
	}
	public void shuffle() {
		//Collections.shuffle(players);
		for(Player player : players) {
			player.rollInitiative();
		}
		Collections.sort(players, new Comparator<Player>(){
			@Override
			public int compare(Player p1, Player p2) {
				int diffi = p2.initiativeRoll - p1.initiativeRoll;
				if(diffi != 0)
					return diffi;
				double difff = p2.dodge.val - p1.dodge.val;
				if(difff != 0)
					return (int)Math.signum(difff);
				//TODO: Agility and awareness, when I add those stats.
				return (int)Math.signum(p2.initiativeRollFractionalPart - p1.initiativeRollFractionalPart);
			}
		});
	}
	@Override
	public Iterator<Player> iterator() {
		return new Iterator<Player>(){
			int i=0;
			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public Player next() {
				while(players.get(i).incapacitated) {
					i = (i+1)%players.size();
				}
				Player c = players.get(i);
				i = (i+1)%players.size();
				return c;
			}
			
		};
	}
}
