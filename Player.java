import java.util.ArrayList;

public class Player {
	private float maxWeight;
	private ArrayList<Item> inventory;
	private float currentWeight;
	private float score;
	private int moves;
	
	Player(ArrayList<Item> newInventory, float newScore, int newMoves) {
		setMaxWeight(20);
		setInventory(newInventory);
		setCurrentWeight(0);
		setScore(newScore);
		setMoves(newMoves);
	}
	
	public ArrayList<Item> getInventory() {
		return inventory;
	}
	public void setInventory(ArrayList<Item> inventory) {
		this.inventory = inventory;
	}
	
	public float getMaxWeight() {
		return maxWeight;
	}
	public void setMaxWeight(float maxWeight) {
		this.maxWeight = maxWeight;
	}

	public float getCurrentWeight() {
		return currentWeight;
	}

	public void setCurrentWeight(float currentWeight) {
		this.currentWeight = currentWeight;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public int getMoves() {
		return moves;
	}

	public void setMoves(int moves) {
		this.moves = moves;
	}
}
