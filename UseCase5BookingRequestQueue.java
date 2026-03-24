import java.util.LinkedList;
import java.util.Queue;

// Represents a booking request
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

// Manages booking request queue
class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add request to queue
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added: " + reservation);
    }

    // View all queued requests
    public void displayQueue() {
        System.out.println("\nCurrent Booking Queue:");
        if (queue.isEmpty()) {
            System.out.println("No pending requests.");
            return;
        }

        for (Reservation r : queue) {
            System.out.println(r);
        }
    }

    // Process next request (for future use case)
    public Reservation processNextRequest() {
        return queue.poll(); // FIFO removal
    }
}

public class UseCase5BookingRequestQueue {
    public static void main(String[] args) {

        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Simulating incoming booking requests
        bookingQueue.addRequest(new Reservation("Sara", "Deluxe"));
        bookingQueue.addRequest(new Reservation("Rahul", "Suite"));
        bookingQueue.addRequest(new Reservation("Anita", "Standard"));

        // Display queue (no allocation happens here)
        bookingQueue.displayQueue();
    }
}