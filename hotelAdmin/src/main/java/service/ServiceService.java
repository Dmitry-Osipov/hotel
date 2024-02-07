package service;

import comparators.ServiceTimeComparator;
import essence.Identifiable;
import essence.person.AbstractClient;
import essence.provided.ProvidedService;
import essence.service.AbstractService;
import essence.service.ServiceStatusTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.service.ProvidedServicesRepository;
import repository.service.ServiceRepository;
import ui.utils.csv.FileAdditionResult;
import ui.utils.id.IdFileManager;
import ui.utils.validators.UniqueIdValidator;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

/**
 * Класс отвечает за обработку данных по услугам.
 */
public class ServiceService extends AbstractFavorService {
    private static final Logger serviceLogger = LoggerFactory.getLogger(ServiceService.class);
    private static final Logger providedServiceLogger = LoggerFactory.getLogger("service.ProvidedServiceService");
    private final ServiceRepository serviceRepository;
    private final ProvidedServicesRepository providedServicesRepository;

    public ServiceService(ServiceRepository serviceRepository, ProvidedServicesRepository providedServicesRepository) {
        this.serviceRepository = serviceRepository;
        this.providedServicesRepository = providedServicesRepository;
    }

    /**
     * Метод добавляет новую услугу в список всех услуг отеля.
     * @param service Новая услуга.
     * @return {@code true}, если услуга была добавлена, иначе {@code false}.
     */
    public boolean addService(AbstractService service) {
        int serviceId = service.getId();
        serviceLogger.info("Вызван метод добавления услуги с ID {}", serviceId);
        boolean added = serviceRepository.getServices().add(service);

        if (added) {
            serviceLogger.info("Добавлена новая услуга с ID {}", serviceId);
        } else {
            serviceLogger.warn("Не удалось добавить услугу с ID {}", serviceId);
        }

        return added;
    }

    /**
     * Метод обновления данных услуги. Валидация услуги происходит по её ID.
     * @param service Новые данные услуги, собранные в классе услуги.
     * @return {@code true}, если удалось обновить данные, иначе {@code false}.
     */
    public boolean updateService(AbstractService service) {
        int serviceId = service.getId();
        serviceLogger.info("Вызван метод обновления услуги с ID {}", serviceId);
        for (AbstractService currentService : serviceRepository.getServices()) {
            if (currentService.getId() == serviceId) {
                currentService.setName(service.getName());
                currentService.setPrice(service.getPrice());
                currentService.setStatus(service.getStatus());
                currentService.setServiceTime(service.getServiceTime());
                serviceLogger.info("Обновлена услуга с ID {}", serviceId);

                return true;
            }
        }

        serviceLogger.warn("Не удалось обновить услугу с ID {}", serviceId);
        return false;
    }

    /**
     * Метод addProvidedService добавляет оказанную услугу в репозиторий оказанных услуг.
     * @param providedService Оказанная услуга для добавления.
     * @return {@code true}, если услуга успешно добавлена, иначе {@code false}.
     */
    public boolean addProvidedService(ProvidedService providedService) {
        int providedServiceId = providedService.getId();
        providedServiceLogger.info("Вызван метод добавления новой оказанной услуги с ID {}", providedServiceId);
        boolean added = providedServicesRepository.getServices().add(providedService);

        if (added) {
            providedServiceLogger.info("Добавлена новая оказанная услуга с ID {}", providedServiceId);
        } else {
            providedServiceLogger.warn("Не удалось добавить оказанную услугу с ID {}", providedServiceId);
        }

        return added;
    }

