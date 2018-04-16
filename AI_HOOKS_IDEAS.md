# Possible ways to make a Slay the Spire AI

1. Hardcoding an AI to respond to certain situations in certain ways.
2. Do a search over possible states using heuristics to evaluate the success of the game state following some sequence of actions
3. Use some type of machine learning to train a network to evaluate the game state and return a response. This would require either a network that keeps memory or for many parts of the game state to be fed in as input such as the current relics, discard pile, draw pile, etc...

All of these would have in common the need for a framework by which the computer could play the game.

# What actions are taken in Slay the Spire?

1. At nodes there may be text options, i.e. some events have options 1, 2, 3... etc where each option has a different result.
2. At nodes there may also be battles which will require their own treatment.
3. Some events bring up specific mini games (like the goblin select or spin the wheel mini game) which will.
4. There is the need to bring up the map and evaluate next options of which map state to take.
5. There is the need to be able to select between card rewards, gold rewards, and relic rewards.

## Battle actions

1. The player needs to select cards to use and targets for those cards including many edge cases like armaments which bring up a select menu that would somehow have to be represented to the api. To assist in the player's ability to try different card orders and see what each would do, we would want some way for the AI to test the results of taking an action through the API that (hopefully) wouldn't visually affect anything and would then allow the game to reset its state to the start of the turn. Since the visual side of the game is so coupled to the underlying logic I'm not sure if there would be any way to let an AI **test** what a card would do before using it because using the card will necessarily create the visual affects. This becomes especially tricky with how some cards **absolutely rely** on the timing system in order to properly execute their actions. What I guess this means is that providing an AI for simulating the result of an action seems much more challenging to create.
2. This means the AI will either need to hard code actions or provide it's own system for simulating the result of using cards.
3. To actually use cards the AI would simply provide a list of cards to play in order and the necessary information about how to play the cards. An issue that would come up with this is that BaseMod would have to know what each card individual card does in order to properly select cards for cards that make you discard or upgrade or etc... Perhaps a different solution would be for the AI to provide a list of cards and callback objects such that when an action results from the playing of a card it's callback object would process the action.
4. We could provide a feature to instead allow the AI to step through each card play one-by-one and evaluate the game state after every card that is played. This could be necessary or at least useful for cards that are random so the AI can react to what happens as a result of the RNG. However other than for RNG this feature wouldn't be too useful because the AI would need to simulate the result of its actions for itself anyway.
5. The only real input a player gets during combat is which cards to use and what targets so this should cover the combat system. The AI would also need to explicitly call an end turn function because some cards are 0 cost so there is no way to automatically end the turn on 0 energy and there's the top relic that would mean we don't end the turn on 0 cards either.
6. Somehow we will have to capture when a card action entirely completes and the game state finishes being updated. Perhaps we could have some thread on a tight loop check the size of the game's action manager queue and then say that we're at the start of a new choice for the AI when it gets to size 0. Better yet we could just inject a patch into the methods adding and removing from the queue so we don't need to inspect it with a thread.

# A realistic implementation of simulating card actions

1. Is there any way to make a copy of the player and copy of the monsters in the room which actions could be run against? There probably would be a way to suppress visual effects by preventing draw calls from going through while simulating actions but since actions necessarily take a bunch of time to run there isn't any way to use the game engine itself to simulate the actions.
2. Sadly what this means is that for simulating the effect of a card, we would **have** to hard code in **just** the changes to game state that each card does since the existing card code does not make any attempt to separate the part that updates game state and the part that updates the visuals, i.e. we would have to manually code that strike does 6/9 damage to the target it's used on. We would end up using a lot of constructs very similar to how the game currently runs and it would be able to grab the necessary data like damage from the actual card instance we're using (which is good) but actually figuring out what to do with that data would be impossible.
3. Note that this means that **each** character would need to get it's own API for simulating cards which would make an AI project require lots of work to work with modded characters or new characters and etc...
4. Plus for every card change, new card, and possibly even relic additions/changes new logic would have to be added to the simulator to see what would happen since the game itself **can't do it**.
5. This isn't impossible but it would mean that AI development and testing would probably want to be done on only specific versions of the game rather than making any attempt to stay up-to-date with the game's changes.

# Would this game be interesting to try and make an AI for?

1. Well optimal play for Slay the Spire would be fairly challenging. AI would have to make predictions based on cards in the library, hand, exhaust, and discard piles which leaves a lot of variables for an AI to figure out a good solution. If we reimplemented the slay the spire card game engine then we could train an AI that would do a much better job of winning fights, etc... but since it's a player vs enemy type game that would require reimplementing enemies too so I can't see a good way to do this.
2. Actually training an AI would be very difficult for that reason but we could get somewhere just by running it against the actual game client over and over. If we run against the actual game client the AI would only get one shot versus every enemy it battles and would be unable to practice the same fight to get better at making decisions in fights.
3. In all likelihood the game is too complicated to write an effective AI that is trained with machine learning. Hardcoded AI would be possible with an API for making automated actions but that wouldn't be all that interesting since this is just player versus enemy.
