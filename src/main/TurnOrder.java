package main;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

//Simple convenience class
public class TurnOrder implements Iterable<Player> {
	private List<Player> Players;
	public TurnOrder(Team...teams) {
		Players = new ArrayList<Player>();
		for(Team team : teams) {
			Players.addAll(team.members);
		}
	}
	public TurnOrder(Player...Players) {
		this.Players = new ArrayList<Player>(Arrays.asList(Players));
	}
	public void shuffle() {
		Collections.shuffle(Players);
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
				while(Players.get(i).incapacitated) {
					i = (i+1)%Players.size();
				}
				Player c = Players.get(i);
				i = (i+1)%Players.size();
				return c;
			}
			
		};
	}
}
