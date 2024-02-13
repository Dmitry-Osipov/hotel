package essence.room;

import essence.service.AbstractFavor;
import lombok.Getter;
import lombok.Setter;
import utils.exceptions.AccessDeniedException;
import utils.file.FileAdditionResult;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Properties;

@Setter
@Getter
public class Room extends AbstractFavor implements AbstractRoom, Comparable<AbstractRoom> {
    private int number;
    private int capacity;
    private RoomStatusTypes status = RoomStatusTypes.AVAILABLE;
    private int stars;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    public Room() {
    }

    public Room(int id, int number, int capacity, int price) {
        super(id, price);
        this.number = number;
        this.capacity = capacity;
    }

    /**
     * Метод установки нового статуса.
     * @param status Новый статус.
     * @throws IOException Ошибка ввода/вывода.
     * @throws AccessDeniedException Ошибка запрета изменения статуса комнаты.
     */
    public void setStatus(RoomStatusTypes status) throws IOException, AccessDeniedException {
        if (getStatusChangePermissionFromPropertyFile()) {
            this.status = status;
        } else {
            throw new AccessDeniedException("Запрещено изменение статуса комнаты");
        }
    }

    /**
     * Метод получает данные по возможности изменения статуса номера.
     * @return Булево-значение.
     * @throws IOException Ошибка ввода/вывода.
     */
    private boolean getStatusChangePermissionFromPropertyFile() throws IOException {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(FileAdditionResult.getPropertyFile())) {
            properties.load(fis);
            return Boolean.parseBoolean(properties.getProperty("enable_room_status_change"));
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        return getId() == ((Room) obj).getId();
    }

    @Override
    public int compareTo(AbstractRoom o) {
        return o.getStars() - stars;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + getId() + "; " +
                "stars=" + stars + "; " +
                "number=" + number + "; " +
                "capacity=" + capacity + "; " +
                "price=" + getPrice() + "; " +
                "status=" + status + "; " +
                "check-in time=" + checkInTime + "; " +
                "check-out time=" + checkOutTime +
                '}';
    }
}
