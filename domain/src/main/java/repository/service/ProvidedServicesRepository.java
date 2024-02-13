package repository.service;

import essence.provided.ProvidedService;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс отвечает за хранение списка оказанных услуг. Реализован с использованием паттерна Singleton для обеспечения
 * единственного экземпляра репозитория оказанных услуг.
 */
@Getter
public class ProvidedServicesRepository {
    private final List<ProvidedService> providedServices = new ArrayList<>();

    /**
     * Получение экземпляра репозитория. Метод следует использовать для получения доступа к репозиторию клиентов.
     * @return Единственный экземпляр ProvidedServicesRepository.
     */
    public static ProvidedServicesRepository getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    private ProvidedServicesRepository() {
    }

    /**
     * Служебный класс содержит единственный экземпляр репозитория для обеспечения ленивой инициализации и
     * единственности.
     */
    private static class SingletonHolder {
        /**
         * Единственный экземпляр, создаваемый при загрузке класса.
         */
        public static final ProvidedServicesRepository HOLDER_INSTANCE = new ProvidedServicesRepository();
    }
}
