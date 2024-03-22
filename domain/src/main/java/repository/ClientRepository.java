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

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
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
     * @throws SQLException если произошла ошибка SQL.
     * @throws NoSuchFieldException если запрашиваемое поле не существует.
     * @throws InvocationTargetException если вызванный метод вызывает исключение.
     * @throws NoSuchMethodException если запрашиваемый метод не существует.
     * @throws InstantiationException если попытка создать экземпляр класса абстрактного класса, интерфейса, массива
     * абстрактных классов или интерфейсов, или если класс, указанный в параметре типа, является абстрактным.
     * @throws IllegalAccessException если доступ к классу или его полям был закрыт.
     * @throws ClassNotFoundException если класс с указанным именем не найден.
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
     * @throws SQLException если произошла ошибка SQL.
     * @throws IllegalAccessException если доступ к классу или его полям был закрыт.
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
