/**
 * ============================================================
 * MAIN CLASS - UseCase10BookingCancellation
 * ============================================================
 *
 * Use Case 10: Booking Cancellation & Inventory Rollback
 *
 * Description:
 * This class demonstrates how confirmed
 * bookings can be safely cancelled and
 * system state is rolled back correctly.
 *
 * Inventory is restored and room IDs are
 * released using rollback logic.
 *
 * @version 10.0
 */

import java.util.*;

// Reservation class
class Reservation {
    private String guestName;
    private String roomId;
    private String roomType;

    public Reservation(String guestName, String roomId, String roomType) {
        this.guestName = guestName;
        this.roomId = roomId;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> availability = new HashMap<>();

    public InventoryService() {
        availability.put("Single", 1);
        availability.put("Double", 1);
        availability.put("Suite", 1);
    }

    public void increment(String type) {
        availability.put(type, availability.getOrDefault(type, 0) + 1);
    }
}

// Booking history
class BookingHistory {
    private Map<String, Reservation> bookings = new HashMap<>();

    public void addBooking(Reservation r) {
        bookings.put(r.getRoomId(), r);
    }

    public Reservation getBooking(String roomId) {
        return bookings.get(roomId);
    }

    public void removeBooking(String roomId) {
        bookings.remove(roomId);
    }

    public boolean exists(String roomId) {
        return bookings.containsKey(roomId);
    }
}

// Cancellation Service with rollback using Stack
class CancellationService {
    private InventoryService inventory;
    private BookingHistory history;
    private Stack<String> rollbackStack = new Stack<>();

    public CancellationService(InventoryService inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void cancelBooking(String roomId) {

        // Validate existence
        if (!history.exists(roomId)) {
            System.out.println("Cancellation failed: Booking does not exist for Room ID: " + roomId);
            return;
        }

        // Get booking
        Reservation r = history.getBooking(roomId);

        // Push to rollback stack (LIFO)
        rollbackStack.push(roomId);

        // Restore inventory
        inventory.increment(r.getRoomType());

        // Remove from history
        history.removeBooking(roomId);

        // Confirmation
        System.out.println("Booking cancelled for Guest: "
                + r.getGuestName()
                + ", Room ID: "
                + roomId);
    }
}

public class BookMyStayApp {

    /**
     * Application entry point.
     */
    public static void main(String[] args) {

        System.out.println("Booking Cancellation & Rollback\n");

        // Initialize services
        InventoryService inventory = new InventoryService();
        BookingHistory history = new BookingHistory();

        // Add confirmed bookings (simulate previous use case)
        history.addBooking(new Reservation("Abhi", "Single-1", "Single"));
        history.addBooking(new Reservation("Subha", "Double-1", "Double"));

        // Initialize cancellation service
        CancellationService cancelService = new CancellationService(inventory, history);

        // Perform cancellations
        cancelService.cancelBooking("Single-1");   // valid
        cancelService.cancelBooking("Suite-1");    // invalid (not exists)
    }
}