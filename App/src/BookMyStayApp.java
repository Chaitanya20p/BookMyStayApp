/**
 * ============================================================
 * MAIN CLASS - UseCase9ErrorHandlingValidation
 * ============================================================
 *
 * Use Case 9: Error Handling & Validation
 *
 * Description:
 * This class demonstrates how invalid
 * booking inputs are detected and handled
 * using validation and custom exceptions.
 *
 * The system prevents invalid state changes
 * and ensures safe execution.
 *
 * @version 9.0
 */

import java.util.*;

// Custom Exception
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

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

// Inventory with validation
class InventoryService {
    private Map<String, Integer> availability = new HashMap<>();

    public InventoryService() {
        availability.put("Single", 1);
        availability.put("Double", 1);
        availability.put("Suite", 0); // intentionally 0 to test validation
    }

    public void validateRoomType(String type) throws InvalidBookingException {
        if (!availability.containsKey(type)) {
            throw new InvalidBookingException("Invalid room type: " + type);
        }
    }

    public void validateAvailability(String type) throws InvalidBookingException {
        if (availability.get(type) <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + type);
        }
    }

    public void decrement(String type) throws InvalidBookingException {
        int count = availability.get(type);
        if (count <= 0) {
            throw new InvalidBookingException("Cannot decrement. No rooms left for: " + type);
        }
        availability.put(type, count - 1);
    }
}

// Booking service with validation (fail-fast)
class BookingService {
    private InventoryService inventory;

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
    }

    public void processBooking(Reservation r) {
        try {
            // Step 1: Validate input
            inventory.validateRoomType(r.getRoomType());

            // Step 2: Validate availability
            inventory.validateAvailability(r.getRoomType());

            // Step 3: Allocate (safe)
            inventory.decrement(r.getRoomType());

            // Step 4: Confirm booking
            System.out.println("Booking successful for Guest: "
                    + r.getGuestName()
                    + ", Room Type: "
                    + r.getRoomType());

        } catch (InvalidBookingException e) {
            // Graceful error handling
            System.out.println("Booking failed: " + e.getMessage());
        }
    }
}

public class BookMyStayApp {

    /**
     * Application entry point.
     */
    public static void main(String[] args) {

        System.out.println("Error Handling & Validation\n");

        // Initialize services
        InventoryService inventory = new InventoryService();
        BookingService bookingService = new BookingService(inventory);

        // Test cases (valid + invalid)
        Reservation r1 = new Reservation("Abhi", "Single");     // valid
        Reservation r2 = new Reservation("Subha", "Suite");     // no availability
        Reservation r3 = new Reservation("Vannathi", "Deluxe"); // invalid type

        // Process bookings
        bookingService.processBooking(r1);
        bookingService.processBooking(r2);
        bookingService.processBooking(r3);
    }
}