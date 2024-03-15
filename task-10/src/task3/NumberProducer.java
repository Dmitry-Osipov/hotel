package task3;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class NumberProducer implements Runnable {
    private static final Random random = new Random();
    private final BlockingQueue<Integer> queue;

    public NumberProducer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                int number = random.nextInt(100);
                queue.put(number);
                System.out.println("Произведено число: " + number);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
