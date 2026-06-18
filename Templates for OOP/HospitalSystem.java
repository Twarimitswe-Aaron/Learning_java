import java.util.ArrayList;
import java.util.List;

/*
 * EXAM QUESTION (typical phrasing):
 * Design a hospital management system with the following requirements:
 * a) Each patient has a unique ID, name, age, and a list of diagnoses/medical history.
 * b) Each doctor has a unique ID, name, specialization, and a maximum number of
 *    patients they can attend to per day (e.g. 10).
 * c) Patients can book appointments with doctors.
 * d) The system should prevent double-booking a doctor at the same time slot.
 * e) Implement methods to add diagnosis to a patient's record.
 * f) Implement a method to display a doctor's daily schedule.
 * g) Use inheritance to represent different staff types (Doctor, Nurse) sharing
 *    a common Staff base class.
 * h) Ensure encapsulation and validate all inputs.
 *
 * DESIGN NOTES:
 * - Staff (abstract) -> Doctor, Nurse: inheritance + polymorphism via getDuties().
 * - Patient owns its medical history (encapsulation - only patient can mutate it
 *   via addDiagnosis()).
 * - Appointment is a simple data-holding class linking Patient + Doctor + time slot.
 * - Hospital is a controller class that prevents double-booking by checking
 *   existing appointments before confirming a new one.
 */

class InvalidAppointmentException extends Exception {
    public InvalidAppointmentException(String message) {
        super(message);
    }
}

class DoctorUnavailableException extends Exception {
    public DoctorUnavailableException(String message) {
        super(message);
    }
}

// ---------- Patient ----------
class Patient {
    private final String patientId;
    private String name;
    private int age;
    private final List<String> medicalHistory;

