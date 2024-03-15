package task3;

import java.util.concurrent.BlockingQueue;

public class NumberConsumer implements Runnable {
    private final BlockingQueue<Integer> queue;

    public NumberConsumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                int number = queue.take();
                System.out.println("Потреблено число: " + number);
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }
}
