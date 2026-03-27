/**
 * ============================================================
 * MAIN CLASS - UseCase4RoomSearch
 * ============================================================
 *
 * Use Case 4: Room Search & Availability Check
 *
 * Description:
 * This class demonstrates how guests
 * can view available rooms without
 * modifying inventory data.
 *
 * The system enforces read-only access
 * by design and usage discipline.
 *
 * @version 4.0
 */

import java.util.*;

class Room {
    private String type;
    private int beds;
    private int size;
    private double price;

    public Room(String type, int beds, int size, double price) {
        this.type = type;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public int getBeds() {
        return beds;
    }

    public int getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }
}

class Inventory {
    private Map<String, Integer> availability;

    public Inventory() {
        availability = new HashMap<>();

        // Initial availability (read-only for search)
        availability.put("Single", 5);
        availability.put("Double", 3);
        availability.put("Suite", 2);
    }

    public int getAvailableCount(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }

    public Set<String> getRoomTypes() {
        return availability.keySet();
    }
}

class SearchService {
    private Inventory inventory;
    private Map<String, Room> roomData;

    public SearchService(Inventory inventory, Map<String, Room> roomData) {
        this.inventory = inventory;
        this.roomData = roomData;
    }

    public void displayAvailableRooms() {
        System.out.println("Room Search\n");

        for (String type : inventory.getRoomTypes()) {
            int available = inventory.getAvailableCount(type);

            // Filter unavailable rooms
            if (available > 0) {
                Room room = roomData.get(type);

                System.out.println(type + " Room:");
                System.out.println("Beds: " + room.getBeds());
                System.out.println("Size: " + room.getSize() + " sqft");
                System.out.println("Price per night: " + room.getPrice());
                System.out.println("Available: " + available);
                System.out.println();
            }
        }
    }
}

public class BookMyStayApp {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

        // Step 1: Create room domain objects
        Map<String, Room> roomData = new HashMap<>();
        roomData.put("Single", new Room("Single", 1, 250, 1500.0));
        roomData.put("Double", new Room("Double", 2, 400, 2500.0));
        roomData.put("Suite", new Room("Suite", 3, 750, 5000.0));

        // Step 2: Create inventory (state holder)
        Inventory inventory = new Inventory();

        // Step 3: Create search service (read-only access)
        SearchService searchService = new SearchService(inventory, roomData);

        // Step 4: Guest searches for rooms
        searchService.displayAvailableRooms();
    }
}