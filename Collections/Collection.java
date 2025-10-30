import java.util.*;

public class Collection {
    public  static  void main(String[] args){
        List<Integer> c=new ArrayList();
        c.add(4);
        c.add(2);


        for(Object obj:c){
            int num=(Integer)obj;
            System.out.println(num*2);
        }

        Iterator<Integer> numbers= c.iterator();
        while(numbers.hasNext()){
            int num=numbers.next().intValue();
            System.out.println(num);
        }
        c.add(1,3);
        c.remove(2);
        c.sort(Integer::compareTo);
        System.out.println(c);

        Map<String, Integer> marks=new HashMap<>();
        marks.put("A",1);
        marks.put("B",2);

        System.out.println(marks);
    }
}