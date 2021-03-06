package pacman;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable,KeyListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean isRunning = false;
	
	public static final int WIDTH = 640,HEIGHT = 480;
	public static final String TITLE = "PAC-MAN";
	
	private Thread thread;
	
	public static Player player;
	public static Level level;
	public static SpriteSheet spritesheet;
	
	public static final int PAUSE_SCREEN = 0,GAME = 1;
	public static int STATE = -1;
	
	public boolean isSpace = false;
	
	private int time = 0;
	private int targetFrames = 45;
	private boolean showText = true;
	
	public Game() {
		Dimension dimension = new Dimension(Game.WIDTH,Game.HEIGHT);
		setPreferredSize(dimension);
		setMinimumSize(dimension);
		setMaximumSize(dimension);
		
		addKeyListener(this);
		STATE = PAUSE_SCREEN;

		spritesheet = new SpriteSheet("/pacmansheet.png");
		
		new Texture();
	}
	
	public synchronized void start() {
		if(isRunning) return;
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop() {
		if (isRunning) return;
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void tick() {
		if(STATE == GAME) {
		player.tick();
		level.tick();
		}else if(STATE == PAUSE_SCREEN) {
			time++;
			if(time == targetFrames) {
				time = 0;
				if(showText) {
					showText = false;
				}else {
					showText = true;
				}
			}
			if(isSpace) {
				isSpace = false;
				player = new Player(Game.WIDTH/2,Game.HEIGHT/2);
				level = new Level("/map.png");
				STATE = GAME;
			}
		}
		
	}
	
	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.setColor(new Color(83,24,124));
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		if(STATE == GAME) {
			player.render(g);
			level.render(g);
		}else if(STATE == PAUSE_SCREEN) {
			int boxWidth = 480;
			int boxHeight = 360;
			int xx = Game.WIDTH / 2 - boxWidth/2;
			int yy = Game.HEIGHT / 2 - boxHeight/2;
			g.setColor(new Color(224,183,214));
			g.fillRect(xx, yy, boxWidth, boxHeight);
			
			g.setColor(new Color(83,24,124));
			g.setFont(new Font(Font.DIALOG,Font.BOLD,20));
			if(showText) g.drawString("Press space to start new game!", xx+70, yy+190);
				
		}
		g.dispose();
		bs.show();
		
	}
	
	public void run() {
		requestFocus();
		int fps = 0;
		double timer = System.currentTimeMillis();
		long lastTime = System.nanoTime();
		double targetTick = 60.0;
		double delta = 0;
		double ns = 1000000000/targetTick;
		
		while(isRunning) {
			long now = System.nanoTime();
			delta+=(now - lastTime) / ns;
			lastTime = now;
			
			while(delta >= 1) {
				tick();
				render();
				fps++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >=1000) {
				System.out.println(fps);
				fps = 0;
				timer+=1000;
			}
				
		}
		
		stop();
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		JFrame frame = new JFrame(Game.TITLE);
		frame.add(game);
		frame.setResizable(false);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		frame.setVisible(true);
		
		game.start();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(STATE == GAME) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) player.right = true;
		if(e.getKeyCode() == KeyEvent.VK_LEFT) player.left = true;
		if(e.getKeyCode() == KeyEvent.VK_UP) player.up = true;
		if(e.getKeyCode() == KeyEvent.VK_DOWN) player.down = true;
		}else if(STATE == PAUSE_SCREEN) {
			if(e.getKeyCode() == KeyEvent.VK_SPACE) {
				isSpace = true;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) player.right = false;
		if(e.getKeyCode() == KeyEvent.VK_LEFT) player.left = false;
		if(e.getKeyCode() == KeyEvent.VK_UP) player.up = false;
		if(e.getKeyCode() == KeyEvent.VK_DOWN) player.down = false;
	}

}
