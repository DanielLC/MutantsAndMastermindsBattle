package main;

public class Character implements Comparable<Character> {
	public String name;
	public double activeDefense;
	public double toughness;
	public double attack;
	public double damage;
	public int bruises;
	public boolean minion = false;
	public boolean staggered = false;
	public boolean incapacitated = false;
	public Team team = null;
	public static boolean verbose = false;
	
	public Character(String name, double activeDefense, double toughness, double attack, double damage) {
		this.name = name;
		this.activeDefense = activeDefense;
		this.toughness = toughness;
		this.attack = attack;
		this.damage = damage;
	}
	
	public Character(String name) {
		this.name = name;
	}
	
	public Character(String name, double powerLevel) {
		this(name, powerLevel, powerLevel, powerLevel, powerLevel);
	}
	
	public void reset() {
		bruises = 0;
		staggered = false;
		incapacitated = false;
	}
	
	public boolean attack(Character target) {
		if(!minion && target.minion) {
			if(attack > target.activeDefense) {
				print(name + " hit " + target + ", minion, as a routine check");
				return target.takeHit(damage);
			}
			double roll = 1+Math.random()*20;
			if(roll >= 20 &&  attack != Double.MAX_VALUE) {
				print(name + " critically hit " + target + ", minion, defeating them instantly");
				return target.incapacitate();
			}
			if(roll + attack > target.activeDefense + 10) {
				print(name + " hit " + target.name);
				return target.takeHit(damage);
			}
			return false;
		}
		double roll = 1+Math.random()*20;
		if(roll >= 20 && attack != Double.MAX_VALUE && !(minion && !target.minion)) {		//If they have a Perception attack, there's no natural 20's. And minions don't get critical hits on non-minions.
			print(name + " critically hit " + target.name);
			return target.takeHit(damage + 5);
		}
		if(roll + attack > target.activeDefense + 10) {
			print(name + " hit " + target.name);
			return target.takeHit(damage);
		}
		print(name + " missed " + target.name);
		return false;
	}
	
	public boolean attack(Team target) {
		return attack(target.getTarget());
	}
	
	//returns true
	private boolean incapacitate() {
		incapacitated = true;
		if(team == null)
			return true;
		else
			return team.loseMember();
	}
	
	public boolean takeHit(double effect) {
		double ranks = (((Math.random()*20 + toughness - bruises) - (effect + 15))/5);
		if(ranks > 0) {		//Made save
			print(name + " tanked attack");
			return false;
		} else if(minion) {
			print(name + ", minion, was incapacitated");
			return incapacitate();
		}
		if(ranks > -1) {	//Failed by one rank
			print(name + " was bruised");
			++bruises;
			return false;
		}
		if(ranks > -2) {	//Failed by two ranks
			print(name + " was dazed");
			//TODO: dazed
			++bruises;
			return false;
		}
		if(ranks > -3) {	//Failed by three ranks
			++bruises;
			if(staggered) {
				print(name + " was staggered again, and incapacitated");
				return incapacitate();
			} else {
				print(name + " was staggered");
				staggered = true;
				return false;
			}
		}
		print(name + " was incapacitated");
		return incapacitate();
	}
	
	private void print(String s) {
		if(verbose)
			System.out.println(s);
	}

	@Override
	public int compareTo(Character arg0) {
		return (int)Math.signum((arg0.attack + arg0.damage - arg0.activeDefense - arg0.toughness) - (attack + damage - activeDefense - toughness));
	}
	
	public String toString() {
		return name + "\t" + attack + "\t" + damage + "\t" + activeDefense + "\t" + toughness;
		
	}
}
