package main;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import effects.*;

public class Player {
	public String name;
	public Stat dodge;
	public Stat parry;
	public Stat toughness;
	public Stat fortitude;
	public Stat will;
	public int bruises;
	public boolean minion = false;
	public boolean staggered = false;
	public boolean incapacitated = false;
	public boolean recoveryRemaining = true;
	public Team team = null;
	public int[] conditions = new int[Condition.COUNT.ordinal()];
	public Hashtable<Affliction, AfflictionInstance> afflictions = new Hashtable<Affliction, AfflictionInstance>();
	public List<Effect> effects;
	public double initiative;
	public int initiativeRoll;
	public double initiativeRollFractionalPart;
	public boolean powerAttack;
	public boolean preciseAttack;
	public boolean allOutAttack;
	public boolean defensiveAttack;
	public Stat powerAttackValue;
	public Stat allOutAttackValue;
	public Stat cover;
	public Stat concealment;
	public Stat range;
	public Stat[] stats = new Stat[Stat.COUNT];
	public int regenRank;
	public int regenStatus;
	
	public Player() {
		effects = new ArrayList<Effect>(1);
		for(byte i=0; i<Stat.COUNT; ++i) {
			stats[i] = new Stat(0);
		}
		dodge				= stats[Stat.DODGE];
		parry				= stats[Stat.PARRY];
		toughness			= stats[Stat.TOUGHNESS];
		fortitude			= stats[Stat.FORTITUDE];
		will				= stats[Stat.WILL];
		powerAttackValue	= stats[Stat.POWER_ATTACK];
		allOutAttackValue	= stats[Stat.ALL_OUT_ATTACK];
		cover				= stats[Stat.COVER];
		concealment			= stats[Stat.CONCEALMENT];
		range				= stats[Stat.RANGE];
	}
	
	public Player(Player c, Team team, int playerNumber) {
		this();
		this.name = c.name + playerNumber;
		this.dodge = c.dodge;
		this.parry = c.parry;
		this.toughness = c.toughness;
		this.effects = new ArrayList<Effect>(c.effects.size());
		for(Effect e : c.effects) {
			effects.add(e);
		}
		this.fortitude = c.fortitude;
		this.will = c.will;
		this.minion = c.minion;
		this.team = team;
	}
	
	public Player(String name, double dodge, double parry, double toughness, double attack, double damage, double fortitude, double will) {
		this();
		this.name = name;
		this.dodge.val = dodge;
		this.parry.val = parry;
		this.toughness.val = toughness;
		effects = new ArrayList<Effect>(1);
		effects.add(new Damage(this, attack, damage));
		this.fortitude.val = fortitude;
		this.will.val = will;
	}
	
	public Player(String name) {
		this();
		this.name = name;
		effects = new ArrayList<Effect>(1);
		//effects.add(new Damage(this, 0, 0));
	}
	
	public Player(String name, double powerLevel) {
		this(name, powerLevel, powerLevel, powerLevel, powerLevel, powerLevel, powerLevel, powerLevel);
	}
	
	public Player(double powerLevel) {
		this("", powerLevel, powerLevel, powerLevel, powerLevel, powerLevel, powerLevel, powerLevel);
	}
	
	public double getAttackModifier() {
		return -powerAttackValue.val - range.val;
		//Charge attacks will need to be added too, and maybe some other custom modifiers. Impaired and disabled are not included, since they're added at the end to all checks, attack or otherwise.
	}
	
	public double getEffectModifier() {
		return powerAttackValue.val + allOutAttackValue.val;
		//Impaired and disabled are not included, since they're added at the end to all checks, attack or otherwise.
	}
	
	public void rollInitiative() {
		initiativeRollFractionalPart = initiative + roll();
		initiativeRoll = (int)Math.floor(initiativeRollFractionalPart);
		initiativeRollFractionalPart -= initiativeRoll;
	}
	
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
		
		//return 2 + (Math.floor(Math.random()) + Math.floor(Math.random()))*10		//2d10, only integers because trying to include fractional values messes it up.
		
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
	
