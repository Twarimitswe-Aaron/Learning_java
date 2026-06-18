public class PaymentAdapter implements PaymentProcessor{
    private ThirdPartyPayment tp=new ThirdPartyPayment();
    PaymentAdapter(ThirdPartyPayment tp){
        this.tp=tp;
    }
    @Override
    public void pay(int val){
        tp.pay(val);
    }
}
