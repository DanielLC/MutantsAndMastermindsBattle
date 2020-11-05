package effects;

import main.Player;

public class Affliction {
	public Condition rank1;
	public Condition rank2;
	public Condition rank3;
	public double effectRank;
	public String resistance;	//TODO: Maybe change this to an enum?
	public boolean cumulative = false;
	
	public Affliction(Condition rank1, Condition rank2, Condition rank3, double effectRank, String resistance) {
		this.rank1 = rank1;
		this.rank2 = rank2;
		this.rank3 = rank3;
		this.effectRank = effectRank;
		this.resistance = resistance;
	}
	
	public void afflict(Player target, double modifier) {
		AfflictionInstance instance = target.afflictions.get(this);
		if(instance == null) {
			instance = new AfflictionInstance(this, target);
			target.afflictions.put(this, instance);
		}
		instance.activeResist(modifier);
	}
}
