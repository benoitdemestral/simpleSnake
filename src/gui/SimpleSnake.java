package gui;

import gamePieces.Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import mapPieces.BodyPiece;
import mapPieces.Food;

public class SimpleSnake extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel gamePanel;
	private static final int GAME_SIZE = 25;
	private static final int TILE_SIZE = 30;
	private Queue<String> commandQueue = new LinkedList<>();
	private static boolean started = false;
	private Game game = new Game(GAME_SIZE, this);
	private GameWindow gw = new GameWindow(game);

	private static boolean defeat = false;

	public SimpleSnake() {
		super("SimpleSnake");
		gamePanel = new JPanel(new BorderLayout()) {
			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(GAME_SIZE * TILE_SIZE, GAME_SIZE * TILE_SIZE);
			}
		};
		gamePanel.setFocusable(true);
		gamePanel.requestFocusInWindow();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// final Game game = new Game(GAME_SIZE, this); // SHOULD THIS BE FINAL ?
		gamePanel.add(gw, BorderLayout.CENTER);
		add(gamePanel);
		setVisible(true);
		pack();
		setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2);
		gamePanel.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				String direction = "";
				switch (key) {
				case 38:
				case 87:
					direction = "up";
					break;
				case 37:
				case 65:
					direction = "left";
					break;
				case 40:
				case 83:
					direction = "down";
					break;
				case 39:
				case 68:
					direction = "right";
					break;
				case 32: // space
					if (defeat) {
						// reinitialise everything
						reinit();
					}
					break;
				default:
					break;
				}

				if (!started && !direction.equals("")) {
					started = true;
					game.world().snake().changeDirection(direction);
				}
				if (commandQueue.size() < 3) {
					commandQueue.add(direction);
				}
			}
		});
	}

	public void reinit() {
		started = false;
		defeat = false;
		game.world().snake().reset();
		game.world().reset();
		game.reset();
		gw.resetScore();
		repaint();
	}

	public String getCommand() {
		return commandQueue.poll();
	}

	public void defeat() {
		defeat = true;
	}

	public static void main(String[] args) {
		new SimpleSnake();
	}

	public static class GameWindow extends JComponent {
		private static final long serialVersionUID = 1L;
		private static Game game;
		private static int score;

		@SuppressWarnings("static-access")
		public GameWindow(Game game) {
			this.game = game;
			score = 0;
		}

		@Override
		public void paintComponent(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			Rectangle visibleRect = getVisibleRect();
			int x = visibleRect.x;
			int y = visibleRect.y;
			int width = visibleRect.width;
			int height = visibleRect.height;

			// --- BACKGROUND ---

			BufferedImage bgImg = null;
			try {
				bgImg = ImageIO.read(new File("data/bg_big.jpg"));
			} catch (Exception e) {
			}
			if (bgImg != null) {
				g2d.drawImage(bgImg, 0, 0, null);
			} else {
				g2d.setColor(Color.DARK_GRAY);
				g2d.fillRect(x, y, width, height);
				g2d.setColor(new Color(60, 60, 60));

				for (int i = 0; i < GAME_SIZE; i++) {
					for (int j = 0; j < GAME_SIZE; j++) {
						g2d.drawRect(i * TILE_SIZE, j * TILE_SIZE, 1000, 1000);
					}
				}
			}

			// --- FOOD ---
			HashSet<Food> food = game.world().getFood();
			BufferedImage bonusImg = null;
			BufferedImage foodImg = null;
			try {
				bonusImg = ImageIO.read(new File("data/bonus.png"));
				foodImg = ImageIO.read(new File("data/food.png"));
			} catch (Exception e) {
			}
			for (Food foodItem : food) {
				if (foodItem.worth() > 1)
					g2d.drawImage(bonusImg, foodItem.x() * TILE_SIZE, foodItem.y() * TILE_SIZE, null);
				else
					g2d.drawImage(foodImg, foodItem.x() * TILE_SIZE, foodItem.y() * TILE_SIZE, null);
			}

			// --- BODY ---
			BufferedImage tailImg = null;
			BufferedImage fullTailImg = null;
			try {
				tailImg = ImageIO.read(new File("data/tail_final.png"));
				fullTailImg = ImageIO.read(new File("data/fullTail_final.png"));
			} catch (Exception e) {
			}
			for (BodyPiece piece : game.world().snake().getTrail()) {
				if (tailImg != null && fullTailImg != null) {
					if (piece.isFull())
						g2d.drawImage(fullTailImg, piece.x() * TILE_SIZE, piece.y() * TILE_SIZE, null);
					else
						g2d.drawImage(tailImg, piece.x() * TILE_SIZE, piece.y() * TILE_SIZE, null);

				}
			}

			// --- HEAD ---
			BodyPiece head = game.world().snake().getHead();
			// g2d.setColor(Color.GRAY);
			// g2d.fillRect(head.x() * TILE_SIZE, head.y() * TILE_SIZE, TILE_SIZE, TILE_SIZE);

			BufferedImage headImg = null;
			try {
				headImg = ImageIO.read(new File("data/head_final.jpg"));
			} catch (Exception e) {
			}
			if (headImg != null) {
				g2d.drawImage(headImg, head.x() * TILE_SIZE, head.y() * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
			} else {
				g2d.setColor(Color.GRAY);
				g2d.fillRect(head.x() * TILE_SIZE, head.y() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
			}

			// --- SCORE ---
			g2d.setColor(Color.BLACK);
			g2d.fillRect(5, 5, 24, 10);
			g2d.setColor(Color.WHITE);
			String scoreString = "";
			if (score < 10) {
				scoreString = "00" + score;
			} else if (score < 100) {
				scoreString = "0" + score;
			} else {
				scoreString = Integer.toString(score);
			}
			g2d.drawString(scoreString, 5, 15);
			// nothing is to be drawn after this!

			if (defeat) {
				g2d.setColor(new Color(255, 40, 40, 150));
				g2d.fillRect(0, 0, getWidth(), getHeight());

				g.setFont(new Font("Arial", Font.BOLD, 40));
				g2d.setColor(Color.WHITE);

				TextLayout layout = new TextLayout("Final Score : " + score, g.getFont(), g2d.getFontRenderContext());
				Rectangle2D bounds = layout.getBounds();
				layout.draw(g2d, (float) ((GAME_SIZE * TILE_SIZE - bounds.getWidth()) / 2.0),
						(float) ((GAME_SIZE * TILE_SIZE + bounds.getHeight()) / 2.0) - TILE_SIZE * 3);
				TextLayout layout2 = new TextLayout("Press SPACE to start again.", g.getFont(), g2d.getFontRenderContext());
				Rectangle2D bounds2 = layout2.getBounds();
				layout2.draw(g2d, (float) ((GAME_SIZE * TILE_SIZE - bounds2.getWidth()) / 2.0),
						(float) ((GAME_SIZE * TILE_SIZE + bounds2.getHeight()) / 2.0) + TILE_SIZE * 3);
			}
		}

		public int score() {
			return score;
		}

		public void resetScore() {
			score = 0;
		}

		public static void augmentScore(int value) {
			if (value < 0) {
				throw new IllegalArgumentException("Augmented score with a negative value !");
			}
			score += value;
			game.changeNonBoostedSpeed(value * 5);
		}
	}
}
