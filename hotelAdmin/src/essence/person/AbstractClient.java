package essence.person;

import java.time.LocalDateTime;

public interface AbstractClient {
    /**
     * Метод получения номера телефона.
     * @return Номер телефона.
     */
    String getPhoneNumber();

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
}
