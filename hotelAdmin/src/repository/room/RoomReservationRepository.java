package repository.room;

import essence.reservation.RoomReservation;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс отвечает за хранение списка резервов отеля. Реализован с использованием паттерна Singleton для обеспечения
 * единственного экземпляра репозитория зарезервированных комнат.
 */
@Getter
public class RoomReservationRepository {
    private final List<RoomReservation> reservations = new ArrayList<>();

    /**
     * Получение экземпляра репозитория. Метод следует использовать для получения доступа к репозиторию клиентов.
     * @return Единственный экземпляр RoomReservationRepository.
     */
    public static RoomReservationRepository getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    private RoomReservationRepository() {
    }

    /**
     * Служебный класс содержит единственный экземпляр репозитория для обеспечения ленивой инициализации и
     * единственности.
     */
    private static class SingletonHolder {
        /**
         * Единственный экземпляр, создаваемый при загрузке класса.
         */
        public static final RoomReservationRepository HOLDER_INSTANCE = new RoomReservationRepository();
    }
}
