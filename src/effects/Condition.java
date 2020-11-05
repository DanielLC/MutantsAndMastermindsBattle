package effects;

public enum Condition {
	NULL,		//used if the attack doesn't have an effect in that condition
	//Rank 1 conditions
	DAZED,		//also entranced, no move action
	HINDERED,	//also fatigued, half speed
	IMPAIRED,	//-2 to all checks
	VULNERABLE,	//halve active defenses and round up
	//Rank 2 conditions
	COMPELLED,	//dazed and controlled
	DEFENSELESS,//acitve defense set to zero. Either attacks are routine checks, or hits are all critical
	DISABLED,	//-5 to all checks
	STUNNED,	//also exhausted, dazed and hindered
	IMMOBILE,	//can't move
	PRONE,		//hindered, -5 to parry, +5 to dodge, +5 to ranged attack
	//Rank 3 conditions
	CONTROLLED,	//works for the other team
	INCAPACITATED,//also all the other rank 3 conditions
	//Count variable, which is used for the length of the list and is not a condition
	COUNT
}