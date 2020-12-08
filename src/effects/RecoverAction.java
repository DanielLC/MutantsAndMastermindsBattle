package effects;

import java.util.List;

import main.Player;

//This is only here so the GUI can recognize it as an effect. I'm not sure it's a great idea. It might be better to make a more general standard action class.
public class RecoverAction extends Effect {
	public RecoverAction(Player user) {
		super(user);
	}

	@Override
	public void affect(Player target, double modifier) {
		user.staggered = false;
	}

	@Override
	protected Effect startClone() {
		return new RecoverAction(null);
	}

	@Override
	public String getType() {
		return "Recover";
	}
	public void attack(List<Player> targets) {
		user.staggered = false;
	}

}
