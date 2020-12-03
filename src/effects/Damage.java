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
		int degree = target.check(target.getToughness(), effectRank + modifier + 15);
		if(degree >= 0) {		//Made save
			Main.print(target.name + " tanked the damage");
			return;
		} else if(target.minion) {
			Main.print(target.name + ", minion, was incapacitated");
			target.incapacitate();
			return;
		}
		switch(degree) {
		case -1:	//Failed by one rank
			Main.print(target.name + " was bruised");
			++target.bruises;
			return;
		case -2:	//Failed by two ranks
			Main.print(target.name + " was dazed");
			++target.bruises;
			return;
		case -3:	//Failed by three ranks
			++target.bruises;
			if(target.staggered) {
				Main.print(target.name + " was staggered again, and incapacitated");
				target.incapacitate();
				return;
			} else {
				Main.print(target.name + " was staggered");
				target.staggered = true;
				return;
			}
		default:	//Failed by four ranks
			Main.print(target.name + " was incapacitated");
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
