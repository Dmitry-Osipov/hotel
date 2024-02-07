package repository.service;

import essence.service.AbstractService;
import lombok.Getter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

/**
 * Класс отвечает за хранение услуг отеля. Реализован с использованием паттерна Singleton для обеспечения
 * единственного экземпляра репозитория услуг.
 */
@Getter
@ToString
public class ServiceRepository {
    private final Set<AbstractService> services = new HashSet<>();

    /**
     * Получение экземпляра репозитория. Метод следует использовать для получения доступа к репозиторию клиентов.
     * @return Единственный экземпляр ServiceRepository.
     */
    public static ServiceRepository getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    private ServiceRepository() {
    }

    /**
     * Служебный класс содержит единственный экземпляр репозитория для обеспечения ленивой инициализации и
     * единственности.
     */
    private static class SingletonHolder {
        /**
         * Единственный экземпляр, создаваемый при загрузке класса.
         */
        public static final ServiceRepository HOLDER_INSTANCE = new ServiceRepository();
    }
}
