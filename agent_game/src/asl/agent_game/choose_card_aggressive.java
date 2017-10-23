// Internal action code for project agent_game

package agent_game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import jason.*;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class choose_card_aggressive extends DefaultInternalAction {

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
        //Other useful information to parse include the life count of each player and their position in the 
        //lineup.
        ArrayList<Double> unavailableCards = new ArrayList<Double>();
        ArrayList<String> playerOrder = new ArrayList<String>();
        ArrayList<ArrayList<Double>> playedCards = new ArrayList<ArrayList<Double>>();
        
        for(int j = 0; j < 4; ++j)
        {
        	playerOrder.add(null);
        	playedCards.add(new ArrayList<Double>());
        }
        
        //This array will keep track of how many of each card has been played. The indices of the array
        // denote the value of the card (0 -> value 1; 1 -> value 2; ...) and the number stored at that 
        // location is the count of how many other relevant players have played that card.
        int[] playedCount = new int[5];
        
        //This array will store the life of each player. The index of the array denotes the related player number
        //(0 -> player1; 1 -> player2; ...) and the value stored there is the current life according to the percepts.
        int[] lifeCount = new int[4];

        //Loop for parsing all the percepts
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
        	else if(lit.getFunctor().toString().equals("life")){
        		int lookAt = Integer.parseInt(""+(lit.getTerm(0).toString().charAt(lit.getTerm(0).toString().length()-1))) -1;
        		lifeCount[lookAt] = Integer.parseInt(lit.getTerm(1).toString());
        	}
        	else if(lit.getFunctor().toString().equals("round")){
        		round = Integer.parseInt((lit.getTerm(0).toString()));
        	}
        }
        
        //random card select for the first round.
        //This keeps the starting lineup from starting the same every time.
        if(round == 1)
        {
            Random rng = new Random();
            choice = rng.nextInt(5) + 1;
            
            while(choice < 2 || choice > 4)
            {
            	choice = rng.nextInt(5) + 1;
            }

            NumberTerm result = new NumberTermImpl(choice);
            un.unifies(result, args[1]);
            return true;
        }
        
    	//find the player(s) with the lowest health.
        int low = 4;
        int me = Integer.parseInt(""+(args[0].toString().charAt(args[0].toString().length()-1))) -1;
        ArrayList<Integer> lowestLife = new ArrayList<Integer>();
    	for(int l = 0; l < lifeCount.length; ++l)
    	{
        	if(lifeCount[l] < low && l != (int) me){
        		low = lifeCount[l];
        	}
    	}
    	
        for(int j = 0; j < lifeCount.length; ++j)
        {
        	if(lifeCount[j] == low && j != Integer.parseInt(""+(args[0].toString().charAt(args[0].toString().length()-1))) -1){
        		lowestLife.add(j);
        	}
        }
        
		//if they are behind me, choose the highest card between them.
        int k = playerOrder.size()-1;
    	int[] behindPlayerCards = new int[5];
    	
    	//flag for if a player with the lowest health exists behind me.
    	boolean behind = false;

    	while(k > 0 && (playerOrder.get(k) == null || !(playerOrder.get(k).equals(args[0].toString()))))
    	{
    		if(playerOrder.get(k) != null){
	    		int lookAt = Integer.parseInt(""+playerOrder.get(k).charAt(playerOrder.get(k).length()-1)) -1;
	    		if(lowestLife.contains(lookAt)){
	    			behind = true;
		    		for(int j = 0; j < playedCards.get(lookAt).size(); ++j){
	        			behindPlayerCards[(int) (playedCards.get(lookAt).get(j)-1)]++;
		        	}
	    		}
	    		--k;
    		}
    		else{
    			--k;
    		}
    	} 	
    	
    	if(behind)
    	{
        	for(int j = 4; j >=0; --j)
        	{
        		if(behindPlayerCards[j] == 0 && !unavailableCards.contains((double) j+1)){
        			choice = (double) j+1;
        			decided = true;
        			break;
        		}
        	}
    	}
    	else{
    		//If the least healthy player(s) is in front of me, play all of their highest card +1

    		k = 0;
        	int[] forwardPlayerCards = new int[5];

        	while(k > 0 && (playerOrder.get(k) == null || !(playerOrder.get(k).equals(args[0].toString()))))
        	{
        		if(playerOrder.get(k) != null){
	        		int lookAt = Integer.parseInt(""+playerOrder.get(k).charAt(playerOrder.get(k).length()-1)) -1;
	        		if(lowestLife.contains(lookAt)){
	    	    		for(int j = 0; j < playedCards.get(lookAt).size(); ++j){
	            			forwardPlayerCards[(int) (playedCards.get(lookAt).get(j)-1)]++;
	    	        	}
	        		}
	        		++k;
        		}
        		else{
        			++k;
        		}
        	}

           	double temp = 0;
           	if(forwardPlayerCards[4] == k){
	           	for(int j = 3; j >=0; --j)
	           	{
	           		if(k != 0 && forwardPlayerCards[j] != 0 && !unavailableCards.contains((double) (j+1)+1)){
	           			temp = (double) (j+1)+1;
	           			decided = true;
	           			break;
	           		}
	           	}
	           	choice = temp;
           	}
    	}

        //If still undecided, take the lowest card to bide for time.
        if(!decided){
    		choice = 2.0;
            while(unavailableCards.contains((double) choice))
            {
            	if((choice+1) > 5)
            		choice = 2.0;
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
