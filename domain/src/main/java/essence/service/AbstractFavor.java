package essence.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

/**
 * Абстрактный класс услуги. Фиксирует минимальную цену услуги, имеет реализацию установки цены. Забирает на себя
 * стандартные поля для каждой услуги (id, price).
 */
@Getter
@Setter
public abstract class AbstractFavor {
    private int id;
    private int price;
    @JsonIgnore
    private int MIN_PRICE = 1500;

    protected AbstractFavor() {
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
