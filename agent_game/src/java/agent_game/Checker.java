package agent_game;

import jason.asSyntax.Literal;

public class Checker extends Thread{

//This thread runs along side the agent threads and only looks at the table. If
//coniditions are correct, it will trigger the game to process the cards and complete 
//the round. The agents then check to see if it is clear to proceed by detecting if the 
//table is cleared off (indicating that the game has finalized).
	
	GameModel model;
	GetBitView view;
	
	public Checker(GameModel m, GetBitView v)
	{
		model = m;
		view = v;
	}
	
	public void run(){
		while(true)
		{
			if(model.finished)
			{
				try {
					this.sleep(1000);
					model.finalize();
					view.updateView();
				}
				catch(Exception e)
				{}
			}
			else
			{
				try {
					this.sleep(250);
				}
				catch(Exception e)
				{ }
			}
		}
	}
}