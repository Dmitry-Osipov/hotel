package repository;

import annotations.annotation.Component;
import annotations.annotation.InjectByInterface;
import dao.IDao;
import dao.JdbcDao;
import essence.reservation.RoomReservation;
import lombok.ToString;
import utils.exceptions.TechnicalException;

import java.sql.SQLException;
import java.util.List;

/**
 * Класс отвечает за хранение списка резервов отеля.
 */
@Component
@ToString
public class RoomReservationRepository {
    @InjectByInterface(clazz = JdbcDao.class)
    private IDao dao;

    /**
     * Возвращает список всех бронирований номеров отеля.
     * @return список бронирований номеров отеля.
     * @throws SQLException если произошла ошибка при получении данных из базы данных.
     */
    public <T extends RoomReservation> List<T> getReservations() throws SQLException {
        return (List<T>) dao.getAll(RoomReservation.class);
    }

    /**
     * Сохраняет или обновляет информацию о бронировании в репозитории.
     * @param reservation объект бронирования, который нужно сохранить или обновить.
     * @throws SQLException если произошла ошибка при выполнении SQL-запроса.
     */
    public void saveOrUpdate(RoomReservation reservation) throws SQLException {
        try {
            dao.update(reservation);
        } catch (TechnicalException e) {
            dao.save(reservation);
        }
    }
}
