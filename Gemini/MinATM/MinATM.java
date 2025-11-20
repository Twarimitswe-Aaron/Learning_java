package Gemini.MinATM;

public class MinATM {
    private double balance=1000.5;
    private double withdrawAmount=200;
    public static void main(String[] args) {
        int count=3;
        MinATM ATM=new MinATM();
        while(count>0){
            ATM.withdraw(200);
            count--;
        }
    }

   void withdraw(double amount){
     if(amount>balance){
        System.out.println("Insufficient balance");
    }else{
        balance-=amount;
        System.out.println("Transaction success. New Balance: ["+ amount+"]");
    }
   }
}
