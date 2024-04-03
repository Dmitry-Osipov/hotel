package repository;

import dao.IDao;
import essence.reservation.RoomReservation;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import utils.exceptions.TechnicalException;

import java.sql.SQLException;
import java.util.List;

/**
 * Класс отвечает за хранение списка резервов отеля.
 */
@Repository
@ToString
public class RoomReservationRepository {
    private final IDao dao;

    @Autowired
    public RoomReservationRepository(@Qualifier("hiberDao") IDao dao) {
        this.dao = dao;
    }

    /**
     * Возвращает список всех бронирований номеров отеля.
     * @return список бронирований номеров отеля.
     * @throws SQLException если произошла ошибка при получении данных из базы данных.
     */
    public <T extends RoomReservation> List<T> getReservations() throws SQLException {
        return (List<T>) dao.getAll(RoomReservation.class);
    }

    /**
     * Сохраняет информацию о бронировании в репозитории.
     * @param reservation объект бронирования, который нужно сохранить или обновить.
     * @throws SQLException если произошла ошибка при выполнении SQL-запроса.
     */
    public void save(RoomReservation reservation) throws SQLException {
        dao.save(reservation);
    }

    /**
     * Обновляет информацию о бронировании в репозитории.
     * @param reservation объект бронирования, который нужно сохранить или обновить.
     * @throws SQLException если произошла ошибка при выполнении SQL-запроса.
     * @throws TechnicalException если не удалось обновить резервацию.
     */
    public void update(RoomReservation reservation) throws SQLException {
        dao.update(reservation);
    }
}
