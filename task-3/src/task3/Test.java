package task3;

public class Test {
    public static void main(String[] args) {
        Car car = new Car("76482HJJHBBB32427429HJG12");
        AssemblyLine line = new AssemblyLine();
        System.out.println(line.assembleProduct(car));
    }
}
