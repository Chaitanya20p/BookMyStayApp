/**
 * ============================================================
 * MAIN CLASS - UseCase12DataPersistenceRecovery
 * ============================================================
 *
 * Use Case 12: Data Persistence & System Recovery
 *
 * Description:
 * This class demonstrates how booking
 * and inventory data are persisted to
 * a file and restored after restart.
 *
 * Serialization and deserialization
 * are used for durability.
 *
 * @version 12.0
 */

import java.io.*;
import java.util.*;

// Reservation class (Serializable)
class Reservation implements Serializable {
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

// Wrapper class for persistence
class SystemState implements Serializable {
    Map<String, Integer> inventory;
    List<Reservation> bookings;

    public SystemState(Map<String, Integer> inventory, List<Reservation> bookings) {
        this.inventory = inventory;
        this.bookings = bookings;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.ser";

    // Save state to file
    public void save(SystemState state) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(state);
            System.out.println("System state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    // Load state from file
    public SystemState load() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            System.out.println("System state loaded successfully.");
            return (SystemState) in.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No saved state found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading state. Starting with safe defaults.");
        }
        return null;
    }
}

public class BookMyStayApp {

    /**
     * Application entry point.
     */
    public static void main(String[] args) {

        System.out.println("Data Persistence & System Recovery\n");

        PersistenceService persistence = new PersistenceService();

        // Try loading existing state
        SystemState state = persistence.load();

        Map<String, Integer> inventory;
        List<Reservation> bookings;

        if (state == null) {
            // Initialize fresh state
            inventory = new HashMap<>();
            inventory.put("Single", 2);
            inventory.put("Double", 1);

            bookings = new ArrayList<>();
            bookings.add(new Reservation("Abhi", "Single"));
            bookings.add(new Reservation("Subha", "Double"));

            System.out.println("Initialized new system state.\n");
        } else {
            // Restore state
            inventory = state.inventory;
            bookings = state.bookings;

            System.out.println("Recovered previous system state.\n");
        }

        // Display current state
        System.out.println("Current Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }

        System.out.println("\nBooking History:");
        for (Reservation r : bookings) {
            System.out.println("Guest: " + r.getGuestName()
                    + ", Room Type: " + r.getRoomType());
        }

        // Save state before shutdown
        persistence.save(new SystemState(inventory, bookings));
    }
}