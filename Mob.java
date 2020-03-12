
public class Mob {
	private String name;
	private String desc;
	private int powerLvl;
	Boolean defeated;

	public Mob(String newName, String newDesc, int newPowerLvl, Boolean newDefeated) {
		name = newName;
		desc= newDesc;
		powerLvl = newPowerLvl;
		defeated = newDefeated;
	}
	
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

	public int getPowerLvl() {
		return powerLvl;
	}

	public void setPowerLvl(int powerLvl) {
		this.powerLvl = powerLvl;
	}
	
	public Boolean getDefeated() {
		return defeated;
	}
	
	public void setDefeated(Boolean defeated) {
		this.defeated = defeated;
	}
} // end of Mob class
