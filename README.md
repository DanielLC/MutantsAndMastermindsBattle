You can use this by setting up teams in team1.txt and team2.txt. The file battleSim.exe will run the battle the given number of times and show the result. The program battleSim.exe runs a single game, which you can control. It's meant to be useful for a GM.

The battles roll initiative to decide the turn order, but always attacks in the order the players are listed in the team files. This lets you test out who to target first, but is a problem if you want a non-optimal game where characters attack each other more randomly. I'll probably add that as option eventually.

I'm not sure how the resistance check for afflictions is supposed to work if the effect is used twice with different modifiers (such as +5 for critical hit). Currently I have it just overwrite it with the last value.

The supported stats are:

General:

Player:				Set the name of the player and start their stats.
Dodge
Parry
Active defense:		Sets Dodge and Parry to that value. Useful when they're the same, which they almost always are because of how power levels work.
Toughness
Fortitude
Will
Bruises:			Sets the number of -1 modifiers to Toughness they start with. This will be useful if it ever becomes possible to save characters in the middle of the fight.
Minion
Staggered:			Like bruises, not useful now, but might come in handy later.
Incapacitated:		Same.
Power attack
Precise attack
All-out attack
Defensive attack
Damage:				Set the name of the Damage power, and the next lines give its stats.
Affliction:			Set the name of the Affliction power, and the next lines give its stats.
Initiative
Copies:				Adds the given number of copies of this player to the team. This must be the last attribute added.

Attacks (Affliction or Damage):

Attack:				Sets the attack modifier.
Rank:				Sets the rank of an attack.
Threat range:		Sets the threat range. Default is 1. One rank of dangerous gives 2, etc.
Dangerous:			Number of ranks of Dangerous. Sets threat range to one more than the value given.
Improved critical	Same as Dangerous.
Multiattack
Area
Perception
Ranged:				Sets the active defense to Dodge, rather than the default Parry.
Resistance:			Sets the resistance. Damage defaults to Toughness. Affliction does not have a default, and will crash if one is not given. Must be Toughness, Fortitude, Will, Dodge, or Parry.
Active defense:		Sets the active defense. Default is Parry. Must be Toughness, Fortitude, Will, Dodge, or Parry.

Afflictions:

Degree 1:			Sets the condition for the first degree of failure. Can be Null (if it does nothing), Dazed, Hindered, Impaired, Vulnerable, Compelled, Defenseless, Disabled, Stunned, Immobile, Prone, Controlled, and Incapacitated.
Only Hindered, Vulnerable Defenseless, Disabled, and Incapacitated are actually implemented in battleSime.exe.
Degree 2:			Sets the condition for the second degree of failure. Same conditions as Degree 1.
Degree 3:			Sets the condition for the second degree of failure. Same conditions as Degree 1.
Cumulative
Progressive
Instant recovery
Limited degree:		Sets the third degree to be the same as second degree. As a result of how this is implemented, you have to set it after setting the second degree.

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

* The idea amount of Damage is 4 more than your opponent's Toughness (assuming the same PL), winning 59.314% ± 0.047% of the time. The ideal amount of Toughness is as far as possible from 4 less than your opponent's Damage in either direction.

* Perception wins 69.742% ± 0.041% of the time. Equivalent to on PL higher.

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