
public class Weapon extends Item{
	private int dmg;
	
	public Weapon(String newName, float newWeight, int newDmg) {
		super(newName, newWeight);
		dmg = newDmg;
	}
	
/////////////////////////// Setters and Getters ///////////////////////////

	public int getDmg() {
		return dmg;
	}

	public void setDmg(int dmg) {
		this.dmg = dmg;
	}
} // end of Weapon class
