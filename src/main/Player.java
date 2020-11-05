package main;

import java.util.Hashtable;

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
	public boolean perception;
	public Team team = null;
	public int[] conditions = new int[Condition.COUNT.ordinal()];
	public Hashtable<Affliction, AfflictionInstance> afflictions = new Hashtable<Affliction, AfflictionInstance>();
	public Affliction affliction;
	
	public int check(double ability, double difficultyClass, boolean natural20AutoHit) {
		double roll = 1 + Math.random()*20;
		if(Main.verbose)	//Theoretically redundant, but it takes too much time formatting this when I won't show it anyway.
			Main.print(String.format("%s rolled d20%+.0f vs %.0f: %.0f", name, ability, difficultyClass, roll));
		if(roll >= 20) {
			if(natural20AutoHit)
				return Integer.MAX_VALUE;
			roll += 5;
		}
		if(conditions[Condition.DISABLED.ordinal()] > 0) {
			ability -= 5;
		} else if(conditions[Condition.IMPAIRED.ordinal()] > 0) {
			ability -= 2;
		}
		double val = (roll + ability - difficultyClass)/5;
		val += Math.signum(val);	//(int) rounds toward zero. I need to round away from zero.
		return (int) val;
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
		double modifier = 0;
		if(!minion && target.minion) {
			if(attack > target.getActiveDefense()) {
				Main.print(name + " hit " + target + ", minion, as a routine check");
				affect(target, modifier);
				return;
			}
			int degree = check(attack, target.getActiveDefense() + 10, true);
			if(!perception && (degree == Integer.MAX_VALUE || target.isDefenseless())) {
				Main.print(name + " critically hit " + target + ", minion, defeating them instantly");
				target.incapacitate();
				return;
			}
			if(degree >= 0) {
				if(multiattackSingleTarget) {
					if(degree >= 3)
						modifier += 5;
					else if(degree >= 2)
						modifier += 2;
				}
				Main.print(name + " hit " + target.name);
				affect(target, modifier);
				return;
			}
			return;
		}
		int degree = 1;
		if(!perception) {
			degree = check(attack, target.getActiveDefense() + 10, true);
			if((degree == Integer.MAX_VALUE || target.isDefenseless()) && !(minion && !target.minion)) {
				Main.print("Critical");
				modifier += 5;	
			}
		}
		if(degree >= 0) {
			if(multiattackSingleTarget) {
				if(degree >= 3)
					modifier += 5;
				else if(degree >= 2)
					modifier += 2;
			}
			Main.print(name + " hit " + target.name);
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
		double degree = check(toughness - bruises, effect + 15, false);
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
		int degree = check(activeDefense, effect + 10, false);
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
