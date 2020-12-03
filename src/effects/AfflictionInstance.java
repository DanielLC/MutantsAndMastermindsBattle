package effects;
import main.Main;
import main.Player;

public class AfflictionInstance {
	public Affliction affliction;
	public Player target;
	public Condition currentCondition = Condition.NULL;
	public int currentDegree = 0;
	public double defenseRank;
	public double effectiveRank;
	
	//TODO: will doesn't seem like it's being set. Or it's cleared somehow.
	public AfflictionInstance(Affliction affliction, Player target) {
		this.affliction = affliction;
		this.target = target;
		switch(affliction.resistance) {
		case "fortitude":
			this.defenseRank = target.fortitude;
			break;
		case "will":
			this.defenseRank = target.will;
			break;
		default:
			throw new RuntimeException("Error: resistance '" + affliction.resistance + "' not found.");
		}
		Main.print(target.name + " resists with " + affliction.resistance + ": " + this.defenseRank);
	}
	
	public void activeResist(double modifier) {	//Used when attacked
		this.effectiveRank = affliction.effectRank + modifier;
		int degree = target.check(defenseRank, effectiveRank+10);
		if(affliction.cumulative && currentDegree < 0) {
			//System.out.println("Degree: " + degree + "\tCurrent Degree: " + currentDegree);
			degree += currentDegree;
		}
		if(degree >= currentDegree)				//Positive degree means they resisted.
			return;
		if(degree == -1) {
			setCondition(affliction.degree1);
		} else if(degree == -2) {
			setCondition(affliction.degree2);
		} else {
			setCondition(affliction.degree3);
		}
		currentDegree = degree;
	}
	
	public void passiveResist() {	//Used at the end of defenders turn
		int degree = target.check(defenseRank, effectiveRank+10);
		if(degree >= 0) {
			setCondition(Condition.NULL);
		}
	}
	
	private void setCondition(Condition newCondition) {
		Main.print(target.name + " is now " + newCondition);
		--target.conditions[currentCondition.ordinal()];
		++target.conditions[newCondition.ordinal()];
		currentCondition = newCondition;
		if(newCondition == Condition.INCAPACITATED)
			target.incapacitate();
	}
}
