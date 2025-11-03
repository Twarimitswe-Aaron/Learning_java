
abstract class PaymentGateway {
    public abstract void payment(double amount);

}

class Paypal extends PaymentGateway {
    @Override
    public void payment(double amount) {
        System.out.println("Payment of " + amount + " made using Paypal");
    }

}

class MasterCard extends PaymentGateway {
    @Override
    public void payment(double amount) {
        System.out.println("Payment of " + amount + " made using MasterCard");
    }

}

class Momo extends PaymentGateway {

    @Override
    public void payment(double amount) {
        System.out.println("Payment of " + amount + " made using Momo");
    }

}

class CryptoWallet extends PaymentGateway {
    @Override
    public void payment(double amount) {
        System.out.println("Payment of " + amount + " made using CryptoWallet");
    }
}

class PaymentProcessor {
    private PaymentGateway paymentGateway;

    //dependency injection via constructor

    public PaymentProcessor(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public void process(double amount) {
        paymentGateway.payment(amount);
    }
}

public class Payement {
    public static void main(String[] args) {
        PaymentProcessor paymentProcessor1 = new PaymentProcessor(new Paypal());
        PaymentProcessor paymentProcessor2 = new PaymentProcessor(new Momo());
        PaymentProcessor paymentProcessor3 = new PaymentProcessor(new MasterCard());
        PaymentProcessor paymentProcessor4 = new PaymentProcessor(new CryptoWallet());

        paymentProcessor1.process(100.0);
        paymentProcessor2.process(200.0);
        paymentProcessor3.process(300.0);
        paymentProcessor4.process(400.0);

    }
}
