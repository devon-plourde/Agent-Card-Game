package agent_game;

import java.awt.Color;
import java.io.Console;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.logging.Level;

//import BlocksEnvironment.Block;
import jason.asSyntax.Literal;
import jason.asSyntax.Structure;

public class GetBit extends jason.environment.Environment {
	
	private GameModel model;
	protected static GetBitView view;
	private int numberOfPlayers;
	private static int sleepTime = 1000;
	private static ArrayList<String> agent_names;
	
	protected static int roundNum;
	
	private Scanner user_input = new Scanner(System.in);
	
	public static void waiting()
	{
		try {
		    Thread.sleep(sleepTime);                 
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
	}
	
	public void init(String args[])
	{
		sleepTime = Integer.parseInt(args[0]);
		numberOfPlayers = Integer.parseInt(args[1]);
		agent_names = new ArrayList<String>();
		
		for(int i = 2; i < args.length; ++i)
		{
			agent_names.add(args[i]);
		}
		
		initWorld();
		waiting();
	}
	
	public void initWorld()
	{
		model = new GameModel(numberOfPlayers);		
		view = new GetBitView(model);
		updateAllPercept();
		
		(new Checker(model, view)).start();
	}
	
	public void updateAllPercept() {
		System.out.println("\n");
		
		clearAllPercepts();

		for(int i = 0; i < agent_names.size(); ++i)
		{
			ArrayList<Card> temp = model.getPlayed(i);
			for(int j = 0; j < temp.size(); ++j)
			{
				addPercept(Literal.parseLiteral("played("+agent_names.get(i)+","+temp.get(j).getVal()+")"));
			}
			
			addPercept(Literal.parseLiteral("life("+agent_names.get(i)+","+model.getLife(i)+")"));
			
			for(int j = 0; j < model.lineup.length; ++j)
			{
				if(model.lineup[j] == i)
				{
					addPercept(Literal.parseLiteral("position("+agent_names.get(i)+","+j+")"));
				}
			}
		}
		
		roundNum = model.getRound();
		System.out.println("Starting round "+roundNum);
		
		addPercept(Literal.parseLiteral("round("+roundNum+")"));
		
		//This piece of code allows a player to input card values to play.
		//If this is run, you must disable the first player in the agent_game.mas2j file
		//As well, System.console() will return null if this is run in an IDE so create an
		//ant build and run through a console to enable.
//		Console co = System.console();
//		System.out.println(co);
//		try{
//		    String s = co.readLine();
//		    model.playCard("agent1", s);
//		}
//		finally{}
	}
	
	static void updateView()
	{
		view.updateView();
		waiting();
	}
	
	synchronized public boolean executeAction(String ag, Structure action) {
	    
		boolean success = false;
		
		try {						
			if (action.getFunctor().equals("playCard")) 
			{			
				System.out.println(ag + " is doing: " + action);

				if (sleepTime > 0) {
					Thread.sleep(sleepTime);
				}
				
				success = model.playCard(ag, action.getTerm(0).toString());
				model.allGood();

				addPercept(Literal.parseLiteral("finised("+ag+","+roundNum+")"));
				updateView();
				
				return success;
			}
			
			else if (action.getFunctor().equals("check")) 
			{
				if (sleepTime > 0) {
					Thread.sleep(sleepTime/4);
				}

				if(model.finished && model.emptyTable()){
					model.finished = false;
					updateAllPercept();
				}
				
				return true;

			}
			else {
				System.out.println("Action: "+ag+" is not yet implemented");
			}
		} 
		
		catch (Exception e) {
			System.out.println("error executing " + action + " for " + ag +":  "+e);
		}
		return false;
	}
	
	
}
