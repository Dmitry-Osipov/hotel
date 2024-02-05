package service;

import comparators.ServiceTimeComparator;
import essence.Identifiable;
import essence.person.AbstractClient;
import essence.provided.ProvidedService;
import essence.service.AbstractService;
import essence.service.ServiceStatusTypes;
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
    private final ServiceRepository serviceRepository;
    private final ProvidedServicesRepository providedServicesRepository;

    public ServiceService(ServiceRepository serviceRepository, ProvidedServicesRepository providedServicesRepository) {
        this.serviceRepository = serviceRepository;
        this.providedServicesRepository = providedServicesRepository;
    }

    /**
     * Метод добавляет новую услугу в список всех услуг отеля.
     * @param service Новая услуга.
     * @return true, если услуга была добавлена, иначе false.
     */
    public boolean addService(AbstractService service) {
        return serviceRepository.getServices().add(service);
    }

    /**
     * Метод обновления данных услуги. Валидация услуги происходит по её ID.
     * @param service Новые данные услуги, собранные в классе услуги.
     * @return true, если удалось обновить данные, иначе false.
     */
    public boolean updateService(AbstractService service) {
        for (AbstractService currentService : serviceRepository.getServices()) {
            if (currentService.getId() == service.getId()) {
                currentService.setName(service.getName());
                currentService.setPrice(service.getPrice());
                currentService.setStatus(service.getStatus());
                currentService.setServiceTime(service.getServiceTime());

                return true;
            }
        }

        return false;
    }

    /**
     * Метод addProvidedService добавляет оказанную услугу в репозиторий оказанных услуг.
     * @param providedService Оказанная услуга для добавления.
     * @return {@code true}, если услуга успешно добавлена, иначе {@code false}.
     */
    public boolean addProvidedService(ProvidedService providedService) {
        return providedServicesRepository.getServices().add(providedService);
    }

    /**
     * Метод updateProvidedService обновляет информацию об оказанной услуге в репозитории оказанных услуг.
     * Если услуга с указанным ID найдена и успешно обновлена, метод возвращает true, иначе - false.
     * @param providedService Обновленная информация об оказанной услуге.
     * @return {@code true}, если информация успешно добавлена, иначе {@code false}.
     */
    public boolean updateProvidedService(ProvidedService providedService) {
        for (ProvidedService currentProvidedService : providedServicesRepository.getServices()) {
            if (currentProvidedService.getId() == providedService.getId()) {
                currentProvidedService.setService(providedService.getService());
                currentProvidedService.setServiceTime(providedService.getServiceTime());
                currentProvidedService.setBeneficiaries(providedService.getBeneficiaries());

                return true;
            }
        }

        return false;
    }

    /**
     * Метод проводит услугу для конкретного клиента.
     * @param client Клиент.
     * @param service Услуга.
     * @return true, если услуга оказана успешно, иначе false.
     */
    public boolean provideService(AbstractClient client, AbstractService service) {
        if (!serviceRepository.getServices().contains(service)) {
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
        return true;
    }

    /**
     * Метод формирует список услуг, оказанных конкретному клиенту, с сортировкой по увеличению цены.
     * @param client Клиент.
     * @return Список услуг.
     */
    public List<AbstractService> getClientServicesByPrice(AbstractClient client) {
        return streamClientServices(client).sorted().toList();
    }

    /**
     * Метод формирует список услуг, оказанных конкретному клиенту, с сортировкой по убыванию времени оказания услуги.
     * @param client Клиент.
     * @return Список услуг.
     */
    public List<AbstractService> getClientServicesByTime(AbstractClient client) {
        return streamClientServices(client).sorted(new ServiceTimeComparator()).toList().reversed();
    }

    /**
     * Метод возвращает список всех услуг.
     * @return Список всех услуг.
     */
    public List<AbstractService> getServices() {
        return serviceRepository.getServices().stream().toList();
    }

    /**
     * Метод возвращает список оказанных услуг.
     * @return Список оказанных услуг.
     */
    public List<ProvidedService> getProvidedServices() {
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
