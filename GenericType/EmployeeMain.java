import java.util.Arrays;
import java.util.List;

public class EmployeeMain {
    public static void main(String[] args){
        List<Integer> list1=Arrays.asList(1,2,3,4,5);
        List<String> list2=Arrays.asList("A","B","C","D","E");
        displayList(list1);
        displayList(list2);
    }
    public static void displayList(List<?> list){
        for(Object var:list){
            System.out.println(var);
        }
    }
}
