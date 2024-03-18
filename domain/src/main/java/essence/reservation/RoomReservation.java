package essence.reservation;

import essence.Identifiable;
import essence.person.AbstractClient;
import essence.room.AbstractRoom;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс резервации.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class RoomReservation implements Identifiable {
    private int id;
    private List<AbstractClient> clients = new ArrayList<>();
    private AbstractRoom room;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    public RoomReservation(int id, AbstractRoom room, LocalDateTime checkInTime, LocalDateTime checkOutTime,
                           List<AbstractClient> clients) {
        this.id = id;
        this.room = room;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.clients.addAll(clients);
    }
}
