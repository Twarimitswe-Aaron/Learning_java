public class Fibbonaci {
    public static void main(String args[]){
        int position=5;
        int number=0;
        if(position>10){
            System.out.println("Position should be less than or equal to 10");
            return;
        }
        int a=0,b=1,sum=0;
        if(position==1){
            System.out.println("The number at position 1 is 0");
            return;
        }else if(position==2){
            System.out.println("The number at position 2 is 1");
            return;
        }else{
            
            for(int i=3;i<position;i++){
                sum=a+b;
                a=b;
                b=sum;
  
            }

        }

        System.err.println("The number at position "+position+" is "+(a+b));
    }
}