    public Patient(String patientId, String name, int age) {
        if (patientId == null || patientId.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient ID cannot be empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (age < 0 || age > 130) {
            throw new IllegalArgumentException("Invalid age: " + age);
        }
        this.patientId = patientId;
        this.name = name;
        this.age = age;
        this.medicalHistory = new ArrayList<>();
    }

    public String getPatientId() {
        return patientId;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void addDiagnosis(String diagnosis) {
        if (diagnosis == null || diagnosis.trim().isEmpty()) {
            throw new IllegalArgumentException("Diagnosis cannot be empty.");
        }
        medicalHistory.add(diagnosis);
    }

    public List<String> getMedicalHistory() {
        return new ArrayList<>(medicalHistory);
    }

    public void displayHistory() {
        System.out.println("Medical history for " + name + ":");
        if (medicalHistory.isEmpty()) {
            System.out.println("  (no records)");
        } else {
            medicalHistory.forEach(d -> System.out.println("  - " + d));
        }
    }
}

// ---------- Staff hierarchy (inheritance + polymorphism) ----------
abstract class Staff {
    protected String staffId;
    protected String name;

    public Staff(String staffId, String name) {
        if (staffId == null || staffId.trim().isEmpty()) {
            throw new IllegalArgumentException("Staff ID cannot be empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        this.staffId = staffId;
        this.name = name;
    }

    public String getStaffId() {
        return staffId;
    }

    public String getName() {
        return name;
    }

    public abstract String getDuties(); // polymorphic description
}

class Doctor extends Staff {
    private static final int MAX_APPOINTMENTS_PER_DAY = 10;
    private String specialization;
    private final List<Appointment> schedule;

    public Doctor(String staffId, String name, String specialization) {
        super(staffId, name);
        if (specialization == null || specialization.trim().isEmpty()) {
            throw new IllegalArgumentException("Specialization cannot be empty.");
        }
        this.specialization = specialization;
        this.schedule = new ArrayList<>();
    }

    public String getSpecialization() {
        return specialization;
    }

    @Override
    public String getDuties() {
        return "Diagnoses and treats patients (Specialty: " + specialization + ")";
    }

    boolean isSlotTaken(String timeSlot) {
        return schedule.stream().anyMatch(a -> a.getTimeSlot().equals(timeSlot));
    }

    void addAppointment(Appointment appt) throws DoctorUnavailableException {
        if (schedule.size() >= MAX_APPOINTMENTS_PER_DAY) {
            throw new DoctorUnavailableException(
                    "Dr. " + name + " has reached the max of " + MAX_APPOINTMENTS_PER_DAY + " appointments today.");
        }
        schedule.add(appt);
    }

    public void displaySchedule() {
        System.out.println("Dr. " + name + "'s schedule (" + schedule.size() + "/" + MAX_APPOINTMENTS_PER_DAY + "):");
        if (schedule.isEmpty()) {
            System.out.println("  (no appointments)");
        } else {
            for (Appointment a : schedule) {
                System.out.println("  - " + a);
            }
        }
    }
}

class Nurse extends Staff {
    private String ward;

    public Nurse(String staffId, String name, String ward) {
        super(staffId, name);
        if (ward == null || ward.trim().isEmpty()) {
            throw new IllegalArgumentException("Ward cannot be empty.");
        }
        this.ward = ward;
    }

    @Override
    public String getDuties() {
        return "Assists patients and doctors in ward: " + ward;
    }
}

// ---------- Appointment ----------
class Appointment {
    private final Patient patient;
    private final Doctor doctor;
    private final String timeSlot; // e.g. "2026-06-18 10:00"

    public Appointment(Patient patient, Doctor doctor, String timeSlot) {
        if (patient == null || doctor == null) {
            throw new IllegalArgumentException("Patient and doctor must not be null.");
        }
        if (timeSlot == null || timeSlot.trim().isEmpty()) {
            throw new IllegalArgumentException("Time slot cannot be empty.");
        }
        this.patient = patient;
        this.doctor = doctor;
        this.timeSlot = timeSlot;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    @Override
    public String toString() {
        return timeSlot + " with " + patient.getName();
    }
}

// ---------- Hospital (controller) ----------
class Hospital {
    private final List<Doctor> doctors = new ArrayList<>();
    private final List<Patient> patients = new ArrayList<>();

    public void registerDoctor(Doctor doctor) {
        doctors.add(doctor);
    }

    public void registerPatient(Patient patient) {
        patients.add(patient);
    }

    public Appointment bookAppointment(Patient patient, Doctor doctor, String timeSlot)
            throws InvalidAppointmentException, DoctorUnavailableException {
        if (doctor.isSlotTaken(timeSlot)) {
            throw new InvalidAppointmentException(
                    "Dr. " + doctor.getName() + " already has an appointment at " + timeSlot);
        }
        Appointment appt = new Appointment(patient, doctor, timeSlot);
        doctor.addAppointment(appt); // may throw DoctorUnavailableException
        return appt;
    }
}

// ---------- Demo ----------
public class HospitalSystem {
    public static void main(String[] args) {
        Hospital hospital = new Hospital();

        try {
            Doctor doctor = new Doctor("D1", "Mugisha", "Cardiology");
            Nurse nurse = new Nurse("N1", "Keza", "ICU");
            hospital.registerDoctor(doctor);

            Patient patient1 = new Patient("P1", "Eric", 34);
            Patient patient2 = new Patient("P2", "Diane", 28);
            hospital.registerPatient(patient1);
            hospital.registerPatient(patient2);

            hospital.bookAppointment(patient1, doctor, "2026-06-18 10:00");
            hospital.bookAppointment(patient2, doctor, "2026-06-18 11:00");

            doctor.displaySchedule();

            patient1.addDiagnosis("Hypertension - prescribed medication");
            patient1.displayHistory();

            System.out.println(nurse.getName() + ": " + nurse.getDuties());

            // This should fail - same time slot already booked
            hospital.bookAppointment(patient2, doctor, "2026-06-18 10:00");

        } catch (InvalidAppointmentException | DoctorUnavailableException e) {
            System.out.println("Booking error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }
}
