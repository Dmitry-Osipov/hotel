package task4;

import java.time.LocalDateTime;

public class PrintTimeTask implements Runnable {
    private final int seconds;

    public PrintTimeTask(int seconds) {
        this.seconds = seconds * 1000;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(LocalDateTime.now());
            try {
                Thread.sleep(seconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }
}
