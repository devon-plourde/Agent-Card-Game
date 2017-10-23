// Internal action code for project agent_game

package agent_game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import jason.*;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class choose_card_random extends DefaultInternalAction {

	//args[0] is the name of the agent given from the agent itself.
	//args[1] is the variable to unify the answer with.
	
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
  	
    	//Getting the list of percepts from the agent that called this internal action.
        double choice = 0;
        Iterator<Literal> i = ts.getAg().getBB().getPercepts();
        
        //iterating through the percepts for useful information.
        //This agent is looking for cards it has already played to rule them out of selection.
        ArrayList<Double> unavailableCards = new ArrayList<Double>();
        while(i.hasNext())
        {
        	Literal lit = i.next();
        	
        	if(lit.getFunctor().toString().equals("played") && lit.getTerm(0).toString().equals(args[0].toString())){
        		unavailableCards.add((double) Integer.parseInt(lit.getTerm(1).toString()));
        	}
        }
        
        //Actually choosing the card. Takes a random number between 1 - 5 ([0 - 4] +1) and decrements by 
        // 1 if the card has already been played. If the card valued at 1 has been played, it
        // starts again at 5 and continues to decrement.
        
        //This makes it slightly less random than one would like, however this saves on reasoning cycles
        //which would otherwise slow the game down to the point of being mistaken as frozen.
        Random rng = new Random();
        choice = rng.nextInt(5) + 1;
        
        while(unavailableCards.contains(choice))
        {
        	if((choice-1) <= 0)
        		choice = 5.0;
        	else
        		choice = choice-1;
        }
        
        //Unifying the choice of card with the second argument (variable).
        NumberTerm result = new NumberTermImpl(choice);
        un.unifies(result, args[1]);
        
        // everything ok, so returns true
        return true;
    }
}
