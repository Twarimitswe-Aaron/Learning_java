public class StaticKeyword  {
    public static void main(String[] args) {
        Student s=new Student();
        Student s1=new Student();
        Student s2=new Student();
        s.displayCount();
        s1.displayCount();
        s2.displayCount();

        BankAccount a=new BankAccount();
        BankAccount b=new BankAccount();
        BankAccount c=new BankAccount();
        a.interest();
        b.interest();
        c.interest();

        a.interestRate=10;

//        c.interestRate=10;
        BankAccount d=new BankAccount();
        BankAccount e=new BankAccount();
        BankAccount f=new BankAccount();
        d.interest();
        e.interestRate=18;

        e.interest();
        f.interest();

    }
}

class Student{
    public static int  count=0;

    void displayCount(){
        count++;
        System.out.println(count);
    }
}

class  BankAccount{
    public  static  int interestRate=12;

    void interest(){
        int principal=2000;
        int time=2;
        int annualInterest=principal*time*interestRate;
        System.out.println(annualInterest);

    }
}