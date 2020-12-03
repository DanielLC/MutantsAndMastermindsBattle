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
		Damage damage = null;
		Affliction affliction = null;
		Effect effect = null;
		Scanner s = new Scanner(file);
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
					effect.ranged = true;
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
					case "resistance":
						affliction.resistance = end.toLowerCase();
						continue;
					case "cumulative":
						affliction.cumulative = true;
						continue;
					case "":		//Starting a line with a colon is a comment.
						continue;
					}
				}
				//There's no Damge-specific variables, so I don't need to worry about that.
				//If it's not one of these, they must be done with the effect. So I set that to null and fall through to the player variables.
				
				effect = null;
				affliction = null;
				damage = null;
			}
			
			switch(start) {
			case "player":
				player = new Player();
				player.name = end;
				players.add(player);
				break;
			case "dodge":
				player.dodge = Double.parseDouble(end);
				break;
			case "parry":
				player.parry = Double.parseDouble(end);
				break;
			case "active defense":
				player.dodge = Double.parseDouble(end);
				player.parry = player.dodge;
			case "toughness":
				player.toughness = Double.parseDouble(end);
				break;
			case "fortitude":
				player.fortitude = Double.parseDouble(end);
				break;
			case "will":
				player.will = Double.parseDouble(end);
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
				damage = new Damage(player);
				effect = damage;
				effect.name = end;
				player.effects.add(effect);
				break;
			case "affliction":
				affliction = new Affliction(player);
				effect = affliction;
				effect.name = end;
				player.effects.add(effect);
				break;
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