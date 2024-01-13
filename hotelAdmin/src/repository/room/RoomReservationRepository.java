package repository.room;

import essence.reservation.RoomReservation;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс отвечает за хранение списка резервов отеля.
 */
@Getter
public class RoomReservationRepository {
    private final List<RoomReservation> reservations = new ArrayList<>();
}
