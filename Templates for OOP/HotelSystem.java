import java.util.ArrayList;
import java.util.List;

/*
 * EXAM QUESTION (typical phrasing):
 * Design a hotel reservation system with the following requirements:
 * a) Each room has a unique room number, type, and price per night.
 * b) There are different room types: StandardRoom and DeluxeRoom, with
 *    different pricing rules (e.g. deluxe rooms include a service charge).
 *    Use inheritance and polymorphism.
 * c) Each guest has a unique ID, name, and can make a reservation.
 * d) A room cannot be booked if it's already reserved for overlapping dates.
 * e) Implement methods to book and cancel a reservation.
 * f) Implement a method to calculate the total cost of a stay.
 * g) Implement a method to display all currently available rooms.
 * h) Ensure encapsulation and validate all inputs.
 *
 * DESIGN NOTES:
 * - Room (abstract) -> StandardRoom, DeluxeRoom: polymorphism via
 *   calculateTotalCost(nights).
 * - Reservation holds check-in/check-out as simple day-numbers for simplicity
 *   (in a real system you'd use java.time.LocalDate).
 * - Room keeps its own list of reservations and checks for date overlap
 *   before accepting a new one.
 */

class RoomNotAvailableException extends Exception {
    public RoomNotAvailableException(String message) {
        super(message);
    }
}

class InvalidDateRangeException extends Exception {
    public InvalidDateRangeException(String message) {
        super(message);
    }
}

// ---------- Guest ----------
class Guest {
    private final String id;
    private final String name;

    public Guest(String id, String name) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Guest ID cannot be empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Guest name cannot be empty.");
        }
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}

// ---------- Reservation (day numbers used in place of real dates for simplicity) ----------
class Reservation {
    private final Guest guest;
    private final int checkInDay;
    private final int checkOutDay;

    public Reservation(Guest guest, int checkInDay, int checkOutDay) {
        this.guest = guest;
        this.checkInDay = checkInDay;
        this.checkOutDay = checkOutDay;
    }

    public int getCheckInDay() {
        return checkInDay;
    }

    public int getCheckOutDay() {
        return checkOutDay;
    }

    public int getNights() {
        return checkOutDay - checkInDay;
    }

    public Guest getGuest() {
        return guest;
    }

    boolean overlaps(int otherCheckIn, int otherCheckOut) {
        // Two ranges overlap if one starts before the other ends, both ways
        return checkInDay < otherCheckOut && otherCheckIn < checkOutDay;
    }

    @Override
    public String toString() {
        return guest.getName() + ": day " + checkInDay + " to day " + checkOutDay;
    }
}

// ---------- Room hierarchy ----------
abstract class Room {
    protected final String roomNumber;
    protected double pricePerNight;
    private final List<Reservation> reservations;

    public Room(String roomNumber, double pricePerNight) {
        if (roomNumber == null || roomNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Room number cannot be empty.");
        }
        if (pricePerNight <= 0) {
            throw new IllegalArgumentException("Price per night must be positive.");
        }
        this.roomNumber = roomNumber;
        this.pricePerNight = pricePerNight;
        this.reservations = new ArrayList<>();
    }

    public abstract double calculateTotalCost(int nights); // polymorphic pricing

    public abstract String getType();

    public String getRoomNumber() {
        return roomNumber;
    }

    private void validateDates(int checkIn, int checkOut) throws InvalidDateRangeException {
        if (checkIn < 0 || checkOut <= checkIn) {
            throw new InvalidDateRangeException(
                    "Invalid date range: check-in=" + checkIn + ", check-out=" + checkOut);
        }
    }

    public Reservation book(Guest guest, int checkInDay, int checkOutDay)
            throws InvalidDateRangeException, RoomNotAvailableException {
        validateDates(checkInDay, checkOutDay);
        for (Reservation r : reservations) {
            if (r.overlaps(checkInDay, checkOutDay)) {
                throw new RoomNotAvailableException(
                        "Room " + roomNumber + " is already booked for an overlapping period: " + r);
            }
        }
        Reservation reservation = new Reservation(guest, checkInDay, checkOutDay);
        reservations.add(reservation);
        return reservation;
    }

    public void cancel(Reservation reservation) {
        reservations.remove(reservation);
    }

    public boolean isAvailableOn(int day) {
        for (Reservation r : reservations) {
            if (day >= r.getCheckInDay() && day < r.getCheckOutDay()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "Room " + roomNumber + " (" + getType() + ") - " + pricePerNight + "/night";
    }
}

class StandardRoom extends Room {
    public StandardRoom(String roomNumber, double pricePerNight) {
        super(roomNumber, pricePerNight);
    }

    @Override
    public double calculateTotalCost(int nights) {
        if (nights <= 0) {
            throw new IllegalArgumentException("Nights must be positive.");
        }
        return pricePerNight * nights;
    }

    @Override
    public String getType() {
        return "Standard";
    }
}

class DeluxeRoom extends Room {
    private static final double SERVICE_CHARGE_RATE = 0.15; // 15% service charge

    public DeluxeRoom(String roomNumber, double pricePerNight) {
        super(roomNumber, pricePerNight);
    }

    @Override
    public double calculateTotalCost(int nights) {
        if (nights <= 0) {
            throw new IllegalArgumentException("Nights must be positive.");
        }
        double base = pricePerNight * nights;
        return base + (base * SERVICE_CHARGE_RATE);
    }

    @Override
    public String getType() {
        return "Deluxe";
    }
}

// ---------- Hotel (controller) ----------
class Hotel {
    private final List<Room> rooms = new ArrayList<>();

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void displayAvailableRooms(int day) {
        System.out.println("Rooms available on day " + day + ":");
        boolean any = false;
        for (Room r : rooms) {
            if (r.isAvailableOn(day)) {
                System.out.println("  - " + r);
                any = true;
            }
        }
        if (!any) {
            System.out.println("  (none available)");
        }
    }
}

// ---------- Demo ----------
public class HotelSystem {
    public static void main(String[] args) {
        try {
            Hotel hotel = new Hotel();
            Room r101 = new StandardRoom("101", 30000);
            Room r201 = new DeluxeRoom("201", 60000);
            hotel.addRoom(r101);
            hotel.addRoom(r201);

            Guest guest1 = new Guest("G1", "Aline");
            Guest guest2 = new Guest("G2", "Tom");

            Reservation res1 = r101.book(guest1, 10, 13); // 3 nights
            System.out.println("Booked: " + res1 + " | Total cost: " + r101.calculateTotalCost(res1.getNights()));

            Reservation res2 = r201.book(guest2, 10, 12); // 2 nights
            System.out.printf("Booked: %s | Total cost: %.2f%n", res2, r201.calculateTotalCost(res2.getNights()));

            hotel.displayAvailableRooms(10); // both rooms occupied on day 10
            hotel.displayAvailableRooms(20); // both free on day 20

            r101.cancel(res1);
            hotel.displayAvailableRooms(10); // r101 free again

            // This should fail - overlaps with guest2's stay in r201
            r201.book(guest1, 11, 14);

        } catch (RoomNotAvailableException | InvalidDateRangeException e) {
            System.out.println("Booking error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }
}
