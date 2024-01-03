package task_2;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<Flower> bouquet = Factory.generateFlowers(25);
        for (Flower e : bouquet) {
            System.out.println(e);
        }
        System.out.println(Calculator.calcCostBouquet(bouquet));
    }
}
