package task_3;


public class Engine extends CarPart {
    private final double workingVolume;

    public Engine(int serialNumber, double workingVolume) {
        super(serialNumber);
        this.workingVolume = workingVolume;
    }

    @Override
    public String toString() {
        return "Engine{" +
                "serial number=" + getSerialNumber() + "; " +
                "workingVolume=" + workingVolume +
                '}';
    }
}
