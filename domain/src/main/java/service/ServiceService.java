package service;

import essence.person.AbstractClient;
import essence.provided.ProvidedService;
import essence.service.AbstractService;
import essence.service.ServiceStatusTypes;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.ProvidedServicesRepository;
import repository.ServiceRepository;
import utils.comparators.ServiceTimeComparator;
import utils.exceptions.EntityContainedException;
import utils.exceptions.ErrorMessages;
import utils.exceptions.InvalidDataException;
import utils.exceptions.NoEntityException;
import utils.exceptions.TechnicalException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

/**
 * Класс отвечает за обработку данных по услугам.
 */
@Service
@ToString
public class ServiceService extends AbstractFavorService {
    private static final Logger serviceLogger = LoggerFactory.getLogger(ServiceService.class);
    private static final Logger providedServiceLogger = LoggerFactory.getLogger("service.ProvidedServiceService");
    private final ServiceRepository serviceRepository;
    private final ProvidedServicesRepository providedServicesRepository;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository, ProvidedServicesRepository providedServicesRepository) {
        this.serviceRepository = serviceRepository;
        this.providedServicesRepository = providedServicesRepository;
    }

    /**
     * Метод добавляет новую услугу в список всех услуг отеля.
     * @param service Новая услуга.
     * @throws EntityContainedException Ошибка вылетает, когда услуга уже содержится в отеле (невозможно повторно
     * добавить).
     * @throws SQLException если произошла ошибка SQL.
     */
    public void addService(AbstractService service) throws EntityContainedException, SQLException {
        serviceLogger.info("Вызван метод добавления услуги");
        serviceRepository.save(service);
        serviceLogger.info("Добавлена новая услуга");
    }

    /**
     * Метод обновления данных услуги. Валидация услуги происходит по её ID.
     * @param service Новые данные услуги, собранные в классе услуги.
     * @return {@code true}, если удалось обновить данные, иначе {@code false}.
     * @throws SQLException если произошла ошибка SQL.
     */
    public boolean updateService(AbstractService service) throws SQLException {
        int serviceId = service.getId();
        serviceLogger.info("Вызван метод обновления услуги с ID {}", serviceId);

        try {
            serviceRepository.update(service);
            serviceLogger.info("Обновлена услуга с ID {}", serviceId);
            return true;
        } catch (TechnicalException e) {
            serviceLogger.error("Не удалось обновить услугу с ID {}", serviceId);
            return false;
        }
    }

    /**
     * Метод addProvidedService добавляет оказанную услугу в репозиторий оказанных услуг.
     * @param providedService Оказанная услуга для добавления.
     * @throws SQLException если произошла ошибка SQL.
     */
    public void addProvidedService(ProvidedService providedService) throws SQLException {
        providedServiceLogger.info("Вызван метод добавления новой оказанной услуги");
        providedServicesRepository.save(providedService);
        providedServiceLogger.info("Добавлена новая оказанная услуга");
    }

    /**
     * Метод updateProvidedService обновляет информацию об оказанной услуге в репозитории оказанных услуг.
     * @param providedService Обновленная информация об оказанной услуге.
     * @return {@code true}, если информация успешно добавлена, иначе {@code false}.
     * @throws SQLException если произошла ошибка SQL.
     */
    public boolean updateProvidedService(ProvidedService providedService) throws SQLException {
        int providedServiceId = providedService.getId();
        providedServiceLogger.info("Вызван метод обновления оказанной услуги с ID {}", providedServiceId);

        try {
            providedServicesRepository.update(providedService);
            providedServiceLogger.info("Обновилась проведённая услуга с ID {}", providedServiceId);
            return true;
        } catch (TechnicalException e) {
            providedServiceLogger.error("Не удалось обновить оказанную услугу с ID {}", providedServiceId);
            return false;
        }
    }

    /**
     * Метод проводит услугу для конкретного клиента.
     * @param client Клиент.
     * @param service Услуга.
     * @throws NoEntityException Ошибка вылетает, если услуги нет в отеле.
     * @throws SQLException если произошла ошибка SQL.
     * @throws InvalidDataException если услуга была проведена ранее.
     * @throws TechnicalException если не удалось обновить услугу.
     */
    public void provideService(AbstractClient client, AbstractService service) throws NoEntityException, SQLException {
        String startMessage = "Вызван метод оказания услуги с ID {} для клиента с ID {}";
        int serviceId = service.getId();
        int clientId = client.getId();
        serviceLogger.info(startMessage, serviceId, clientId);
        providedServiceLogger.info(startMessage, serviceId, clientId);

        if (!serviceRepository.getServices().contains(service)) {
            String message = "Провалена попытка оказания услуги с ID {} для клиента с ID {}";
            serviceLogger.error(message, serviceId, clientId);
            providedServiceLogger.error(message, serviceId, clientId);
            throw new NoEntityException(ErrorMessages.NO_SERVICE.getMessage());
        }

        if (service.getStatus().equals(ServiceStatusTypes.RENDERED)) {
            throw new InvalidDataException(ErrorMessages.SERVICE_PROVIDED_ADDITION_FAILURE.getMessage());
        }

        LocalDateTime now = LocalDateTime.now();
        ProvidedService providedService = new ProvidedService();
        providedService.setService(service);
        providedService.setServiceTime(now);
        providedService.setClient(client);
        addProvidedService(providedService);
        service.setServiceTime(now);
        service.setStatus(ServiceStatusTypes.RENDERED);
        serviceRepository.update(service);
        serviceLogger.info("Услуга с ID {} была оказана клиенту с ID {}", serviceId, clientId);
    }

    /**
     * Метод формирует список услуг, оказанных конкретному клиенту, с сортировкой по увеличению цены.
     * @param client Клиент.
     * @return Список услуг.
     * @throws SQLException если произошла ошибка SQL.
     */
    public List<AbstractService> getClientServicesByPrice(AbstractClient client) throws SQLException {
        int clientId = client.getId();
        providedServiceLogger.info("Вызван метод получения списка оказанных услуг, отсортированных по возрастанию " +
                "цены, для клиента с ID {}", clientId);
        List<AbstractService> services = streamClientServices(client).sorted().toList();
        providedServiceLogger.info("Для клиента с ID {} получен следующий список оказанных услуг, " +
                "отсортированный по возрастанию цены: {}", clientId, services);
        return services;
    }

    /**
     * Метод формирует список услуг, оказанных конкретному клиенту, с сортировкой по убыванию времени оказания услуги.
     * @param client Клиент.
     * @return Список услуг.
     * @throws SQLException если произошла ошибка SQL.
     */
    public List<AbstractService> getClientServicesByTime(AbstractClient client) throws SQLException {
        int clientId = client.getId();
        providedServiceLogger.info("Вызван метод получения списка оказанных услуг, отсортированных по убыванию " +
                "времени оказания услуги, для клиента с ID {}", clientId);
        List<AbstractService> services = streamClientServices(client).sorted(new ServiceTimeComparator())
                .toList().reversed();
        providedServiceLogger.info("Для клиента с ID {} получен следующий список оказанных услуг, отсортированный " +
                "по убыванию времени оказания услуги: {}", clientId, services);
        return services;
    }

    /**
     * Метод возвращает список всех услуг.
     * @return Список всех услуг.
     * @throws SQLException если произошла ошибка SQL.
     */
    public List<AbstractService> getServices() throws SQLException {
        serviceLogger.info("Вызван метод получения списка всех услуг");
        return serviceRepository.getServices();
    }

    /**
     * Метод возвращает список оказанных услуг.
     * @return Список оказанных услуг.
     * @throws SQLException если произошла ошибка SQL.
     */
    public List<ProvidedService> getProvidedServices() throws SQLException {
        providedServiceLogger.info("Вызван метод получения списка оказанных услуг");
        return providedServicesRepository.getProvidedServices();
    }

    /**
     * Получает услугу из репозитория по указанному идентификатору.
     * @param serviceId Уникальный идентификатор услуги.
     * @return Услуга с указанным идентификатором.
     * @throws SQLException Если возникает ошибка доступа к базе данных.
     * @throws TechnicalException Если возникает техническая ошибка при выполнении операции.
     */
    public AbstractService getServiceById(int serviceId) throws SQLException {
        serviceLogger.info("Вызван метод получения клиента по ID {}", serviceId);
        try {
            AbstractService service = serviceRepository.getServiceById(serviceId);
            serviceLogger.info("Удалось получить клиента по ID {}", serviceId);
            return service;
        } catch (TechnicalException e) {
            serviceLogger.error("Не удалось получить клиента по ID {}", serviceId);
            throw e;
        }
    }

    /**
     * Удаляет услугу из базы данных.
     * @param service Услуга, которая должна быть удалена.
     * @throws SQLException Если возникает ошибка доступа к базе данных.
     * @throws TechnicalException Если возникает техническая ошибка при выполнении операции.
     */
    public void deleteClient(AbstractService service) throws SQLException {
        int serviceId = service.getId();
        serviceLogger.info("Вызван метод удаления клиента с ID {}", serviceId);
        try {
            serviceRepository.deleteService(service);
            serviceLogger.info("Удалось удалить клиента с ID {}", serviceId);
        } catch (TechnicalException e) {
            serviceLogger.error("Не удалось удалить клиента с ID {}", serviceId);
            throw e;
        }
    }

    /**
     * Служебный метод предназначен для устранения дублирования кода. Метод фильтрует стрим от списка услуг по
     * содержанию конкретного клиента в списке.
     * @param client Клиент.
     * @return Отфильтрованный стрим.
     * @throws SQLException если произошла ошибка SQL.
     */
    private Stream<AbstractService> streamClientServices(AbstractClient client) throws SQLException {
        return providedServicesRepository.getProvidedServices()
                .stream()
                .filter(service -> List.of(service.getClient()).contains(client)
                        && service.getService().getStatus() == ServiceStatusTypes.RENDERED)
                .map(ProvidedService::getService);
    }
}
