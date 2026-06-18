import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
 * EXAM QUESTION (typical phrasing):
 * Design a transport (bus) ticketing system with the following requirements:
 * a) Each bus has a unique bus number, route, and a fixed number of seats.
 * b) Each passenger has a name, ID, and can book a seat on a bus.
 * c) A seat cannot be double-booked - the system must prevent two passengers
 *    from booking the same seat (consider concurrent bookings).
 * d) Implement methods to book and cancel a ticket.
 * e) Implement a method to display all available seats on a bus.
 * f) Use inheritance to model different bus types (Standard, Luxury) with
 *    different ticket pricing (polymorphism).
 * g) Ensure encapsulation and validate all inputs.
 *
 * DESIGN NOTES:
 * - Bus (abstract) -> StandardBus, LuxuryBus: inheritance + polymorphism via
 *   calculateFare().
 * - Seats are tracked as a boolean array / list inside Bus; booking a seat is
 *   synchronized to be safe if multiple passengers try to book concurrently
 *   (e.g. an online booking system with many users hitting the server at once).
 * - Ticket is an immutable record of a confirmed booking.
 */

class SeatNotAvailableException extends Exception {
    public SeatNotAvailableException(String message) {
        super(message);
    }
}

class InvalidSeatException extends Exception {
    public InvalidSeatException(String message) {
        super(message);
    }
}

// ---------- Passenger ----------
class Passenger {
    private final String id;
    private final String name;

    public Passenger(String id, String name) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Passenger ID cannot be empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Passenger name cannot be empty.");
        }
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

// ---------- Bus hierarchy (inheritance + polymorphism) ----------
abstract class Bus {
    protected String busNumber;
    protected String route;
    protected int totalSeats;
    private final boolean[] seatBooked; // true = booked

    public Bus(String busNumber, String route, int totalSeats) {
        if (busNumber == null || busNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Bus number cannot be empty.");
        }
        if (route == null || route.trim().isEmpty()) {
            throw new IllegalArgumentException("Route cannot be empty.");
        }
        if (totalSeats <= 0 || totalSeats > 100) {
            throw new IllegalArgumentException("Total seats must be between 1 and 100.");
        }
        this.busNumber = busNumber;
        this.route = route;
        this.totalSeats = totalSeats;
        this.seatBooked = new boolean[totalSeats]; // index 0..totalSeats-1 -> seat 1..totalSeats
    }

    public abstract double calculateFare(); // polymorphic: differs per bus type

    private void validateSeatNumber(int seatNumber) throws InvalidSeatException {
        if (seatNumber < 1 || seatNumber > totalSeats) {
            throw new InvalidSeatException(
                    "Seat " + seatNumber + " does not exist. Valid range: 1-" + totalSeats);
        }
    }

    // synchronized: prevents two threads (passengers) from booking the same
    // seat at the exact same time -> avoids double-booking race condition
    public synchronized Ticket bookSeat(Passenger passenger, int seatNumber)
            throws InvalidSeatException, SeatNotAvailableException {
        validateSeatNumber(seatNumber);
        if (seatBooked[seatNumber - 1]) {
            throw new SeatNotAvailableException("Seat " + seatNumber + " is already booked.");
        }
        seatBooked[seatNumber - 1] = true;
        return new Ticket(passenger, this, seatNumber, calculateFare());
    }

    public synchronized void cancelSeat(int seatNumber) throws InvalidSeatException {
        validateSeatNumber(seatNumber);
        seatBooked[seatNumber - 1] = false;
    }

    public synchronized void displayAvailableSeats() {
        System.out.println("Available seats on bus " + busNumber + " (" + route + "):");
        StringBuilder sb = new StringBuilder("  ");
        boolean any = false;
        for (int i = 0; i < totalSeats; i++) {
            if (!seatBooked[i]) {
                sb.append(i + 1).append(" ");
                any = true;
            }
        }
        System.out.println(any ? sb.toString() : "  (fully booked)");
    }

    public String getBusNumber() {
        return busNumber;
    }

    public String getRoute() {
        return route;
    }
}

class StandardBus extends Bus {
    public StandardBus(String busNumber, String route, int totalSeats) {
        super(busNumber, route, totalSeats);
    }

    @Override
    public double calculateFare() {
        return 2000.0; // flat fare, RWF
    }
}

class LuxuryBus extends Bus {
    public LuxuryBus(String busNumber, String route, int totalSeats) {
        super(busNumber, route, totalSeats);
    }

    @Override
    public double calculateFare() {
        return 5000.0; // higher fare for luxury service
    }
}

// ---------- Ticket ----------
class Ticket {
    private final Passenger passenger;
    private final Bus bus;
    private final int seatNumber;
    private final double fare;

    public Ticket(Passenger passenger, Bus bus, int seatNumber, double fare) {
        this.passenger = passenger;
        this.bus = bus;
        this.seatNumber = seatNumber;
        this.fare = fare;
    }

    @Override
    public String toString() {
        return "Ticket: " + passenger.getName() + " | Bus " + bus.getBusNumber() +
                " (" + bus.getRoute() + ") | Seat " + seatNumber + " | Fare: " + fare;
    }
}

// ---------- Runnable: simulate concurrent booking attempts ----------
class BookingTask implements Runnable {
    private final Bus bus;
    private final Passenger passenger;
    private final int seatNumber;

    public BookingTask(Bus bus, Passenger passenger, int seatNumber) {
        this.bus = bus;
        this.passenger = passenger;
        this.seatNumber = seatNumber;
    }

    @Override
    public void run() {
        try {
            Ticket ticket = bus.bookSeat(passenger, seatNumber);
            System.out.println("SUCCESS: " + ticket);
        } catch (SeatNotAvailableException | InvalidSeatException e) {
            System.out.println("FAILED for " + passenger.getName() + ": " + e.getMessage());
        }
    }
}

// ---------- Demo ----------
public class TransportSystem {
    public static void main(String[] args) {
        try {
            Bus bus = new LuxuryBus("RAD123A", "Kigali-Musanze", 20);

            // Two passengers race to book the SAME seat (seat 5) concurrently
            Passenger p1 = new Passenger("PSG1", "Claire");
            Passenger p2 = new Passenger("PSG2", "Eric");

            ExecutorService executor = Executors.newFixedThreadPool(2);
            executor.execute(new BookingTask(bus, p1, 5));
            executor.execute(new BookingTask(bus, p2, 5));

            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);

            bus.displayAvailableSeats();

            // Normal sequential bookings
            Ticket t = bus.bookSeat(new Passenger("PSG3", "Sandrine"), 1);
            System.out.println(t);

            bus.cancelSeat(1);
            bus.displayAvailableSeats();

        } catch (SeatNotAvailableException | InvalidSeatException | InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }
}
