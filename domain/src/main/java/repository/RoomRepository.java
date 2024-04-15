package repository;

import dao.IDao;
import essence.room.AbstractRoom;
import essence.room.Room;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import utils.exceptions.TechnicalException;

import java.sql.SQLException;
import java.util.List;

/**
 * Класс отвечает за хранение комнат отеля.
 */
@Repository
@ToString
public class RoomRepository {
    private final IDao dao;

    @Autowired
    public RoomRepository(@Qualifier("hiberDao") IDao dao) {
        this.dao = dao;
    }

    /**
     * Возвращает список всех номеров отеля.
     * @return список номеров отеля.
     * @throws SQLException если произошла ошибка при получении данных из базы данных.
     */
    public <T extends AbstractRoom> List<T> getRooms() throws SQLException {
        return (List<T>) dao.getAll(Room.class);
    }

    /**
     * Сохраняет информацию о номере в репозитории.
     * @param room объект номера, который нужно сохранить или обновить.
     * @throws SQLException если произошла ошибка при выполнении SQL-запроса.
     */
    public void save(AbstractRoom room) throws SQLException {
        dao.save(room);
    }

    /**
     * Обновляет информацию о номере в репозитории.
     * @param room объект номера, который нужно сохранить или обновить.
     * @throws SQLException если произошла ошибка при выполнении SQL-запроса.
     * @throws TechnicalException если произошла ошибка обновления комнаты.
     */
    public void update(AbstractRoom room) throws SQLException {
        dao.update(room);
    }

    /**
     * Получает комнату по ее уникальному идентификатору.
     * @param id Уникальный идентификатор комнаты.
     * @return Комната с указанным ID.
     * @throws SQLException Если происходит ошибка доступа к базе данных.
     * @throws TechnicalException Если возникает техническая ошибка при выполнении операции.
     */
    public AbstractRoom getRoomById(int id) throws SQLException {
        return dao.getOne(id, Room.class);
    }

    /**
     * Удаляет комнату из репозитория.
     * @param room Комната, которая должна быть удалена.
     * @throws SQLException Если происходит ошибка доступа к базе данных.
     * @throws TechnicalException Если возникает техническая ошибка при выполнении операции.
     */
    public void deleteRoom(AbstractRoom room) throws SQLException {
        dao.delete(room);
    }
}
