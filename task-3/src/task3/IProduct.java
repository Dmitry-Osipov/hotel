package task3;

public interface IProduct {
    /**
     * Требуется реализовать метод по установка первой части продукта в продукт.
     * @param part Часть продукта.
     */
    void installFirstPart(IProductPart part);

    /**
     * Требуется реализовать метод по установка второй части продукта в продукт.
     * @param part Часть продукта.
     */
    void installSecondPart(IProductPart part);

    /**
     * Требуется реализовать метод по установка третьей части продукта в продукт.
     * @param part Часть продукта.
     */
    void installThirdPart(IProductPart part);
}
