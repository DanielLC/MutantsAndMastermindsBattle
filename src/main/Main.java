package main;

public class Main {
	public static void main(String[] args) {
		//Character.verbose = true;
		int battles = 1000000;
		/*for(double i=-4; i<=-3; i += 0.1) {
			Character alice = new Character("Alice");
			Character bob = new Character("Bob", i);
			Character charlie = new Character("Charlie", 1);
			Team red = new Team(alice, bob);
			Team blue = new Team(charlie);
			System.out.println(i + ":\t" + simulate2(red, blue, battles)*100.0/battles);
		}*/
		Character alice = new Character("Alice", 3.75);
		alice.minion = true;
		Character bob = new Character("Bob");
		System.out.println(simulate(alice,bob,battles)*100.0/battles);
		/*Character alice = new Character("Alice");
		Character bob = new Character("Bob");
		Character charlie = new Character("Charlie", 2.31);
		Team red = new Team(alice, bob);
		Team blue = new Team(charlie);
		System.out.println(simulate2(red, blue, battles)*100.0/battles);*/
		/*Character alice = new Character("Alice");
		Character bob = new Character("Bob");
		bob.minion = true;
		Character charlie = new Character("Charlie", 1.4);
		Team red = new Team(alice, bob);
		Team blue = new Team(charlie);
		System.out.println(simulate2(red, blue, battles)*100.0/battles);*/
	}
	
	public static int simulate(Character alice, Character bob, int battles) {
		int aliceWins = 0;
		int turns = 0;
		for(int i=0; i<battles/2; ++i) {
			while(true) {
				++turns;
				if(alice.attack(bob)) {
					++aliceWins;
					break;
				}
				++turns;
				if(bob.attack(alice)) {
					break;
				}
			}
			alice.reset();
			bob.reset();
		}
		for(int i=battles/2; i<battles; ++i) {
			while(true) {
				if(bob.attack(alice)) {
					break;
				}
				if(alice.attack(bob)) {
					++aliceWins;
					break;
				}
			}
			alice.reset();
			bob.reset();
		}
		return aliceWins;
		//return turns;
	}
	
	//TODO
	public static int simulate2(Team red, Team blue, int battles) {
		//System.out.println(red);
		//System.out.println(blue);
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
