import java.util.ArrayList;
import java.util.List;

/*
 * EXAM QUESTION (typical phrasing):
 * Design a parking lot management system with the following requirements:
 * a) The parking lot has a fixed number of slots, divided by vehicle size
 *    (e.g. small slots for motorcycles, large slots for cars/trucks).
 * b) Each vehicle has a unique license plate and a type (Motorcycle, Car,
 *    Truck), each occupying a different number of slots / requiring a
 *    different slot size. Use inheritance and polymorphism for fee calculation.
 * c) A vehicle can only park if there is an available slot of the right size.
 * d) Implement methods to park a vehicle and to remove (exit) a vehicle.
 * e) Implement a method to calculate the parking fee based on duration and
 *    vehicle type.
 * f) Implement a method to display the current number of available slots
 *    per size.
 * g) Ensure encapsulation and validate all inputs.
 *
 * DESIGN NOTES:
 * - Vehicle (abstract) -> Motorcycle, Car, Truck: polymorphism via
 *   calculateFee(hours) and requiredSlotSize().
 * - ParkingLot tracks available slots per SlotSize and assigns/frees them as
 *   vehicles enter/exit.
 * - A simple "ticket" tracks entry time so fee can be calculated on exit.
 */

enum SlotSize {
    SMALL, MEDIUM, LARGE
}

class NoAvailableSlotException extends Exception {
    public NoAvailableSlotException(String message) {
        super(message);
    }
}

class VehicleNotFoundException extends Exception {
    public VehicleNotFoundException(String message) {
        super(message);
    }
}

abstract class Vehicle {
    protected final String licensePlate;

    public Vehicle(String licensePlate) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new IllegalArgumentException("License plate cannot be empty.");
        }
        this.licensePlate = licensePlate;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public abstract SlotSize requiredSlotSize();

    public abstract double calculateFee(double hoursParked); // polymorphic pricing
}

class Motorcycle extends Vehicle {
    public Motorcycle(String licensePlate) {
        super(licensePlate);
    }

    @Override
    public SlotSize requiredSlotSize() {
        return SlotSize.SMALL;
    }

    @Override
    public double calculateFee(double hoursParked) {
        if (hoursParked < 0) {
            throw new IllegalArgumentException("Hours parked cannot be negative.");
        }
        return Math.ceil(hoursParked) * 200; // 200 RWF per hour, rounded up
    }
}

class Car extends Vehicle {
    public Car(String licensePlate) {
        super(licensePlate);
    }

    @Override
    public SlotSize requiredSlotSize() {
        return SlotSize.MEDIUM;
    }

    @Override
    public double calculateFee(double hoursParked) {
        if (hoursParked < 0) {
            throw new IllegalArgumentException("Hours parked cannot be negative.");
        }
        return Math.ceil(hoursParked) * 500;
    }
}

class Truck extends Vehicle {
    public Truck(String licensePlate) {
        super(licensePlate);
    }

    @Override
    public SlotSize requiredSlotSize() {
        return SlotSize.LARGE;
    }

    @Override
    public double calculateFee(double hoursParked) {
        if (hoursParked < 0) {
            throw new IllegalArgumentException("Hours parked cannot be negative.");
        }
        return Math.ceil(hoursParked) * 1000;
    }
}

// ---------- Ticket: tracks entry time for fee calculation ----------
class ParkingTicket {
    private final Vehicle vehicle;
    private final long entryTimeMillis;

    public ParkingTicket(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.entryTimeMillis = System.currentTimeMillis();
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public double getHoursParked(long exitTimeMillis) {
        double hours = (exitTimeMillis - entryTimeMillis) / (1000.0 * 60 * 60);
        return Math.max(hours, 0.0167); // minimum ~1 minute billed as a tiny fraction of an hour
    }
}

// ---------- ParkingLot (controller) ----------
class ParkingLot {
    private int availableSmall;
    private int availableMedium;
    private int availableLarge;
    private final List<ParkingTicket> activeTickets = new ArrayList<>();

    public ParkingLot(int smallSlots, int mediumSlots, int largeSlots) {
        if (smallSlots < 0 || mediumSlots < 0 || largeSlots < 0) {
            throw new IllegalArgumentException("Slot counts cannot be negative.");
        }
        this.availableSmall = smallSlots;
        this.availableMedium = mediumSlots;
        this.availableLarge = largeSlots;
    }

    public synchronized ParkingTicket parkVehicle(Vehicle vehicle) throws NoAvailableSlotException {
        SlotSize size = vehicle.requiredSlotSize();
        switch (size) {
            case SMALL:
                if (availableSmall <= 0) throw new NoAvailableSlotException("No small slots available.");
                availableSmall--;
                break;
            case MEDIUM:
                if (availableMedium <= 0) throw new NoAvailableSlotException("No medium slots available.");
                availableMedium--;
                break;
            case LARGE:
                if (availableLarge <= 0) throw new NoAvailableSlotException("No large slots available.");
                availableLarge--;
                break;
        }
        ParkingTicket ticket = new ParkingTicket(vehicle);
        activeTickets.add(ticket);
        System.out.println(vehicle.getLicensePlate() + " parked in a " + size + " slot.");
        return ticket;
    }

    public synchronized double removeVehicle(String licensePlate) throws VehicleNotFoundException {
        ParkingTicket found = null;
        for (ParkingTicket t : activeTickets) {
            if (t.getVehicle().getLicensePlate().equals(licensePlate)) {
                found = t;
                break;
            }
        }
        if (found == null) {
            throw new VehicleNotFoundException("No active ticket found for: " + licensePlate);
        }

        long exitTime = System.currentTimeMillis();
        double hours = found.getHoursParked(exitTime);
        double fee = found.getVehicle().calculateFee(hours);

        // free up the slot
        switch (found.getVehicle().requiredSlotSize()) {
            case SMALL: availableSmall++; break;
            case MEDIUM: availableMedium++; break;
            case LARGE: availableLarge++; break;
        }
        activeTickets.remove(found);

        System.out.printf("%s exited. Hours: %.4f | Fee: %.2f%n", licensePlate, hours, fee);
        return fee;
    }

    public synchronized void displayAvailability() {
        System.out.println("Available slots -> Small: " + availableSmall +
                ", Medium: " + availableMedium + ", Large: " + availableLarge);
    }
}

// ---------- Demo ----------
public class ParkingLotSystem {
    public static void main(String[] args) {
        try {
            ParkingLot lot = new ParkingLot(2, 2, 1); // 2 small, 2 medium, 1 large

            lot.displayAvailability();

            ParkingTicket t1 = lot.parkVehicle(new Motorcycle("MOTO-001"));
            ParkingTicket t2 = lot.parkVehicle(new Car("CAR-001"));
            ParkingTicket t3 = lot.parkVehicle(new Truck("TRK-001"));

            lot.displayAvailability();

            // Simulate a short wait, then exit
            Thread.sleep(100);
            lot.removeVehicle("CAR-001");

            lot.displayAvailability();

            // Fill the only large slot, then try parking another truck (should fail)
            lot.parkVehicle(new Truck("TRK-002"));

        } catch (NoAvailableSlotException | VehicleNotFoundException e) {
            System.out.println("Parking error: " + e.getMessage());
        } catch (IllegalArgumentException | InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
