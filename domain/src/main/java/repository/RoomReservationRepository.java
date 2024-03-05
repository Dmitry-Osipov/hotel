package repository;

import annotations.annotation.Component;
import essence.reservation.RoomReservation;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс отвечает за хранение списка резервов отеля. Реализован с использованием паттерна Singleton для обеспечения
 * единственного экземпляра репозитория зарезервированных комнат.
 */
@Component
@Getter
@ToString
public class RoomReservationRepository {
    private final List<RoomReservation> reservations = new ArrayList<>();
}
