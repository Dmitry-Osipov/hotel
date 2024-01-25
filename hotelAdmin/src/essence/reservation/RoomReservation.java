package essence.reservation;

import essence.Identifiable;
import essence.person.AbstractClient;
import essence.room.AbstractRoom;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class RoomReservation implements Identifiable {
    private final int id;
    private final List<AbstractClient> clients = new ArrayList<>();
    @Setter
    private AbstractRoom room;
    @Setter
    private LocalDateTime checkInTime;
    @Setter
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
