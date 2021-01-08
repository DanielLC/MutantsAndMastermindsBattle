package main;

public class Stat {
	public double val;
	public Stat(double val) {
		this.val = val;
	}
	public Stat(Stat stat) {
		this.val = stat.val;
	}

	public static final byte DODGE =			0;
	public static final byte PARRY =			1;
	public static final byte TOUGHNESS =		2;
	public static final byte FORTITUDE =		3;
	public static final byte WILL =				4;
	public static final byte POWER_ATTACK =		5;
	public static final byte ALL_OUT_ATTACK =	6;
	public static final byte COVER =			7;
	public static final byte CONCEALMENT =		8;
	public static final byte RANGE =			9;
	public static final byte COUNT =			10;
	
	public static String toString(byte stat) {
		switch(stat) {
		case DODGE:
			return "DODGE";
		case PARRY:
			return "PARRY";
		case TOUGHNESS:
			return "TOUGHNESS";
		case FORTITUDE:
			return "FORTITUDE";
		case WILL:
			return "WILL";
		case POWER_ATTACK:
			return "POWER_ATTACK";
		case ALL_OUT_ATTACK:
			return "ALL_OUT_ATTACK";
		case COVER:
			return "COVER";
		case CONCEALMENT:
			return "CONCEALMENT";
		case RANGE:
			return "RANGE";
		}
		throw new RuntimeException("ERROR: Stat " + stat + " not found.");
	}
	
	public static byte parse(String s) {
		switch(s.trim().toLowerCase()) {
		case "dodge":
			return DODGE;
		case "parry":
			return PARRY;
		case "toughness":
			return TOUGHNESS;
		case "fortitude":
			return FORTITUDE;
		case "will":
			return WILL;
		case "power_attack":
			return POWER_ATTACK;
		case "all_out_attack":
			return ALL_OUT_ATTACK;
		case "cover":
			return COVER;
		case "concealment":
			return CONCEALMENT;
		case "range":
			return RANGE;
		}
		throw new RuntimeException("ERROR: Stat '" + s.trim().toLowerCase() + "' not found.");
	}
}
