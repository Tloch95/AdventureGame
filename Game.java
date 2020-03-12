import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

public class Game {
	private static HashMap<String, Item> treasures = new HashMap<String, Item>();
	private static HashMap<String, Item> weapons = new HashMap<String, Item>();
	private static HashMap<String, Mob> mobs = new HashMap<String, Mob>();
	private static LinkedHashMap<String, Room> rooms = new LinkedHashMap<String, Room>();
	private static List<Room> startRooms = new ArrayList<Room>();
	private static List<Directions> roomConnectionDirections = new ArrayList<Directions>();
	private static List<Room> endRooms = new ArrayList<Room>();
	private static Player player = null;
	private static Room stashRoom = null;

	public static void main(String[] args) {
		readGameFile(args);
		playGame();
	} // end of main method

	// This method does most of the work for playing the game, and calls the method that takes care of taking commands (REPL).
	private static void playGame() {
		stashRoom = rooms.get(rooms.entrySet().toArray()[0]);
		Scanner input = new Scanner(System.in);
		System.out.println("Welcome to the adventure!");
		System.out.println("What class would you like to play as (Warrior, Dwarf, or Ranger)?");
		Boolean validCharChoice = false;
		do {
			String playerClass = input.next(); 
			if (playerClass.toLowerCase().equals("warrior")) {
				player = new Warrior(null, 0, 0);
				validCharChoice = true;
			} else if (playerClass.toLowerCase().equals("dwarf")) {
				validCharChoice = true;
				player = new Dwarf(null, 0, 0);
			} else if (playerClass.toLowerCase().equals("ranger")) {
				validCharChoice = true;
				player = new Ranger(null, 0, 0);
			} else {
				System.out.println("Invalid choice.");
			} // end of if
		} while (!validCharChoice);
		
		System.out.println("Starting the adventure...");
		Boolean gameFinished = false;
		do {
			startREPL(gameFinished, input);
		} while (!gameFinished);
		System.exit(0);
	} // end of playGame method

