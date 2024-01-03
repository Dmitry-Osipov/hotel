package task_3;


public class Chassis extends CarPart {
    public Chassis(int serialNumber) {
        super(serialNumber);
    }

    @Override
    public String toString() {
        return "Chassis{serial number=" + getSerialNumber() + '}';
    }
}
