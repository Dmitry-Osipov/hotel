package essence.person;

import java.time.LocalDateTime;

public interface AbstractClient {
    /**
     * Метод получения номера телефона.
     * @return Номер телефона.
     */
    String getPhoneNumber();

    String getFio();

    void setCheckInTime(LocalDateTime checkInTime);

    LocalDateTime getCheckInTime();

    void setCheckOutTime(LocalDateTime checkOutTime);

    LocalDateTime getCheckOutTime();
}
