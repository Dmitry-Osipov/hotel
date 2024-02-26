package repository;

import annotations.annotation.Component;
import essence.room.AbstractRoom;
import lombok.Getter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

/**
 * Класс отвечает за хранение комнат отеля. Реализован с использованием паттерна Singleton для обеспечения
 * единственного экземпляра репозитория комнат.
 */
@Component
@Getter
@ToString
public class RoomRepository {
    private final Set<AbstractRoom> rooms = new HashSet<>();
}
