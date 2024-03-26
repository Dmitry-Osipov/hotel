package repository;

import annotations.annotation.Component;
import annotations.annotation.InjectByInterface;
import annotations.factory.InitializeComponent;
import dao.IDao;
import dao.JdbcDao;
import essence.provided.ProvidedService;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import utils.exceptions.TechnicalException;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс отвечает за хранение списка оказанных услуг. Реализован с использованием паттерна Singleton для обеспечения
 * единственного экземпляра репозитория оказанных услуг.
 */
@Component
@Getter
@ToString
public class ProvidedServicesRepository implements InitializeComponent {
    private final List<ProvidedService> providedServices = new ArrayList<>();
    @InjectByInterface(clazz = JdbcDao.class)
    private IDao dao;

    /**
     * Метод инициализации, который загружает все сервисы из базы данных и добавляет их в коллекцию.
     */
    @Override
    @SneakyThrows
    public void init() {
        providedServices.addAll(dao.getAll(ProvidedService.class));
    }

    /**
     * Метод сохранения сервисов в базу данных.
     * Перебирает все сервисы из коллекции и обновляет их в базе данных.
     * В случае возникновения технической ошибки при обновлении, метод сохраняет сервис в базе данных.
     */
    @SneakyThrows
    public void saveToDb() {
        for (ProvidedService providedService : providedServices) {
            try {
                dao.update(providedService);
            } catch (TechnicalException e) {
                dao.save(providedService);
            }
        }
    }
}
