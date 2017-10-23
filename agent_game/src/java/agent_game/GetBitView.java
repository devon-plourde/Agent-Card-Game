package agent_game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

import javax.swing.*;


class PlayerView extends JComponent{
	
	private GameModel model;
	private Rectangle dimensions;
	
	private int offset = 5;
	private int playerSize = 50;
	private int cardWidth = 70;
	private int cardHeight = 100;
		
	public PlayerView(GameModel m, Rectangle r)
	{
		this.model = m;
		this.dimensions = r;
	}
	
	public void paint(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;
		drawP1(g2);
		drawP2(g2);
		drawP3(g2);
		drawP4(g2);
		drawTable(g2);
	}
	
	public void drawP1(Graphics2D g2) {
		
		int playerNumber = 0;
		
	    ArrayList<Card> playerHand = model.getHand(playerNumber); 
	    ArrayList<Card> playedCards = model.getPlayed(playerNumber);
	    
	    if(model.getLife(playerNumber) > 0){
			for(int i = -3; i < (playerHand.size() -3); ++i)
			{				
			    g2.setColor(model.getPlayerColor(playerNumber));
				g2.fill(new RoundRectangle2D.Double(((dimensions.getWidth()/2))+(i*(cardWidth + offset)),
							dimensions.getHeight()-(cardHeight+60), cardWidth, cardHeight, 10,10));	
				
				if(playerHand.get(i + 3).getVisible()){
					g2.setColor(Color.BLACK);
					g2.setFont(new Font("default", Font.BOLD, 14));
					g2.drawString(""+playerHand.get(i+3).getVal(), (float) ((dimensions.getWidth()/2)+(i*(cardWidth + offset)))+cardWidth/2,
								(float) (dimensions.getHeight()-(cardHeight/2+60)));
				}
	
			}
			for(int i = -3; i < (playedCards.size() -3); ++i)
			{
			    g2.setColor(model.getPlayerColor(playerNumber));
				g2.fill(new RoundRectangle2D.Double((dimensions.getWidth()/2)+(i*(cardWidth/2 + offset)),
						(dimensions.getHeight()-(cardHeight+60)) - (cardHeight/2 + offset), cardWidth/2, cardHeight/2, 10,10));	
				
				g2.setColor(Color.BLACK);
				g2.setFont(new Font("default", Font.BOLD, 12));
				g2.drawString(""+playedCards.get(i+3).getVal(), (float) ((dimensions.getWidth()/2)+(i*(cardWidth/2 + offset)))+cardWidth/2/2,
						(float) (dimensions.getHeight()-(cardHeight+60)) - cardHeight/2/2);
			}
	    }
	}
	
	public void drawP2(Graphics2D g2) {
		
		int playerNumber = 1;
		
	    ArrayList<Card> playerHand = model.getHand(playerNumber); 
	    ArrayList<Card> playedCards = model.getPlayed(playerNumber);

	    if(model.getLife(playerNumber) > 0){
		    for(int i = -3; i < (playerHand.size() -3); ++i)
			{				
			    g2.setColor(model.getPlayerColor(playerNumber));
			    g2.fill(new RoundRectangle2D.Double(((dimensions.getWidth()/2))+(i*(cardWidth + offset)),
						10, cardWidth, cardHeight, 10,10));	
				
			    if(playerHand.get(i + 3).getVisible()){
					g2.setColor(Color.BLACK);
					g2.setFont(new Font("default", Font.BOLD, 14));
					g2.drawString(""+playerHand.get(i+3).getVal(), (float) (((dimensions.getWidth()/2))+(i*(cardWidth + offset)))+cardWidth/2,
								(float) 10 + cardHeight/2);
			    }
	
			}		
			for(int i = -3; i < (playedCards.size() -3); ++i)
			{
			    g2.setColor(model.getPlayerColor(playerNumber));
			    g2.fill(new RoundRectangle2D.Double(((dimensions.getWidth()/2))+(i*(cardWidth/2 + offset)),
						10 + (cardHeight + offset), cardWidth/2, cardHeight/2, 10,10));	
			    
				g2.setColor(Color.BLACK);
				g2.setFont(new Font("default", Font.BOLD, 12));
				g2.drawString(""+playedCards.get(i+3).getVal(), (float) ((dimensions.getWidth()/2)+(i*(cardWidth/2 + offset)))+cardWidth/2/2,
						(float) 10 + cardHeight +offset+ cardHeight/2/2);
			}
	    }
	}

