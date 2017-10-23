package agent_game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class GameModel {

	//This class represents the game and all of it's attributes. The various arrays and ArrayLists
	//that are present are used to represent values for different players. The indices of the arrays
	//are representative of the player the stored values are associated with (i.e. player 1 ==> first
	//position in the array, player 2 ==> 2nd position, etc.).
	
	//Ideally these values would be wrapped up in a "player" class that could be referenced by calling the
	//player object. The reason this is not the case is that I was unsure how the agent's would be introduced
	//into the environment and whether or not I could associate them to a specific object (the "player" class).
	//For this reason, I took the approach of keeping the player data in the game model to be accessed
	//universally by the agent's using the GetBit environment as an interface between them just in case there
	//were unforeseen restrictions on agent implementation.
	
	//I should probably note that, now having created an application with the intented use for agents, a player
	//class would certainly be prefered to the current representation and would almost certainly help in 
	//fixing some of the current issues with this project.
	
	protected ArrayList<ArrayList<Card>> hands;
	protected ArrayList<ArrayList<Card>> played;
	protected Card[] table;
	protected int[] lineup;
	protected int[] playerLife;
	
	public boolean finished;
	
	private int minHandSize = 2;
	private int playerHealth = 4;
	private Color[] playerColors = {Color.YELLOW, Color.BLUE, Color.RED, Color.WHITE};
	private int numberOfPlayers;
	private GetBitView gb;
	private int roundNum;
		
	public GameModel(int x)
	{
		this.numberOfPlayers = x;
		init();
	}
		
	public void init()
	{
		System.out.println("Initializing game...");
		hands = new ArrayList<ArrayList<Card>>();
		played = new ArrayList<ArrayList<Card>>();		
		lineup = new int[numberOfPlayers];
		finished = false;
		roundNum = 0;
		
		table = new Card[numberOfPlayers];
		playerLife = new int[numberOfPlayers];

			
		//Creates a hand of cards for each player
		ArrayList<Card> playerHand;
		ArrayList<Card> temp;
		for(int j = 0; j < numberOfPlayers; ++j)
		{
			temp = new ArrayList<Card>();
			playerHand = new ArrayList<Card>();
			for(int i = 1; i < 6; ++i)
			{
				if(j != 0){
					playerHand.add(new Card(i, playerColors[j], false));				
				}	
				else{
					playerHand.add(new Card(i, playerColors[j], true));				
				}
			}
				
			hands.add(playerHand);
			played.add(temp);
			lineup[j] = j;
			table[j] = null;
			playerLife[j] = playerHealth;
		}		
	}
	
	public boolean playCard(String agent, String card)
	{
		//Action for agents to play a card from their hand.
		boolean found = false;
		int player = Integer.parseInt(""+ agent.charAt(agent.length()-1));		
		int value = Integer.parseInt(card);
				
		for(int i = 0; i < hands.get(player-1).size(); ++i)
		{
			if(hands.get(player-1).get(i).getVal() == value)
			{
				found = true;
				Card hand = hands.get(player-1).get(i);
				hand.setVisible(true);
				table[player-1] = hand;
				hands.get(player-1).remove(i);
				break;
			}
		}
		if(!found){
			return false;
		}
		return true;
	}
		
	//Simple check to see if the game can proceed to finalize the round and proceed with a new one.	
	public void allGood()
	{		
		boolean temp = true;
		for(int i = 0; i < table.length; ++i)
		{
			if(table[i] == null && getLife(i) > 0)
				temp = false;
		}
		finished = temp;
	}
		
	//This method is responsible for processing the cards once they have all been played, moving the
	//cards to their designated areas, decrementing the appropriate player's life, and returning cards
	//back to a player's hand. This method has been constructed in "blocks" for the purposes of simplicity
	//and ease. It's almost certain that improvements can be made, however with such a low amount of cards
	//to process (maximum 6 considering a 6 player game), the runtime is not really a problem.
	
	//Each "block" is identified by additonal comments below.
	public void finalize()
	{		
		if(!finished)
		{
			return;
		}
		
		//This ArrayList is created and initialized so that we can duplicate the cards that 
		//were played in order to safely modify them without any lasting impact on the game's actual
		//components.
		ArrayList<Card> realValues = new ArrayList<Card>();
		
		for(int i = 0; i < table.length; ++i){
				realValues.add(null);
		}
		
		//Detects ties amongst player cards. If a tie is detected, the value of the current card being
		//considered is replaced by a zero in order to ensure that it drifts backward as sorting progresses.
		for(int i = 0; i < table.length; ++i)
		{
			boolean tie = false;	
			if(table[i] != null){
				for(int j = 0; j < table.length; ++j)
				{									
					if(table[j] != null){
						if(i != j && table[i].getVal() == table[j].getVal())
							tie = true;
					}
				}
						
				for(int l = 0; l < table.length; ++l)
				{
					if(table[i].getColor() == getPlayerColor(lineup[l])){
						if(!tie)
							realValues.set(l, new Card(table[i].getVal(), table[i].getColor(), false));
						else
							realValues.set(l, new Card(0, table[i].getColor(), false));
					}
				}
			}
		}
		
		for(int i = 0; i < realValues.size(); ++i)
		{
			if(realValues.get(i) == null)
				realValues.set(i, new Card(-1, null, false));
		}
		
		
		//Sorts the cards based on their values. The sorting algorithm is insertion sort as
		//we are dealing with a smaller input size, it most naturally reflects the action of 
		//sorting a hand of cards, and it is stable to that order is guaranteed to be 
		//preserved.
		for(int i = 1; i < realValues.size(); ++i)
		{
			Card temp = realValues.get(i);
			int j;
		    for(j = i-1; j >= 0 && realValues.get(j).getVal() < temp.getVal() && realValues.get(j).getVal() != -1; j--)
		    {
		    		realValues.set(j+1,realValues.get(j));
		    }
		    	realValues.set(j+1, temp);
		}
	
		
		//indication if the sort was a success. Does not cause any errors, but is a useful
		//indication for troubleshooting.
		boolean test = true;
		for(int i = 0; i < realValues.size(); ++i)
		{
			if(i != 0 && realValues.get(i-1).getVal() < realValues.get(i).getVal())
				test = false;
			
			if(realValues.get(i).getColor() != null){
				lineup[i] = getPlayer(realValues.get(i).getColor());
			}
			else{
				lineup[i] = -1;
			}
		}
		System.out.println();
		if(test)
			System.out.println("Sorting is a success!");
		else
			System.out.println("Sorting failed...");
		
		
		//move cards to 'played' area
		for(int i = 0; i < table.length; ++i)
		{
			played.get(i).add(table[i]);
			table[i] = null;

		}

		//Causes the player in the last position to be bitten. This step is ignored for the 
		//first round. If the player in the last position is already dead (life == 0) then 
		//an offset is calculated to get the farthest player in the back that isn't already dead.
		//The player who was just hurt also gets their cards back.
		if(roundNum != 1){
			int offset = 1;
			while(lineup[lineup.length-offset] == -1)
			{
				offset++;
			}
			playerLife[lineup[lineup.length-offset]]--;

			
			int temp = lineup[lineup.length-offset];
		    for(int j = lineup.length-(offset+1); j >= 0; j--) 
		    {
		    	lineup[j+1] = lineup[j];
		    }
		    lineup[0] = temp;
		    
		    returnHand(temp);
		}
		
		//check hands aren't too empty. If a hand is smaller than the minimum, the cards on the 
		//table are returned. 
		for(int i = 0; i < hands.size(); ++i)
		{
			if(hands.get(i).size() < minHandSize)
			{
				returnHand(i);
			}
		}
		
		//Signals the end of the finalize method.
		System.out.println("Done!");
	}
	
	//Method for returning the cards of a player to that player's hand.
	//The .setVisible(false) command is there for a game with a user so that
	//the user doesn't see the card values of cards in another player's hand.
	
	private void returnHand(int player)
	{
		for(int j = 0; j < played.get(player).size(); ++j)
		{
			Card temp = played.get(player).get(j);
			if(player != 0)
			{
//				played.get(player).get(j).setVisible(false);
			}
			hands.get(player).add(temp);
		}
		played.set(player, new ArrayList<Card>());
	}

	
	//Various getters, setters, and checkers
	public ArrayList<Card> getHand(int x){
		return hands.get(x);
	}
	
	public ArrayList<Card> getPlayed(int x){
		return played.get(x);
	}
	
	public int[] getLineup(){
		return this.lineup;
	}
	
	public Color getPlayerColor(int x){
		return playerColors[x];
	}
	
	public int getPlayer(Color c)
	{
		int i = 0;
		for(; i < playerColors.length; ++i)
		{
			if(playerColors[i] == c)
				return i;
		}
		++i;
		return i;
	}
	
	public Card[] getTable(){
		return this.table;
	}
	
	public int getLife(int i){
		if(i == -1)
			return 0;
		else
			return playerLife[i];
	}
	
	public int getRound()
	{
		roundNum++;
		return roundNum;
	}

	public boolean emptyTable() {
		for(int i = 0; i < table.length; ++i)
		{
			if(table[i] != null && playerLife[i] != 0)
			{
				return false;
			}
		}
		return true;
	}
	
}
