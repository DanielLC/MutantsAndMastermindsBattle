package effects;
import main.Main;
import main.Player;

public class AfflictionInstance {
	public Affliction affliction;
	public Player target;
	public Condition currentCondition = Condition.NULL;
	public int currentDegree = 0;
	public double effectiveRank;
	
	public AfflictionInstance(Affliction affliction, Player target) {
		this.affliction = affliction;
		this.target = target;
	}
	
	public void activeResist(double modifier) {	//Used when attacked
		this.effectiveRank = affliction.effectRank + modifier;
		if(Main.verbose)
			Main.print(target.name + " rolls to actively resist " + affliction.name);
		int degree = target.check(target.stats[affliction.resistance].val, effectiveRank+10);
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
		if(Main.verbose)
			Main.print(target.name + " rolls to passively resist " + affliction.name);
		int degree = target.check(target.stats[affliction.resistance].val, effectiveRank+10);
		if(degree >= 0) {
			setCondition(Condition.NULL);
		}
	}
	
	private void setCondition(Condition newCondition) {
		if(newCondition != Condition.NULL)
			Main.print(target.name + " is now " + newCondition);
		else if(currentCondition != Condition.NULL)
			Main.print(target.name + " is no longer " + currentCondition);
		--target.conditions[currentCondition.ordinal()];
		++target.conditions[newCondition.ordinal()];
		currentCondition = newCondition;
		if(newCondition == Condition.INCAPACITATED)
			target.incapacitate();
	}
}
