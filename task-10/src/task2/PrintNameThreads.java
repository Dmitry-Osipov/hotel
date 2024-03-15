package task2;

public class PrintNameThreads {
    public static void main(String[] args) {
        Thread firstThread = new Thread(() -> {
            while (true) {
                System.out.println(Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread secondThread = new Thread(() -> {
            try {
                Thread.sleep(300);
                while (true) {
                    System.out.println(Thread.currentThread().getName());
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        });

        firstThread.start();
        secondThread.start();
    }
}
