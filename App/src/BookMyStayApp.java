/*
 * ================================================================
 * MAIN CLASS - UseCase2RoomInitialization
 * ================================================================
 *
 * Use Case 2: Basic Room Types & Static Availability
 *
 * Description:
 * This class demonstrates room initialization
 * using domain models before introducing
 * centralized inventory management.
 *
 * Availability is represented using
 * simple variables to highlight limitations.
 *
 * @version 2.1
 */

abstract class Room {
    protected int beds;
    protected int size; // in sqft
    protected double price;

    public Room(int beds, int size, double price) {
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public abstract String getRoomType();

    public void displayDetails() {
        System.out.println(getRoomType() + ":");
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sqft");
        System.out.println("Price per night: " + price);
    }
}

// Single Room
class SingleRoom extends Room {
    public SingleRoom() {
        super(1, 250, 1500.0);
    }

    public String getRoomType() {
        return "Single Room";
    }
}

// Double Room
class DoubleRoom extends Room {
    public DoubleRoom() {
        super(2, 400, 2500.0);
    }

    public String getRoomType() {
        return "Double Room";
    }
}

// Suite Room
class SuiteRoom extends Room {
    public SuiteRoom() {
        super(3, 750, 5000.0);
    }

    public String getRoomType() {
        return "Suite Room";
    }
}

public class BookMyStayApp {

    /**
     * Application entry point.
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

        // Create room objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static availability
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        System.out.println("Hotel Room Initialization\n");

        // Single Room
        single.displayDetails();
        System.out.println("Available: " + singleAvailable + "\n");

        // Double Room
        doubleRoom.displayDetails();
        System.out.println("Available: " + doubleAvailable + "\n");

        // Suite Room
        suite.displayDetails();
        System.out.println("Available: " + suiteAvailable);
    }
}