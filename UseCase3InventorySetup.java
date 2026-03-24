import java.util.HashMap;
import java.util.Map;

/**
 * RoomInventory
 * 
 * This class manages the centralized inventory of rooms available
 * in the Hotel Booking System. Room types and their available counts
 * are stored using a HashMap to ensure fast access and updates.
 * 
 * @author Sara
 * @version 3.0
 */
class RoomInventory {

    // HashMap storing room type -> available room count
    private Map<String, Integer> inventory;

    /**
     * Constructor initializes the inventory with default room types.
     */
    public RoomInventory() {
        inventory = new HashMap<>();

        // Register room types and their availability
        inventory.put("Single", 10);
        inventory.put("Double", 8);
        inventory.put("Suite", 5);
    }

    /**
     * Returns the number of rooms available for a given type
     */
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    /**
     * Updates the room availability count
     */
    public void updateAvailability(String roomType, int newCount) {
        inventory.put(roomType, newCount);
    }

    /**
     * Displays the current inventory state
     */
    public void displayInventory() {
        System.out.println("\nCurrent Room Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " Rooms Available: " + entry.getValue());
        }
    }
}

/**
 * UseCase3InventorySetup
 * 
 * This class demonstrates the setup and usage of the RoomInventory
 * component for centralized inventory management.
 * 
 * @author Sara
 * @version 3.1
 */
public class UseCase3InventorySetup {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("   Book My Stay - Inventory v3.1 ");
        System.out.println("=================================");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Display initial inventory
        inventory.displayInventory();

        // Check availability
        System.out.println("\nChecking availability of Suite rooms...");
        System.out.println("Available Suites: " + inventory.getAvailability("Suite"));

        // Update availability
        System.out.println("\nUpdating Suite availability...");
        inventory.updateAvailability("Suite", 4);

        // Display updated inventory
        inventory.displayInventory();
    }
}
