import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.Timer;

public class FlappyBird implements ActionListener, MouseListener, KeyListener {
	public static FlappyBird flappybird;
	public Renderer renderer;
	public Rectangle bird;
	public final int WIDTH = 800, HEIGHT=800;
	public ArrayList<Rectangle> columns;
	public Random rand;
	int ticks = 0;
	int yMotion = 0;
	public Boolean gameOver = false;
	public Boolean started = false;
	public int score = 0;
	public BufferedImage birdImage;
	
	public FlappyBird() {
		try {
			URL url = getClass().getResource("bird.png");
			System.out.println(url.toString());
			birdImage = ImageIO.read(new File(url.getPath()));
		}
		catch(Exception e) {
			System.out.println(e.toString());
		}
		Dimension screenSize = new Dimension(WIDTH, HEIGHT);
		Timer timer = new Timer(20, this);
		JFrame jframe = new JFrame();
		renderer = new Renderer();
		rand = new Random();
		jframe.add(renderer);
		jframe.setSize(screenSize);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setResizable(false);
		jframe.setTitle("Flappy Bird");
		jframe.addMouseListener(this);
		jframe.addKeyListener(this);
		jframe.setVisible(true);
		columns = new ArrayList<Rectangle>();
		
		bird = new Rectangle(WIDTH/2-10, HEIGHT/2-10, 20, 20);
		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);
		timer.start();
		
	}
	public void addColumn(Boolean start) {
		int space = 300;
		int width = 100;
		int height = 50 + rand.nextInt(300);
		
		if(start) {
		
		columns.add(new Rectangle(WIDTH + width + columns.size()*300,HEIGHT-height-120,width,height));
		columns.add(new Rectangle(WIDTH + width + (columns.size()-1) * 300,0,width,HEIGHT-height - space));
		}
		else {
			columns.add(new Rectangle(columns.get(columns.size()-1).x+600,HEIGHT - height-120,width,height));
			columns.add(new Rectangle(columns.get(columns.size()-1).x,0,width,HEIGHT-height-space));
		}
		
	}
	
	private void jump() {
	/*	if(gameOver) {
			gameOver = false;
			columns.clear();
			bird = new Rectangle(WIDTH/2-10, HEIGHT/2-10, 20, 20);
			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);
			yMotion = 0;
			score = 0;
		}
		*/
		if(!started) {
			started = true;			
		}
		else if (!gameOver) {
			if(yMotion>0) {
				yMotion = 0;
			}
			yMotion -=10;
		}
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ticks++;
		int speed = 10;
		if(started) {
			for(int i = 0; i< columns.size(); i++) {
				Rectangle column = columns.get(i);
				column.x -= speed;
				
			}
			if(ticks%2 ==0 && yMotion < 15) {
				yMotion+=2;
			}
			
			for(int i = 0; i< columns.size(); i++) {
				Rectangle column = columns.get(i);
				if(column.x + column.width < 0) {
					columns.remove(column);
					if(column.y == 0) {
						addColumn(false);
					}
				}
				
			}
			
			bird.y += yMotion;
			
			for(Rectangle column : columns) {
				if(column.y == 0 && bird.x + bird.width/2 > column.x + column.width/2 -10 && bird.x + bird.width/2 < column.x+column.width/2+10) {
					if(!gameOver) {
					//System.out.println("SKORE");
					score++;
					}
				}
				
				if(column.intersects(bird)) {
					gameOver = true;
					bird.x = column.x - bird.width;
					
				}
			}
			
			if(bird.y > HEIGHT - 120 || bird.y <= 0) {
				gameOver = true;
			}
		}
		if(bird.y + yMotion >= HEIGHT -120) {
			bird.y = HEIGHT - 120 - bird.height;
			gameOver = true;
		}
		
		
		renderer.repaint();
		
	}
	
	public void paintColumns(Graphics g, Rectangle column) {
		g.setColor(Color.green.darker());
		g.fillRect(column.x, column.y, column.width, column.height);
		
		
	}
	public void repaint(Graphics g) {
		//bakrunnur
		g.setColor(Color.cyan); 
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		//bird
		g.setColor(Color.red);
		//g.fillRect(bird.x, bird.y, bird.width, bird.height);
		g.drawImage(birdImage, bird.x, bird.y, null);
		
		g.setColor(Color.orange);
		g.fillRect(0, HEIGHT-120, WIDTH, 150);
		
		g.setColor(Color.green);
		g.fillRect(0, HEIGHT-120, WIDTH, 20);
		
		for(Rectangle A : columns) {
			paintColumns(g,A);
			
		}
		
		g.setColor(Color.white);
		g.setFont(new Font("Arial",1, 100));
		
		if(gameOver) {
			g.drawString("Game Over", 75, HEIGHT/2-50);
			
		}
		if(!started) {
			g.drawString("Click to start", 100, HEIGHT/2-50);			
		}
		
		if(started) {
			g.drawString(String.valueOf(score), WIDTH/2-25, 100);
		}
		
		if (started && gameOver) {
			g.setFont(new Font("Arial", 1, 40));
			g.drawString("Press Escape to restart!", 100, 400);
		}
		
		
		
	}

	public static void main(String[] args) {
		flappybird = new FlappyBird();
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		jump();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

		if(keyCode == KeyEvent.VK_UP) {
			jump();
		}
		if(gameOver && keyCode==KeyEvent.VK_ESCAPE) {
			gameOver = false;
			columns.clear();
			bird = new Rectangle(WIDTH/2-10, HEIGHT/2-10, 20, 20);
			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);
			yMotion = 0;
			score = 0;
		}
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
