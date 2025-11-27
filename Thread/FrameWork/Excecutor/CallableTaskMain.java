package Thread.FrameWork.Excecutor;
import java.util.concurrent.*;
import java.util.*;

class CallableTask implements Callable<Integer>{
    int value;
    public CallableTask(int value){
        this.value=value;

    }

    @Override
    public Integer call(){
        return value*2;

    }
}

public class CallableTaskMain {
  public static void main(String []args){
      ExecutorService es=Executors.newFixedThreadPool(1);
    Future<Integer> value=es.submit(new CallableTask(1));
    List<Callable<Integer>> Callables=new ArrayList<>();
    for(int i=0;i<=10;i++){
        Callables.add(new CallableTask(i));
    }

    try{
        System.out.println(value.get());
        System.out.println("GameOver");
        
        List<Future<Integer>> results=es.invokeAll(Callables);
        for(Future<Integer> val:results){
            System.out.print(val.get()+", ");

        }
   }catch(InterruptedException e){

   }catch(ExecutionException e){

   }
  }
}