    /**
     * Метод updateProvidedService обновляет информацию об оказанной услуге в репозитории оказанных услуг.
     * Если услуга с указанным ID найдена и успешно обновлена, метод возвращает true, иначе - false.
     * @param providedService Обновленная информация об оказанной услуге.
     * @return {@code true}, если информация успешно добавлена, иначе {@code false}.
     */
    public boolean updateProvidedService(ProvidedService providedService) {
        int providedServiceId = providedService.getId();
        providedServiceLogger.info("Вызван метод обновления оказанной услуги с ID {}", providedServiceId);
        for (ProvidedService currentProvidedService : providedServicesRepository.getServices()) {
            if (currentProvidedService.getId() == providedServiceId) {
                currentProvidedService.setService(providedService.getService());
                currentProvidedService.setServiceTime(providedService.getServiceTime());
                currentProvidedService.setBeneficiaries(providedService.getBeneficiaries());
                providedServiceLogger.info("Удалось обновить оказанную услугу с ID {}", providedServiceId);

                return true;
            }
        }

        providedServiceLogger.warn("Не удалось обновить оказанную услугу с ID {}", providedServiceId);
        return false;
    }

    /**
     * Метод проводит услугу для конкретного клиента.
     * @param client Клиент.
     * @param service Услуга.
     * @return {@code true}, если услуга оказана успешно, иначе {@code false}.
     */
    public boolean provideService(AbstractClient client, AbstractService service) {
        String startMessage = "Вызван метод оказания услуги с ID {} для клиента с ID {}";
        int serviceId = service.getId();
        int clientId = client.getId();
        serviceLogger.info(startMessage, serviceId, clientId);
        providedServiceLogger.info(startMessage, serviceId, clientId);
        if (!serviceRepository.getServices().contains(service)) {
            String message = "Провалена попытка оказания услуги с ID {} для клиента с ID {}";
            serviceLogger.warn(message, serviceId, clientId);
            providedServiceLogger.warn(message, serviceId, clientId);
            return false;
        }

        String path = FileAdditionResult.getIdDirectory() + "provided_service_id.txt";
        int id = IdFileManager.readMaxId(path);
        if (!UniqueIdValidator.validateUniqueId(getProvidedServices(), id)) {
            id = getProvidedServices().stream().mapToInt(Identifiable::getId).max().orElse(0) + 1;
        }
        try {
            IdFileManager.writeMaxId(path, id + 1);
        } catch (IOException e) {
            System.out.println("\n" + FileAdditionResult.FAILURE.getMessage());
        }

        LocalDateTime now = LocalDateTime.now();
        addProvidedService(new ProvidedService(id, service, now, client));
        service.setServiceTime(now);
        service.setStatus(ServiceStatusTypes.RENDERED);
        serviceLogger.info("Услуга с ID {} была оказана клиенту с ID {}", serviceId, clientId);
        return true;
    }

    /**
     * Метод формирует список услуг, оказанных конкретному клиенту, с сортировкой по увеличению цены.
     * @param client Клиент.
     * @return Список услуг.
     */
    public List<AbstractService> getClientServicesByPrice(AbstractClient client) {
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
     */
    public List<AbstractService> getClientServicesByTime(AbstractClient client) {
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
     */
    public List<AbstractService> getServices() {
        serviceLogger.info("Вызван метод получения списка всех услуг");
        return serviceRepository.getServices().stream().toList();
    }

    /**
     * Метод возвращает список оказанных услуг.
     * @return Список оказанных услуг.
     */
    public List<ProvidedService> getProvidedServices() {
        providedServiceLogger.info("Вызван метод получения списка оказанных услуг");
        return providedServicesRepository.getServices();
    }

    /**
     * Служебный метод предназначен для устранения дублирования кода. Метод фильтрует стрим от списка услуг по
     * содержанию конкретного клиента в списке.
     * @param client Клиент.
     * @return Отфильтрованный стрим.
     */
    private Stream<AbstractService> streamClientServices(AbstractClient client) {
        return providedServicesRepository.getServices()
                .stream()
                .filter(service -> service.getBeneficiaries().contains(client)
                        && service.getService().getStatus() == ServiceStatusTypes.RENDERED)
                .map(ProvidedService::getService);
    }
}