	// This is the method for taking commands (REPL).
	private static void startREPL(Boolean gameFinished, Scanner input) {
		Room currentRoom = stashRoom;
		do {
			System.out.println("> ");
			switch (input.next().toLowerCase()) {
				case "look": 
					System.out.println("Name: " + currentRoom.getName());
					System.out.println("Description: " + currentRoom.getDesc());
					System.out.println("There are connections in the following directions: ");
					List<Directions> dirsToReport = new ArrayList<Directions>();
					for (Room room : startRooms) {
						if (room.equals(currentRoom)) {
							dirsToReport.add(roomConnectionDirections.get(startRooms.indexOf(room)));
						} // end of if
					} // end of for
					for (Directions dir : dirsToReport) {
						System.out.print(dir); 
						if (!dir.equals(dirsToReport.indexOf(dirsToReport.size()))) {
							System.out.print(", ");
						} // end of if
					} // end of for
					System.out.println("Items: ");
					if (currentRoom.getItems() != null) {
						List<String> itemNames = new ArrayList<String>();
						for (Item item : currentRoom.getItems()) {
							itemNames.add(item.getName());
						}
						String[] sortedItemNames = (String[]) itemNames.toArray();
						Arrays.sort(sortedItemNames);
						for (String item : sortedItemNames) {
							if (sortedItemNames.length == 1) {
								System.out.print(sortedItemNames[0]);
							} else {
								System.out.print(item);
								if (!item.equals(sortedItemNames[sortedItemNames.length - 1])) {
									System.out.print(", ");
								} // end of if
							} // end of if
						} // end of for
					} else {
						System.out.print("none");
					} // end of if
					System.out.println("Mob: ");
					if (currentRoom.getMob() != null) { 
						System.out.print(currentRoom.getMob().getName());
					} else {
						System.out.print("none");
					} // end of if
					break;
				case "examine":
					String thingToExamine = input.nextLine();
					if (thingToExamine.equals(currentRoom.getMob().getName())) {
						System.out.println("Name: " + currentRoom.getMob().getName());
						System.out.println("Description: " + currentRoom.getMob().getDesc());
					} // end of if
					
					for (Item item : currentRoom.getItems()) {
						if (thingToExamine.equals(item.getName())) {
							if (item instanceof Weapon) {
								Weapon weapon = (Weapon) item;
								System.out.println("Name: " + weapon.getName());
								System.out.println("Weight: " + weapon.getWeight());
								System.out.println("Value: " + weapon.getDmg());
							} else if (item instanceof Treasure) {
								Treasure treasure = (Treasure) item;
								System.out.println("Name: " + treasure.getName());
								System.out.println("Weight: " + treasure.getWeight());
								System.out.println("Value: " + treasure.getValue());
							} // end of if
						} // end of if
					} // end of for
					break;
				case "move":
					String direction = input.nextLine().trim();
					Directions dir = null;
					if (direction.equals(Directions.EAST)) {
						dir = Directions.EAST;
					} else if (direction.equals(Directions.WEST)) {
						dir = Directions.WEST;
					} else if (direction.equals(Directions.NORTH)) {
						dir = Directions.NORTH;
					} else if (direction.equals(Directions.SOUTH)) {
						dir = Directions.SOUTH;
					} else {
						System.out.println("Invalid direction.");
					} // end of if
					if (dir != null) {
						for (Room room : startRooms) {
							if (roomConnectionDirections.get(startRooms.indexOf(room)).equals(dir) && room.equals(currentRoom)) {
								currentRoom = endRooms.get(startRooms.indexOf(room));
							} else {
								System.out.println("Invalid direction.");
							} // end of if
						} // end of for
					} // end of if
					break;
				case "pickup":
					if (currentRoom.getMob().getDefeated() == false) {
						System.out.println("You must destroy the mob first.");
						break;
					}
					Boolean itemToPickupFound = false;
					String itemToPickup = input.nextLine().trim();
					for (Item item : currentRoom.getItems()) {
						if ((itemToPickup.equals(item.getName())) && (currentRoom.getMob().getDefeated() == true)) {
							player.getInventory().add(item);
							if (player.getCurrentWeight() + item.getWeight() <= player.getMaxWeight()) {
								player.setCurrentWeight(player.getCurrentWeight() + item.getWeight());
								itemToPickupFound = true;
							} else {
								System.out.println("I cannot carry this item.");
							} // end of if
						} // end of if
					} // end of for
					if (!itemToPickupFound) {
						System.out.println("Invalid item.");
					}
					break;
				case "drop":
					Boolean itemToDropFound = false;
					String itemToDrop = input.nextLine().trim();
					if (player.getInventory() != null) {
						for (Item item : player.getInventory()) {
							if (itemToDrop.equals(item.getName())){
								player.getInventory().remove(item);
								currentRoom.getItems().add(item);
								itemToDropFound = true;
							} // end of if
						} // end of for
						if (!itemToDropFound) {
							System.out.println("Invalid item.");
						} // end of if
					} else {
						System.out.println("Invalid item.");
					} // end of if
				case "stash":
					Boolean itemToStashFound = false;
					String itemToStash = input.nextLine().trim();
					if (currentRoom != stashRoom) {
						System.out.println("Invalid command.");
					} else {
						for (Item item : player.getInventory()) {
							if (itemToStash.equals(item.getName()) && item instanceof Treasure) {
								Treasure treasureToStash = (Treasure) item;
								player.getInventory().remove(item);
								player.setScore(player.getScore() + treasureToStash.getValue());
								itemToStashFound = true;
							} // end of if
						} // end of for
					} // end of if
					
					if (!itemToStashFound) {
						System.out.println("Invalid item.");
					} // end of if
				case "fight":
					Boolean itemToFightWithFound = false;
					String itemToFightWith = input.nextLine().trim();
					Mob mobToFight = currentRoom.getMob();
					if (mobToFight != null) {
						for (Item item : player.getInventory()) {
							if (itemToFightWith.equals(item.getName()) && item instanceof Weapon) {
								itemToFightWithFound = true;
								Weapon weaponToFightWith = (Weapon) item;
								if (weaponToFightWith.getDmg() >= mobToFight.getPowerLvl()) {
									System.out.println("You destroyed " + mobToFight.getName() + " with power level " + mobToFight.getPowerLvl() + "!");
									currentRoom.setMob(null);
								} else {
									System.out.println(mobToFight.getName() + " defeated you! Try a different weapon.");
								} // end of if
							} // end of if
						} // end of for
						
						if (!itemToFightWithFound) {
							System.out.println("Invalid weapon.");
						} // end of if
					} else {
						System.out.println("No mob found.");
					} // end of if
				case "inventory":
					List<String> inventoryItemNames = new ArrayList<String>();
					for (Item item : player.getInventory()) {
						inventoryItemNames.add(item.getName());
					}
					String[] sortedInventoryNames = (String[]) inventoryItemNames.toArray();
					Arrays.sort(sortedInventoryNames);
					List<Item> sortedInventoryItems = new ArrayList<Item>();
					for (String item : sortedInventoryNames) {
						if (weapons.get(item) != null) {
							sortedInventoryItems.add(weapons.get(item));
						} else {
							sortedInventoryItems.add(treasures.get(item));
						} // end of if
					} // end of for
					
					for (Item item : sortedInventoryItems) {
						if (weapons.get(item.getName()) != null) {
							Weapon inventoryWeapon = (Weapon) item;
							System.out.println("Name " + inventoryWeapon.getName());
							System.out.println("Weight: " + inventoryWeapon.getWeight());
							System.out.println("Power: " + inventoryWeapon.getDmg());
							System.out.println();
						} else {
							Treasure inventoryTreasure = (Treasure) item;
							System.out.println("Name " + inventoryTreasure.getName());
							System.out.println("Weight: " + inventoryTreasure.getWeight());
							System.out.println("Value: " + inventoryTreasure.getValue());
							System.out.println();
						} // end of if
					} // end of for
				case "quit":
					float finalScore;
					if (player.getMoves() != 0) {
						finalScore = (player.getScore() / player.getMoves());
					} else {
						finalScore = 0;
					}
					System.out.println("Finishing game...");
					System.out.println("Final score: " + finalScore);
					System.exit(0);
					break;
				default:
					System.out.println("Invalid command.");
			} // end of switch
		} while (!gameFinished);
	}