	public int check(double ability, double difficultyClass, double roll) {
		if(Main.verbose) {
			int plus = (int)(ability + getModifier());
			Main.print(String.format("%s rolled d20%+d vs %.0f: %.0f%2$+d = %.0f", name, plus, difficultyClass, roll, roll + plus));
		}
		if(roll >= 20) {
			roll += 5;
		}
		double val = (roll + ability + getModifier() - difficultyClass)/5;
		val += Math.signum(val);	//(int) rounds toward zero. I need to round away from zero.
		if(Main.verbose) {
			int degMag = Math.abs((int)val);
			Main.print((val > 0 ? "SUCCEESS" : "FAILURE") + " by " + degMag + " degree" + (degMag > 1 ? "s" : ""));
		}
		return (int) val;
	}
	
	public int check(double ability, double difficultyClass) {
		return check(ability, difficultyClass, roll());
	}
	
	public double getDodge() {
		double total = dodge.val;
		if(conditions[Condition.DEFENSELESS.ordinal()] > 0) {
			total = 0;
		} else if(conditions[Condition.VULNERABLE.ordinal()] > 0) {
			total = Math.floor(total/2);
		}
		if(conditions[Condition.PRONE.ordinal()] > 0) {
			total -= 5;
		}
		total += concealment.val;		//TODO: Concealment shouldn't help against area attacks. I'm going to need to change stuff to make that work. It's just supposed to subtract from the attack of the attack, not improve your dodge.
		total += cover.val;				//TODO: This one does help against area attacks, but it has that explicitly. Theoretically, there should be times where this doesn't work.
		return total;
	}
	
	public double getParry() {
		double total = parry.val;
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
	
	public double getActiveDefense(boolean ranged) {
		if(ranged)
			return getDodge();
		else
			return getParry();
	}
	
	public boolean isDefenseless() {
		return conditions[Condition.DEFENSELESS.ordinal()] > 0;
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
	
	//TODO: Add dazed
	public void regen() {
		if(regenRank <= 0)
			return;
		regenStatus += regenRank;
		while(regenStatus >= 10) {
			if(bruises > 0) {
				--bruises;
				if(Main.verbose)
					Main.print(name + " Regenerates from a bruise");
			} else if(staggered) {
				staggered = false;
				if(Main.verbose)
					Main.print(name + " Regenerates from the Staggered condition");
			} else {
				regenStatus = 0;
				return;
			}
			regenStatus -= 10;
		}
		if(bruises <= 0 && !staggered) {
			regenStatus = 0;
			return;
		}
	}
	
	public int regenTurnsRemaining() {
		return (9-regenStatus)/regenRank;
	}
	
	public void takeTurn() {
		regen();
		if(staggered && recoveryRemaining) {
			staggered = false;
			recoveryRemaining = false;
		} else {
			effects.get(0).attack(team.enemyTeam);
		}
		for(AfflictionInstance a : afflictions.values()) {
			a.passiveResist();
		}
	}
	
	public void incapacitate() {
		incapacitated = true;
		if(team != null)
			team.loseMember();
	}
	
	public void dodgeArea(Effect effect) {
		if(Main.verbose)
			Main.print(name + " rolls to dodge area " + effect.name);
		int degree = check(dodge.val, effect.effectRank + 10);
		if(degree >= 0) {	//Successful dodge
			effect.affect(this, Math.max(Math.floor(effect.effectRank/2),1) - effect.effectRank);	//Half effect rounded down to minimum of 1
			//effect.affect(this, -5);	//Much better way of doing it
		} else {
			effect.affect(this, 0);
		}
	}
	
	public String getConditionString() {
		StringBuilder s = new StringBuilder();
		for(int i=1; i<Condition.COUNT.ordinal(); ++i) {
			if(conditions[i] > 0)
				s.append(Condition.values()[i]).append('\n');
		}
		return s.toString();
	}
	
	public String toString() {
		return "Name:\t" + name + "\tDodge:\t" + dodge + "\tParry:\t" + parry + "\tToughness:\t" + toughness + "\tAttack:\t" + effects.get(0).attack + "\tEffect Rank:\t" + effects.get(0).effectRank;
	}
}
