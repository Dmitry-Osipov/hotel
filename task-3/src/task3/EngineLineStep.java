package task3;

import java.util.Random;

public class EngineLineStep implements ILineStep {
    private static final Random random = new Random();
    private static final int randomSerialNumber = random.nextInt(10000);
    private static final double randomWorkingVolume = random.nextDouble(7.4);

    /**
     * Метод производит сборку двигателя.
     * @return Двигатель автомобиля.
     */
    @Override
    public IProductPart buildProductPart() {
        return new Engine(randomSerialNumber, randomWorkingVolume);
    }
}
