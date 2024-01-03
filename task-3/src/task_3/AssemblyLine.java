package task_3;

import java.util.*;

public class AssemblyLine implements IAssemblyLine {
    private static final List<ILineStep> parts = new ArrayList<>();

    /**
     * Метод собирает продукт по частям.
     * @param product Продукт, который требуется собрать.
     * @return Собранный продукт.
     */
    @Override
    public IProduct assembleProduct(IProduct product) {
        System.out.println("Начинаем сборку автомобиля...\n");
        parts.add(new BodyLineStep());
        product.installFirstPart(parts.get(0).buildProductPart());
        parts.add(new ChassisLineStep());
        product.installSecondPart(parts.get(1).buildProductPart());
        parts.add(new EngineLineStep());
        product.installThirdPart(parts.get(2).buildProductPart());
        for (ILineStep part : parts) part.buildProductPart();
        System.out.println("Сборка автомобиля окончена!\n");
        return product;
    }
}