	public void drawP3(Graphics2D g2) {
		
		int playerNumber = 2;
		
	    ArrayList<Card> playerHand = model.getHand(playerNumber);
	    ArrayList<Card> playedCards = model.getPlayed(playerNumber);
	    
	    if(model.getLife(playerNumber) > 0){
		    for(int i = -3; i < (playerHand.size() -3); ++i)
			{				
			    g2.setColor(model.getPlayerColor(playerNumber));
			    g2.fill(new RoundRectangle2D.Double(10, 
						((dimensions.getHeight()/2))+(i*(cardWidth+5))+10, cardHeight,cardWidth, 10,10));
			    
				if(playerHand.get(i + 3).getVisible()){
					g2.setColor(Color.BLACK);
					g2.setFont(new Font("default", Font.BOLD, 14));
					g2.drawString(""+playerHand.get(i+3).getVal(), (float) 10+cardWidth/2,
								(float) ((dimensions.getHeight()/2))+(i*(cardWidth+5)) + cardHeight/2);
				}
	
			}		
			for(int i = -3; i < (playedCards.size() -3); ++i)
			{
			    g2.setColor(model.getPlayerColor(playerNumber));
			    g2.fill(new RoundRectangle2D.Double(10 + (cardHeight + offset), 
						((dimensions.getHeight()/2))+(i*(cardWidth/2+5))+10, cardHeight/2,cardWidth/2, 10,10));	
			    
				g2.setColor(Color.BLACK);
				g2.setFont(new Font("default", Font.BOLD, 12));
				g2.drawString(""+playedCards.get(i+3).getVal(), (float) 10 + (cardHeight + offset)+cardWidth/2/2,
						(float) ((dimensions.getHeight()/2))+(i*(cardWidth/2+5))+10+ cardHeight/2/2);
			}
		}	
	}
	
	public void drawP4(Graphics2D g2) {
		
		int playerNumber = 3;
		
	    ArrayList<Card> playerHand = model.getHand(playerNumber);
	    ArrayList<Card> playedCards = model.getPlayed(playerNumber);
	    
	    if(model.getLife(playerNumber) > 0){
		    for(int i = -3; i < (playerHand.size() -3); ++i)
			{				
			    g2.setColor(model.getPlayerColor(playerNumber));
			    g2.fill(new RoundRectangle2D.Double(dimensions.getWidth()-(cardHeight+30), 
						(dimensions.getHeight()/2)+(i*(cardWidth+5))+10, cardHeight,cardWidth, 10,10));
			    
				if(playerHand.get(i + 3).getVisible()){
					g2.setColor(Color.BLACK);
					g2.setFont(new Font("default", Font.BOLD, 14));
					g2.drawString(""+playerHand.get(i+3).getVal(), (float) (dimensions.getWidth()-(cardHeight+30))+cardWidth/2,
								(float) ((dimensions.getHeight()/2)+(i*(cardWidth+5))) + cardHeight/2);
				}
	
			}		
			for(int i = -3; i < (playedCards.size() -3); ++i)
			{
			    g2.setColor(model.getPlayerColor(playerNumber));
			    g2.fill(new RoundRectangle2D.Double(dimensions.getWidth()-(cardHeight+30) - (cardHeight/2 + offset), 
						((dimensions.getHeight()/2))+(i*(cardWidth/2+5))+10, cardHeight/2,cardWidth/2, 10,10));
			    
				g2.setColor(Color.BLACK);
				g2.setFont(new Font("default", Font.BOLD, 12));
				g2.drawString(""+playedCards.get(i+3).getVal(), (float) (dimensions.getWidth()-(cardHeight+30) - (cardHeight/2 + offset))+cardWidth/2/2,
						(float) (((dimensions.getHeight()/2))+(i*(cardWidth/2+5))+10)+ cardHeight/2/2);
			}
	    }
	}

	public void drawTable(Graphics2D g2)
	{
		int[] lineup =  model.getLineup();
		Card[] temp = model.getTable();
		
		for(int i = -2; i < lineup.length-2; ++i)
		{
			if(model.getLife(lineup[i+2]) > 0){
				g2.setColor(model.getPlayerColor(lineup[i+2]));
				g2.fill(new Rectangle.Double((dimensions.getWidth()/2) + (i*(playerSize+5))+10,dimensions.getHeight()/2 - 2 *playerSize, playerSize,playerSize));
				
				g2.setColor(Color.BLACK);
				g2.setFont(new Font("default", Font.BOLD, 12));		
				g2.drawString(""+model.getLife(lineup[i+2]), (float) ((dimensions.getWidth()/2) + (i*(playerSize+5))+10) + playerSize/2 ,
									(float) (dimensions.getHeight()/2 - ((2 *playerSize)/2)) - playerSize/2);
			
				if(temp[i+2] != null){
					g2.setColor(temp[i+2].getColor());
					g2.fill(new RoundRectangle2D.Double((dimensions.getWidth()/2) + (i*(cardWidth+5))+10, 
							dimensions.getHeight()/2, cardWidth,cardHeight, 10,10));
					
					if(temp[i+2].getVisible()){
						g2.setColor(Color.BLACK);
						g2.setFont(new Font("default", Font.BOLD, 14));
						g2.drawString(""+temp[i+2].getVal(), (float) ((dimensions.getWidth()/2) + (i*(cardWidth+5))+10)+cardWidth/2,
									(float) (dimensions.getHeight()/2)+(cardHeight/2));
					}
				}
			}
		}
	}

}


public class GetBitView{
	
	private int windowWidth = 1000;
	private int windowHeight = 1000;
	JFrame frame;
	
	public GetBitView(GameModel model)
	{				
		frame = new JFrame("Get Bit!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(Color.GREEN);
		
		frame.setSize(windowWidth, windowHeight);	
		frame.setVisible(true);
		
		frame.getContentPane().add(new PlayerView(model, new Rectangle(windowWidth, windowHeight)), BorderLayout.CENTER);
	}
	
	public void updateView()
	{
		frame.getContentPane().revalidate();
		frame.getContentPane().repaint();		
	}
}




