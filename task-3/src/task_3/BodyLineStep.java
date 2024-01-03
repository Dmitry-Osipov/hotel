package task_3;

import java.util.Random;

public class BodyLineStep implements ILineStep {
    private static final Random random = new Random();
    private static final int randomSerialNumber = random.nextInt(10000);
    private static final Color[] colors = Color.values();

    /**
     * Метод проводит сборку кузова.
     * @return Кузов автомобиля.
     */
    @Override
    public IProductPart buildProductPart() {
        return new Body(randomSerialNumber, colors[random.nextInt(colors.length)]);
    }
}
