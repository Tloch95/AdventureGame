import java.util.ArrayList;

public class Ranger extends Player {

	Ranger(ArrayList<Item> newInventory, float newScore, int newMoves) {
		super(newInventory, newScore, newMoves);
	}
	
	@Override
	public int getMoves() {
		return (int) Math.floor(this.getMoves() / 2);
	}
}
