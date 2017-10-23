package agent_game;

import java.awt.Color;
import java.awt.Rectangle;

public class Card {

//This class represents the cards in the game. Just getters and setters 
//to return or change important values.
	
	private int cardWidth = 70;
	private int cardHeight = 100;
			
	private Color cardColor;
	private int value;
	private boolean visible;
	
	public Card(int x, Color c, boolean v)	{
		this.value = x;
		this.cardColor = c;
		this.visible = true;
	}
	
	public int getVal()	{
		return value;
	}
	
	public Color getColor()	{
		return this.cardColor;
	}
	
	public void setVisible(boolean b)	{
		this.visible = b;
	}
	
	public boolean getVisible()	{
		return this.visible;
	}
	

}
