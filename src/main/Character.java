package main;

public class Character implements Comparable<Character> {
	public String name;
	public double activeDefense;
	public double toughness;
	public double attack;
	public double damage;
	public int bruises;
	public boolean minion = false;
	public boolean multiattack = false;
	public boolean selectiveArea = false;
	public boolean staggered = false;
	public boolean incapacitated = false;
	public Team team = null;
	public static boolean verbose = false;
	
	public Character(Character c, Team team) {
		this.name = c.name;
		this.activeDefense = c.activeDefense;
		this.toughness = c.toughness;
		this.attack = c.attack;
		this.damage = c.damage;
		this.minion = c.minion;
		this.multiattack = c.multiattack;
		this.team = team;
	}
	
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
	
	public Character(double powerLevel) {
		this("", powerLevel, powerLevel, powerLevel, powerLevel);
	}
	
	public void reset() {
		bruises = 0;
		staggered = false;
		incapacitated = false;
	}
	
	private boolean attack(Character target, double damage, boolean multiattackSingleTarget) {
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
			double successValue = roll + attack - (target.activeDefense + 10);
			if(successValue >= 0) {
				if(multiattackSingleTarget) {
					if(successValue >= 15)
						damage += 5;
					else if(successValue >= 10)
						damage += 2;
				}
				print(name + " hit " + target.name);
				return target.takeHit(damage);
			}
			return false;
		}
		double roll = 1+Math.random()*20;
		boolean natural20 = false;
		if(roll >= 20 && attack != Double.MAX_VALUE && !(minion && !target.minion)) {		//If they have a Perception attack, there's no natural 20's. And minions don't get critical hits on non-minions.
			print("Critical");
			damage += 5;
			natural20 = true;
		}
		double successValue = roll + attack - (target.activeDefense + 10);
		if(successValue >= 0 || natural20) {
			if(multiattackSingleTarget) {
				if(successValue >= 15)
					damage += 5;
				else if(successValue >= 10)
					damage += 2;
			}
			print(name + " hit " + target.name);
			return target.takeHit(damage);
		}
		print(name + " missed " + target.name);
		return false;
	}

	private boolean attack(Character target, boolean multiattackSingleTarget) {
		return attack(target, damage, multiattackSingleTarget);
	}

	private boolean attack(Character target, double damage) {
		return attack(target, damage, false);
	}
	
	private boolean attack(Character target) {
		return attack(target, damage, false);
	}
	
	public boolean attack(Team target) {
		if(multiattack) {
			int n = target.getMembersRemaining();
			if(n > 1) {
				for(Character targetC : target.getAllTargets()) {
					attack(targetC, damage-n);
				}
				return target.getMembersRemaining() == 0;
			} else {
				return attack(target.getTarget(), true);
			}
		} else if(selectiveArea) {
			for(Character targetC : target.getAllTargets()) {
				targetC.dodgeArea(damage);
			}
			return target.getMembersRemaining() == 0;
		} else {
			return attack(target.getTarget());
		}
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
	
	public boolean dodgeArea(double effect) {
		double roll = 1 + Math.random()*20;
		if(roll >= 20)	//Critical success
			roll += 5;
		if(roll + activeDefense >= effect + 10) {	//Successful dodge
			return takeHit(Math.max(Math.floor(effect/2),1));	//Half effect rounded down to minimum of 1
			//takeHit(effect-5);	//Much better way of doing it
		} else {
			return takeHit(effect);
		}
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
