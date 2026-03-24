import java.util.*;

// Reservation with status
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private boolean isCancelled;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.isCancelled = false;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancel() {
        this.isCancelled = true;
    }

    @Override
    public String toString() {
        return "Reservation[ID=" + reservationId +
                ", Guest=" + guestName +
                ", RoomType=" + roomType +
                ", Cancelled=" + isCancelled + "]";
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> inventory;

    public InventoryService() {
        inventory = new HashMap<>();
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);
    }

    public void incrementRoom(String roomType) {
        inventory.put(roomType, inventory.get(roomType) + 1);
    }

    public void decrementRoom(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("Inventory: " + inventory);
    }
}

// Booking History (stores all bookings)
class BookingHistory {
    private Map<String, Reservation> bookings;

    public BookingHistory() {
        bookings = new HashMap<>();
    }

    public void addBooking(Reservation r) {
        bookings.put(r.getReservationId(), r);
    }

    public Reservation getBooking(String id) {
        return bookings.get(id);
    }

    public void displayAll() {
        System.out.println("\nBooking History:");
        for (Reservation r : bookings.values()) {
            System.out.println(r);
        }
    }
}

// Cancellation Service
class CancellationService {

    private InventoryService inventory;
    private BookingHistory history;
    private Stack<String> rollbackStack;

    public CancellationService(InventoryService inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
        this.rollbackStack = new Stack<>();
    }

    public void cancelBooking(String reservationId) {

        Reservation r = history.getBooking(reservationId);

        if (r == null) {
            System.out.println("Cancellation failed: Reservation does not exist.");
            return;
        }

        if (r.isCancelled()) {
            System.out.println("Cancellation failed: Already cancelled.");
            return;
        }

        // Push to rollback stack
        rollbackStack.push(reservationId);

        // Restore inventory
        inventory.incrementRoom(r.getRoomType());

        // Mark as cancelled (do NOT delete)
        r.cancel();

        System.out.println("Booking cancelled successfully for " + r.getGuestName());
    }

    public void displayRollbackStack() {
        System.out.println("\nRollback Stack: " + rollbackStack);
    }
}

// Main class
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        BookingHistory history = new BookingHistory();

        // Simulated confirmed bookings
        Reservation r1 = new Reservation("DEL-1", "Sara", "Deluxe");
        Reservation r2 = new Reservation("SUI-1", "Rahul", "Suite");

        history.addBooking(r1);
        history.addBooking(r2);

        // Simulate allocation (reduce inventory)
        inventory.decrementRoom("Deluxe");
        inventory.decrementRoom("Suite");

        CancellationService cancelService = new CancellationService(inventory, history);

        // Valid cancellation
        cancelService.cancelBooking("DEL-1");

        // Duplicate cancellation
        cancelService.cancelBooking("DEL-1");

        // Invalid cancellation
        cancelService.cancelBooking("XYZ-999");

        history.displayAll();
        inventory.displayInventory();
        cancelService.displayRollbackStack();
    }
}