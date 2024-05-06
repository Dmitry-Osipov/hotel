package controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import config.TestConfig;
import dto.ProvidedServiceDto;
import dto.ServiceDto;
import essence.person.AbstractClient;
import essence.person.Client;
import essence.provided.ProvidedService;
import essence.service.AbstractService;
import essence.service.Service;
import essence.service.ServiceNames;
import essence.service.ServiceStatusTypes;
import jakarta.servlet.ServletException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import service.ClientService;
import service.ServiceService;
import utils.DtoConverter;
import utils.comparators.ServiceTimeComparator;
import utils.exceptions.TechnicalException;
import utils.file.ResponseEntityPreparer;
import web.controller.RestServiceController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig
@ContextConfiguration(classes = TestConfig.class)
class RestServiceControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private List<AbstractClient> clients;
    private List<AbstractService> services;
    private List<ProvidedService> providedServices;
    private MockMvc sut;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ResponseEntityPreparer responsePreparer;

    @BeforeEach
    public void setUp() {
        AbstractClient client1 = new Client(1, "Osipov D. R.", "+7(902)902-98-11");
        client1.setCheckInTime(LocalDateTime.of(2024, 1, 25, 22, 0, 37));
        AbstractClient client2 = new Client(2, "Musofranova N. S.", "+7(961)150-09-97");
        client2.setCheckInTime(LocalDateTime.of(2024, 1, 25, 22, 0, 37));
        AbstractClient client3 = new Client(3, "Belyakova I. S.", "+7(953)180-00-61");
        client3.setCheckInTime(LocalDateTime.of(2024, 1, 25, 22, 0, 37));
        AbstractClient client4 = new Client(4, "Kondrashin E. V.", "+7(991)234-11-00");
        AbstractClient client5 = new Client(5, "Lebedev G.I.", "+7(921)728-21-01");
        clients = new ArrayList<>(List.of(client1, client2, client3, client4, client5));

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

        sut = MockMvcBuilders.standaloneSetup(
                new RestServiceController(serviceService, clientService, responsePreparer)).build();
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        clients = null;
        services = null;
        providedServices = null;
        sut = null;
    }

    @Test
    @SneakyThrows
    void getAllServicesDoesNotThrowException() {
        when(serviceService.getServices()).thenReturn(services);
        List<ServiceDto> expected = DtoConverter.listServicesToDtos(services);
        String jsonResponse = sut.perform(get("/api/services"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<ServiceDto> actual = objectMapper.readValue(jsonResponse, new TypeReference<>() {});

        assertEquals(expected, actual);

        verify(serviceService, times(1)).getServices();
    }

    @Test
    @SneakyThrows
    void getAllServicesThrowsServletException() {
        doThrow(TechnicalException.class).when(serviceService).getServices();

        assertThrows(ServletException.class,
                () -> sut.perform(get("/api/services")).andExpect(status().isInternalServerError()));

        verify(serviceService, times(1)).getServices();
    }

    @Test
    @SneakyThrows
    void getServiceByIdDoesNotThrowException() {
        int id = 1;
        AbstractService service = services.get(id - 1);
        when(serviceService.getServiceById(id)).thenReturn(service);
        ServiceDto expected = DtoConverter.convertServiceToDto(service);
        String jsonResponse = sut.perform(get("/api/services/{id}", id))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        ServiceDto actual = objectMapper.readValue(jsonResponse, ServiceDto.class);

        assertEquals(expected, actual);

        verify(serviceService, times(1)).getServiceById(id);
    }

    @Test
    @SneakyThrows
    void getServiceByIdThrowsServletException() {
        int id = 0;
        doThrow(TechnicalException.class).when(serviceService).getServiceById(id);

        assertThrows(ServletException.class, () ->
                sut.perform(get("/api/services/{id}", id)).andExpect(status().isNotFound()));

        verify(serviceService, times(1)).getServiceById(id);
    }

    @Test
    @SneakyThrows
    void addServiceDoesNotThrowException() {
        ServiceDto dto =
                DtoConverter.convertServiceToDto(new Service(6, ServiceNames.PARKING, 5000));

        assertDoesNotThrow(() -> sut.perform(post("/api/services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Service " + dto.getName() + " added successfully")));

        verify(serviceService, times(1)).addService(DtoConverter.convertDtoToService(dto));
    }

    @Test
    @SneakyThrows
    void addServiceThrowsServletException() {
        ServiceDto dto =
                DtoConverter.convertServiceToDto(new Service(6, ServiceNames.PARKING, 5000));
        doThrow(TechnicalException.class).when(serviceService).addService(DtoConverter.convertDtoToService(dto));

        assertThrows(ServletException.class, () -> sut.perform(post("/api/services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError()));

        verify(serviceService, times(1)).addService(DtoConverter.convertDtoToService(dto));
    }

    @Test
    @SneakyThrows
    void updateServiceDoesNotThrowException() {
        ServiceDto dto =
                DtoConverter.convertServiceToDto(new Service(6, ServiceNames.PARKING, 5000));
        String jsonResponse = sut.perform(put("/api/services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        ServiceDto actual = objectMapper.readValue(jsonResponse, ServiceDto.class);

        assertEquals(dto, actual);

        verify(serviceService, times(1)).updateService(DtoConverter.convertDtoToService(dto));
    }

    @Test
    @SneakyThrows
    void updateServiceThrowsServletException() {
        ServiceDto dto =
                DtoConverter.convertServiceToDto(new Service(6, ServiceNames.PARKING, 5000));
        doThrow(TechnicalException.class).when(serviceService).updateService(DtoConverter.convertDtoToService(dto));

        assertThrows(ServletException.class, () -> sut.perform(put("/api/services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError()));

        verify(serviceService, times(1)).updateService(DtoConverter.convertDtoToService(dto));
    }

    @Test
    @SneakyThrows
    void deleteServiceDoesNotThrowException() {
        int id = 2;
        when(serviceService.getServiceById(id)).thenReturn(services.get(id - 1));

        assertDoesNotThrow(() -> sut.perform(delete("/api/services/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted service with ID = " + id)));

        verify(serviceService, times(1)).deleteService(services.get(id - 1));
        verify(serviceService, times(1)).getServiceById(id);
    }

    @Test
    @SneakyThrows
    void deleteServiceThrowsServletException() {
        int id = 0;
        doThrow(TechnicalException.class).when(serviceService).deleteService(any());

        assertThrows(ServletException.class, () -> sut.perform(delete("/api/services/{id}", id))
                .andExpect(status().isOk()));

        verify(serviceService, times(0)).deleteService(services.get(0));
        verify(serviceService, times(0)).deleteService(services.get(1));
        verify(serviceService, times(0)).deleteService(services.get(2));
        verify(serviceService, times(0)).deleteService(services.get(3));
        verify(serviceService, times(0)).deleteService(services.get(4));
    }

    @Test
    @SneakyThrows
    void exportServicesDoesNotThrowException() {
        Resource resource = new ByteArrayResource("CSV data".getBytes());
        when(responsePreparer.prepareResponseEntity(anyString(), anyList())).thenReturn(ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=services.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource));

        assertDoesNotThrow(() -> sut.perform(get("/api/services/service-file"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=services.csv"))
                .andExpect(content().contentType("text/csv"))
                .andExpect(content().string("CSV data")));

        verify(responsePreparer, times(1)).prepareResponseEntity(anyString(), anyList());
    }

    @Test
    @SneakyThrows
    void importServicesDoesNotThrowException() {
        MockMultipartFile file = new MockMultipartFile("file", "services.csv",
                MediaType.TEXT_PLAIN_VALUE, "CSV data".getBytes());

        assertDoesNotThrow(() -> sut.perform(multipart("/api/services/service-file").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Services imported successfully")));

        verify(serviceService, times(1)).importServices(anyList());
    }

    @Test
    @SneakyThrows
    void countPriceServiceDoesNotThrowException() {
        int id = 1;
        when(serviceService.getFavorPrice(id)).thenReturn(services.get(id - 1).getPrice());
        int expected = 2000;
        String jsonResponse = sut.perform(get("/api/services/service-price/{id}", id))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        int actual = objectMapper.readValue(jsonResponse, Integer.class);

        assertEquals(expected, actual);

        verify(serviceService, times(1)).getFavorPrice(id);
    }

    @Test
    @SneakyThrows
    void countPriceServiceThrowsServletException() {
        int id = 10;
        doThrow(TechnicalException.class).when(serviceService).getFavorPrice(anyInt());

        assertThrows(ServletException.class,
                () -> sut.perform(get("/api/services/service-price/{id}", id))
                        .andExpect(status().isNotFound()));

        verify(serviceService, times(1)).getFavorPrice(id);
    }

    @Test
    @SneakyThrows
    void exportProvidedServiceDoesNotThrowException() {
        Resource resource = new ByteArrayResource("CSV data".getBytes());
        when(responsePreparer.prepareResponseEntity(anyString(), anyList())).thenReturn(ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=provided.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource));

        assertDoesNotThrow(() -> sut.perform(get("/api/services/provided-file"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=provided.csv"))
                .andExpect(content().contentType("text/csv"))
                .andExpect(content().string("CSV data")));

        verify(responsePreparer, times(1)).prepareResponseEntity(anyString(), anyList());
    }

    @Test
    @SneakyThrows
    void importProvidedServiceDoesNotThrowException() {
        MockMultipartFile file = new MockMultipartFile("file", "provided.csv",
                MediaType.TEXT_PLAIN_VALUE, "CSV data".getBytes());

        assertDoesNotThrow(() -> sut.perform(multipart("/api/services/provided-file").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Provided services imported successfully")));

        verify(serviceService, times(1)).importProvidedServices(anyList());
    }

    @Test
    @SneakyThrows
    void provideServiceDoesNotThrowException() {
        ProvidedServiceDto dto = DtoConverter.convertProvidedServiceToDto(
                new ProvidedService(3, services.get(1), LocalDateTime.now(), clients.get(3)));

        assertDoesNotThrow(() -> sut.perform(post("/api/services/provided-service")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Service provided successfully")));

        verify(serviceService, times(1)).provideService(any());
    }

    @Test
    @SneakyThrows
    void provideServiceThrowsServletException() {
        ProvidedServiceDto dto = DtoConverter.convertProvidedServiceToDto(providedServices.getFirst());
        doThrow(TechnicalException.class).when(serviceService).provideService(any());

        assertThrows(ServletException.class, () -> sut.perform(post("/api/services/provided-service")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError()));

        verify(serviceService, times(1)).provideService(any());
    }

    @Test
    @SneakyThrows
    void getClientServicesByPriceDoesNotThrowException() {
        int id = 1;
        AbstractClient client = clients.get(id - 1);
        List<AbstractService> services = providedServices.stream()
                .filter(service -> Objects.equals(service.getClient(), client)
                        && service.getService().getStatus() == ServiceStatusTypes.RENDERED)
                .map(ProvidedService::getService)
                .sorted()
                .toList();
        when(clientService.getClientById(id)).thenReturn(client);
        when(serviceService.getClientServicesByPrice(client)).thenReturn(services);
        String expected = objectMapper.writeValueAsString(DtoConverter.listServicesToDtos(services));
        String jsonResponse = sut.perform(get("/api/services/client-services-by-price/{id}", id))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(expected, jsonResponse);

        verify(serviceService, times(1)).getClientServicesByPrice(any());
        verify(clientService, times(1)).getClientById(anyInt());
    }

    @Test
    @SneakyThrows
    void getClientServicesByPriceThrowsServletException() {
        int id = 10;
        doThrow(TechnicalException.class).when(clientService).getClientById(anyInt());
        doThrow(TechnicalException.class).when(serviceService).getClientServicesByPrice(any());

        assertThrows(ServletException.class,
                () -> sut.perform(get("/api/services/client-services-by-price/{id}", id))
                        .andExpect(status().isNotFound()));

        verify(clientService, times(1)).getClientById(anyInt());
    }

    @Test
    @SneakyThrows
    void getClientServicesByTimeDoesNotThrowException() {
        int id = 1;
        AbstractClient client = clients.get(id - 1);
        List<AbstractService> services = providedServices.stream()
                .filter(service -> Objects.equals(service.getClient(), client)
                        && service.getService().getStatus() == ServiceStatusTypes.RENDERED)
                .map(ProvidedService::getService)
                .sorted(new ServiceTimeComparator())
                .toList();
        when(clientService.getClientById(id)).thenReturn(client);
        when(serviceService.getClientServicesByPrice(client)).thenReturn(services);
        String expected = objectMapper.writeValueAsString(DtoConverter.listServicesToDtos(services));
        String jsonResponse = sut.perform(get("/api/services/client-services-by-price/{id}", id))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(expected, jsonResponse);

        verify(serviceService, times(1)).getClientServicesByPrice(any());
        verify(clientService, times(1)).getClientById(anyInt());
    }
}
