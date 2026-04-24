package Strategy;

public class PaymentPortal {
    public static void main(String[] args) {
        PaymentContext context = new PaymentContext(new CreditCardPayment());
        context.pay(5);

        context.setPaymentStrategy(new MomoPayment());
        context.pay(10);
    }
}
