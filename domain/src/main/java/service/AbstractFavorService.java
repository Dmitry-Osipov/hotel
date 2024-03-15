package service;

import essence.service.AbstractFavor;

/**
 * Абстрактный класс сервиса. Сервиса, наследующие этот класс, должны оперировать с наследниками класса AbstractFavor.
 */
public abstract class AbstractFavorService {
    /**
     * Метод подсчитывает цену конкретного номера или услуги.
     * @param favor Комната или услуга.
     * @return Стоимость аренды комнаты на день или разового оказания услуги.
     */
    public int getFavorPrice(AbstractFavor favor) {
        return favor.getPrice();
    }
}
