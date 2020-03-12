import java.util.ArrayList;

public class Room {
	private String name;
	private String desc;
	private ArrayList<Item> items;
	private Mob mob;

	public Room(String newName, String newDesc, ArrayList<Item> newItems, Mob newMob) {
		name = newName;
		desc= newDesc;
		items = newItems;
		mob = newMob;
	} // end of Room constructor
	
/////////////////////////// Setters and Getters ///////////////////////////

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

	public Mob getMob() {
		return mob;
	}

	public void setMob(Mob mob) {
		this.mob = mob;
	}
} // end of Room class
