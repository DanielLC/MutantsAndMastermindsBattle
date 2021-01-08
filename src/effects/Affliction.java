package effects;

import main.Player;
import main.Stat;

public class Affliction extends Effect {
	public Condition degree1;
	public Condition degree2;
	public Condition degree3;
	public boolean cumulative = false;
	
	public Affliction(Player user, Condition degree1, Condition degree2, Condition degree3, double attack, double effectRank, byte resistance) {
		super(user);
		this.degree1 = degree1;
		this.degree2 = degree2;
		this.degree3 = degree3;
		this.attack = attack;
		this.effectRank = effectRank;
		this.resistance = resistance;
	}
	
	public Affliction(Player user) {
		super(user);
		this.degree1 = Condition.NULL;
		this.degree2 = Condition.NULL;
		this.degree3 = Condition.NULL;
	}
	
	@Override
	public void affect(Player target, double modifier) {
		AfflictionInstance instance = target.afflictions.get(this);
		if(instance == null) {
			instance = new AfflictionInstance(this, target);
			target.afflictions.put(this, instance);
		}
		instance.activeResist(modifier);
	}

	@Override
	protected Effect startClone() {
		return new Affliction(null, degree1, degree2, degree3, 0, 0, resistance);
	}

	@Override
	public String getType() {
		return "Affliction";
	}
}
