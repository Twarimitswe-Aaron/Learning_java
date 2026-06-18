import java.util.*; // Import the java.util package containing the Collections framework classes

public class Collection { // Define the main class named Collection
    public  static  void main(String[] args){ // Main method, the entry point of the program
        List<Integer> c=new ArrayList<>(); // Initialize an ArrayList of Integers using the List interface
        c.add(4); // Add the integer 4 to the list
        c.add(2); // Add the integer 2 to the list


        for(Object obj:c){ // Iterate over the list elements, treating them as generic Objects
            int num=(Integer)obj; // Cast the Object back to Integer and unbox to a primitive int
            System.out.println(num*2); // Multiply the number by 2 and print the result
        }

        Iterator<Integer> numbers= c.iterator(); // Get an Iterator to traverse the Integer list
        while(numbers.hasNext()){ // Loop while there are more elements in the iterator
            int num=numbers.next().intValue(); // Get the next Integer and extract its primitive int value
            System.out.println(num); // Print the retrieved number
        }
        c.add(1,3); // Insert the number 3 at index 1 in the list
        c.remove(2); // Remove the element at index 2 from the list
        c.sort(Integer::compareTo); // Sort the list in ascending order using Integer's compareTo method
        System.out.println(c); // Print the contents of the sorted list

        Map<String, Integer> marks=new HashMap<>(); // Initialize a HashMap mapping String keys to Integer values
        marks.put("A",1); // Add a key-value pair: "A" -> 1
        marks.put("B",2); // Add a key-value pair: "B" -> 2

        System.out.println(marks); // Print all the key-value mappings in the map
    }
}