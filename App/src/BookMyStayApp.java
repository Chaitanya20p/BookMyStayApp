/**
 * ============================================================
 * MAIN CLASS - UseCase6RoomAllocation
 * ============================================================
 *
 * Use Case 6: Reservation Confirmation & Room Allocation
 *
 * Description:
 * This class demonstrates how booking
 * requests are confirmed and rooms
 * are allocated safely.
 *
 * It consumes booking requests in FIFO
 * order and updates inventory immediately.
 *
 * @version 6.0
 */

import java.util.*;

// Reservation class (same as Use Case 5)
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

// Queue for booking requests
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public boolean hasRequests() {
        return !queue.isEmpty();
    }

    public Reservation getNextRequest() {
        return queue.poll();
    }
}

// Inventory service (manages availability)
class InventoryService {
    private Map<String, Integer> availability = new HashMap<>();

    public InventoryService() {
        availability.put("Single", 2);
        availability.put("Double", 2);
        availability.put("Suite", 1);
    }

    public boolean isAvailable(String type) {
        return availability.getOrDefault(type, 0) > 0;
    }

    public void decrement(String type) {
        availability.put(type, availability.get(type) - 1);
    }
}

// Booking service (allocation logic)
class BookingService {
    private InventoryService inventory;

    // Track allocated room IDs per type
    private Map<String, Set<String>> allocatedRooms = new HashMap<>();

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
    }

    public void processBooking(Reservation r) {
        String type = r.getRoomType();

        if (!inventory.isAvailable(type)) {
            System.out.println("No rooms available for " + type);
            return;
        }

        // Initialize set if not exists
        allocatedRooms.putIfAbsent(type, new HashSet<>());

        Set<String> assigned = allocatedRooms.get(type);

        // Generate unique room ID
        int roomNumber = assigned.size() + 1;
        String roomId = type + "-" + roomNumber;

        // Ensure uniqueness
        while (assigned.contains(roomId)) {
            roomNumber++;
            roomId = type + "-" + roomNumber;
        }

        // Allocate
        assigned.add(roomId);

        // Update inventory
        inventory.decrement(type);

        // Confirm booking
        System.out.println("Booking confirmed for Guest: "
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

        System.out.println("Room Allocation Processing\n");

        // Step 1: Create booking queue
        BookingRequestQueue queue = new BookingRequestQueue();

        // Step 2: Add requests (FIFO)
        queue.addRequest(new Reservation("Abhi", "Single"));
        queue.addRequest(new Reservation("Subha", "Single"));
        queue.addRequest(new Reservation("Vannathi", "Suite"));

        // Step 3: Initialize services
        InventoryService inventory = new InventoryService();
        BookingService bookingService = new BookingService(inventory);

        // Step 4: Process queue
        while (queue.hasRequests()) {
            Reservation r = queue.getNextRequest();
            bookingService.processBooking(r);
        }
    }
}