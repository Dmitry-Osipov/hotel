package task_3;

public class Car implements IProduct {
    private String vin;
    private IProductPart body;
    private IProductPart chassis;
    private IProductPart engine;

    public Car(String vin) {
        this.vin = vin;
    }

    @Override
    public void installFirstPart(IProductPart part) {
        System.out.println("Производится установка кузова...");
        this.body = part;
        System.out.println("Кузов установлен!\n");
    }

    @Override
    public void installSecondPart(IProductPart part) {
        System.out.println("Производится устаноква шасси...");
        this.chassis = part;
        System.out.println("Шасси установлены!\n");
    }

    @Override
    public void installThirdPart(IProductPart part) {
        System.out.println("Производится установка двигателя...");
        this.engine = part;
        System.out.println("Двигатель установлен!");
    }
}
