package web;

import dto.ProvidedServiceDto;
import dto.ServiceDto;
import essence.person.AbstractClient;
import essence.provided.ProvidedService;
import essence.service.AbstractFavor;
import essence.service.AbstractService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import service.ClientService;
import service.RoomService;
import service.ServiceService;
import utils.DtoConverter;
import utils.file.ImportCSV;
import utils.file.ResponseEntityPreparer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер для работы с услугами через REST API.
 */
@RestController
@RequestMapping("/api/services")
public class RestServiceController {
    private final RoomService roomService;
    private final ServiceService serviceService;
    private final ClientService clientService;

    @Autowired
    public RestServiceController(RoomService roomService, ServiceService serviceService, ClientService clientService) {
        this.roomService = roomService;
        this.serviceService = serviceService;
        this.clientService = clientService;
    }

    /**
     * Получает список всех услуг.
     * @return {@link ResponseEntity} с кодом 200 (OK) и списком всех услуг в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping
    public ResponseEntity<List<ServiceDto>> getAllServices() throws SQLException {
        List<ServiceDto> services = serviceService.getServices().stream()
                .map(DtoConverter::convertServiceToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(services);
    }

    /**
     * Получает информацию об услуге по её идентификатору.
     * @param id идентификатор услуги.
     * @return {@link ResponseEntity} с кодом 200 (OK) и найденной услугой в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceDto> getServiceById(@PathVariable("id") int id) throws SQLException {
        ServiceDto dto = DtoConverter.convertServiceToDto(serviceService.getServiceById(id));
        return ResponseEntity.ok().body(dto);
    }

    /**
     * Добавляет новую услугу.
     * @param dto данные новой услуги.
     * @param bindingResult результат валидации данных.
     * @return {@link ResponseEntity} с кодом 200 (OK) и добавленной услугой в теле ответа, если данные корректны,
     * или кодом 400 (Bad Request) в случае некорректных данных.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @PostMapping
    public ResponseEntity<ServiceDto> addService(@Valid @RequestBody ServiceDto dto, BindingResult bindingResult)
            throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        AbstractService service = DtoConverter.convertDtoToService(dto);
        serviceService.addService(service);
        List<AbstractService> services = serviceService.getServices();
        return ResponseEntity.ok().body(DtoConverter.convertServiceToDto(services.get(services.size() - 1)));
    }

    /**
     * Обновляет информацию об услуге.
     * @param dto данные обновляемой услуги.
     * @param bindingResult результат валидации данных.
     * @return {@link ResponseEntity} с кодом 200 (OK) и обновленной услугой в теле ответа, если данные корректны,
     * или кодом 400 (Bad Request) в случае некорректных данных.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @PutMapping
    public ResponseEntity<ServiceDto> updateService(@Valid @RequestBody ServiceDto dto, BindingResult bindingResult)
            throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        boolean updated = serviceService.updateService(DtoConverter.convertDtoToService(dto));
        if (!updated) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(dto);
    }

    /**
     * Удаляет услугу по её идентификатору.
     * @param id идентификатор удаляемой услуги.
     * @return {@link ResponseEntity} с кодом 200 (OK) и сообщением об успешном удалении услуги в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteService(@PathVariable("id") int id) throws SQLException {
        AbstractService service = serviceService.getServiceById(id);
        serviceService.deleteService(service);
        return ResponseEntity.ok().body("Deleted service with ID = " + id);
    }

    /**
     * Экспортирует данные об услугах в формате CSV.
     * @return {@link ResponseEntity} с кодом 200 (OK) и файлом CSV в теле ответа.
     * @throws IOException  если возникает ошибка ввода-вывода при экспорте данных.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping("/service-file")
    public ResponseEntity<Resource> exportServicesToCsv() throws IOException, SQLException {
        List<ServiceDto> services = serviceService.getServices().stream()
                .map(DtoConverter::convertServiceToDto)
                .collect(Collectors.toList());
        return ResponseEntityPreparer.prepareResponseEntity("services.csv", services, roomService,
                serviceService, clientService);
    }

    /**
     * Импортирует данные об услугах из файла CSV.
     * @param file файл CSV с данными об услугах.
     * @return {@link ResponseEntity} с кодом 200 (OK) и сообщением об успешном импорте данных в теле ответа.
     * @throws IOException  если возникает ошибка ввода-вывода при чтении файла.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @PostMapping("/service-file")
    public ResponseEntity<String> importServicesFromCsv(@RequestParam("file") MultipartFile file)
            throws IOException, SQLException {
        List<AbstractService> services = ImportCSV.parseEntityDtoFromCsv(file, ServiceDto.class).stream()
                .map(DtoConverter::convertDtoToService)
                .collect(Collectors.toList());
        for (AbstractService service : services) {
            boolean updated = serviceService.updateService(service);
            if (!updated) {
                serviceService.addService(service);
            }
        }

        return ResponseEntity.ok().body("Services imported successfully");
    }

    /**
     * Получает стоимость услуги.
     * @param id идентификатор услуги.
     * @return {@link ResponseEntity} с кодом 200 (OK) и стоимостью услуги в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping("/service-price/{id}")
    public ResponseEntity<Integer> countPriceService(@PathVariable("id") int id) throws SQLException {
        int price = serviceService.getFavorPrice((AbstractFavor) serviceService.getServiceById(id));
        return ResponseEntity.ok().body(price);
    }

    /**
     * Экспортирует данные о предоставленных услугах в формате CSV.
     * @return {@link ResponseEntity} с кодом 200 (OK) и файлом CSV в теле ответа.
     * @throws IOException  если возникает ошибка ввода-вывода при экспорте данных.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping("/provided-file")
    public ResponseEntity<Resource> exportProvidedServicesToCsv() throws IOException, SQLException {
        List<ProvidedServiceDto> providedServices = serviceService.getProvidedServices().stream()
                .map(DtoConverter::convertProvidedServiceToDto)
                .collect(Collectors.toList());
        return ResponseEntityPreparer.prepareResponseEntity("provided.csv", providedServices, roomService,
                serviceService, clientService);
    }

    /**
     * Импортирует данные о предоставленных услугах из файла CSV.
     * @param file файл CSV с данными о предоставленных услугах.
     * @return {@link ResponseEntity} с кодом 200 (OK) и сообщением об успешном импорте данных в теле ответа.
     * @throws IOException  если возникает ошибка ввода-вывода при чтении файла.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @PostMapping("/provided-file")
    public ResponseEntity<String> importProvidedServicesFromCsv(@RequestParam("file") MultipartFile file)
            throws IOException, SQLException {
        List<ProvidedServiceDto> dtos = ImportCSV.parseEntityDtoFromCsv(file, ProvidedServiceDto.class);
        List<ProvidedService> providedServices = new ArrayList<>(dtos.size());

        for (ProvidedServiceDto dto : dtos) {
            ProvidedService providedService = DtoConverter.convertDtoToProvidedService(dto, serviceService,
                    clientService);
            providedServices.add(providedService);
        }

        for (ProvidedService providedService : providedServices) {
            boolean updated = serviceService.updateProvidedService(providedService);
            if (!updated) {
                serviceService.addProvidedService(providedService);
            }
        }

        return ResponseEntity.ok().body("Provided services imported successfully");
    }

    /**
     * Предоставляет услугу клиенту.
     * @param dto данные о предоставляемой услуге.
     * @param bindingResult результат валидации данных.
     * @return ResponseEntity с кодом 200 (OK) и предоставленной услугой в теле ответа, если данные корректны,
     * или кодом 400 (Bad Request) в случае некорректных данных.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @PostMapping("/provided-service")
    public ResponseEntity<ProvidedServiceDto> provideService(@Valid @RequestBody ProvidedServiceDto dto,
                                                             BindingResult bindingResult) throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        ProvidedService providedService = DtoConverter.convertDtoToProvidedService(dto, serviceService, clientService);
        serviceService.provideService(providedService.getClient(), providedService.getService());
        List<ProvidedService> providedServices = serviceService.getProvidedServices();
        return ResponseEntity.ok()
                .body(DtoConverter.convertProvidedServiceToDto(providedServices.get(providedServices.size() - 1)));
    }

    /**
     * Получает список услуг для клиента по их стоимости.
     * @param id идентификатор клиента.
     * @return {@link ResponseEntity} с кодом 200 (OK) и списком услуг для клиента в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping("/client-services-by-price/{id}")
    public ResponseEntity<List<ServiceDto>> getClientServicesByPrice(@PathVariable("id") int id) throws SQLException {
        if (id < 1) {
            return ResponseEntity.badRequest().build();
        }

        AbstractClient client = clientService.getClientById(id);
        List<ServiceDto> services = serviceService.getClientServicesByPrice(client).stream()
                .map(DtoConverter::convertServiceToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(services);
    }

    /**
     * Получает список услуг для клиента по их времени предоставления.
     * @param id идентификатор клиента.
     * @return {@link ResponseEntity} с кодом 200 (OK) и списком услуг для клиента в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping("/client-services-by-time/{id}")
    public ResponseEntity<List<ServiceDto>> getClientServicesByTime(@PathVariable("id") int id) throws SQLException {
        if (id < 1) {
            return ResponseEntity.badRequest().build();
        }

        AbstractClient client = clientService.getClientById(id);
        List<ServiceDto> services = serviceService.getClientServicesByTime(client).stream()
                .map(DtoConverter::convertServiceToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(services);
    }
}
