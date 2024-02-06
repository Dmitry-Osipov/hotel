package repository.room;

import lombok.Getter;
import lombok.ToString;
import essence.room.AbstractRoom;

import java.util.HashSet;
import java.util.Set;

/**
 * Класс отвечает за хранение комнат отеля. Реализован с использованием паттерна Singleton для обеспечения
 * единственного экземпляра репозитория комнат.
 */
@Getter
@ToString
public class RoomRepository {
    private final Set<AbstractRoom> rooms = new HashSet<>();

    /**
     * Получение экземпляра репозитория. Метод следует использовать для получения доступа к репозиторию клиентов.
     * @return Единственный экземпляр RoomRepository.
     */
    public static RoomRepository getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    private RoomRepository() {
    }

    /**
     * Служебный класс содержит единственный экземпляр репозитория для обеспечения ленивой инициализации и
     * единственности.
     */
    private static class SingletonHolder {
        /**
         * Единственный экземпляр, создаваемый при загрузке класса.
         */
        public static final RoomRepository HOLDER_INSTANCE = new RoomRepository();
    }
}
