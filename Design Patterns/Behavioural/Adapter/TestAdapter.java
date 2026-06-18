public class TestAdapter {
    public static void main(String[] args) {
        ThirdPartyPayment tp=new ThirdPartyPayment();
        PaymentProcessor pp=new PaymentAdapter(tp);
        pp.pay(12);
    }
}
