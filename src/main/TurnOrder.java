package main;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
	public void shuffle() {	//TODO: Figure out a way to do this with initiative.
		Collections.shuffle(players);
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
