package task3;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class NumberProcessor {
    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(10);
        Thread consumer = new Thread(new NumberConsumer(queue));
        Thread producer = new Thread(new NumberProducer(queue));
        producer.start();
        consumer.start();
    }
}
