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
 * Класс отвечает за хранение списка резервов отеля. Реализован с использованием паттерна Singleton для обеспечения
 * единственного экземпляра репозитория зарезервированных комнат.
 */
@Component
@ToString
public class RoomReservationRepository {
    @InjectByInterface(clazz = JdbcDao.class)
    private IDao dao;

    // TODO: дока
    public <T extends RoomReservation> List<T> getReservations() throws SQLException {
        return (List<T>) dao.getAll(RoomReservation.class);
    }

    public void saveOrUpdate(RoomReservation reservation) throws SQLException {
        try {
            dao.update(reservation);
        } catch (TechnicalException e) {
            dao.save(reservation);
        }
    }
}
