package mapPieces;

public class BodyPiece extends MapElement {
	private boolean full;

	public BodyPiece(int x, int y, boolean full) {
		super(x, y);
		this.full = full;
	}

	public boolean isFull() {
		return full;
	}

	public void makeEmpty() {
		full = false;
	}

	public static enum Orientation {
		VERTICAL, HORIZONTAL, UPRIGHT, UPLEFT, DOWNRIGHT, DOWNLEFT
	}
}
