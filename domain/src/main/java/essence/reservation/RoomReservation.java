package essence.reservation;

import essence.Identifiable;
import essence.person.AbstractClient;
import essence.person.Client;
import essence.room.AbstractRoom;
import essence.room.Room;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс резервации.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(schema = "hotel", name = "room_reservation")
public class RoomReservation implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
            targetEntity = Client.class)
    @JoinTable(schema = "hotel", name = "reservation_client", joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id"))
    private List<AbstractClient> clients;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
            targetEntity = Room.class)
    @JoinColumn(name = "room_id")
    private AbstractRoom room;

    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;

    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;

    public RoomReservation(int id, AbstractRoom room, LocalDateTime checkInTime, LocalDateTime checkOutTime,
                           List<AbstractClient> clients) {
        this.id = id;
        this.room = room;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.clients = clients;
    }
}
