package Primitives;

import java.util.*;

// Step 1: Create the Trackable interface
interface Trackable {
    void addGrade(Object grade);

    void showGrades();
}

// Step 2: Base class
class GradeBook {
    String subject;

    GradeBook(String subject) {
        this.subject = subject;
    }
}

// Step 3: Generic GradeBook class
class GenericGradeBook<T> extends GradeBook implements Trackable {
    private List<T> grades = new ArrayList<>();

    public GenericGradeBook(String subject) {
        super(subject);
    }

    @Override
    public void addGrade(Object grade) {
        grades.add((T) grade);
    }

    @Override
    public void showGrades() {
        System.out.println("Grades for " + subject + ": " + grades);
    }
}

// Step 4: Main class
public class SchoolSystem {
    public static void main(String[] args) {
        GenericGradeBook<Integer> mathGrades = new GenericGradeBook<>("Math");
        mathGrades.addGrade(90);
        mathGrades.addGrade(85);
        mathGrades.showGrades();

        GenericGradeBook<String> historyGrades = new GenericGradeBook<>("History");
        historyGrades.addGrade("A");
        historyGrades.addGrade("B+");
        historyGrades.showGrades();

        GenericGradeBook<Double> physicsGrades = new GenericGradeBook<>("Physics");
        physicsGrades.addGrade(92.5);
        physicsGrades.addGrade(88.3);
        physicsGrades.showGrades();
    }
}
