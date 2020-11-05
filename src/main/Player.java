package main;

import java.util.Hashtable;
import java.util.Scanner;

import effects.*;

public class Player implements Comparable<Player> {
	public String name;
	public double activeDefense;
	public double toughness;
	public double attack;
	public double damage;
	public double fortitude;
	public double will;
	public int bruises;
	public boolean minion = false;
	public boolean multiattack = false;
	public boolean selectiveArea = false;
	public boolean staggered = false;
	public boolean incapacitated = false;
	public boolean perception = false;
	public double improvedCritical = 0;
	public Team team = null;
	public int[] conditions = new int[Condition.COUNT.ordinal()];
	public Hashtable<Affliction, AfflictionInstance> afflictions = new Hashtable<Affliction, AfflictionInstance>();
	public Affliction affliction;
	
	//Returns the modifier on all checks from disabled or impaired.
	public int getModifier() {
		if(conditions[Condition.DISABLED.ordinal()] > 0) {
			return -5;
		} else if(conditions[Condition.IMPAIRED.ordinal()] > 0) {
			return -2;
		}
		return 0;
	}
	
	public double roll() {
		return 1 + Math.random()*20;						//Standard d20
		
		//return 2 + (Math.random() + Math.random())*10		//2d10
		
		/*double roll1 = 1 + Math.random()*20;				//3d20 drop highest and lowest
		double roll2 = 1 + Math.random()*20;
		double roll3 = 1 + Math.random()*20;
		double temp;
		if(roll2 < roll1) {
			temp = roll1;
			roll1 = roll2;
			roll2 = temp;
		}
		if(roll3 < roll2)
			roll2 = roll3;
		if(roll2 < roll1)
			return roll1;
		return roll2;*/
		
		/*System.out.println("Enter roll for " + name + ": ");	//Use actual dice, or maybe fudge the roll
		return new Scanner(System.in).nextDouble();*/
	}
	
	//Returns 0 for miss, 1 for hit, and 2 for crit.
	//What about multiattack? That will need to know the degree of success.
	public int attackRoll(double attack, double defense, double roll) {
		boolean nat20 = roll >= 20;
		boolean threatRange = roll >= 20 - improvedCritical;
		boolean basicHit = roll + attack + getModifier() > defense;
		if(nat20 || basicHit) {				//If it's a hit
			if(threatRange && basicHit) {	//If they rolled high enough for a crit and were able to hit without the auto hit (needed for it to count as a crit)
				//Main.print(name + " scored a critical hit!");		//Attack has all this, but with more detail. And these aren't technically always correct. If a minion rolls a crit against a non-minion, it doesn't actually get a crit.
				return 2;
			} else {
				//Main.print(name + " scored a hit");
				return 1;					//Basic hit
			}
		} else {
			//Main.print(name + " missed");
			return 0;						//Miss
		}
	}
	
	public int check(double ability, double difficultyClass, double roll) {
		if(Main.verbose)	//Theoretically redundant, but it takes too much time formatting this when I won't show it anyway.
			Main.print(String.format("%s rolled d20%+.0f vs %.0f: %.0f", name, ability, difficultyClass, roll));
		if(roll >= 20) {
			roll += 5;
		}
		double val = (roll + ability + getModifier() - difficultyClass)/5;
		val += Math.signum(val);	//(int) rounds toward zero. I need to round away from zero.
		return (int) val;
	}
	
	public int check(double ability, double difficultyClass) {
		return check(ability, difficultyClass, roll());
	}
	
	public double getActiveDefense() {
		double total = activeDefense;
		if(conditions[Condition.DEFENSELESS.ordinal()] > 0) {
			total = 0;
		} else if(conditions[Condition.VULNERABLE.ordinal()] > 0) {
			total = Math.floor(total/2);
		}
		if(conditions[Condition.PRONE.ordinal()] > 0) {
			total -= 5;
		}
		return total;
	}
	
	public boolean isDefenseless() {
		return conditions[Condition.DEFENSELESS.ordinal()] > 0;
	}
	
	public double getToughness() {
		return toughness - bruises;
	}
	
	public Player(Player c, Team team, int playerNumber) {
		this.name = c.name + playerNumber;
		this.activeDefense = c.activeDefense;
		this.toughness = c.toughness;
		this.attack = c.attack;
		this.damage = c.damage;
		this.fortitude = c.fortitude;
		this.will = c.will;
		this.minion = c.minion;
		this.multiattack = c.multiattack;
		this.team = team;
	}
	
	public Player(String name, double activeDefense, double toughness, double attack, double damage, double fortitude, double will) {
		this.name = name;
		this.activeDefense = activeDefense;
		this.toughness = toughness;
		this.attack = attack;
		this.damage = damage;
		this.fortitude = fortitude;
		this.will = will;
	}
	
	public Player(String name) {
		this.name = name;
	}
	
	public Player(String name, double powerLevel) {
		this(name, powerLevel, powerLevel, powerLevel, powerLevel, powerLevel, powerLevel);
	}
	
	public Player(double powerLevel) {
		this("", powerLevel, powerLevel, powerLevel, powerLevel, powerLevel, powerLevel);
	}
	
