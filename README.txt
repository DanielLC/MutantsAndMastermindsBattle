This program is designed to play test simple battles in Mutants and Masterminds by the thousands. That way you can figure out things like how many ranks you can put into Summon until it just isn't fair.

gui.GUI is the main file to run if you want to use the GUI. main.Main is there for hard-coding the battles, which lets you do things like put them in a for loop to look at the results after slight changes. Or just to use features not yet added to the GUI.

If you aren't familiar with programming, you can just download battleSim.jar and run that to use the GUI.

The battles randomize the turns, but assume you attack the members in the opposing team in order. This lets you test out who to target first, but is a problem if you want a non-optimal game where characters attack each other more randomly. I'll probably add that as option eventually.

I'm not sure how the resistance check for afflictions is supposed to work if the effect is used twice with different modifiers (such as +5 for critical hit). Currently I have it just overwrite it with the last value.

Here's some things I've found so far:

Here's a table of how likely you are to win vs a character some number of PL lower:

0.00:	50.064% ± 0.049%
0.25:	55.253% ± 0.048%
0.50:	60.291% ± 0.047%
0.75:	65.125% ± 0.045%
1.00:	69.723% ± 0.041%
1.25:	74.074% ± 0.038%
1.50:	78.133% ± 0.033%
1.75:	81.638% ± 0.029%
2.00:	84.750% ± 0.025%
2.25:	87.438% ± 0.022%
2.50:	89.862% ± 0.018%
2.75:	91.871% ± 0.015%
3.00:	93.569% ± 0.012%

* The character who goes first wins 52.876% ± 0.049% of the time.

* The average match is 12.445082 turns long (counting each player going as one turn).

* Putting an extra point in Toughness and one less in the active defenses (or equivalently, an extra point in damage and one less in accuracy) lets you win 53.824% ± 0.049% of the time.

* The idea amount of Damage is 4 more than your opponent's Toughness (assuming the same PL), winning 59.314% ± 0.047% of the time. The ideal amount of Toughness is as far from 4 less than you're opponents Damage in either direction.

* Perception wins 69.742% ± 0.041% of the time. Equivelent to on PL higher.

* Two characters working together are evenly matched against one 2.4 power levels higher.

* Three characters are evenly matched against someone 3.9 levels higher.

* Four are evenly matched against 5.1 levels higher.

* One character with a sidekick 3.5 levels lower is evenly matched against someone one power level higher than them. For example, if you're in a PL 10 game you'd be balanced playing as someone PL 9 with a PL 6 side kick.

* Multiattack against a single target lets you win 59.966% ± 0.047% of the time.

* Two on two, two characters with multiattack win 59.94% ± 0.15% of the time, and are even against someone 0.5 PL above them.

* Four on four, all the characters having multiattack win 60.25% ± 0.15% of the time, and are even against someone 0.8 PL above them.

* One character with multiattack is balanced against four that are 4 PL lower (as opposed to 5.1).

Here's some old stuff I haven't gotten around to retesting. Area attacks were buggy, and Recover actions weren't implemented.

* All else being equal, attacking stronger characters first is better, but it's extremely minor. If two pairs fight, with a PL difference of 10 attacking the strongest first wins 58% of the time. And it gradually increases to approach that.

* If the weaker character is additionally a minion, it's better to go for the minion first if they're even (winning 69% of the time), but as the minions get weaker this gets less useful, with it not mattering who you attack at 6 power levels, and after that you're actually better off killing the stronger enemy first.

* Afflictions are underpowered. Impaired/disabled/incapacitated wins 21% of the time. Vulnerable/defenseless/incapacitated (at PL10) is a mere 15.3% of the time.

* Adding in Perception, it still only increases to 26% victory for impaired/disabled/incapacitated (vulnerable and defensless are irrelevant to Perception attacks).

* It's not just that it needs to be mixed with Damage. Even having one person with Affliction in a large team will still result result in losing more often.

* Adding Cumulative gives the opposite problem, with them winning 67% of the time for impaired/disabled/incapacitated and 63% for vulnerable/defenseless/incapacitated.

* Giving +2 to the effect rank gets impaired/disabled/incapacitate to win 49% of the time, and +2.3 to vulnerable/defenseless/incapacitated wins 50% of the time. Just give +2 to both if you want to try to balance them.