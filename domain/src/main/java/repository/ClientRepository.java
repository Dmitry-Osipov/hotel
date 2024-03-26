package repository;

import annotations.annotation.Component;
import annotations.annotation.InjectByInterface;
import annotations.factory.InitializeComponent;
import dao.IDao;
import dao.JdbcDao;
import essence.person.AbstractClient;
import essence.person.Client;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import utils.exceptions.TechnicalException;

import java.util.HashSet;
import java.util.Set;

/**
 * Класс отвечает за хранение клиентов отеля. Реализован с использованием паттерна Singleton для обеспечения
 * единственного экземпляра репозитория клиентов.
 */
@Component
@Getter
@ToString
public class ClientRepository implements InitializeComponent {
    private final Set<AbstractClient> clients = new HashSet<>();
    @InjectByInterface(clazz = JdbcDao.class)
    private IDao dao;

    /**
     * Метод инициализации, который загружает все сервисы из базы данных и добавляет их в коллекцию.
     */
    @Override
    @SneakyThrows
    public void init() {
        clients.addAll(dao.getAll(Client.class));
    }

    /**
     * Метод сохранения сервисов в базу данных.
     * Перебирает все сервисы из коллекции и обновляет их в базе данных.
     * В случае возникновения технической ошибки при обновлении, метод сохраняет сервис в базе данных.
     */
    @SneakyThrows
    public void saveToDb() {
        for (AbstractClient client : clients) {
            try {
                dao.update(client);
            } catch (TechnicalException e) {
                dao.save(client);
            }
        }
    }
}
