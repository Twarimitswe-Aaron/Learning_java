import java.util.regex.*;

public class Regex{
    public static void main(String[] args){
        Pattern p=Pattern.compile("ca");
        Matcher m=p.matcher("rca");
        boolean res=m.find();
        System.out.println(res);
        System.out.println(Pattern.matches("hello", "Hello"));
        System.out.println(Pattern.compile("hello").matcher("hello").matches());
    }
}