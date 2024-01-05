package task2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Factory {
    private static final Random random = new Random();

    /**
     * Служебный метод генерирует цветок по переданному типу.
     * @param flowerType Тип цветка.
     * @return Цветок заданного типа.
     */
    private static Flower generateFlower(FlowerType flowerType) {
        Color[] colors = Color.values();
        int cost = random.nextInt(150, 500);
        return switch (flowerType) {
            case LILY -> new Lily(cost, colors[random.nextInt(colors.length)]);
            case ROSE -> new Rose(cost, colors[random.nextInt(colors.length)]);
            case CARNATION -> new Carnation(cost, colors[random.nextInt(colors.length)]);
        };
    }

    /**
     * Служебный метод генерирует случайный цветок.
     * @return Один из цветков класса перечисления FlowerType.
     */
    private static Flower generateFlower() {
        int typeIndex = random.nextInt(3);
        return switch (typeIndex) {
            case 0 -> generateFlower(FlowerType.LILY);
            case 1 -> generateFlower(FlowerType.ROSE);
            case 2 -> generateFlower(FlowerType.CARNATION);
            default -> null;
        };
    }

    /**
     * Метод генерирует несколько цветов сразу.
     * @param count количество цветов, которое требуется сгенерировать.
     * @return Динамический массив цветов.
     */
    public static List<Flower> generateFlowers(int count) {
        ArrayList<Flower> flowers = new ArrayList<>();

        if (count <= 0) {
            return flowers;
        }

        for (int i = 0; i < count; i++) {
            flowers.add(generateFlower());
        }
        return flowers;
    }
}
