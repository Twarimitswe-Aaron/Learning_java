package Thread.ProducerConsumer;

public class ProducerConsumerMain {
    public static void main(String[] args) {
        ProducerConsumer pc = new ProducerConsumer(10);
        Thread t1 = new Thread(() -> {
            for (int i = 0; i <= 20; i++) {
                try {
                    pc.produce(i);
                    Thread.sleep(10);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i <= 10; i++) {
                try {
                    pc.consume();
                    Thread.sleep(10);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        t2.start();

    }
}
