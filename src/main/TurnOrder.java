package main;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

//Simple convenience class
public class TurnOrder implements Iterable<Character> {
	private List<Character> characters;
	public TurnOrder(Team...teams) {
		characters = new ArrayList<Character>();
		for(Team team : teams) {
			characters.addAll(team.members);
		}
	}
	public TurnOrder(Character...characters) {
		this.characters = new ArrayList<Character>(Arrays.asList(characters));
	}
	public void shuffle() {
		Collections.shuffle(characters);
	}
	@Override
	public Iterator<Character> iterator() {
		return new Iterator<Character>(){
			int i=0;
			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public Character next() {
				while(characters.get(i).incapacitated) {
					i = (i+1)%characters.size();
				}
				Character c = characters.get(i);
				i = (i+1)%characters.size();
				return c;
			}
			
		};
	}
}
