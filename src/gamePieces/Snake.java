package gamePieces;

import java.util.ArrayList;
import java.util.HashSet;

import mapPieces.BodyPiece;
import mapPieces.Food;

public class Snake {
	private int headX;
	private int headY;
	private int worldSize;
	private Game game;
	private String direction;
	private ArrayList<BodyPiece> trail;
	private HashSet<Food> foodMap;
	private boolean hasJustEaten = false;
	private boolean hasEatenLastTurn = false;
	private int length = 0;
	private final int STARTING_LENGTH = 6;

	public Snake(int worldSize, Game game) {
		this.worldSize = worldSize;
		this.game = game;
		headX = (worldSize) / 2;
		headY = (worldSize) / 2;
		trail = new ArrayList<BodyPiece>();
		this.direction = "";
	}

	public void reset() {
		headX = (worldSize) / 2;
		headY = (worldSize) / 2;
		trail = new ArrayList<BodyPiece>();
		direction = "";
	}

	public boolean move() {
		hasEatenLastTurn = hasJustEaten;
		hasJustEaten = false;
		if (trail.size() > 0) {
			length = trail.size() + 1;
			if (trail.get(0).isFull()) {
				trail.get(0).makeEmpty();
			} else if (length >= STARTING_LENGTH) {
				trail.remove(0);
			}
		}
		int nextX = headX;
		int nextY = headY;
		switch (direction) {
		case "up":
			nextY--;
			break;
		case "down":
			nextY++;
			break;
		case "left":
			nextX--;
			break;
		case "right":
			nextX++;
			break;
		}
		if (trailContains(new BodyPiece(nextX, nextY, false))) {
			game.defeat();
			// defeat (ouroboros)
		}
		if (nextX == worldSize || nextX == -1 || nextY == worldSize || nextY == -1) {
			game.defeat();
			// defeat (wall-plant)
		}
		Food next = new Food(nextX, nextY, 0, -1);
		Food actualNext = foodMapContains(next);
		if (actualNext != null) {
			game.world().removeFood(next);
			hasJustEaten = true;
			game.speedUp();
			gui.SimpleSnake.GameWindow.augmentScore(actualNext.worth());
		}
		if (hasEatenLastTurn)
			trail.add(new BodyPiece(headX, headY, true));
		else
			trail.add(new BodyPiece(headX, headY, false));
		headX = nextX;
		headY = nextY;

		return hasJustEaten && actualNext.worth() == 1;
	}

	public void changeDirection(String newDirection) {

		String oldDirection = direction;

		if (oldDirection.equals("") && !newDirection.equals("")) {
			game.runGameLoop();
		}
		// no U turns
		switch (newDirection) {
		case "up":
			if (!direction.equals("down"))
				direction = newDirection;
			break;
		case "down":
			if (!direction.equals("up"))
				direction = newDirection;
			break;
		case "left":
			if (!direction.equals("right"))
				direction = newDirection;
			break;
		case "right":
			if (!direction.equals("left"))
				direction = newDirection;
			break;
		default:
			break;
		}

	}

	public ArrayList<BodyPiece> getTrail() {
		return trail;
	}

	public boolean trailContains(BodyPiece that) {
		ArrayList<BodyPiece> newTrail = new ArrayList<>(trail);
		for (BodyPiece t : newTrail) {
			if (t.equals(that)) {
				return true;
			}
		}
		return false;
	}

	public Food foodMapContains(Food that) {
		for (Food f : foodMap) {
			if (that.equals(f)) {
				return f;
			}
		}
		return null;
	}

	public BodyPiece getHead() {
		return new BodyPiece(headX, headY, false);
	}

	public void giveFoodMap(HashSet<Food> foodMap) {
		this.foodMap = new HashSet<Food>(foodMap);
	}

}
