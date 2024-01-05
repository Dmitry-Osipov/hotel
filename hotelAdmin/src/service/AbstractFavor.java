package service;

import lombok.Getter;

@Getter
public abstract class AbstractFavor {
    private final int id;
    private int price;
    private final int MIN_PRICE = 1500;

    public AbstractFavor(int id, int price) {
        this.id = id;
        setPrice(price);
    }

    /**
     * Метод устанавливает новую цену на услугу.
     * @param price Новая цена.
     */
    public void setPrice(int price) {
        if (checkPrice(price)) this.price = price;
        else this.price = MIN_PRICE;
    }

    /**
     * Служебный метод проверяет цену.
     * @param price Новая цена.
     * @return true, если новая цена больше старой и если новая цена больше минимальной цены, иначе false.
     */
    private boolean checkPrice(int price) {
        return this.price < price && MIN_PRICE < price;
    }
}
