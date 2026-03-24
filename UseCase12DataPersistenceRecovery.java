import java.io.*;
import java.util.*;

// Reservation (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return "Reservation[ID=" + reservationId +
                ", Guest=" + guestName +
                ", RoomType=" + roomType + "]";
    }
}

// Inventory Service (Serializable)
class InventoryService implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> inventory;

    public InventoryService() {
        inventory = new HashMap<>();
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void displayInventory() {
        System.out.println("Inventory: " + inventory);
    }
}

// Booking History (Serializable)
class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Reservation> bookings;

    public BookingHistory() {
        bookings = new ArrayList<>();
    }

    public void addBooking(Reservation r) {
        bookings.add(r);
    }

    public List<Reservation> getBookings() {
        return bookings;
    }

    public void display() {
        System.out.println("\nBooking History:");
        for (Reservation r : bookings) {
            System.out.println(r);
        }
    }
}

// Wrapper class for persistence
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    InventoryService inventory;
    BookingHistory history;

    public SystemState(InventoryService inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.ser";

    // Save state
    public static void save(SystemState state) {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            out.writeObject(state);
            System.out.println("System state saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    // Load state
    public static SystemState load() {
        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            System.out.println("System state loaded successfully.");
            return (SystemState) in.readObject();

        } catch (FileNotFoundException e) {
            System.out.println("No saved state found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading state. Starting with clean state.");
        }

        return null;
    }
}

// Main class
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        // Try loading previous state
        SystemState state = PersistenceService.load();

        InventoryService inventory;
        BookingHistory history;

        if (state == null) {
            inventory = new InventoryService();
            history = new BookingHistory();
        } else {
            inventory = state.inventory;
            history = state.history;
        }

        // Simulate some bookings
        history.addBooking(new Reservation("DEL-1", "Sara", "Deluxe"));
        history.addBooking(new Reservation("SUI-1", "Rahul", "Suite"));

        // Display current state
        history.display();
        inventory.displayInventory();

        // Save before exit
        PersistenceService.save(new SystemState(inventory, history));
    }
}