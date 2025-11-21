package Thread.Intro;

class Boy extends Thread {
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

class Girl extends Thread {
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
        Girl g = new Girl();
        Boy b = new Boy();
        b.start();
        g.start();
    }

}
