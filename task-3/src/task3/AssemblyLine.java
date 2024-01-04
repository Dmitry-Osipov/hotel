package task3;


import java.util.ArrayList;
import java.util.List;

public class AssemblyLine implements IAssemblyLine {
    private static final List<ILineStep> parts = new ArrayList<>();

    public AssemblyLine(ILineStep ...steps) {
        parts.addAll(List.of(steps));
    }

    /**
     * Метод собирает продукт по частям.
     * @param product Продукт, который требуется собрать.
     * @return Собранный продукт.
     */
    @Override
    public IProduct assembleProduct(IProduct product) {
        System.out.println("Начинаем сборку автомобиля...\n");

        product.installFirstPart(parts.get(0).buildProductPart());
        product.installSecondPart(parts.get(1).buildProductPart());
        product.installThirdPart(parts.get(2).buildProductPart());
        parts.clear();

        System.out.println("Сборка автомобиля окончена!\n");
        return product;
    }
}
