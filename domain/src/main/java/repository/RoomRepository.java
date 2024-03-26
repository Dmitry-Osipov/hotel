package repository;

import annotations.annotation.Component;
import annotations.annotation.InjectByInterface;
import annotations.factory.InitializeComponent;
import dao.IDao;
import dao.JdbcDao;
import essence.room.AbstractRoom;
import essence.room.Room;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import utils.exceptions.TechnicalException;

import java.util.HashSet;
import java.util.Set;

/**
 * Класс отвечает за хранение комнат отеля. Реализован с использованием паттерна Singleton для обеспечения
 * единственного экземпляра репозитория комнат.
 */
@Component
@Getter
@ToString
public class RoomRepository implements InitializeComponent {
    private final Set<AbstractRoom> rooms = new HashSet<>();
    @InjectByInterface(clazz = JdbcDao.class)
    private IDao dao;

    /**
     * Метод инициализации, который загружает все сервисы из базы данных и добавляет их в коллекцию.
     */
    @Override
    @SneakyThrows
    public void init() {
        rooms.addAll(dao.getAll(Room.class));
    }

    /**
     * Метод сохранения сервисов в базу данных.
     * Перебирает все сервисы из коллекции и обновляет их в базе данных.
     * В случае возникновения технической ошибки при обновлении, метод сохраняет сервис в базе данных.
     */
    @SneakyThrows
    public void saveToDb() {
        for (AbstractRoom room : rooms) {
            try {
                dao.update(room);
            } catch (TechnicalException e) {
                dao.save(room);
            }
        }
    }
}
