package task_3;


public class Body extends CarPart {
    private Color color;

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
