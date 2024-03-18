package essence.service;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import essence.Identifiable;

import java.time.LocalDateTime;

/**
 * Интерфейс требует реализации методов услуги.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = Service.class, name = "Service")})
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
     * Метод получения времени оказания услуги.
     * @return Время оказания услуги.
     */
    LocalDateTime getServiceTime();

    /**
     * Метод получения цены услуги.
     * @return Цена.
     */
    int getPrice();

    /**
     * Метод установки новой цены услуги.
     * @param price Новая цена.
     */
    void setPrice(int price);

    /**
     * Метод установки нового названия услуги.
     * @param name Название услуги.
     */
    void setName(ServiceNames name);

    /**
     * Метод получения названия услуги.
     * @return Название услуги.
     */
    ServiceNames getName();
}
