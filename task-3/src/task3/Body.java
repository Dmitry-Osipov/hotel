package task3;


public class Body extends CarPart {
    private final Color color;

    public Body(int serialNumber, Color color) {
        super(serialNumber);
        this.color = color;
    }

    @Override
    public String toString() {
        return "Body{" +
                "serial number=" + getSerialNumber() + "; " +
                "color=" + color +
                '}';
    }
}
