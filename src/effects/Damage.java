package effects;

import main.Main;
import main.Player;

public class Damage extends Effect {
	
	public Damage(Player user, double attack, double effectRank) {
		super(user);
		this.attack = attack;
		this.effectRank = effectRank;
	}

	public Damage(Player user) {
		super(user);
	}

	@Override
	public void affect(Player target, double modifier) {
		if(Main.verbose)
			Main.print(target.name + " rolls to resist Damage");
		int degree = target.check(target.getToughness(), effectRank + modifier + 15);
		if(degree >= 0) {		//Made save
			if(Main.verbose)
				Main.print(target.name + " tanks the damage");
			return;
		} else if(target.minion) {
			if(Main.verbose)
				Main.print(target.name + ", minion, is incapacitated");
			target.incapacitate();
			return;
		}
		switch(degree) {
		case -1:	//Failed by one rank
			if(Main.verbose)
				Main.print(target.name + " is bruised");
			++target.bruises;
			return;
		case -2:	//Failed by two ranks
			if(Main.verbose)
				Main.print(target.name + " is dazed");
			++target.bruises;
			return;
		case -3:	//Failed by three ranks
			++target.bruises;
			if(target.staggered) {
				if(Main.verbose)
					Main.print(target.name + " is staggered again, and incapacitated");
				target.incapacitate();
				return;
			} else {
				if(Main.verbose)
					Main.print(target.name + " is staggered");
				target.staggered = true;
				return;
			}
		default:	//Failed by four ranks
			Main.print(target.name + " is incapacitated");
			target.incapacitate();
		}
	}

	@Override
	protected Effect startClone() {
		return new Damage(null,0,0);
	}

	@Override
	public String getType() {
		return "Damage";
	}

}