	// This method reads in the game file, and "sets up" the game. It takes the command line arguments as its parameter, in order to open the file.
	private static void readGameFile(String[] args) {
		Scanner gameFileInput = null;
		try {
			File gameFile = new File(args[0]);
			gameFileInput = new Scanner(gameFile);
		} catch (FileNotFoundException e) {
			System.out.println("File not found/given or incorrect! Exiting...");
			System.exit(1);
		} // end of try catch
		
		while (gameFileInput.hasNext()) {
			if (gameFileInput.next().trim().toLowerCase().equals("define")) {
				switch (gameFileInput.next().trim().toLowerCase()) {
					case "treasure":
						String[] nameLine = gameFileInput.nextLine().split(":");
						String newTreasureName = nameLine[1].trim();
						String[] weightLine = gameFileInput.nextLine().split(":");
						float newTreasureWeight = Float.parseFloat(weightLine[1].trim());
						String[] valueLine = gameFileInput.nextLine().split(":");
						int newTreasureValue = Integer.parseInt(valueLine[1].trim());
						Item newTreasure = new Treasure(newTreasureName, newTreasureWeight, newTreasureValue);
						if (treasures.containsKey(newTreasureName)) {
							System.out.println("Treasure " + newTreasureName + " seems to be defined more than once. Please make necessary adjustments and try again.");
							System.exit(1);
						} else {
							treasures.put(newTreasureName, newTreasure);
						} // end of if
						break;
					case "weapon":
						nameLine = gameFileInput.nextLine().split(":");
						String newWeaponName = nameLine[1].trim();
						weightLine = gameFileInput.nextLine().split(":");
						float newWeaponWeight = Float.parseFloat(weightLine[1].trim());
						String[] dmgLine = gameFileInput.nextLine().split(":");
						int newWeaponDmg = Integer.parseInt(dmgLine[1].trim());
						Item newWeapon = new Weapon(newWeaponName, newWeaponWeight, newWeaponDmg);
						if (treasures.containsKey(newWeaponName)) {
							System.out.println("Weapon " + newWeaponName + " seems to be defined more than once. Please make necessary adjustments and try again.");
							System.exit(1);
						} else {
							weapons.put(newWeaponName, newWeapon);
						} // end of if
						break;
					case "mob":
						nameLine = gameFileInput.nextLine().split(":");
						String newMobName = nameLine[1].trim();
						String[] descLine = gameFileInput.nextLine().split(":");
						String newMobDesc = descLine[1].trim();
						String[] powerLvlLine = gameFileInput.nextLine().split(":");
						int newMobPowerLvl = Integer.parseInt(powerLvlLine[1].trim());
						Mob newMob = new Mob(newMobName, newMobDesc, newMobPowerLvl, false);
						if (treasures.containsKey(newMobName)) {
							System.out.println("Mob " + newMobName + " seems to be defined more than once. Please make necessary adjustments and try again.");
							System.exit(1);
						} else {
							mobs.put(newMobName, newMob);
						} // end of if
						break;
					case "room":
						nameLine = gameFileInput.nextLine().split(":");
						String newRoomName = nameLine[1].trim();
						descLine = gameFileInput.nextLine().split(":");
						String newRoomDesc = descLine[1].trim();
						ArrayList<Item> newRoomItems = new ArrayList<Item>();
						List<String> newRoomItemsStrings = Arrays.asList((gameFileInput.nextLine().trim().substring(7)).split("\\s*,\\s"));
						for (String itemToAddString : newRoomItemsStrings) {
							// Check the treasures HashMap for the item, if it's not there, check weapons.
							Item itemToAdd = treasures.get(itemToAddString);
							if (itemToAdd == null) {
								weapons.get(itemToAddString);
							} // end of if
							newRoomItems.add(itemToAdd);
						} // end of foreach
						Mob newRoomMob = mobs.get(gameFileInput.next().substring(5).trim());
						Room newRoom = new Room(newRoomName, newRoomDesc, newRoomItems, newRoomMob);
						rooms.put(newRoomName, newRoom);
						break;
				} // end of switch
			} else {
				if (gameFileInput.nextLine().contains("EAST")) {
					String[] roomConnection = gameFileInput.nextLine().split("Directions.EAST");
					startRooms.add(rooms.get(roomConnection[0].trim()));
					roomConnectionDirections.add(Directions.EAST);
					endRooms.add(rooms.get(roomConnection[2].trim()));
				} else if (gameFileInput.nextLine().contains("WEST")) {
					String[] roomConnection = gameFileInput.nextLine().split("Directions.WEST");
					startRooms.add(rooms.get(roomConnection[0].trim()));
					roomConnectionDirections.add(Directions.WEST);
					endRooms.add(rooms.get(roomConnection[2].trim()));
				} else if (gameFileInput.nextLine().contains("NORTH")) {
					String[] roomConnection = gameFileInput.nextLine().split("Directions.NORTH");
					startRooms.add(rooms.get(roomConnection[0].trim()));
					roomConnectionDirections.add(Directions.NORTH);
					endRooms.add(rooms.get(roomConnection[2].trim()));
				} else if (gameFileInput.nextLine().contains("SOUTH")) {
					String[] roomConnection = gameFileInput.nextLine().split("Directions.SOUTH");
					startRooms.add(rooms.get(roomConnection[0].trim()));
					roomConnectionDirections.add(Directions.SOUTH);
					endRooms.add(rooms.get(roomConnection[2].trim()));
				}
			} // end of if
		} // end of while
		gameFileInput.close();
	} // end of readGameFile method
} // end of Game class
