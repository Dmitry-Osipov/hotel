package task1;

public class StateTest {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            System.out.println("Состояние thread после запуска: " + Thread.currentThread().getState());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }

            synchronized (StateTest.class) {
                try {
                    StateTest.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        });

        System.out.println("Состояние thread до запуска: " + thread.getState());
        thread.start();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        System.out.println("Состояние thread перед остановкой: " + thread.getState());
        synchronized (StateTest.class) {
            StateTest.class.notify();
        }

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        System.out.println("Состояние thread после завершения: " + thread.getState());

        final Object monitor = new Object();

        Thread waitingThread = new Thread(() -> {
            synchronized (monitor) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread blockedThread = new Thread(() -> {
            synchronized (monitor) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        });

        waitingThread.start();
        blockedThread.start();
        System.out.println("Состояние waitingThread: " + waitingThread.getState());
        System.out.println("Состояние blockedThread: " + blockedThread.getState());
    }
}
