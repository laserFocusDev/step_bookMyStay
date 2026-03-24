import java.util.*;

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

// Thread-safe Inventory Service
class InventoryService {

    private Map<String, Integer> inventory;

    public InventoryService() {
        inventory = new HashMap<>();
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);
    }

    // Critical section
    public synchronized boolean allocateRoom(String roomType) {

        int available = inventory.getOrDefault(roomType, 0);

        if (available <= 0) {
            return false;
        }

        // Simulate delay (to expose race conditions if not synchronized)
        try { Thread.sleep(50); } catch (InterruptedException e) {}

        inventory.put(roomType, available - 1);
        return true;
    }

    public void displayInventory() {
        System.out.println("Final Inventory: " + inventory);
    }
}

// Booking Processor (Runnable)
class BookingProcessor implements Runnable {

    private Queue<Reservation> queue;
    private InventoryService inventory;

    public BookingProcessor(Queue<Reservation> queue, InventoryService inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {

        while (true) {

            Reservation reservation;

            // Synchronize queue access
            synchronized (queue) {
                if (queue.isEmpty()) return;
                reservation = queue.poll();
            }

            if (reservation != null) {
                boolean success = inventory.allocateRoom(reservation.getRoomType());

                if (success) {
                    System.out.println(Thread.currentThread().getName() +
                            " SUCCESS: " + reservation.getGuestName());
                } else {
                    System.out.println(Thread.currentThread().getName() +
                            " FAILED: " + reservation.getGuestName());
                }
            }
        }
    }
}

// Main class
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) throws InterruptedException {

        Queue<Reservation> queue = new LinkedList<>();

        // Multiple requests for limited rooms
        queue.offer(new Reservation("Sara", "Deluxe"));
        queue.offer(new Reservation("Rahul", "Deluxe")); // conflict
        queue.offer(new Reservation("Anita", "Suite"));
        queue.offer(new Reservation("John", "Suite"));   // conflict

        InventoryService inventory = new InventoryService();

        // Thread pool
        Thread t1 = new Thread(new BookingProcessor(queue, inventory), "Thread-1");
        Thread t2 = new Thread(new BookingProcessor(queue, inventory), "Thread-2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        inventory.displayInventory();
    }
}