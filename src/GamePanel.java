import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.*;
import javax.swing.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GamePanel extends JPanel implements ActionListener {

	static final int SCREEN_WIDTH = 800;
	static final int SCREEN_HEIGHT = 800;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
	int DELAY = 100;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyparts = 3;
	int applesEaten;
	int appleX;
	int appleY;
	int speedX;
	int speedY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	int randomNum = ThreadLocalRandom.current().nextInt(1, 10);

	private enum STATE{
		MENU,
		GAME,
	};
	
	private STATE State = STATE.MENU;
	
	GamePanel() {
			
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.green);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
		

	}

	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);

	}

	public void draw(Graphics g) {
		if (running) {
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

			for (int i = 0; i < bodyparts; i++) {
				if (i == 0) {
					Color mato = new Color(219, 172, 18);
					g.setColor(mato);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					g.setColor(new Color(45, 180, 0));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);

				}
			}
			g.setColor(Color.red);
			g.setFont(new Font("Serif", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Pisteet: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Pisteet: " + applesEaten)) / 2,
					g.getFont().getSize());
			if ((applesEaten == randomNum) && (applesEaten > 0)) {
				g.setColor(Color.blue);
				g.fillOval(speedX, speedY, UNIT_SIZE, UNIT_SIZE);
			}
		} else {
			gameOver(g);

		}
	}

	public void move() {
		for (int i = bodyparts; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}
		switch (direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;

		}
	}

	public void newApple() {
		appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
		appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
	}
      // omenan tarkitus
	public void checkApple() {
		if ((x[0] == appleX) && (y[0] == appleY)) {
			bodyparts++;
			applesEaten++;
			newApple();
			if(applesEaten == randomNum) {
				speedX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
				speedY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
			}
					
			if(applesEaten > randomNum) {
				speedX = -1;
				speedY = -1;
				randomNum = ThreadLocalRandom.current().nextInt((applesEaten + 1), (applesEaten + 10));
			}
			
            // jos syö normaalin omenan arvottava uusi 2x omena, mutta ei saa arpoa joka
			// kerta
		}

	}

	public void newSpeed() {
			
	}

	public void checkSpeed() {
		if ((x[0] == speedX) && (y[0] == speedY)) {
			speedX = -1;
			speedY = -1;
			bodyparts++;
			applesEaten++;
			applesEaten++;
			randomNum = ThreadLocalRandom.current().nextInt((applesEaten + 1), (applesEaten + 10));
			newSpeed();
		}

	}

	public void checkCollisions() {
		for (int i = bodyparts; i > 0; i--) {
			if ((x[0] == x[i] && y[0] == y[i])) {
				running = false;
			}
		}

		if (x[0] < 0) {
			running = false;
		}

		if (x[0] > SCREEN_WIDTH) {
			running = false;
		}
		if (y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		if (y[0] < 0) {
			running = false;
		}
		if (!running) {
			timer.stop();

		}
	}
	

	public void gameOver(Graphics g) {
		g.setColor(Color.red);
		g.setFont(new Font("Serif", Font.BOLD, 100));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("GAME OVER", (SCREEN_WIDTH - metrics.stringWidth("GAME OVER")) / 2, SCREEN_HEIGHT / 2);

		g.setColor(Color.red);
		g.setFont(new Font("Serif", Font.BOLD, 40));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Pisteet: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Pisteet: " + applesEaten)) / 2,
				g.getFont().getSize());

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			move();
			checkCollisions();
			checkApple();
			checkSpeed();
			

		}
		repaint();

	}

	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {

			case KeyEvent.VK_LEFT:
				if (direction != 'R') {
					direction = 'L';
					break;
				}
			case KeyEvent.VK_RIGHT:
				if (direction != 'L') {
					direction = 'R';
					break;
				}
			case KeyEvent.VK_UP:
				if (direction != 'D') {
					direction = 'U';
					break;
				}
			case KeyEvent.VK_DOWN:
				if (direction != 'U') {
					direction = 'D';
					break;
				}
			}

		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
