package main;

import java.util.Scanner;

public class Play {
	static Scanner s = new Scanner(System.in);
	
	public static void main(String[] args) {
		double powerLevel = 10;
		Team red = new Team(new Player("Red", powerLevel), 2);
		Team blue = new Team(new Player("Blue", powerLevel), 2);
		red.enemyTeam = blue;
		blue.enemyTeam = red;
		TurnOrder order = new TurnOrder(red, blue);
		order.shuffle();
		
		for(Player p : order) {
			takeTurn(p);
			if(p.team.enemyTeam.getMembersRemaining() <= 0);
				System.out.println(p + " Wins!");
		}
	}
	
	public static void takeTurn(Player p) {
		while(true) {
			System.out.println("It is " + p.name + "'s turn.");
			System.out.println("Who do you want to attack?");
			int i = 1;
			for(Player q : p.team.enemyTeam.members) {
				if(!q.incapacitated) {
					System.out.println(i + ":\t" + q.name);
					++i;
				}
			}
			//System.out.println(i + ":\tAttack ally");
			//++i;
			System.out.println(i + ":\tEnd turn");
			int choice = s.nextInt();
			i = 1;
			for(Player q : p.team.enemyTeam.members) {
				if(!q.incapacitated) {
					if(i == choice) {
						p.effects.get(0).attack(q, 0, 0, false);
						return;
					}
					++i;
				}
			}
			if(i == choice) {
				return;
			}
			System.out.println(choice + " was not an option. Please choose again.");
		}
	}
}
