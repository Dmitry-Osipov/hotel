package service;

import comparators.ServiceTimeComparator;
import essence.person.AbstractClient;
import essence.provided.ProvidedService;
import essence.service.AbstractService;
import essence.service.ServiceStatusTypes;
import repository.service.ProvidedServicesRepository;
import repository.service.ServiceRepository;

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
     * Метод проводит услугу для конкретного клиента.
     * @param client Клиент.
     * @param service Услуга.
     * @return true, если услуга оказана успешно, иначе false.
     */
    public boolean provideService(AbstractClient client, AbstractService service) {
        if (!serviceRepository.getServices().contains(service)) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        providedServicesRepository.getServices().add(new ProvidedService(service, now, client));
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
     * @return Список услуг.
     */
    public List<AbstractService> getServices() {
        return serviceRepository.getServices().stream().toList();
    }

    /**
     * Слежубный метод предназначен для устранения дублирования кода. Метод фильтрует стрим от списка услуг по
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
