package Thread.FrameWork.Excecutor;
import java.util.concurrent.*;

class Bask{
    int a;
    public Bask(int a){
        this.a=a;
    }
    public int run(){
        return a;
    }
}

public class ReturnValueExecutor {
public static void main(String []args){
        ExecutorService executorService=Executors.newFixedThreadPool(2);
    executorService.execute(()->{
        Bask b1=new Bask(1);
        int value=b1.run();
        System.out.println(value);
    } );
}
    
}
