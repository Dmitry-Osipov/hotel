package service;

import annotations.annotation.Autowired;
import annotations.annotation.Component;
import essence.Identifiable;
import essence.person.AbstractClient;
import essence.provided.ProvidedService;
import essence.service.AbstractService;
import essence.service.ServiceStatusTypes;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.ProvidedServicesRepository;
import repository.ServiceRepository;
import utils.comparators.ServiceTimeComparator;
import utils.exceptions.EntityContainedException;
import utils.exceptions.ErrorMessages;
import utils.exceptions.NoEntityException;
import utils.file.DataPath;
import utils.file.FileAdditionResult;
import utils.file.id.IdFileManager;
import utils.file.serialize.SerializationUtils;
import utils.validators.UniqueIdValidator;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

/**
 * Класс отвечает за обработку данных по услугам.
 */
@Component
@ToString
public class ServiceService extends AbstractFavorService {
    private static final Logger serviceLogger = LoggerFactory.getLogger(ServiceService.class);
    private static final Logger providedServiceLogger = LoggerFactory.getLogger("service.ProvidedServiceService");
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private ProvidedServicesRepository providedServicesRepository;

    /**
     * Метод добавляет новую услугу в список всех услуг отеля.
     * @param service Новая услуга.
     * @throws EntityContainedException Ошибка вылетает, когда услуга уже содержится в отеле (невозможно повторно
     * добавить).
     */
    public void addService(AbstractService service) throws EntityContainedException {
        int serviceId = service.getId();
        serviceLogger.info("Вызван метод добавления услуги с ID {}", serviceId);
        boolean added = serviceRepository.getServices().add(service);

        if (!added) {
            serviceLogger.error("Не удалось добавить услугу с ID {}", serviceId);
            throw new EntityContainedException(ErrorMessages.SERVICE_CONTAINED.getMessage());
        }

        serviceLogger.info("Добавлена новая услуга с ID {}", serviceId);
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

        serviceLogger.error("Не удалось обновить услугу с ID {}", serviceId);
        return false;
    }

    /**
     * Метод addProvidedService добавляет оказанную услугу в репозиторий оказанных услуг.
     * @param providedService Оказанная услуга для добавления.
     */
    public void addProvidedService(ProvidedService providedService) {
        int providedServiceId = providedService.getId();
        providedServiceLogger.info("Вызван метод добавления новой оказанной услуги с ID {}", providedServiceId);
        providedServicesRepository.getProvidedServices().add(providedService);
        providedServiceLogger.info("Добавлена новая оказанная услуга с ID {}", providedServiceId);
    }

    /**
     * Метод updateProvidedService обновляет информацию об оказанной услуге в репозитории оказанных услуг.
     * @param providedService Обновленная информация об оказанной услуге.
     * @return {@code true}, если информация успешно добавлена, иначе {@code false}.
     */
    public boolean updateProvidedService(ProvidedService providedService) {
        int providedServiceId = providedService.getId();
        providedServiceLogger.info("Вызван метод обновления оказанной услуги с ID {}", providedServiceId);
        for (ProvidedService currentProvidedService : providedServicesRepository.getProvidedServices()) {
            if (currentProvidedService.getId() == providedServiceId) {
                currentProvidedService.setService(providedService.getService());
                currentProvidedService.setServiceTime(providedService.getServiceTime());
                currentProvidedService.setBeneficiaries(providedService.getBeneficiaries());
                providedServiceLogger.info("Удалось обновить оказанную услугу с ID {}", providedServiceId);

                return true;
            }
        }

        providedServiceLogger.error("Не удалось обновить оказанную услугу с ID {}", providedServiceId);
        return false;
    }

    /**
     * Метод проводит услугу для конкретного клиента.
     * @param client Клиент.
     * @param service Услуга.
     * @throws NoEntityException Ошибка вылетает, если услуги нет в отеле.
     */
    public void provideService(AbstractClient client, AbstractService service) throws NoEntityException {
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

        String path = DataPath.ID_DIRECTORY.getPath() + "provided_service_id.txt";
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
        return providedServicesRepository.getProvidedServices();
    }

    /**
     * Метод производит сериализацию данных по услугам и проведённым услугам.
     */
    public void serializeServicesData() {
        serializeServiceRepo();
        serializeProvidedServiceRepo();
    }

    /**
     * Метод производит десериализацию данных по услугам и проведённым услугам.
     */
    public void deserializeServicesData() {
        deserializeServiceRepo();
        deserializeProvidedServiceRepo();
    }

    /**
     * Служебный метод производит десериализацию данных по услугам.
     */
    private void deserializeServiceRepo() {
        serviceLogger.info("Происходит десериализация данных по услугам");
        try {
            String path = DataPath.SERIALIZE_DIRECTORY.getPath() + "services";
            ServiceRepository repo = SerializationUtils.deserialize(ServiceRepository.class, path);
            for (AbstractService service : repo.getServices()) {
                if (!updateService(service)) {
                    addService(service);
                }
            }
        } catch (IOException e) {
            serviceLogger.error("Десериализация данных по услугам не произошла");
        }
        serviceLogger.info("Десериализация данных по услугам завершена");
    }

    /**
     * Служебный метод производит десериализацию данных по проведённым услугам.
     */
    private void deserializeProvidedServiceRepo() {
        providedServiceLogger.info("Происходит десериализация данных по проведённым услугам");
        try {
            String path = DataPath.SERIALIZE_DIRECTORY.getPath() + "provided_services";
            ProvidedServicesRepository repo = SerializationUtils.deserialize(ProvidedServicesRepository.class, path);
            for (ProvidedService providedService : repo.getProvidedServices()) {
                if (!updateProvidedService(providedService)) {
                    addProvidedService(providedService);
                }
            }
        } catch (IOException e) {
            providedServiceLogger.error("Десериализация данных по проведённым услугам не произошла");
        }
        providedServiceLogger.info("Десериализация данных по проведённым услугам завершена");
    }

    /**
     * Служебный метод производит сериализацию данных по услугам.
     */
    private void serializeServiceRepo() {
        serviceLogger.info("Происходит сериализация данных по услугам");
        try {
            String path = DataPath.SERIALIZE_DIRECTORY.getPath() + "services";
            SerializationUtils.serialize(serviceRepository, path);
        } catch (IOException e) {
            serviceLogger.error("Сериализация данных по услугам не произошла");
        }
        serviceLogger.info("Сериализация данных по услугам завершена");
    }

    /**
     * Служебный метод производит сериализацию данных по проведённым услугам.
     */
    private void serializeProvidedServiceRepo() {
        providedServiceLogger.info("Происходит сериализация данных по проведённым услугам");
        try {
            String path = DataPath.SERIALIZE_DIRECTORY.getPath() + "provided_services";
            SerializationUtils.serialize(providedServicesRepository, path);
        } catch (IOException e) {
            providedServiceLogger.error("Сериализация данных по проведённым услугам не произошла");
        }
        providedServiceLogger.info("Сериализация данных по проведённым услугам завершена");
    }

    /**
     * Служебный метод предназначен для устранения дублирования кода. Метод фильтрует стрим от списка услуг по
     * содержанию конкретного клиента в списке.
     * @param client Клиент.
     * @return Отфильтрованный стрим.
     */
    private Stream<AbstractService> streamClientServices(AbstractClient client) {
        return providedServicesRepository.getProvidedServices()
                .stream()
                .filter(service -> service.getBeneficiaries().contains(client)
                        && service.getService().getStatus() == ServiceStatusTypes.RENDERED)
                .map(ProvidedService::getService);
    }
}
