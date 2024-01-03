package task_3;

import java.util.Random;

public class ChassisLineStep implements ILineStep {
    private static int randomSerialNumber = new Random().nextInt(10000);

    /**
     * Метод производит сборку шасси.
     * @return Шасси автомобиля.
     */
    @Override
    public IProductPart buildProductPart() { return new Chassis(randomSerialNumber); }
}
