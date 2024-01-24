package essence.service;

import essence.Identifiable;

import java.time.LocalDateTime;

public interface AbstractService extends Identifiable {
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

    /**
     * Метод установки нового названия услуги.
     * @param name Название услуги.
     */
    void setName(ServiceNames name);
}
