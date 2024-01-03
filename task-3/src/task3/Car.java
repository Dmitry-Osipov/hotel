package task3;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Car implements IProduct {
    private final String vin;
    private IProductPart body;
    private IProductPart chassis;
    private IProductPart engine;

    public Car(String vin) {
        this.vin = vin;
    }

    /**
     * Устанавливает на автомобиль кузов.
     * @param part Кузов автомобиля.
     */
    @Override
    public void installFirstPart(IProductPart part) {
        System.out.println("Производится установка кузова...");
        this.body = part;
        System.out.println("Кузов установлен!\n");
    }

    /**
     * Устанавливает шасси на автомобиль.
     * @param part Шасси автомобиля.
     */
    @Override
    public void installSecondPart(IProductPart part) {
        System.out.println("Производится устаноква шасси...");
        this.chassis = part;
        System.out.println("Шасси установлены!\n");
    }

    /**
     * Устанавливает двигатель на автомобиль.
     * @param part Двигатель автомобиля.
     */
    @Override
    public void installThirdPart(IProductPart part) {
        System.out.println("Производится установка двигателя...");
        this.engine = part;
        System.out.println("Двигатель установлен!\n");
    }
}
