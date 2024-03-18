package task4;

public class PrintTime {
    public static void main(String[] args) {
        Thread timePrinter = new Thread(new PrintTimeTask(1));
        timePrinter.setDaemon(true);
        timePrinter.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
