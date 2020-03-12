import java.util.ArrayList;

public class Dwarf extends Player {

	Dwarf(ArrayList<Item> newInventory, float newScore, int newMoves) {
		super(newInventory, newScore, newMoves);
		this.setMaxWeight(30);
	}
}
