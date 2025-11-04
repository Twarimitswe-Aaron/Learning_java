
import java.util.*;

public class GenericAdder<T> {
    public static <T> void addElements(List<T> list,T... items){
        for(T it:items){
            list.add(it);
        }
    }

    public static void main(String[] args) {
        List<Integer> intList=new ArrayList<>();
        addElements(intList ,1,2,3,4,5);
        System.out.println("Integer List: " + intList);

        List<String> strList=new ArrayList<>();
        addElements(strList ,"A","B","C","D","E");
        System.out.println("String List: " + strList);

    }
}
