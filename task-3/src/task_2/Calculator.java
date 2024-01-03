package task_2;

import java.util.List;

public class Calculator {
    /**
     * Метод производит расчёт стоимости букета.
     * @param bouquet Динамический массив цветков.
     * @return Стоимость всего букета.
     */
    public static int calcCostBouquet(List<Flower> bouquet) {
        int count = 0;
        if (bouquet.isEmpty()) return count;

        for (Flower flower : bouquet) count += flower.getCost();
        return count;
    }
}
