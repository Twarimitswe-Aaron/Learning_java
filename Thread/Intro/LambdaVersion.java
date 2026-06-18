package Thread.Intro;

class Counter {
    int count;

    public synchronized void increment() {
        count++;
    }
}

public class LambdaVersion {

    public static void main(String[] args) {
        Counter count = new Counter();
        // Runnable b = new Runnable() {

        // public void run() {
        // for (int i = 0; i < 20; i++) {
        // count.increment();
        // System.out.println("boy");
        // try {
        // Thread.sleep(10);

        // } catch (Exception e) {

        // }

        // }

        // }

        // };

        Runnable b =()-> {
                for (int i = 0; i < 20; i++) {
                    count.increment();
                    System.out.println("boy");
                    try {
                        Thread.sleep(10);

                    } catch (Exception e) {

                    }

                }

            

        };

        Runnable g = () -> {
            for (int i = 0; i < 20; i++) {
                count.increment();
                System.out.println("girl");
                try {
                    Thread.sleep(10);

                } catch (Exception e) {

                }

            }
        };

        Thread t1 = new Thread(b);
        Thread t2 = new Thread(g);
        t1.start();
        t2.start();
    }

}
