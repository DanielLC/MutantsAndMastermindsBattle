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
		//this.effectiveRank = affliction.effectRank + modifier;
		double curRank = affliction.effectRank + modifier;
		this.effectiveRank = Math.max(effectiveRank, curRank);
		if(Main.verbose)
			Main.print(target.name + " rolls to actively resist " + affliction.name);
		int degree = target.check(target.stats[affliction.resistance].val, curRank+10);
		if(affliction.cumulative && currentDegree < 0) {
			//System.out.println("Degree: " + degree + "\tCurrent Degree: " + currentDegree);
			degree += currentDegree;
		}
		if(degree >= currentDegree)				//Positive degree means they resisted.
			return;
		currentDegree = degree;
		setCondition(degree);
	}
	
	public void passiveResist() {	//Used at the end of defenders turn
		//The code does not remove conditions when you've recovered from them. So if it doesn't affect you, you don't roll.
		if(currentDegree >= 0)
			return;
		if(affliction.instantRecovery) {
			if(Main.verbose)
				Main.print(target.name + " instantly recovers from " + affliction.name);
			currentDegree = 0;
			setCondition(Condition.NULL);
		}
		if(Main.verbose)
			Main.print(target.name + " rolls to passively resist " + affliction.name);
		int degree = target.check(target.stats[affliction.resistance].val, effectiveRank+10);
		if(degree >= 0) {
			setCondition(Condition.NULL);
		} else if(affliction.progressive) {
			--degree;
			setCondition(degree);
		}
	}
	
	private void setCondition(int degree) {
		if(degree >= 0) {
			setCondition(Condition.NULL);
		} else if(degree == -1) {
			setCondition(affliction.degree1);
		} else if(degree == -2) {
			setCondition(affliction.degree2);
		} else {
			setCondition(affliction.degree3);
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
