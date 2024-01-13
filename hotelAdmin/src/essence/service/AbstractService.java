package essence.service;

import essence.person.AbstractClient;

import java.time.LocalDateTime;
import java.util.List;

public interface AbstractService {
    /**
     * Метод устанавливает новый статус исполнения услуги.
     * @param status Новый статус.
     */
    void setStatus(ServiceStatusTypes status);

    /**
     * Метод получения статуса исполнения услуги.
     * @return Статус.
     */
    ServiceStatusTypes getStatus();

    /**
     * Метод установки времени оказания услуги.
     * @param serviceTime Время оказания услуги.
     */
    void setServiceTime(LocalDateTime serviceTime);

    /**
     * Метод получения времени окзания услуги.
     * @return Время оказания услуги.
     */
    LocalDateTime getServiceTime();

    /**
     * Метод получения цены услуги.
     * @return Цена.
     */
    int getPrice();
}
