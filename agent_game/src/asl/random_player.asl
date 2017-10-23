// Agent random_player in project agent_game

/* Initial beliefs and rules */

continue(X) :- round(Y) & finished(X,Y).

/* Initial goals */

!start.

/* Plans */

+!start <- !choose_card.
    			   
//plans to play a card. The first line tries to catch agents that are dead and trying to 
//play a card anyway. The third line attempts to fix a problem where an agent stops recieving
//its life total after a player is killed and thinks it is no longer dead.
+!play_card(X) : .my_name(Y) & life(Y, 0)  <- .print("kill confirmed (play_card)").
+!play_card(X) : .my_name(Y) & not finished(Y, _) <- playCard(X); !check.
+!play_card(X) : not life(_,_) <- .print(X, " is already dead").

//These are plans to choose a card to play. The first line tries to find if an agent is dead and
//tells it to do nothing  if it is. The third line is here in case an agent has a plan to choose a 
//card before it is supposed to.
+!choose_card : .my_name(Y) & life(Y, 0) <- .print("kill confirmed (choose_card)").
+!choose_card : .my_name(X) & not continue(X) <- .my_name(X); agent_game.choose_card_random(X, Y); !play_card(Y).
+!choose_card <- .print("Cannot proceed"); drop_desire(choose_card); !check.

//These plans are used to help synchronize the agents so they don't go playing all their cards at
//once. The first line is to catch agents that have already died. The second line is cyclic and will
//make the agent continuously check the table until it is prompted to play another card by way of
//losing its 'round' percept.
+!check : .my_name(Y) & life(Y, 0) <- .print("kill confirmed (check)").
+!check <- check; !check.

//Once a new round starts, the old percept is removed and a new round is added. This is the signal
//for agents to select a new card to play.
-round(X) : .my_name(Y) & life(Y, 0) <- .print(Y, " is already dead").
-round(X) <- .drop_intention(check); !choose_card.
    	   
