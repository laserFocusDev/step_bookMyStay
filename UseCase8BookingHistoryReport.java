import java.util.*;

// Reservation (extended with roomId for history)
class Reservation {
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    @Override
    public String toString() {
        return "Reservation[Guest=" + guestName +
                ", RoomType=" + roomType +
                ", RoomID=" + roomId + "]";
    }
}

// Booking History (stores confirmed bookings)
class BookingHistory {

    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    // Add confirmed booking
    public void addBooking(Reservation reservation) {
        history.add(reservation);
        System.out.println("Added to history: " + reservation);
    }

    // Retrieve all bookings
    public List<Reservation> getAllBookings() {
        return history;
    }
}

// Reporting Service
class BookingReportService {

    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    // Display all bookings
    public void displayAllBookings() {
        System.out.println("\n--- Booking History ---");

        List<Reservation> bookings = history.getAllBookings();

        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : bookings) {
            System.out.println(r);
        }
    }

    // Summary report
    public void generateSummary() {
        List<Reservation> bookings = history.getAllBookings();

        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation r : bookings) {
            roomTypeCount.put(
                r.getRoomType(),
                roomTypeCount.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        System.out.println("\n--- Booking Summary ---");
        for (String type : roomTypeCount.keySet()) {
            System.out.println(type + " bookings: " + roomTypeCount.get(type));
        }

        System.out.println("Total bookings: " + bookings.size());
    }
}

// Main class
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService(history);

        // Simulated confirmed bookings (from Use Case 6)
        history.addBooking(new Reservation("Sara", "Deluxe", "DEL-1"));
        history.addBooking(new Reservation("Rahul", "Suite", "SUI-1"));
        history.addBooking(new Reservation("Anita", "Standard", "STD-1"));

        // Reporting
        reportService.displayAllBookings();
        reportService.generateSummary();
    }
}