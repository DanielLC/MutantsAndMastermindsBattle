package effects;
import java.util.List;

import main.Main;
import main.Player;
import main.Team;

public abstract class Effect {
	public String name;
	public double attack;
	public boolean multiattack = false;
	public boolean selectiveArea = false;
	public boolean perception = false;
	public boolean ranged = false;
	public double threatRange = 1;
	public double effectRank;
	public Player user;
	
	public Effect(Player user) {
		this.user = user;
	}
	
	//Returns 0 for miss, 1 for hit, and 2 for crit.
	//What about multiattack? That will need to know the degree of success.
	public int attackRoll(double attack, double defense, double roll) {
		if(perception)
			return 1;
		if(roll < 2)
			return 0;
		boolean nat20 = roll >= 20;
		boolean inThreatRange = roll >= 20 - threatRange;
		boolean basicHit = roll + attack + user.getModifier() > defense;
		if(nat20 || basicHit) {				//If it's a hit
			if(inThreatRange && basicHit) {	//If they rolled high enough for a crit and were able to hit without the auto hit (needed for it to count as a crit)
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
	
	public void attack(Player target, double attackModifier, double effectModifier, boolean multiattackSingleTarget) {
		if(perception) {
				if(Main.verbose)
			Main.print(user.name + " automatically hits " + target.name + " with Perception attack");
			affect(target, 0);
			return;
		}
		double activeDefense = target.getActiveDefense(ranged);
		if(target.minion && !user.minion && attack >= activeDefense) {
			if(Main.verbose)
				Main.print(user.name + " hits " + target.name + ", minion, as a routine check");
			affect(target, effectModifier);
			return;
		}
		//Note: It's also possible to hit defenseless targets as a routine check, but you don't auto-crit them, so I don't think it's worth it.
		//If I make this so you can play battles instead of just simulate them, it should probably give you the option.
		
		double roll = user.roll();	//At this point, we actually have to roll to hit.
		int hit = attackRoll(attack + attackModifier, activeDefense + 10, roll);
		if(hit == 2) {			//If it's a critical
			if(!user.minion) {
				if(target.minion) {
					if(Main.verbose)
						Main.print(user.name + " critically hits " + target.name + ", minion, defeating them instantly");
					target.incapacitate();
					return;
				} else {
					if(Main.verbose)
						Main.print(user.name + " critically hits " + target.name);
					effectModifier += 5;
				}				//The last option is a minion attacking a non-minion, which can't crit, so it passes through
			}
		}
		if(hit >= 1) {
			if(multiattackSingleTarget) {
				if(Main.verbose)
					Main.print(user + " rolls attack.");
				int degree = user.check(attack, activeDefense + 10, roll);
				if(degree >= 3) {
					effectModifier += 5;
					if(Main.verbose)
						Main.print(user.name + " hits " + target.name + " with a +5 modifier from multiattack.");
				}
				else if(degree >= 2) {
					effectModifier += 2;
					if(Main.verbose)
						Main.print(user.name + " hits " + target.name + " with a +2 modifier from multiattack.");
				}
			} else {
				if(Main.verbose)
					Main.print(user.name + " hits " + target.name);
			}
			affect(target, effectModifier);
			return;
		}
		if(Main.verbose)
			Main.print(user.name + " misses " + target.name);
		return;
	}
	
	public void attack(Team target) {
		if(multiattack) {
			int n = target.getMembersRemaining();
			if(n > 1) {
				for(Player targetC : target.getAllTargets()) {
					attack(targetC, 0, -n, false);
				}
				return;
			} else {
				attack(target.getTarget(), 0, 0, true);
				return;
			}
		} else if(selectiveArea) {
			for(Player targetC : target.getAllTargets()) {
				targetC.dodgeArea(this);
			}
			return;
		} else {
			attack(target.getTarget(), 0, 0, false);
			return;
		}
	}
	
	public void attack(List<Player> targets) {
		int n = targets.size();
		if(n < 1)
			throw new RuntimeException("Error: No targets selected.");
		if(!multiattack && !selectiveArea  && n > 1)
			throw new RuntimeException("Error: Effect " + name + " does not allow multiple targets.");

		double attackModifier = user.getAttackModifier();
		double effectModifier = user.getEffectModifier();
		if(multiattack) {
			if(n == 1) {
				attack(targets.get(0), attackModifier, effectModifier, true);
			} else {
				for(Player targetC : targets) {
					attack(targetC, attackModifier, effectModifier-n, false);
				}
			}
			return;
		}
		if(selectiveArea) {
			for(Player targetC : targets) {
				targetC.dodgeArea(this);
			}
			return;
		}
		attack(targets.get(0), attackModifier, effectModifier, false);	//Normal attack against single target
	}
	
	public abstract void affect(Player target, double modifier);
	protected abstract Effect startClone();
	
	public Effect clone(Player newUser) {
		Effect copy = startClone();
		copy.name = name;
		copy.attack = attack;
		copy.multiattack = multiattack;
		copy.selectiveArea = selectiveArea;
		copy.perception = perception;
		copy.threatRange = threatRange;
		copy.effectRank = effectRank;
		copy.user = newUser;
		return copy;
	}
	
	public String getDescription() {
		StringBuilder s = new StringBuilder();
		s.append(attack);
		s.append('/');
		s.append(effectRank);
		s.append(' ');
		s.append(getType());
		s.append(' ');
		if(multiattack) {
			s.append("multiattack ");
		}
		if(selectiveArea) {
			s.append("area ");
		}
		if(perception) {
			s.append("perception ");
		}
		if(ranged) {
			s.append("ranged ");
		}
		if(threatRange > 0) {
			s.append("improved critical (");
			s.append(threatRange);
			s.append(") ");
		}
		
		return s.toString();
	}
	
	public abstract String getType();
}
