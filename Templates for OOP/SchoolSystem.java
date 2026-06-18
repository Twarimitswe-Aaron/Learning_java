import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * EXAM QUESTION (typical phrasing):
 * Design a school management system with the following requirements:
 * a) Each student has a unique student number, name, and can enroll in courses.
 * b) Each course has a unique code, name, and a maximum capacity of students.
 * c) A student cannot enroll in a course that's already at full capacity.
 * d) Each teacher can teach multiple courses and has a name and employee ID.
 * e) Implement a method for a teacher to assign grades to students for a course.
 * f) Implement a method to calculate a student's GPA from all grades received.
 * g) Use inheritance to model different categories of school members (e.g. a
 *    Person base class extended by Student and Teacher), demonstrating
 *    polymorphism in how each displays their information.
 * h) Ensure encapsulation and validate all inputs.
 *
 * DESIGN NOTES:
 * - Person (abstract) -> Student, Teacher: inheritance + polymorphism via
 *   getSummary().
 * - Course tracks its own enrolled students and enforces capacity itself.
 * - Student keeps a Map<Course, Grade> so GPA calculation is self-contained.
 * - Custom exceptions for capacity and duplicate enrollment - both are
 *   "expected" business errors, not programming bugs.
 */

class CourseFullException extends Exception {
    public CourseFullException(String message) {
        super(message);
    }
}

class AlreadyEnrolledException extends Exception {
    public AlreadyEnrolledException(String message) {
        super(message);
    }
}

enum Grade {
    A(4.0), B(3.0), C(2.0), D(1.0), F(0.0);

    private final double points;

    Grade(double points) {
        this.points = points;
    }

    public double getPoints() {
        return points;
    }
}

// ---------- Person hierarchy ----------
abstract class Person {
    protected String id;
    protected String name;

    public Person(String id, String name) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
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

    public abstract String getSummary(); // polymorphic
}

class Student extends Person {
    private final Map<Course, Grade> grades;
    private final List<Course> enrolledCourses;

    public Student(String id, String name) {
        super(id, name);
        this.grades = new HashMap<>();
        this.enrolledCourses = new ArrayList<>();
    }

    @Override
    public String getSummary() {
        return "Student " + name + " (" + id + ") - " + enrolledCourses.size() + " course(s) enrolled";
    }

    void enroll(Course course) {
        enrolledCourses.add(course);
    }

    public List<Course> getEnrolledCourses() {
        return new ArrayList<>(enrolledCourses);
    }

    void receiveGrade(Course course, Grade grade) {
        if (!enrolledCourses.contains(course)) {
            throw new IllegalStateException(name + " is not enrolled in " + course.getName());
        }
        grades.put(course, grade);
    }

    public double calculateGpa() {
        if (grades.isEmpty()) {
            return 0.0;
        }
        double total = 0;
        for (Grade g : grades.values()) {
            total += g.getPoints();
        }
        return total / grades.size();
    }

    public void displayTranscript() {
        System.out.println("Transcript for " + name + ":");
        if (grades.isEmpty()) {
            System.out.println("  (no grades yet)");
        } else {
            for (Map.Entry<Course, Grade> entry : grades.entrySet()) {
                System.out.println("  - " + entry.getKey().getName() + ": " + entry.getValue());
            }
        }
        System.out.printf("  GPA: %.2f%n", calculateGpa());
    }
}

class Teacher extends Person {
    private final List<Course> coursesTaught;

    public Teacher(String id, String name) {
        super(id, name);
        this.coursesTaught = new ArrayList<>();
    }

    @Override
    public String getSummary() {
        return "Teacher " + name + " (" + id + ") - teaches " + coursesTaught.size() + " course(s)";
    }

    public void assignCourse(Course course) {
        coursesTaught.add(course);
        course.setTeacher(this);
    }

    public void assignGrade(Student student, Course course, Grade grade) {
        if (!coursesTaught.contains(course)) {
            throw new IllegalStateException(name + " does not teach " + course.getName());
        }
        student.receiveGrade(course, grade);
        System.out.println(name + " assigned " + grade + " to " + student.getName() + " for " + course.getName());
    }
}

// ---------- Course ----------
class Course {
    private final String code;
    private final String name;
    private final int capacity;
    private final List<Student> enrolledStudents;
    private Teacher teacher;

    public Course(String code, String name, int capacity) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Course code cannot be empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be empty.");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive.");
        }
        this.code = code;
        this.name = name;
        this.capacity = capacity;
        this.enrolledStudents = new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void enrollStudent(Student student) throws CourseFullException, AlreadyEnrolledException {
        if (enrolledStudents.contains(student)) {
            throw new AlreadyEnrolledException(student.getName() + " is already enrolled in " + name);
        }
        if (enrolledStudents.size() >= capacity) {
            throw new CourseFullException("Course " + name + " is full (" + capacity + " students max).");
        }
        enrolledStudents.add(student);
        student.enroll(this);
    }

    public int getEnrolledCount() {
        return enrolledStudents.size();
    }
}

// ---------- Demo ----------
public class SchoolSystem {
    public static void main(String[] args) {
        try {
            Course math = new Course("MTH101", "Calculus I", 2); // tiny capacity to show the exception
            Teacher teacher = new Teacher("T1", "Mrs. Niyonsaba");
            teacher.assignCourse(math);

            Student s1 = new Student("S1", "Patrick");
            Student s2 = new Student("S2", "Grace");
            Student s3 = new Student("S3", "Kevin");

            math.enrollStudent(s1);
            math.enrollStudent(s2);

            teacher.assignGrade(s1, math, Grade.A);
            teacher.assignGrade(s2, math, Grade.B);

            s1.displayTranscript();
            s2.displayTranscript();

            System.out.println(teacher.getSummary());
            System.out.println(s1.getSummary());

            // This should fail - course is full (capacity 2, already has 2)
            math.enrollStudent(s3);

        } catch (CourseFullException | AlreadyEnrolledException e) {
            System.out.println("Enrollment error: " + e.getMessage());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
