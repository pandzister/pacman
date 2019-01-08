package pacman;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Apple extends Rectangle{
	
	private static final long serialVersionUID = 1L;

	public Apple(int x,int y) {
		setBounds(x+14,y+14,4,4);
	}
	
	public void render(Graphics g) {
		g.setColor(new Color(214,231,171));
		g.fillRect(x,y,width,height);
		
	}

}
