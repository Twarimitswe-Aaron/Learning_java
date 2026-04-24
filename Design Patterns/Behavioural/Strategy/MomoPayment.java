package Strategy;

public class MomoPayment implements PaymentStrategy{
    @Override
    public void pay (double amount){
        System.out.println(amount+ "paid using credit card");
    }
}
