package gamePieces;

import gui.SimpleSnake;

public class Game {
	private World world;
	private boolean running = false;
	public SimpleSnake simpleSnake;
	private final int BASE_SPEED = 200;
	private final int MINIMUM_SPEED = 40;
	private final int SPEED_BOOST = 20;
	private int nonBoostedSpeed = BASE_SPEED;
	private int speed = BASE_SPEED;

	public Game(int size, SimpleSnake simpleSnake) {
		world = new World(size, this);
		this.simpleSnake = simpleSnake;
	}

	public void reset() {
		speed = BASE_SPEED;
		nonBoostedSpeed = BASE_SPEED;
	}

	public void runGameLoop() {
		running = true;
		Thread loop = new Thread() {
			public void run() {
				gameLoop();
			}
		};
		loop.start();
	}

	public void defeat() {
		running = false;
		simpleSnake.defeat();
		simpleSnake.repaint();
	}

	private void gameLoop() {
		while (running) {
			world.update();
			if (!running)
				break;
			simpleSnake.repaint();
			try {
				Thread.sleep(speed);
			} catch (Exception e) {
			}
			if (speed < nonBoostedSpeed)
				speed++;
		}
		// post-defeat screen.
	}

	public void speedUp() {
		if (speed >= MINIMUM_SPEED)
			speed -= SPEED_BOOST;
	}

	public World world() {
		return world;
	}

	public void changeNonBoostedSpeed(int scoreUpBy) {
		nonBoostedSpeed -= scoreUpBy;
	}
}
