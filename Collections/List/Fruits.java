import java.util.*;

public class Fruits{
    public static void main(String[] args) {

        //combine lists into one list

        List<String> tropicalFruits=new ArrayList<>();
        tropicalFruits.add("Mango");
        tropicalFruits.add("Banana");
        tropicalFruits.add("Pineapple");
        tropicalFruits.add("Papaya");

        List<String> citrusFruits=new ArrayList<>();
        citrusFruits.add("Orange");
        citrusFruits.add("Lemon");  
        citrusFruits.add("Lime");
        citrusFruits.add("Grapefruit");

        List<String> allFruits=new ArrayList<>();
        allFruits.addAll(tropicalFruits);
        allFruits.addAll(citrusFruits);

        System.out.println("All Fruits: " + allFruits);

        //List built in methods


        List<String> fruits=new ArrayList<>();
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Mango");
        fruits.add("Orange");
        fruits.add("Cherry");

        fruits.add(1, "Pineapple");
        fruits.remove("mango");
        

    }
}