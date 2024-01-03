package task_3;

public class AssemblyLine implements IAssemblyLine {
    private static IProduct product = new Car("2171781DBSH726612863HJB");

    @Override
    public IProduct assembleProduct(IProductPart part1, IProductPart part2, IProductPart part3) {
        System.out.println("Начинаем сборку автомобиля...");
        //TODO: логика сборки автомобиля.
        System.out.println("Сборка автомобиля окончена!");
        return product;
    }
}
