import java.util.*;

// Reservation class (same as previous use case)
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return "Reservation[Guest=" + guestName + ", RoomType=" + roomType + "]";
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> roomInventory;

    public InventoryService() {
        roomInventory = new HashMap<>();
        roomInventory.put("Standard", 2);
        roomInventory.put("Deluxe", 2);
        roomInventory.put("Suite", 1);
    }

    public boolean isAvailable(String roomType) {
        return roomInventory.getOrDefault(roomType, 0) > 0;
    }

    public void decrementRoom(String roomType) {
        roomInventory.put(roomType, roomInventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory: " + roomInventory);
    }
}

// Booking Service
class BookingService {

    private Queue<Reservation> requestQueue;
    private Set<String> allocatedRoomIds;
    private Map<String, Set<String>> roomTypeMap;
    private InventoryService inventoryService;
    private int roomCounter = 1;

    public BookingService(Queue<Reservation> requestQueue, InventoryService inventoryService) {
        this.requestQueue = requestQueue;
        this.inventoryService = inventoryService;
        this.allocatedRoomIds = new HashSet<>();
        this.roomTypeMap = new HashMap<>();
    }

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        return roomType.substring(0, 3).toUpperCase() + "-" + (roomCounter++);
    }

    // Core allocation logic (atomic)
    public void processBookings() {

        while (!requestQueue.isEmpty()) {

            Reservation reservation = requestQueue.poll();
            String roomType = reservation.getRoomType();

            System.out.println("\nProcessing: " + reservation);

            // Check availability
            if (!inventoryService.isAvailable(roomType)) {
                System.out.println("No rooms available for " + roomType);
                continue;
            }

            // Generate unique ID
            String roomId = generateRoomId(roomType);

            // Ensure uniqueness (extra safety)
            if (allocatedRoomIds.contains(roomId)) {
                System.out.println("Duplicate room ID detected! Skipping...");
                continue;
            }

            // Allocation (atomic logical block)
            allocatedRoomIds.add(roomId);
            roomTypeMap
                .computeIfAbsent(roomType, k -> new HashSet<>())
                .add(roomId);

            inventoryService.decrementRoom(roomType);

            System.out.println("Booking Confirmed!");
            System.out.println("Guest: " + reservation.getGuestName());
            System.out.println("Room Allocated: " + roomId);
        }
    }

    public void displayAllocations() {
        System.out.println("\nRoom Allocations:");
        for (String type : roomTypeMap.keySet()) {
            System.out.println(type + " -> " + roomTypeMap.get(type));
        }
    }
}

// Main class
public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        Queue<Reservation> queue = new LinkedList<>();

        // Simulated booking requests (FIFO)
        queue.offer(new Reservation("Sara", "Deluxe"));
        queue.offer(new Reservation("Rahul", "Suite"));
        queue.offer(new Reservation("Anita", "Deluxe"));
        queue.offer(new Reservation("John", "Suite")); // should fail (only 1 suite)

        InventoryService inventory = new InventoryService();
        BookingService bookingService = new BookingService(queue, inventory);

        bookingService.processBookings();
        bookingService.displayAllocations();
        inventory.displayInventory();
    }
}