package fileio;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import main.*;
import effects.*;

public class PlayerReader {
	/*public static List<Player> readFolder(String folder) {
		try {
			ArrayList<Player> players = new ArrayList<>();
			for(File f : new File(folder).listFiles()) {
				players.add(read(f));
			}
			return players;
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}*/
	public static Team read(String file) {
		try {
			return read(new File(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static Team read(File file) throws FileNotFoundException {
		Team players = new Team();
		Player player = null;
		Affliction affliction = null;
		Effect effect = null;
		Scanner s = new Scanner(file);
		whileloop:
		while(s.hasNext()) {
			String nextLine = s.nextLine();
			if(nextLine.matches("^\\s*$"))		//If it's all whitespace, skip to the next line.
				continue;
			String[] line = nextLine.split(":\\s*");
			String start = line[0].toLowerCase().trim();
			String end = null;
			if(line.length > 1)
				end = line[1].trim();
			
			if(effect != null) {
				switch(start) {
				case "attack":
					effect.attack = Double.parseDouble(end);
					continue;
				case "rank":
					effect.effectRank = Double.parseDouble(end);
					continue;
				case "threat range":
					effect.threatRange = Double.parseDouble(end);
					continue;
				case "dangerous":
				case "improved critical":
					effect.threatRange = Double.parseDouble(end)+1;
					continue;
				case "multiattack":
					effect.multiattack = true;
					continue;
				case "area":
					effect.selectiveArea = true;
					continue;
				case "perception":
					effect.perception = true;
					continue;
				case "ranged":
					effect.activeDefense = Stat.DODGE;
					continue;
				case "resistance":
					affliction.resistance = Stat.parse(end);
					continue;
				case "active defense":
					affliction.activeDefense = Stat.parse(end);
					continue;
				case "":		//Starting a line with a colon is a comment.
					continue;
				}	//If it isn't any of these, it checks based on which kind of effect it is.
				
				if(affliction != null) {
					switch(start) {
					case "degree 1":
						affliction.degree1 = Condition.valueOf(end.toUpperCase());
						continue;
					case "degree 2":
						affliction.degree2 = Condition.valueOf(end.toUpperCase());
						continue;
					case "degree 3":
						affliction.degree3 = Condition.valueOf(end.toUpperCase());
						continue;
					case "cumulative":
						affliction.cumulative = true;
						continue;
					case "progressive":
						affliction.progressive = true;
						continue;
					case "instant recovery":
						affliction.instantRecovery = true;
						continue;
					case "limited degree":
						affliction.degree3 = affliction.degree2;
						continue;
					case "":		//Starting a line with a colon is a comment.
						continue;
					}
				}
				//There's no Damage-specific variables, so I don't need to worry about that.
				//If it's not one of these, they must be done with the effect. So I set that to null and fall through to the player variables.
				
				effect = null;
				affliction = null;
			}
			
			switch(start) {
			case "player":
				player = new Player();
				player.name = end;
				players.add(player);
				effect = null;
				affliction = null;
				break;
			case "dodge":
				player.dodge.val = Double.parseDouble(end);
				break;
			case "parry":
				player.parry.val = Double.parseDouble(end);
				break;
			case "active defense":
				player.dodge.val = Double.parseDouble(end);
				player.parry.val = player.dodge.val;
			case "toughness":
				player.toughness.val = Double.parseDouble(end);
				break;
			case "fortitude":
				player.fortitude.val = Double.parseDouble(end);
				break;
			case "will":
				player.will.val = Double.parseDouble(end);
				break;
			case "bruises":
				player.bruises = Integer.parseInt(end);
				break;
			case "minion":
				player.minion = true;
				break;
			case "staggered":
				player.staggered = true;
				break;
			case "incapacitated":
				player.incapacitated = true;
				break;
			case "power attack":
				player.powerAttack = true;
				break;
			case "precise attack":
				player.preciseAttack = true;
				break;
			case "all-out attack":
				player.allOutAttack = true;
				break;
			case "defensive attack":
				player.defensiveAttack = true;
				break;
			case "damage":
				effect = new Damage(player);
				effect.name = end;
				player.effects.add(effect);
				break;
			case "affliction":
				affliction = new Affliction(player);
				effect = affliction;
				effect.name = end;
				player.effects.add(effect);
				break;
			case "initiative":
				player.initiative = Double.parseDouble(end);
				break;
			case "regeneration":
				player.regenRank = Integer.parseInt(end);
				break;
			case "power level":		//This sets the player's defenses all to that level and automatically gives them an attack at that level (which you can add stuff to).
				double powerLevel = Double.parseDouble(end);
				player.dodge.val = powerLevel;
				player.parry.val = powerLevel;
				player.will.val = powerLevel;
				player.fortitude.val = powerLevel;
				player.toughness.val = powerLevel;
				effect = new Damage(player);
				effect.name = "Damage";
				effect.attack = powerLevel;
				effect.effectRank = powerLevel;
				player.effects.add(effect);
			case "end":		//End finishes reading the file and ignores anything after.
				//System.out.println("End");
				break whileloop;
			case "":		//Starting a line with a colon is a comment.
				continue;
			default:
				System.out.println("Warning: '" + start + "' not found.");	
			}
		}
		s.close();
		return players;
	}
}
