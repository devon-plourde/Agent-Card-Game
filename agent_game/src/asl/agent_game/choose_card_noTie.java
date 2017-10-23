// Internal action code for project agent_game

package agent_game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import jason.*;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class choose_card_noTie extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
    	
    	double choice = 0;
    	boolean decided = false;
    	int round = 0;
    	
    	//list of percepts from the agent executing the action
        Iterator<Literal> i = ts.getAg().getBB().getPercepts();
        
        //iterating through the percepts for useful information.
        //This agent is looking for cards it has already played to rule them out of selection.
        //This will also indicate what cards are available to play.
        //In addition, it will look at what others have played to help determine the best card to play.
        ArrayList<Double> unavailableCards = new ArrayList<Double>();
        ArrayList<String> playerOrder = new ArrayList<String>();
        ArrayList<ArrayList<Double>> playedCards = new ArrayList<ArrayList<Double>>();
        
        //initializes the ArrayLists.
        for(int j = 0; j < 4; ++j)
        {
        	playerOrder.add(null);
        	playedCards.add(new ArrayList<Double>());
        }
        
        //This array will keep track of how many of each card has been played. The indices of the array
        // denote the value of the card (0 -> value 1; 1 -> value 2; ...) and the number stored at that 
        // location is the count of how many other players have played that card.
        int[] playedCount = new int[5];

        while(i.hasNext())
        {
        	Literal lit = i.next();
        	
        	if(lit.getFunctor().toString().equals("played") && lit.getTerm(0).toString().equals(args[0].toString())){
        		unavailableCards.add((double) Integer.parseInt(lit.getTerm(1).toString()));
        	}
        	else if(lit.getFunctor().toString().equals("played")){
        		playedCount[((Integer.parseInt(lit.getTerm(1).toString())) -1)]++;
        		playedCards.get(Integer.parseInt(""+(lit.getTerm(0).toString().charAt(lit.getTerm(0).toString().length()-1))) -1)
        			.add((double) Integer.parseInt(lit.getTerm(1).toString()));
        	}
        	else if(lit.getFunctor().toString().equals("position")){
        		playerOrder.set(Integer.parseInt(lit.getTerm(1).toString()),lit.getTerm(0).toString());
        	}
        	else if(lit.getFunctor().toString().equals("round")){
        		round = Integer.parseInt((lit.getTerm(0).toString()));
        	}
        }
        
        //Choosing a random card for the first hand.
        //Keeps the starting state of the game different every time.
        if(round == 1)
        {
            Random rng = new Random();
            choice = rng.nextInt(5) + 1;

            NumberTerm result = new NumberTermImpl(choice);
            un.unifies(result, args[1]);
            return true;
        }

        //If there is a card that is guaranteed to avoid ties, it is chosen.
        for(int l = 0; l < playedCount.length; ++l)
        {
        	if(playedCount[l] == 3 && !(unavailableCards.contains((double) l+1)))
        	{
        		decided = true;
        		choice = (double) l+1;
        	}
        }

	    //If there is a card that the players in front of this one have all played, choose it (or one of them).
    	int k = 0;
        if(!decided)
        {
        	int[] forwardPlayerCards = new int[5];
        	
        	while(k > 0 && (playerOrder.get(k) == null || !(playerOrder.get(k).equals(args[0].toString()))))
        	{
        		if(playerOrder.get(k) != null){
	        		int lookAt = Integer.parseInt(""+playerOrder.get(k).charAt(playerOrder.get(k).length()-1)) -1;
	 	        
	       	        //find players in front of me. Add the cards they've played to a count.        		
	        		for(int j = 0; j < playedCards.get(lookAt).size(); ++j){
	        			forwardPlayerCards[(int) (playedCards.get(lookAt).get(j)-1)]++;
		        	}
	        		++k;
        		}
        		else
        		{
        			++k;
        		}
        	}


        	double temp = 0;
        	if(forwardPlayerCards[forwardPlayerCards.length-1] != 0){
	        	for(int j = 4; j >=0; --j)
	        	{
	        		if(k != 0 && forwardPlayerCards[j] == k && !unavailableCards.contains((double) j+1)){
	        			temp = (double) j+1;
	        			decided = true;
	        			break;
	        		}
	        	}
	        	choice = temp;
        	}
        }
                   
        //If still undecided, play the lowest card available (cannot guarantee no ties)
        if(!decided)
        {
    		choice = 1.0;
            while(unavailableCards.contains((double) choice))
            {
            	if((choice+1) > 5)
            		choice = 1.0;
            	else
            		choice = choice+1;
            }
        }
     
        
        //Unifies the chosen card with the second argument given to the action (variable).
        NumberTerm result = new NumberTermImpl(choice);
        un.unifies(result, args[1]);
        
        // everything ok, so returns true
        return true;
    }
}
