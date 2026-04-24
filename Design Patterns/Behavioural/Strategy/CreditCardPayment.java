package Strategy;

public class CreditCardPayment implements PaymentStrategy{
    @Override
    public void pay (double amount){
        System.out.println(amount+ "paid using credit card");
    }
}
