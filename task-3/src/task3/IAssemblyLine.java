package task3;

public interface IAssemblyLine {
    /**
     * Требуется реализовать метод сырого продукта в конечный продукт.
     * @param product Сырой продукт.
     * @return Готовый продукт.
     */
    IProduct assembleProduct(IProduct product);
}
