package essence.room;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import essence.Identifiable;

import java.time.LocalDateTime;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = Room.class, name = "Room")})
public interface AbstractRoom extends Identifiable {
    /**
     * Метод установки нового статуса комнаты.
     * @param status Новый статус.
     */
    void setStatus(RoomStatusTypes status);

    /**
     * Метод получения статуса комнаты.
     * @return Статус комнаты.
     */
    RoomStatusTypes getStatus();

    /**
     * Метод получения вместимости комнаты.
     * @return Вместимость комнаты.
     */
    int getCapacity();

    /**
     * Метод добавления новых звёзд комнате.
     * @param stars Количество звёзд.
     */
    void setStars(int stars);

    /**
     * Метод получения количества звёзд комнаты.
     * @return Количество звёзд.
     */
    int getStars();

    /**
     * Метод устанавливает новую цену на комнату.
     * @param price Новая цена.
     */
    void setPrice(int price);

    /**
     * Метод получения цены комнаты.
     * @return Цена.
     */
    int getPrice();

    /**
     * Метод установки времени заселения в комнату.
     * @param checkInTime Время заселения.
     */
    void setCheckInTime(LocalDateTime checkInTime);

    /**
     * Метод получения времени заселения в комнату.
     * @return Время заселения.
     */
    LocalDateTime getCheckInTime();

    /**
     * Метод установки времени выселения из комнаты.
     * @param checkOutTime Время выселения.
     */
    void setCheckOutTime(LocalDateTime checkOutTime);

    /**
     * Метод получения времени выселения из комнаты.
     * @return Время выселения.
     */
    LocalDateTime getCheckOutTime();

    /**
     * Метод получения номера комнаты.
     * @return Номер комнаты.
     */
    int getNumber();

    /**
     * Метод устанавливает новый номер.
     * @param number Новый номер.
     */
    void setNumber(int number);

    /**
     * Метод устанавливает новое значение вместимости.
     * @param capacity Новая вместимость.
     */
    void setCapacity(int capacity);
}
