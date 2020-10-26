package main;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Team {
	public ArrayList<Character> members;
	public Team enemyTeam;
	private int i;
	private int membersRemaining;
	public Team(Character...members) {
		this.members = new ArrayList<Character>(Arrays.asList(members));
		i = 0;
		membersRemaining = members.length;
		for(Character character : members) {
			character.team = this;
		}
	}
	public Team() {
		this.members = new ArrayList<Character>();
		i = 0;
		membersRemaining = 0;
	}
	public void shuffle() {
		Collections.shuffle(members);
	}
	public void sort() {
		Collections.sort(members);
	}
	public void reset() {
		for(Character character : members) {
			character.reset();
		}
		i = 0;
		membersRemaining = members.size();
	}
	public Character getTarget() {
		while(members.get(i).incapacitated) {
			i = (i+1)%members.size();
		}
		return members.get(i);
	}
	public boolean loseMember() {
		--membersRemaining;
		return membersRemaining <= 0;
	}
	public String toString() {
		String s = "";
		for(Character c : members) {
			s += c + "\n";
		}
		return s;
	}
}
