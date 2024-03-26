package repository;

import annotations.annotation.Component;
import annotations.annotation.InjectByInterface;
import annotations.factory.InitializeComponent;
import dao.IDao;
import dao.JdbcDao;
import essence.service.AbstractService;
import essence.service.Service;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import utils.exceptions.TechnicalException;

import java.util.HashSet;
import java.util.Set;

/**
 * Класс отвечает за хранение услуг отеля.
 */
@Component
@Getter
@ToString
public class ServiceRepository implements InitializeComponent {
    private final Set<AbstractService> services = new HashSet<>();
    @InjectByInterface(clazz = JdbcDao.class)
    private IDao dao;

    /**
     * Метод инициализации, который загружает все сервисы из базы данных и добавляет их в коллекцию.
     */
    @Override
    @SneakyThrows
    public void init() {
        services.addAll(dao.getAll(Service.class));
    }

    /**
     * Метод сохранения сервисов в базу данных.
     * Перебирает все сервисы из коллекции и обновляет их в базе данных.
     * В случае возникновения технической ошибки при обновлении, метод сохраняет сервис в базе данных.
     */
    @SneakyThrows
    public void saveToDb() {
        for (AbstractService service : services) {
            try {
                dao.update(service);
            } catch (TechnicalException e) {
                dao.save(service);
            }
        }
    }
}
