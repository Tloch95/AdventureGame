
public class Treasure extends Item{
	private int value;
	
	public Treasure (String newName, float newWeight, int newValue) {
		super(newName, newWeight);
		value = newValue;
	} // end of Treasure constructor

/////////////////////////// Setters and Getters ///////////////////////////
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
} // end of Treasure class
