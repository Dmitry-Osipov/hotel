package repository;

import lombok.Getter;
import lombok.ToString;
import essence.room.AbstractRoom;

import java.util.HashSet;
import java.util.Set;

/**
 * Класс отвечает за хранение комнат отеля.
 */
@Getter
@ToString
public class RoomRepository {
    private final Set<AbstractRoom> rooms = new HashSet<>();
}
