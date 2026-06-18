import java.text.NumberFormat;
import java.util.Locale;


public class NumberFormatting  {
    public static void main(String[] args) {
        double num=500000.456;
        NumberFormat usFormat= NumberFormat.getInstance(Locale.US);
        NumberFormat FrFormat= NumberFormat.getInstance(Locale.FRANCE);
        System.out.println("French format:" + FrFormat.format(num));
        System.out.println("US format:" + usFormat.format(num));
    }
}