import java.util.*;

// Custom Exception for invalid bookings
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation
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
}

// Inventory Service with validation
class InventoryService {

    private Map<String, Integer> inventory;

    public InventoryService() {
        inventory = new HashMap<>();
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }

    public boolean isAvailable(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
    }

    public void allocateRoom(String roomType) throws InvalidBookingException {
        if (!isValidRoomType(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        if (!isAvailable(roomType)) {
            throw new InvalidBookingException("No rooms available for: " + roomType);
        }

        int count = inventory.get(roomType);

        if (count <= 0) {
            throw new InvalidBookingException("Inventory underflow detected!");
        }

        inventory.put(roomType, count - 1);
    }

    public void displayInventory() {
        System.out.println("Inventory: " + inventory);
    }
}

// Validator class (centralized validation)
class BookingValidator {

    public static void validate(Reservation reservation, InventoryService inventory)
            throws InvalidBookingException {

        if (reservation == null) {
            throw new InvalidBookingException("Reservation cannot be null");
        }

        if (reservation.getGuestName() == null || reservation.getGuestName().isEmpty()) {
            throw new InvalidBookingException("Guest name is required");
        }

        if (!inventory.isValidRoomType(reservation.getRoomType())) {
            throw new InvalidBookingException("Invalid room type selected");
        }
    }
}

// Booking Service
class BookingService {

    private InventoryService inventory;

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
    }

    public void processBooking(Reservation reservation) {

        try {
            // Step 1: Validate input (fail-fast)
            BookingValidator.validate(reservation, inventory);

            // Step 2: Allocate room
            inventory.allocateRoom(reservation.getRoomType());

            System.out.println("Booking successful for " + reservation.getGuestName());

        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        }
    }
}

// Main class
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        BookingService bookingService = new BookingService(inventory);

        // Valid booking
        bookingService.processBooking(new Reservation("Sara", "Deluxe"));

        // Invalid room type
        bookingService.processBooking(new Reservation("Rahul", "Luxury"));

        // Empty guest name
        bookingService.processBooking(new Reservation("", "Suite"));

        // Overbooking
        bookingService.processBooking(new Reservation("Anita", "Deluxe"));

        inventory.displayInventory();
    }
}
