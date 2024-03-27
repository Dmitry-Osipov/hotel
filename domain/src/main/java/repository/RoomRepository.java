package repository;

import annotations.annotation.Component;
import annotations.annotation.InjectByInterface;
import dao.IDao;
import dao.JdbcDao;
import essence.room.AbstractRoom;
import essence.room.Room;
import lombok.ToString;
import utils.exceptions.TechnicalException;

import java.sql.SQLException;
import java.util.List;

/**
 * Класс отвечает за хранение комнат отеля. Реализован с использованием паттерна Singleton для обеспечения
 * единственного экземпляра репозитория комнат.
 */
@Component
@ToString
public class RoomRepository {
    @InjectByInterface(clazz = JdbcDao.class)
    private IDao dao;

    // TODO: дока
    public <T extends AbstractRoom> List<T> getRooms() throws SQLException {
        return (List<T>) dao.getAll(Room.class);
    }

    public void saveOrUpdate(AbstractRoom room) throws SQLException {
        try {
            dao.update(room);
        } catch (TechnicalException e) {
            dao.save(room);
        }
    }
}