	public void reset() {
		bruises = 0;
		staggered = false;
		incapacitated = false;
		afflictions.clear();
		for(int i = 0; i < conditions.length; ++i) {
			conditions[i] = 0;
		}
	}
	
	private void affect(Player target, double modifier) {
		if(affliction == null) {
			target.takeHit(damage + modifier);
		} else {
			affliction.afflict(target, modifier);
		}
	}
	
	//TODO: Does not take into account that when a natural 20 is required to hit, it doesn't crit.
	private void attack(Player target, double damage, boolean multiattackSingleTarget) {
		if(perception) {
			Main.print(name + " automatically hit " + target + " with Perception attack.");
			affect(target, 0);
			return;
		}
		double modifier = 0;
		if(target.minion && !minion && attack > target.getActiveDefense()) {
			Main.print(name + " hit " + target + ", minion, as a routine check");
			affect(target, modifier);
			return;
		}
		//Note: It's also possible to hit defenseless targets as a routine check, but you don't auto-crit them, so I don't think it's worth it.
		//If I make this so you can play battles instead of just simulate them, it should probably give you the option.
		
		double roll = roll();	//At this point, we actually have to roll to hit.
		int hit = attackRoll(attack, target.getActiveDefense() + 10, roll);
		if(hit == 2) {			//If it's a critical
			if(!minion) {
				if(target.minion) {
					Main.print(name + " critically hit " + target + ", minion, defeating them instantly");
					target.incapacitate();
					return;
				} else {
					Main.print(name + " critically hit " + target);
					modifier += 5;
				}				//The last option is a minion attacking a non-minion, which can't crit, so it passes through
			}
		}
		if(hit >= 1) {
			if(multiattackSingleTarget) {
				int degree = check(attack, target.getActiveDefense() + 10, roll);
				if(degree >= 3) {
					modifier += 5;
					Main.print(name + " hit " + target.name + " with a +5 modifier from multiattack.");
				}
				else if(degree >= 2) {
					modifier += 2;
					Main.print(name + " hit " + target.name + " with a +2 modifier from multiattack.");
				}
			} else {
				Main.print(name + " hit " + target.name);
			}
			affect(target, modifier);
			return;
		}
		Main.print(name + " missed " + target.name);
		return;
	}

	private void attack(Player target, boolean multiattackSingleTarget) {
		attack(target, damage, multiattackSingleTarget);
	}

	private void attack(Player target, double damage) {
		attack(target, damage, false);
	}
	
	private void attack(Player target) {
		attack(target, damage, false);
	}
	
	public void attack(Team target) {
		if(multiattack) {
			int n = target.getMembersRemaining();
			if(n > 1) {
				for(Player targetC : target.getAllTargets()) {
					attack(targetC, damage-n);
				}
				return;
			} else {
				attack(target.getTarget(), true);
				return;
			}
		} else if(selectiveArea) {
			for(Player targetC : target.getAllTargets()) {
				targetC.dodgeArea(damage);
			}
			return;
		} else {
			attack(target.getTarget());
			return;
		}
	}
	
	public void takeTurn() {
		attack(team.enemyTeam);
		for(AfflictionInstance a : afflictions.values()) {
			a.passiveResist();
		}
	}
	
	//returns true if team is incapacitated.
	public boolean incapacitate() {
		incapacitated = true;
		if(team == null)
			return true;
		else
			return team.loseMember();
	}
	
	public boolean takeHit(double effect) {
		double degree = check(toughness - bruises, effect + 15);
		if(degree >= 0) {		//Made save
			Main.print(name + " tanked attack");
			return false;
		} else if(minion) {
			Main.print(name + ", minion, was incapacitated");
			return incapacitate();
		}
		if(degree == -1) {	//Failed by one rank
			Main.print(name + " was bruised");
			++bruises;
			return false;
		}
		if(degree == -2) {	//Failed by two ranks
			Main.print(name + " was dazed");
			++bruises;
			return false;
		}
		if(degree == -3) {	//Failed by three ranks
			++bruises;
			if(staggered) {
				Main.print(name + " was staggered again, and incapacitated");
				return incapacitate();
			} else {
				Main.print(name + " was staggered");
				staggered = true;
				return false;
			}
		}
		//Failed by four ranks
		Main.print(name + " was incapacitated");
		return incapacitate();
	}
	
	public boolean dodgeArea(double effect) {
		int degree = check(activeDefense, effect + 10);
		if(degree >= 0) {	//Successful dodge
			return takeHit(Math.max(Math.floor(effect/2),1));	//Half effect rounded down to minimum of 1
			//takeHit(effect-5);	//Much better way of doing it
		} else {
			return takeHit(effect);
		}
	}

	@Override
	public int compareTo(Player arg0) {
		return (int)Math.signum((arg0.attack + arg0.damage - arg0.activeDefense - arg0.toughness) - (attack + damage - activeDefense - toughness));
	}
	
	public String toString() {
		return name + "\t" + attack + "\t" + damage + "\t" + activeDefense + "\t" + toughness;
		
	}
}
