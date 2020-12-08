package main;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Team {
	public ArrayList<Player> members;
	public Team enemyTeam;
	private int i;
	private int membersRemaining;
	
	public Team(Player...members) {
		this.members = new ArrayList<Player>(Arrays.asList(members));
		i = 0;
		membersRemaining = members.length;
		for(Player Player : members) {
			Player.team = this;
		}
	}
	
	public Team(Player base, int copies) {
		members = new ArrayList<Player>(copies);
		membersRemaining = copies;
		for(int i=0; i<copies; ++i) {
			members.add(new Player(base, this, i));
		}
	}
	
	public Team() {
		this.members = new ArrayList<Player>();
		i = 0;
		membersRemaining = 0;
	}
	
	public void add(Player member) {
		this.members.add(member);
		++membersRemaining;
		member.team = this;
	}
	
	public void shuffle() {
		Collections.shuffle(members);
	}
	
	public void reset() {
		for(Player Player : members) {
			Player.reset();
		}
		i = 0;
		membersRemaining = members.size();
	}
	public Player getTarget() {
		while(members.get(i).incapacitated) {
			i = i+1;
		}
		return members.get(i);
	}
	public Player[] getAllTargets() {	//Returns up to n targets. Useful for multi-attack and area attacks.
		Player[] players = new Player[membersRemaining];
		int k=i;
		for(int j=0; j<membersRemaining; ++j) {
			while(members.get(k).incapacitated) {
				++k;
			}
			players[j] = members.get(k);
			++k;
		}
		while(members.get(i).incapacitated) {
			++i;
		}
		return players;
	}
	public boolean loseMember() {
		--membersRemaining;
		return membersRemaining <= 0;
	}
	public String toString() {
		String s = "";
		for(Player c : members) {
			s += c + "\n";
		}
		return s;
	}
	public int getMembersRemaining() {
		return membersRemaining;
	}
}
