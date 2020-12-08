package main;

import java.io.PrintStream;

import effects.Affliction;
import effects.Condition;
import effects.Effect;

public class Main {
	public static boolean verbose = false;
	public static playGui.Log log = null;
	
	public static void main(String[] args) {
		int battles = 100000;
		for(double i=0; i<=5; i+=1) {Player alice = new Player("Alice", 0);
			Player bob = new Player("Bob", -i);
			Effect e = alice.effects.get(0);
			e.multiattack = true;
			Team red = new Team(alice);
			Team blue = new Team(bob, 4);
			System.out.printf("%.2f:\t%s%n", i, getWinPercentString(red, blue, battles));
		}
		/*Player alice = new Player("Alice", 4);
		Player bob = new Player("Bob", 4);
		Effect e = alice.effects.get(0);
		e.multiattack = true;
		Team red = new Team(alice);
		Team blue = new Team(bob);
		System.out.println(getWinPercentString(red, blue, battles));*/
	}
	
	public static String getWinPercentString(Team red, Team blue, int battles) {
		return getWinPercentString(simulate(red, blue, battles), battles);
	}
	
	public static String getWinPercentString(int wins, int battles) {
		double percentage = wins*100.0/battles;
		double error = 1.96*percentage*(1-percentage/100)/Math.sqrt(battles);
		if(percentage == 0.0) {
			return "0%";
		} else if(percentage == 100) {
			return "100%";
		} else {
			int places = 1-(int)Math.floor(Math.log10(error));
			if(places > 0)
				return String.format("%." + places + "f%% ± %." + places + "f%%", percentage, error);
			else
				return String.format((int)(percentage+.5) + "% ± " + (int)(error+.5) + "%");
		}
	}
	
	public static int simulate(Team red, Team blue, int battles) {
		//System.out.println(red);
		//System.out.println(blue);
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
