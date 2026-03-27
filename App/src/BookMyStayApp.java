/**
 * ============================================================
 * MAIN CLASS - UseCase11ConcurrentBookingSimulation
 * ============================================================
 *
 * Use Case 11: Concurrent Booking Simulation (Thread Safety)
 *
 * Description:
 * This class demonstrates how multiple
 * booking requests are processed concurrently
 * using threads while ensuring thread safety.
 *
 * Synchronization is used to prevent
 * race conditions and double booking.
 *
 * @version 11.0
 */

import java.util.*;

// Reservation class
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

// Thread-safe Booking Queue
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
    }

    public synchronized Reservation getNextRequest() {
        return queue.poll();
    }

    public synchronized boolean hasRequests() {
        return !queue.isEmpty();
    }
}

// Thread-safe Inventory
class InventoryService {
    private Map<String, Integer> availability = new HashMap<>();

    public InventoryService() {
        availability.put("Single", 2);
        availability.put("Double", 1);
    }

    // Critical section
    public synchronized boolean allocateRoom(String type) {
        int count = availability.getOrDefault(type, 0);

        if (count > 0) {
            availability.put(type, count - 1);
            return true;
        }
        return false;
    }
}

// Booking Processor (Thread)
class BookingProcessor extends Thread {
    private BookingQueue queue;
    private InventoryService inventory;

    public BookingProcessor(BookingQueue queue, InventoryService inventory, String name) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {
            Reservation r;

            // synchronized fetch
            synchronized (queue) {
                if (!queue.hasRequests()) {
                    break;
                }
                r = queue.getNextRequest();
            }

            if (r != null) {
                // critical section (inventory)
                boolean success = inventory.allocateRoom(r.getRoomType());

                if (success) {
                    System.out.println(Thread.currentThread().getName()
                            + " processed booking for "
                            + r.getGuestName()
                            + " (" + r.getRoomType() + ")");
                } else {
                    System.out.println(Thread.currentThread().getName()
                            + " failed booking for "
                            + r.getGuestName()
                            + " (No availability)");
                }
            }
        }
    }
}

public class BookMyStayApp {

    /**
     * Application entry point.
     */
    public static void main(String[] args) {

        System.out.println("Concurrent Booking Simulation\n");

        // Shared resources
        BookingQueue queue = new BookingQueue();
        InventoryService inventory = new InventoryService();

        // Add booking requests
        queue.addRequest(new Reservation("Abhi", "Single"));
        queue.addRequest(new Reservation("Subha", "Single"));
        queue.addRequest(new Reservation("Vannathi", "Single")); // extra (will fail)
        queue.addRequest(new Reservation("Karthik", "Double"));

        // Create multiple threads (simulate concurrent users)
        BookingProcessor t1 = new BookingProcessor(queue, inventory, "Thread-1");
        BookingProcessor t2 = new BookingProcessor(queue, inventory, "Thread-2");

        // Start threads
        t1.start();
        t2.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nAll bookings processed safely.");
    }
}