package service;

import essence.person.AbstractClient;
import essence.provided.ProvidedService;
import essence.service.AbstractFavor;
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
import java.util.Objects;
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
     * @throws SQLException если произошла ошибка SQL.
     */
    public void updateService(AbstractService service) throws SQLException {
        int serviceId = service.getId();
        serviceLogger.info("Вызван метод обновления услуги с ID {}", serviceId);

        try {
            serviceRepository.update(service);
            serviceLogger.info("Обновлена услуга с ID {}", serviceId);
        } catch (TechnicalException e) {
            serviceLogger.error("Не удалось обновить услугу с ID {}", serviceId);
            throw e;
        }
    }

    /**
     * Импортирует список услуг.
     * Метод выполняет импорт списка услуг, обновляя существующие услуги или добавляя новые.
     * @param services список услуг для импорта.
     * @throws SQLException если возникает ошибка при работе с базой данных.
     */
    public void importServices(List<AbstractService> services) throws SQLException {
        serviceLogger.info("Вызван метод импорта услуг");
        for (AbstractService service : services) {
            try {
                updateService(service);
            } catch (TechnicalException e) {
                addService(service);
            }
        }
        serviceLogger.info("Услуги импортированы успешно");
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
     * @throws SQLException если произошла ошибка SQL.
     */
    public void updateProvidedService(ProvidedService providedService) throws SQLException {
        int providedServiceId = providedService.getId();
        providedServiceLogger.info("Вызван метод обновления оказанной услуги с ID {}", providedServiceId);

        try {
            providedServicesRepository.update(providedService);
            providedServiceLogger.info("Обновилась проведённая услуга с ID {}", providedServiceId);
        } catch (TechnicalException e) {
            providedServiceLogger.error("Не удалось обновить оказанную услугу с ID {}", providedServiceId);
            throw e;
        }
    }

    /**
     * Импортирует список проведенных услуг.
     * Метод выполняет импорт списка проведенных услуг, обновляя существующие записи или добавляя новые.
     * @param providedServices список проведенных услуг для импорта.
     * @throws SQLException если возникает ошибка при работе с базой данных.
     */
    public void importProvidedServices(List<ProvidedService> providedServices) throws SQLException {
        providedServiceLogger.info("Вызван метод импорта проведённых услуг");
        for (ProvidedService providedService : providedServices) {
            try {
                updateProvidedService(providedService);
            } catch (TechnicalException e) {
                addProvidedService(providedService);
            }
        }
        providedServiceLogger.info("Проведённые услуги импортированы успешно");
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
     * Метод проводит услугу для конкретного клиента.
     * @param providedService Проведённая услуга.
     * @throws NoEntityException Ошибка вылетает, если услуги нет в отеле.
     * @throws SQLException если произошла ошибка SQL.
     * @throws InvalidDataException если услуга была проведена ранее.
     * @throws TechnicalException если не удалось обновить услугу.
     */
    public void provideService(ProvidedService providedService) throws NoEntityException, SQLException {
        provideService(providedService.getClient(), providedService.getService());
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
        serviceLogger.info("Вызван метод получения услуги по ID {}", serviceId);
        try {
            AbstractService service = serviceRepository.getServiceById(serviceId);
            serviceLogger.info("Удалось получить услугу по ID {}", serviceId);
            return service;
        } catch (TechnicalException e) {
            serviceLogger.error("Не удалось получить услугу по ID {}", serviceId);
            throw e;
        }
    }

    /**
     * Удаляет услугу из базы данных.
     * @param service Услуга, которая должна быть удалена.
     * @throws SQLException Если возникает ошибка доступа к базе данных.
     * @throws TechnicalException Если возникает техническая ошибка при выполнении операции.
     */
    public void deleteService(AbstractService service) throws SQLException {
        int serviceId = service.getId();
        serviceLogger.info("Вызван метод удаления услуги с ID {}", serviceId);
        try {
            serviceRepository.deleteService(service);
            serviceLogger.info("Удалось удалить услугу с ID {}", serviceId);
        } catch (TechnicalException e) {
            serviceLogger.error("Не удалось удалить услугу с ID {}", serviceId);
            throw e;
        }
    }

    /**
     * Метод подсчитывает цену конкретной услуги.
     * @param id уникальный идентификатор услуги.
     * @return Стоимость разового оказания услуги.
     */
    public int getFavorPrice(int id) throws SQLException {
        return getFavorPrice((AbstractFavor) getServiceById(id));
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
                .filter(service -> Objects.equals(service.getClient(), client)
                        && service.getService().getStatus() == ServiceStatusTypes.RENDERED)
                .map(ProvidedService::getService);
    }
}
