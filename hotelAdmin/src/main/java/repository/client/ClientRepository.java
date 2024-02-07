package repository.client;

import essence.person.AbstractClient;
import lombok.Getter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

/**
 * Класс отвечает за хранение клиентов отеля. Реализован с использованием паттерна Singleton для обеспечения
 * единственного экземпляра репозитория клиентов.
 */
@Getter
@ToString
public class ClientRepository {
    private final Set<AbstractClient> clients = new HashSet<>();

    /**
     * Получение экземпляра репозитория. Метод следует использовать для получения доступа к репозиторию клиентов.
     * @return Единственный экземпляр ClientRepository.
     */
    public static ClientRepository getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    private ClientRepository() {
    }

    /**
     * Служебный класс содержит единственный экземпляр репозитория для обеспечения ленивой инициализации и
     * единственности.
     */
    private static class SingletonHolder {
        /**
         * Единственный экземпляр, создаваемый при загрузке класса.
         */
        public static final ClientRepository HOLDER_INSTANCE = new ClientRepository();
    }
}
