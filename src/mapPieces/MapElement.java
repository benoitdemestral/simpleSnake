package mapPieces;

public class MapElement {
	private int x;
	private int y;
	private int worth;

	public MapElement(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public int worth() {
		return worth;
	}

	public boolean equals(MapElement that) {
		return this.x == that.x() && this.y == that.y();
	}
}
