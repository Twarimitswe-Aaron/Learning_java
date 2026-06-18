package Thread.Intro;

class Boy implements Runnable {
    public void run() {
        for (int i = 0; i < 20; i++) {
            System.out.println("Boy");
            try {
                Thread.sleep(10);
            } catch (Exception e) {

            }
        }
    }

}

class Girl implements Runnable {
    public void run() {
        for (int i = 0; i < 20; i++) {
            System.out.println("Girl");
               try {
                Thread.sleep(10);
            } catch (Exception e) {

            }
        }
    }

}

public class Threads {
    public static void main(String args[]) {
        Runnable g = new Girl();
        Runnable b = new Boy();
        Thread t1=new Thread(b);
        Thread t2 =new Thread(g);
        t1.start();
        t2.start();
    }

}
