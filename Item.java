
public class Item {
	private String name;
	private float weight;
	
	Item (String newName, float newWeight) {
		name = newName;
		weight = newWeight;
	} // end of Item constructor
	
/////////////////////////// Setters and Getters ///////////////////////////

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}
} // end of Item class
