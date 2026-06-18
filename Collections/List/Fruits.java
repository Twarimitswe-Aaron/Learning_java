import java.util.*;

/**
 * The Fruits class demonstrates the basic usage of the Java Collections Framework,
 * specifically focusing on the List interface and ArrayList implementation.
 * It shows how to create lists, add elements, combine multiple lists, and use 
 * common List methods.
 */
public class Fruits{
    
    /**
     * The main method serves as the entry point for the program.
     * 
     * @param args Command-line arguments (not used in this program)
     */
    public static void main(String[] args) {

        // --- SECTION 1: Combining lists into one list ---

        // Create a list to store tropical fruits
        List<String> tropicalFruits=new ArrayList<>();
        tropicalFruits.add("Mango");
        tropicalFruits.add("Banana");
        tropicalFruits.add("Pineapple");
        tropicalFruits.add("Papaya");

        // Create a separate list to store citrus fruits
        List<String> citrusFruits=new ArrayList<>();
        citrusFruits.add("Orange");
        citrusFruits.add("Lemon");  
        citrusFruits.add("Lime");
        citrusFruits.add("Grapefruit");

        // Create a master list to hold all fruits
        List<String> allFruits=new ArrayList<>();
        // Use addAll() to append all elements from the tropicalFruits collection
        allFruits.addAll(tropicalFruits);
        // Use addAll() to append all elements from the citrusFruits collection
        allFruits.addAll(citrusFruits);

        // Display the combined list
        System.out.println("All Fruits: " + allFruits);

        // --- SECTION 2: List built-in methods ---

        // Create a new list for demonstrating specific List methods
        List<String> fruits=new ArrayList<>();
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Mango");
        fruits.add("Orange");
        fruits.add("Cherry");

        // add(index, element): Inserts "Pineapple" at index 1, shifting subsequent elements to the right
        fruits.add(1, "Pineapple");
        
        // remove(Object): Removes the first occurrence of the specified element.
        // Note: String matching is case-sensitive, so we use "Mango" instead of "mango" to successfully remove it.
        fruits.remove("Mango");

        System.out.println("Modified Fruits List: " + fruits);
    }
}