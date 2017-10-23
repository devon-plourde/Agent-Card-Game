// Internal action code for project agent_game

package agent_game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import jason.*;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class choose_card_front extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {

    	double choice = 0;
    	
    	//list of percepts from the agent executing the action
        Iterator<Literal> i = ts.getAg().getBB().getPercepts();
        
        //iterating through the percepts for useful information.
        //This agent is looking for cards it has already played to rule them out of selection.
        //This will also indicate what cards are available to play.
        ArrayList<Double> unavailableCards = new ArrayList<Double>();
        while(i.hasNext())
        {
        	Literal lit = i.next();
        	
        	if(lit.getFunctor().toString().equals("played") && lit.getTerm(0).toString().equals(args[0].toString())){
        		unavailableCards.add((double) Integer.parseInt(lit.getTerm(1).toString()));
        	}
        }
        
        //Now we can actually choose a card. This agent plays the highest card in its hand in a
        // naive attempt to stay at the front of the lineup.
        choice = 5.0;
        while(unavailableCards.contains(choice))
        {
        	if((choice-1) <= 0)
        		choice = 5.0;
        	else
        		choice = choice-1;
        }
        
        //Unifies the chosen card with the second argument given to the action (variable).
        NumberTerm result = new NumberTermImpl(choice);
        un.unifies(result, args[1]);
        
        // everything ok, so returns true
        return true;
    }
}
