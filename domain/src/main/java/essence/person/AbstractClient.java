package essence.person;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import essence.Identifiable;

import java.time.LocalDateTime;

/**
 * Интерфейс требует реализации методов клиента.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = Client.class, name = "Client")})
public interface AbstractClient extends Identifiable {
    /**
     * Метод установки нового ID.
     * @param id ID
     */
    void setId(int id);

    /**
     * Метод получения номера телефона.
     * @return Номер телефона.
     */
    String getPhoneNumber();

    /**
     * Метод установки нового номера телефона.
     * @param phoneNumber Номер телефона.
     */
    void setPhoneNumber(String phoneNumber);

    /**
     * Метод получения ФИО клиента.
     * @return ФИО клиента.
     */
    String getFio();

    /**
     * Метод установки времени заселения клиента.
     * @param checkInTime Время заселения.
     */
    void setCheckInTime(LocalDateTime checkInTime);

    /**
     * Метод получения времени заселения клиента.
     * @return Время заселения.
     */
    LocalDateTime getCheckInTime();

    /**
     * Метод установки времени выселения клиента.
     * @param checkOutTime Время выселения.
     */
    void setCheckOutTime(LocalDateTime checkOutTime);

    /**
     * Метод получения времени выселения клиента.
     * @return Время выселения.
     */
    LocalDateTime getCheckOutTime();

    /**
     * Метод установления нового имени клиента.
     * @param fio Новое имя клиента.
     */
    void setFio(String fio);
}
