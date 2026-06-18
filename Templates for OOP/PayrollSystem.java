import java.util.ArrayList;
import java.util.List;

/*
 * EXAM QUESTION (typical phrasing):
 * Design an employee payroll management system with the following requirements:
 * a) Each employee has a unique employee ID, name, and department.
 * b) There are different types of employees: FullTimeEmployee (fixed monthly
 *    salary), PartTimeEmployee (paid hourly, with hours capped per month),
 *    and Manager (fixed salary plus a bonus based on team size). Use
 *    inheritance and polymorphism for salary calculation.
 * c) A manager supervises a list of other employees.
 * d) Implement a method to calculate the total payroll for the whole company.
 * e) Implement a method to give an employee a raise, with validation
 *    (e.g. raise cannot make salary negative or exceed a max percentage).
 * f) Implement a method to display all employees in a department.
 * g) Ensure encapsulation and validate all inputs.
 *
 * DESIGN NOTES:
 * - Employee (abstract) -> FullTimeEmployee, PartTimeEmployee, Manager:
 *   polymorphism via calculateSalary().
 * - Manager "has-a" list of Employee (aggregation), demonstrating composition
 *   alongside inheritance.
 * - Company is the controller class aggregating all employees and computing
 *   payroll by calling calculateSalary() polymorphically on each.
 */

class InvalidRaiseException extends Exception {
    public InvalidRaiseException(String message) {
        super(message);
    }
}

abstract class Employee {
    protected final String employeeId;
    protected String name;
    protected String department;

    public Employee(String employeeId, String name, String department) {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException("Department cannot be empty.");
        }
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public abstract double calculateSalary(); // polymorphic

    // Default raise validation shared by all employees; subclasses may
    // override applyRaise() if they need different rules (e.g. Manager).
    public void applyRaise(double percentage) throws InvalidRaiseException {
        if (percentage <= 0 || percentage > 50) {
            throw new InvalidRaiseException(
                    "Raise percentage must be between 0 and 50. Got: " + percentage);
        }
        // Subclasses implement how the raise actually changes their pay basis
        applyRaiseInternal(percentage);
    }

    protected abstract void applyRaiseInternal(double percentage);

    @Override
    public String toString() {
        return String.format("%s [%s] (%s) - Salary: %.2f", name, employeeId, department, calculateSalary());
    }
}

class FullTimeEmployee extends Employee {
    private double monthlySalary;

    public FullTimeEmployee(String employeeId, String name, String department, double monthlySalary) {
        super(employeeId, name, department);
        if (monthlySalary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative.");
        }
        this.monthlySalary = monthlySalary;
    }

    @Override
    public double calculateSalary() {
        return monthlySalary;
    }

    @Override
    protected void applyRaiseInternal(double percentage) {
        monthlySalary += monthlySalary * (percentage / 100.0);
    }
}

class PartTimeEmployee extends Employee {
    private static final int MAX_HOURS_PER_MONTH = 160;
    private double hourlyRate;
    private int hoursWorked;

    public PartTimeEmployee(String employeeId, String name, String department, double hourlyRate) {
        super(employeeId, name, department);
        if (hourlyRate < 0) {
            throw new IllegalArgumentException("Hourly rate cannot be negative.");
        }
        this.hourlyRate = hourlyRate;
        this.hoursWorked = 0;
    }

    public void logHours(int hours) {
        if (hours < 0) {
            throw new IllegalArgumentException("Hours cannot be negative.");
        }
        if (hoursWorked + hours > MAX_HOURS_PER_MONTH) {
            throw new IllegalArgumentException(
                    "Cannot exceed max " + MAX_HOURS_PER_MONTH + " hours/month. Current: " + hoursWorked);
        }
        hoursWorked += hours;
    }

    @Override
    public double calculateSalary() {
        return hourlyRate * hoursWorked;
    }

    @Override
    protected void applyRaiseInternal(double percentage) {
        hourlyRate += hourlyRate * (percentage / 100.0);
    }
}

class Manager extends FullTimeEmployee {
    private final List<Employee> team;
    private static final double BONUS_PER_TEAM_MEMBER = 50000;

    public Manager(String employeeId, String name, String department, double monthlySalary) {
        super(employeeId, name, department, monthlySalary);
        this.team = new ArrayList<>();
    }

    public void addTeamMember(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null.");
        }
        if (employee == this) {
            throw new IllegalArgumentException("A manager cannot supervise themselves.");
        }
        team.add(employee);
    }

    public List<Employee> getTeam() {
        return new ArrayList<>(team);
    }

    @Override
    public double calculateSalary() {
        // base salary (from FullTimeEmployee) + bonus per team member managed
        return super.calculateSalary() + (team.size() * BONUS_PER_TEAM_MEMBER);
    }
}

// ---------- Company (controller) ----------
class Company {
    private final List<Employee> employees = new ArrayList<>();

    public void hire(Employee employee) {
        employees.add(employee);
    }

    public double calculateTotalPayroll() {
        double total = 0;
        for (Employee e : employees) {
            total += e.calculateSalary(); // polymorphic call
        }
        return total;
    }

    public void displayDepartment(String department) {
        System.out.println("Employees in " + department + ":");
        boolean any = false;
        for (Employee e : employees) {
            if (e.getDepartment().equalsIgnoreCase(department)) {
                System.out.println("  - " + e);
                any = true;
            }
        }
        if (!any) {
            System.out.println("  (none)");
        }
    }
}

// ---------- Demo ----------
public class PayrollSystem {
    public static void main(String[] args) {
        try {
            Company company = new Company();

            FullTimeEmployee dev1 = new FullTimeEmployee("E1", "Aimee", "Engineering", 800000);
            PartTimeEmployee dev2 = new PartTimeEmployee("E2", "Bosco", "Engineering", 5000);
            dev2.logHours(100);

            Manager manager = new Manager("E3", "Claudine", "Engineering", 1200000);
            manager.addTeamMember(dev1);
            manager.addTeamMember(dev2);

            company.hire(dev1);
            company.hire(dev2);
            company.hire(manager);

            company.displayDepartment("Engineering");
            System.out.printf("Total payroll: %.2f%n", company.calculateTotalPayroll());

            dev1.applyRaise(10); // valid raise
            System.out.println("After raise: " + dev1);

            // This should fail - exceeds max allowed raise percentage
            dev1.applyRaise(75);

        } catch (InvalidRaiseException e) {
            System.out.println("Raise error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }
}
