package gamePieces;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import mapPieces.BodyPiece;
import mapPieces.Food;
import mapPieces.MapElement;

public class World {
	private Random rand = new Random();
	private int size;
	private Snake snake;
	private HashSet<Food> foodMap;
	private final int FOOD_BASE_WORTH = 1;
	private final int FOOD_BONUS_WORTH = 3;
	private final int FOOD_BASE_DURATION = -1;
	private final int FOOD_BONUS_DURATION = 30;
	private Game game;

	public World(int size, Game game) {
		this.size = size;
		this.game = game;
		snake = new Snake(size, game);
		foodMap = new HashSet<>();
		addFood(FOOD_BASE_WORTH, FOOD_BASE_DURATION);
	}

	public void reset() {
		foodMap = new HashSet<>();
		addFood(FOOD_BASE_WORTH, FOOD_BASE_DURATION);
	}

	public Game game() {
		return game;
	}

	public void addFood(int worth, int duration) {
		Food newFood = new Food(rand.nextInt(size), rand.nextInt(size), worth, duration);
		ArrayList<BodyPiece> trail = snake.getTrail();
		BodyPiece head = snake.getHead();

		while (trail.contains(newFood) || newFood.equals(head)) {
			newFood = new Food(rand.nextInt(size), rand.nextInt(size), worth, duration);
		}
		foodMap.add(newFood);
		snake.giveFoodMap(foodMap);
	}

	public void update() {
		String nextCommand = game.simpleSnake.getCommand();
		if (nextCommand != null) {
			snake.changeDirection(nextCommand);
		}
		//
		HashSet<Food> newFoodMap = new HashSet<>(foodMap);
		for (Food f : newFoodMap) {
			if (f.duration() == 0) {
				foodMap.remove(f);
			}
		}
		for (Food f : foodMap) {
			if (f.duration() > 0) {
				f.lowerDuration();
			}
		}
		snake.giveFoodMap(foodMap);
		if (snake.move()) {
			addFood(FOOD_BASE_WORTH, FOOD_BASE_DURATION);
		}

		if (rand.nextDouble() < 0.015) {
			addFood(FOOD_BONUS_WORTH, FOOD_BONUS_DURATION); // special food
		}
	}

	public void removeFood(MapElement next) {
		HashSet<Food> newFoodMap = new HashSet<>(foodMap);
		for (Food f : newFoodMap) {
			if (f.equals(next)) {
				foodMap.remove(f);
			}
		}
		snake.giveFoodMap(foodMap);
	}

	public Snake snake() {
		return snake;
	}

	public HashSet<Food> getFood() {
		return foodMap;
	}
}
