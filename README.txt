This program is designed to play test simple battles in Mutants and Masterminds by the thousands. That way you can figure out things like how many ranks you can put into Summon until it just isn't fair.

gui.GUI is the main file to run if you want to use the GUI. main.Main is there for hard-coding the battles, which lets you do things like put them in a for loop to look at the results after slight changes. Or just to use features not yet added to the GUI.

If you aren't familiar with programming, you can just download battleSim.jar and run that to use the GUI.

The battles randomize the turns, but assume you attack the members in the opposing team in order. This lets you test out who to target first, but is a problem if you want a non-optimal game where characters attack each other more randomly. I'll probably add that as option eventually.

Here's some things I've found so far:

* The character who goes first wins 53% of the time.

* The average match is 11.6 turns long (counting each player going as one turn).

* Putting an extra point in Toughness and one less in the active defenses (or equivalently, an extra point in damage and one less in accuracy) lets you win 54% of the time.

* The idea amount of Damage is 3.7 more than your opponent's Toughness (assuming the same PL), winning 58.4% of the time. The ideal amount of Toughness is as far from 3.7 less than you're opponents Damage in either direction. Having the ideal Damage/Toughness ratio is about the same as having an extra 1.5 in the Active Defense + Toughness or Attack + Effect.
Someone ahead by one PL wins 72% of the time.

* A Perception attack is balanced if it has -3 to Damage. On its own, you win 73.5% of the time, so it's roughly equivalent to being one PL higher.

* Two characters working together are evenly matched against one 2.31 power levels higher.

* Three characters are evenly matched against someone 3.8 levels higher.

* Four are evenly matched against 4.85 levels higher.

* One character with a side kick 3.1 levels lower is evenly matched against someone one power level higher than them. For example, if you're in a PL 10 game you'd be balanced playing as someone PL 9 with a PL 6 side kick.

* A minion is even against someone 3.93 power levels lower.

* Multiattack against a single target lets you win 51.4% of the time.

* Two on two, both characters having multiattack is worth a PL difference of 0.4

* Four on four, all the characters having multiattack is worth a PL difference of 1

* One character with multiattack is balanced against four that are 3.8 PL lower (as opposed to 4.85).

* All else being equal, attacking stronger characters first is better, but it's extremely minor. If two pairs fight, with a PL difference of 10 attacking the strongest first wins 58% of the time. And it gradually increases to approach that.

* If the weaker character is additionally a minion, it's better to go for the minion first if they're even (winning 69% of the time), but as the minions get weaker this gets less useful, with it not mattering who you attack at 6 power levels, and after that you're actually better off killing the stronger enemy first.