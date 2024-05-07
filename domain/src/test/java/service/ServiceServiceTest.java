package service;

import essence.Identifiable;
import essence.person.AbstractClient;
import essence.person.Client;
import essence.provided.ProvidedService;
import essence.service.AbstractService;
import essence.service.Service;
import essence.service.ServiceNames;
import essence.service.ServiceStatusTypes;
import jakarta.persistence.OptimisticLockException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.ProvidedServicesRepository;
import repository.ServiceRepository;
import utils.comparators.ServiceTimeComparator;
import utils.exceptions.EntityContainedException;
import utils.exceptions.InvalidDataException;
import utils.exceptions.NoEntityException;
import utils.exceptions.TechnicalException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ServiceServiceTest {
    private List<AbstractService> services;
    private List<ProvidedService> providedServices;
    @Mock
    private ServiceRepository serviceRepository;
    @Mock
    private ProvidedServicesRepository providedServicesRepository;
    @InjectMocks
    private ServiceService sut;

    @BeforeEach
    public void setUp() {
        AbstractService service1 = new Service(1, ServiceNames.CONFERENCE, 2000);
        service1.setStatus(ServiceStatusTypes.RENDERED);
        service1.setServiceTime(LocalDateTime.of(2024, 2, 18, 13, 17, 27));
        AbstractService service2 = new Service(2, ServiceNames.BREAKFAST, 1500);
        AbstractService service3 = new Service(3, ServiceNames.EXCURSION, 20000);
        AbstractService service4 = new Service(4, ServiceNames.MINIBAR, 5000);
        service4.setStatus(ServiceStatusTypes.RENDERED);
        service4.setServiceTime(LocalDateTime.of(2024, 1, 25,  22, 0, 37));
        AbstractService service5 = new Service(5, ServiceNames.PARKING, 3000);
        service5.setStatus(ServiceStatusTypes.RENDERED);
        service5.setServiceTime(LocalDateTime.of(2024, 3, 5, 19, 2, 47));
        services = new ArrayList<>(List.of(service1, service2, service3, service4, service5));

        ProvidedService providedService1 = new ProvidedService(1, service1,
                LocalDateTime.of(2024, 1, 25, 22, 0, 37),
                new Client(1, "Osipov D. R.", "+7(902)902-98-11"));
        ProvidedService providedService2 = new ProvidedService(2, service4,
                LocalDateTime.of(2024, 1, 25, 22, 0, 37),
                new Client(3, "Belyakova I. S.", "+7(953)180-00-61"));
        ProvidedService providedService3 = new ProvidedService(3, service1,
                LocalDateTime.of(2024, 2, 18, 13, 17, 27),
                new Client(1, "Osipov D. R.", "+7(902)902-98-11"));
        ProvidedService providedService4 = new ProvidedService(4, service5,
                LocalDateTime.of(2024, 3, 5, 19, 2, 47),
                new Client(3, "Belyakova I. S.", "+7(953)180-00-61"));
        providedServices = new ArrayList<>(List.of(
                providedService1, providedService2, providedService3, providedService4));

        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        services = null;
        providedServices = null;
    }

    @Test
    @SneakyThrows
    void addServiceDoesNotThrowException() {
        AbstractService service = new Service(6, ServiceNames.MINIBAR, 20000);

        assertDoesNotThrow(() -> sut.addService(service));

        verify(serviceRepository, times(1)).save(service);
    }

    @Test
    @SneakyThrows
    void addServiceThrowsEntityContainedException() {
        AbstractService service = new Service(1, ServiceNames.CONFERENCE, 2000);
        doThrow(EntityContainedException.class).when(serviceRepository).save(service);

        assertThrows(EntityContainedException.class, () -> sut.addService(service));

        verify(serviceRepository, times(1)).save(service);
    }

    @Test
    @SneakyThrows
    void addServiceThrowsOptimisticLockException() {
        AbstractService service = new Service();
        doThrow(OptimisticLockException.class).when(serviceRepository).save(service);

        assertThrows(OptimisticLockException.class, () -> sut.addService(service));

        verify(serviceRepository, times(1)).save(service);
    }

    @Test
    @SneakyThrows
    void updateServiceDoesNotThrowException() {
        AbstractService service = new Service(1, ServiceNames.CONFERENCE, 2000);

        assertDoesNotThrow(() -> sut.updateService(service));

        verify(serviceRepository, times(1)).update(service);
    }

    @Test
    @SneakyThrows
    void updateServiceThrowsTechnicalException() {
        AbstractService service = new Service(0, ServiceNames.CONFERENCE, 2000);
        doThrow(TechnicalException.class).when(serviceRepository).update(service);

        assertThrows(TechnicalException.class, () -> sut.updateService(service));

        verify(serviceRepository, times(1)).update(service);
    }

    @Test
    @SneakyThrows
    void importServicesDoesNotThrowException() {
        AbstractService service1 = services.get(0);
        AbstractService service2 = services.get(1);
        AbstractService service3 = services.get(2);
        AbstractService service4 = services.get(3);
        AbstractService service5 = services.get(4);

        assertDoesNotThrow(() -> sut.importServices(services));

        verify(serviceRepository, times(1)).update(service1);
        verify(serviceRepository, times(1)).update(service2);
        verify(serviceRepository, times(1)).update(service3);
        verify(serviceRepository, times(1)).update(service4);
        verify(serviceRepository, times(1)).update(service5);
    }

    @Test
    @SneakyThrows
    void addProvidedServiceDoesNotThrowException() {
        ProvidedService providedService = new ProvidedService(6, services.getFirst(),
                LocalDateTime.of(2024, 1, 25, 22, 0, 37),
                new Client(1, "Osipov D. R.", "+7(902)902-98-11"));

        assertDoesNotThrow(() -> sut.addProvidedService(providedService));

        verify(providedServicesRepository, times(1)).save(providedService);
    }

    @Test
    @SneakyThrows
    void addProvidedServiceThrowsEntityContainedException() {
        ProvidedService providedService = new ProvidedService(1, services.getFirst(),
                LocalDateTime.of(2024, 1, 25, 22, 0, 37),
                new Client(1, "Osipov D. R.", "+7(902)902-98-11"));
        doThrow(EntityContainedException.class).when(providedServicesRepository).save(providedService);

        assertThrows(EntityContainedException.class, () -> sut.addProvidedService(providedService));

        verify(providedServicesRepository, times(1)).save(providedService);
    }

    @Test
    @SneakyThrows
    void addProvidedServiceThrowsOptimisticLockException() {
        ProvidedService providedService = new ProvidedService();
        doThrow(OptimisticLockException.class).when(providedServicesRepository).save(providedService);

        assertThrows(OptimisticLockException.class, () -> sut.addProvidedService(providedService));

        verify(providedServicesRepository, times(1)).save(providedService);
    }

    @Test
    @SneakyThrows
    void updateProvidedServiceDoesNotThrowException() {
        ProvidedService providedService = new ProvidedService(6, services.getFirst(),
                LocalDateTime.of(2024, 1, 25, 22, 0, 37),
                new Client(1, "Osipov D. R.", "+7(902)902-98-11"));

        assertDoesNotThrow(() -> sut.updateProvidedService(providedService));

        verify(providedServicesRepository, times(1)).update(providedService);
    }

    @Test
    @SneakyThrows
    void updateProvidedServiceThrowsTechnicalException() {
        ProvidedService providedService = new ProvidedService(0, services.getFirst(),
                LocalDateTime.of(2024, 1, 25, 22, 0, 37),
                new Client(1, "Osipov D. R.", "+7(902)902-98-11"));
        doThrow(TechnicalException.class).when(providedServicesRepository).update(providedService);

        assertThrows(TechnicalException.class, () -> sut.updateProvidedService(providedService));

        verify(providedServicesRepository, times(1)).update(providedService);
    }

    @Test
    @SneakyThrows
    void importProvidedServicesDoesNotThrowException() {
        ProvidedService providedService1 = providedServices.get(0);
        ProvidedService providedService2 = providedServices.get(1);
        ProvidedService providedService3 = providedServices.get(2);
        ProvidedService providedService4 = providedServices.get(3);

        assertDoesNotThrow(() -> sut.importProvidedServices(providedServices));

        verify(providedServicesRepository, times(1)).update(providedService1);
        verify(providedServicesRepository, times(1)).update(providedService2);
        verify(providedServicesRepository, times(1)).update(providedService3);
        verify(providedServicesRepository, times(1)).update(providedService4);
    }

    @Test
    @SneakyThrows
    void provideServiceWithNormalServiceDoesNotThrowException() {
        AbstractClient client = new Client(1, "Osipov D. R.", "+7(902)902-98-11");
        AbstractService service = services.get(1);
        when(serviceRepository.getServices()).thenReturn(services);

        assertDoesNotThrow(() -> sut.provideService(client, service));

        verify(serviceRepository, times(1)).getServices();
        verify(serviceRepository, times(1)).update(service);
    }

    @Test
    @SneakyThrows
    void provideServiceWithNewServiceThrowsNoEntityException() {
        AbstractClient client = new Client(1, "Osipov D. R.", "+7(902)902-98-11");
        AbstractService service = new Service(6, ServiceNames.BREAKFAST, 15000);
        when(serviceRepository.getServices()).thenReturn(services);

        assertThrows(NoEntityException.class, () -> sut.provideService(client, service));

        verify(serviceRepository, times(1)).getServices();
        verify(serviceRepository, times(0)).update(service);
    }

    @Test
    @SneakyThrows
    void provideServiceWithRenderedServiceThrowsInvalidDataException() {
        AbstractClient client = new Client(1, "Osipov D. R.", "+7(902)902-98-11");
        AbstractService service = services.getFirst();
        when(serviceRepository.getServices()).thenReturn(services);

        assertThrows(InvalidDataException.class, () -> sut.provideService(client, service));

        verify(serviceRepository, times(1)).getServices();
        verify(serviceRepository, times(0)).update(service);
    }

    @Test
    @SneakyThrows
    void provideServiceWithNormalProvidedServiceDoesNotThrowException() {
        AbstractClient client = new Client(1, "Osipov D. R.", "+7(902)902-98-11");
        AbstractService service = services.get(1);
        ProvidedService providedService = new ProvidedService(5, service, LocalDateTime.now(), client);
        when(serviceRepository.getServices()).thenReturn(services);

        assertDoesNotThrow(() -> sut.provideService(providedService));

        verify(serviceRepository, times(1)).getServices();
        verify(serviceRepository, times(1)).update(service);
    }

    @Test
    @SneakyThrows
    void provideServiceWithProvidedServiceWithoutFieldsThrowsNullPointerException() {
        ProvidedService providedService = new ProvidedService();
        when(serviceRepository.getServices()).thenReturn(services);

        assertThrows(NullPointerException.class, () -> sut.provideService(providedService));

        verify(serviceRepository, times(0)).getServices();
    }

    @Test
    @SneakyThrows
    void provideServiceWithProvidedServiceWithRenderedServiceThrowsInvalidDataException() {
        AbstractClient client = new Client(1, "Osipov D. R.", "+7(902)902-98-11");
        AbstractService service = services.getFirst();
        ProvidedService providedService = new ProvidedService(5, service, LocalDateTime.now(), client);
        when(serviceRepository.getServices()).thenReturn(services);

        assertThrows(InvalidDataException.class, () -> sut.provideService(providedService));

        verify(serviceRepository, times(1)).getServices();
        verify(serviceRepository, times(0)).update(service);
    }

    @Test
    @SneakyThrows
    void provideServiceWithProvidedServiceWithNewServiceThrowsNoEntityException() {
        AbstractClient client = new Client(1, "Osipov D. R.", "+7(902)902-98-11");
        AbstractService service = new Service(6, ServiceNames.BREAKFAST, 15000);
        ProvidedService providedService = new ProvidedService(5, service, LocalDateTime.now(), client);
        when(serviceRepository.getServices()).thenReturn(services);

        assertThrows(NoEntityException.class, () -> sut.provideService(providedService));

        verify(serviceRepository, times(1)).getServices();
        verify(serviceRepository, times(0)).update(service);
    }

    @Test
    @SneakyThrows
    void getClientServicesByPriceDoesNotThrowException() {
        AbstractClient client = new Client(1, "Osipov D. R.", "+7(902)902-98-11");
        when(providedServicesRepository.getProvidedServices()).thenReturn(providedServices);
        List<AbstractService> servicesList = new ArrayList<>(List.of(providedServices.get(0), providedServices.get(2)))
                .stream()
                .map(ProvidedService::getService)
                .sorted()
                .toList();

        assertEquals(servicesList, sut.getClientServicesByPrice(client));

        verify(providedServicesRepository, times(1)).getProvidedServices();
    }

    @Test
    @SneakyThrows
    void getClientServicesByTimeDoesNotThrowException() {
        AbstractClient client = new Client(1, "Osipov D. R.", "+7(902)902-98-11");
        when(providedServicesRepository.getProvidedServices()).thenReturn(providedServices);
        List<AbstractService> servicesList = new ArrayList<>(List.of(providedServices.get(0), providedServices.get(2)))
                .stream()
                .map(ProvidedService::getService)
                .sorted(new ServiceTimeComparator())
                .toList()
                .reversed();

        assertEquals(servicesList, sut.getClientServicesByTime(client));

        verify(providedServicesRepository, times(1)).getProvidedServices();
    }

    @Test
    @SneakyThrows
    void getServiceByIdGivesCurrentData() {
        int id = 1;
        doReturn(services.stream()
                .sorted(Comparator.comparingInt(Identifiable::getId))
                .toList().get(id - 1)).when(serviceRepository).getServiceById(id);

        AbstractService service = assertDoesNotThrow(() -> sut.getServiceById(id));

        assertEquals(service, services.get(id - 1));
        verify(serviceRepository, times(1)).getServiceById(id);
    }

    @Test
    @SneakyThrows
    void getServiceByIdThrowsTechnicalException() {
        int id = 6;
        doThrow(TechnicalException.class).when(serviceRepository).getServiceById(id);

        assertThrows(TechnicalException.class, () -> sut.getServiceById(id));

        verify(serviceRepository, times(1)).getServiceById(id);
    }

    @Test
    @SneakyThrows
    void deleteServiceDoesNotThrowException() {
        AbstractService service = services.getFirst();

        assertDoesNotThrow(() -> sut.deleteService(service));

        verify(serviceRepository, times(1)).deleteService(service);
    }

    @Test
    @SneakyThrows
    void deleteClientThrowsTechnicalException() {
        AbstractService service = new Service(6, ServiceNames.CONFERENCE, 20000);
        doThrow(TechnicalException.class).when(serviceRepository).deleteService(service);

        assertThrows(TechnicalException.class, () -> sut.deleteService(service));

        verify(serviceRepository, times(1)).deleteService(service);
    }
}
