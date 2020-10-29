package main;

public class Main {
	public static void main(String[] args) {
		//Character.verbose = true;
		int battles = 100000;
		for(int i=0; i<=10; ++i) {
			Character alice = new Character("Alice", i);
			alice.selectiveArea = true;
			Character bob = new Character("Bob", i);
			bob.multiattack = true;
			Team red = new Team(alice);
			Team blue = new Team(bob);
			System.out.println(i + "\t" + simulate(red, blue, battles)*100.0/battles);
		}
		/*Character alice = new Character("Alice");
		alice.multiattack = true;
		Character bob = new Character("Bob",-3.8);
		Team red = new Team(alice);
		Team blue = new Team(bob, 4);
		System.out.println(simulate(red, blue, battles)*100.0/battles);*/
		/*Character alice = new Character("Alice");
		Character bob = new Character("Bob");
		bob.minion = true;
		Character charlie = new Character("Charlie", 1.4);
		Team red = new Team(alice, bob);
		Team blue = new Team(charlie);
		System.out.println(simulate(red, blue, battles)*100.0/battles);*/
	}
	
	public static int simulate(Team red, Team blue, int battles) {
		red.enemyTeam = blue;
		blue.enemyTeam = red;
		int redWins = 0;
		int turns = 0;
		TurnOrder order = new TurnOrder(red, blue);
		for(int i=0; i<battles; ++i) {
			order.shuffle();
			for(Character character : order) {
				++turns;
				if(character.attack(character.team.enemyTeam)) {
					if(character.team == red)
						++redWins;
					break;
				}
			}
			red.reset();
			blue.reset();
		}
		return redWins;
	}
}
