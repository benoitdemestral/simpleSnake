package mapPieces;

public class Food extends MapElement {

	private int worth;
	private int duration;

	public Food(int x, int y, int worth, int duration) {
		super(x, y);
		this.worth = worth;
		this.duration = duration;
	}

	public int worth() {
		return worth;
	}
	
	public int duration(){
		return duration;
	}
	
	public void lowerDuration(){
		duration--;
	}

}
