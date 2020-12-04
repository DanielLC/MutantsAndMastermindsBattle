package main;

import java.io.PrintStream;

import effects.Affliction;
import effects.Condition;

public class Main {
	public static boolean verbose = false;
	public static playGui.Log log = null;
	
	public static void main(String[] args) {
		int battles = 10000;
		/*for(int i=0; i<=10; ++i) {
			Player alice = new Player("Alice", i);
			alice.selectiveArea = true;
			Player bob = new Player("Bob", i);
			bob.multiattack = true;
			Team red = new Team(alice);
			Team blue = new Team(bob);
			System.out.println(i + "\t" + simulate(red, blue, battles)*100.0/battles);
		}*/
		/*Player alice = new Player("Alice");
		alice.multiattack = true;
		Player bob = new Player("Bob",-3.8);
		Team red = new Team(alice);
		Team blue = new Team(bob, 4);
		System.out.println(simulate(red, blue, battles)*100.0/battles);*/
		double powerLevel = 10;
		Player alice = new Player("Alice", powerLevel);
		//alice.perception = true;
		//alice.affliction = new Affliction(Condition.IMPAIRED, Condition.DISABLED, Condition.INCAPACITATED, powerLevel+2, "will");
		new Affliction(alice, Condition.VULNERABLE, Condition.DEFENSELESS, Condition.INCAPACITATED, powerLevel, powerLevel+2.6, "will");
		//alice.affliction.cumulative = true;
		Team red = new Team(new Player("Red", powerLevel), 0);
		red.add(alice);
		Player blueMan = new Player("Blue", powerLevel);
		Team blue = new Team(blueMan, 1);
		
		double percentage = simulate(red, blue, battles)*100.0/battles;
		double error = 1.96*percentage*(1-percentage/100)/Math.sqrt(battles);
		if(percentage == 0.0) {
			System.out.println("0%");
		} else if(percentage == 100) {
			System.out.println("100%");
		} else {
			int places = 1-(int)Math.floor(Math.log10(error));
			if(places > 0)
				System.out.printf("%." + places + "f%% ± %." + places + "f%%%n", percentage, error);
			else
				System.out.println((int)(percentage+.5) + "% ± " + (int)(error+.5) + "%");
		}
	}
	
	public static int simulate(Team red, Team blue, int battles) {
		red.enemyTeam = blue;
		blue.enemyTeam = red;
		int redWins = 0;
		//int turns = 0;
		TurnOrder order = new TurnOrder(red, blue);
		for(int i=0; i<battles; ++i) {
			order.shuffle();
			for(Player player : order) {
				//++turns;
				player.takeTurn();
				if(player.team.enemyTeam.getMembersRemaining() == 0) {
					if(player.team == red)
						++redWins;
					break;
				}
			}
			red.reset();
			blue.reset();
		}
		//System.out.println("Average turns: " + turns*1.0/battles);
		return redWins;
	}

	public static void print(String s) {
		if(verbose) {
			if(log == null)
				System.out.println(s);
			else
				log.log(s);
		}
	}
}
