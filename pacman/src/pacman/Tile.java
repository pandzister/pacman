package pacman;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Tile extends Rectangle{
	
	private static final long serialVersionUID = 1L;

	public Tile(int x,int y) {
		setBounds(x,y,32,32);
	}
	
	public void render(Graphics g) {
		g.setColor(new Color(224,183,214));
		g.fillRect(x,  y,  width,  height);
		
	}

}
