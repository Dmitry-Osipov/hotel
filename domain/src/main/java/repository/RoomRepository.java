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
 * Класс отвечает за хранение комнат отеля.
 */
@Component
@ToString
public class RoomRepository {
    @InjectByInterface(clazz = JdbcDao.class)
    private IDao dao;

    /**
     * Возвращает список всех номеров отеля.
     * @return список номеров отеля.
     * @throws SQLException если произошла ошибка при получении данных из базы данных.
     */
    public <T extends AbstractRoom> List<T> getRooms() throws SQLException {
        return (List<T>) dao.getAll(Room.class);
    }

    /**
     * Сохраняет или обновляет информацию о номере в репозитории.
     * @param room объект номера, который нужно сохранить или обновить.
     * @throws SQLException если произошла ошибка при выполнении SQL-запроса.
     */
    public void saveOrUpdate(AbstractRoom room) throws SQLException {
        try {
            dao.update(room);
        } catch (TechnicalException e) {
            dao.save(room);
        }
    }
}
