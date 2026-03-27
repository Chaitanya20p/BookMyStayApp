/**
 * ============================================================
 * MAIN CLASS - UseCase8BookingHistoryReport
 * ============================================================
 *
 * Use Case 8: Booking History & Reporting
 *
 * Description:
 * This class demonstrates how
 * confirmed bookings are stored
 * and reported.
 *
 * The system maintains an ordered
 * audit trail of reservations.
 *
 * @version 8.0
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

// Booking history storage
class BookingHistory {
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    // Add confirmed booking
    public void addBooking(Reservation r) {
        history.add(r);
    }

    // Retrieve all bookings
    public List<Reservation> getAllBookings() {
        return history;
    }
}

// Reporting service
class BookingReportService {

    public void generateReport(List<Reservation> bookings) {

        System.out.println("Booking History Report\n");

        for (Reservation r : bookings) {
            System.out.println("Guest: "
                    + r.getGuestName()
                    + ", Room Type: "
                    + r.getRoomType());
        }
    }
}

public class BookMyStayApp {

    /**
     * Application entry point.
     */
    public static void main(String[] args) {

        System.out.println("Booking History and Reporting\n");

        // Step 1: Create booking history
        BookingHistory history = new BookingHistory();

        // Step 2: Add confirmed bookings
        history.addBooking(new Reservation("Abhi", "Single"));
        history.addBooking(new Reservation("Subha", "Double"));
        history.addBooking(new Reservation("Vannathi", "Suite"));

        // Step 3: Generate report
        BookingReportService reportService = new BookingReportService();
        reportService.generateReport(history.getAllBookings());
    }
}