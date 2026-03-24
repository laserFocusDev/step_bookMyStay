import java.util.HashMap;
import java.util.Map;

/**
 * Room
 * 
 * Domain model representing a room type in the hotel.
 * Contains descriptive information like price and amenities.
 * 
 * @author Sara
 * @version 4.0
 */
class Room {

    private String type;
    private double price;
    private String amenities;

    public Room(String type, double price, String amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public String getAmenities() {
        return amenities;
    }

    public void displayRoomDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Price per night: ₹" + price);
        System.out.println("Amenities: " + amenities);
        System.out.println("---------------------------");
    }
}

/**
 * RoomInventory
 * 
 * Stores centralized room availability using HashMap.
 * This class acts as the single source of truth for inventory state.
 * 
 * @author Sara
 * @version 4.0
 */
class RoomInventory {

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("Single", 5);
        inventory.put("Double", 3);
        inventory.put("Suite", 0); // Example unavailable room
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public Map<String, Integer> getAllInventory() {
        return inventory;
    }
}

/**
 * SearchService
 * 
 * Handles room search operations using read-only access
 * to the inventory.
 * 
 * @author Sara
 * @version 4.0
 */
class SearchService {

    public void searchAvailableRooms(RoomInventory inventory, Map<String, Room> rooms) {

        System.out.println("\nAvailable Rooms:\n");

        for (String roomType : rooms.keySet()) {

            int available = inventory.getAvailability(roomType);

            // Defensive check: show only rooms with availability > 0
            if (available > 0) {
                rooms.get(roomType).displayRoomDetails();
                System.out.println("Rooms Available: " + available);
                System.out.println();
            }
        }
    }
}

/**
 * UseCase4RoomSearch
 * 
 * Demonstrates how guests can search available rooms
 * without modifying system state.
 * 
 * @author Sara
 * @version 4.0
 */
public class UseCase4RoomSearch {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("   Book My Stay - Room Search    ");
        System.out.println("           Version 4.0           ");
        System.out.println("=================================");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Create room domain objects
        Map<String, Room> rooms = new HashMap<>();

        rooms.put("Single", new Room("Single", 2000, "WiFi, TV, Single Bed"));
        rooms.put("Double", new Room("Double", 3500, "WiFi, TV, Double Bed"));
        rooms.put("Suite", new Room("Suite", 7000, "WiFi, TV, King Bed, Living Area"));

        // Initialize search service
        SearchService searchService = new SearchService();

        // Guest searches for rooms
        searchService.searchAvailableRooms(inventory, rooms);
    }
}
