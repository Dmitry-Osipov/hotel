package web.controller;

import dto.ProvidedServiceDto;
import dto.ServiceDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import service.ServiceService;
import utils.DtoConverter;
import utils.file.ImportCSV;
import utils.file.ResponseEntityPreparer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Контроллер для работы с услугами через REST API.
 */
@RestController
@RequestMapping("${api.path}/services")
@Validated
public class RestServiceController {
    private final ServiceService serviceService;
    private final ClientService clientService;
    private final ResponseEntityPreparer responseEntityPreparer;

    @Autowired
    public RestServiceController(ServiceService serviceService, ClientService clientService, ResponseEntityPreparer responseEntityPreparer) {
        this.serviceService = serviceService;
        this.clientService = clientService;
        this.responseEntityPreparer = responseEntityPreparer;
    }

    /**
     * Получает список всех услуг.
     * @return {@link ResponseEntity} с кодом 200 (OK) и списком всех услуг в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping
    public ResponseEntity<List<ServiceDto>> getAllServices() throws SQLException {
        return ResponseEntity.ok().body(DtoConverter.listServicesToDtos(serviceService.getServices()));
    }

    /**
     * Получает информацию об услуге по её идентификатору.
     * @param id идентификатор услуги.
     * @return {@link ResponseEntity} с кодом 200 (OK) и найденной услугой в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceDto> getServiceById(@PathVariable("id") int id) throws SQLException {
        return ResponseEntity.ok().body(DtoConverter.convertServiceToDto(serviceService.getServiceById(id)));
    }

    /**
     * Добавляет новую услугу.
     * @param dto данные новой услуги.
     * @return {@link ResponseEntity} с кодом 200 (OK) и добавленной услугой в теле ответа, если данные корректны,
     * или кодом 400 (Bad Request) в случае некорректных данных.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @PostMapping
    public ResponseEntity<String> addService(@Valid @RequestBody ServiceDto dto) throws SQLException {
        serviceService.addService(DtoConverter.convertDtoToService(dto));
        return ResponseEntity.ok().body("Service " + dto.getName() + " added successfully");
    }

    /**
     * Обновляет информацию об услуге.
     * @param dto данные обновляемой услуги.
     * @return {@link ResponseEntity} с кодом 200 (OK) и обновленной услугой в теле ответа, если данные корректны,
     * или кодом 400 (Bad Request) в случае некорректных данных.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @PutMapping
    public ResponseEntity<ServiceDto> updateService(@Valid @RequestBody ServiceDto dto) throws SQLException {
        serviceService.updateService(DtoConverter.convertDtoToService(dto));
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
        serviceService.deleteService(serviceService.getServiceById(id));
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
        return responseEntityPreparer.prepareResponseEntity("services.csv",
                DtoConverter.listServicesToDtos(serviceService.getServices()));
    }

    /**
     * Импортирует данные об услугах из файла CSV.
     * @param file файл CSV с данными об услугах.
     * @return {@link ResponseEntity} с кодом 200 (OK) и сообщением об успешном импорте данных в теле ответа.
     * @throws IOException  если возникает ошибка ввода-вывода при чтении файла.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @PostMapping("/service-file")
    public ResponseEntity<String> importServicesFromCsv(@RequestParam("file") MultipartFile file) throws IOException,
            SQLException {
        serviceService.importServices(DtoConverter.listServicesFromDtos(
                ImportCSV.parseEntityDtoFromCsv(file, ServiceDto.class)));
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
        return ResponseEntity.ok().body(serviceService.getFavorPrice(id));
    }

    /**
     * Экспортирует данные о предоставленных услугах в формате CSV.
     * @return {@link ResponseEntity} с кодом 200 (OK) и файлом CSV в теле ответа.
     * @throws IOException  если возникает ошибка ввода-вывода при экспорте данных.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping("/provided-file")
    public ResponseEntity<Resource> exportProvidedServicesToCsv() throws IOException, SQLException {
        return responseEntityPreparer.prepareResponseEntity("provided.csv",
                DtoConverter.listProvidedServicesToDtos(serviceService.getProvidedServices()));
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
        serviceService.importProvidedServices(DtoConverter.listProvidedServicesFromDtos(
                ImportCSV.parseEntityDtoFromCsv(file, ProvidedServiceDto.class), serviceService, clientService));
        return ResponseEntity.ok().body("Provided services imported successfully");
    }

    /**
     * Предоставляет услугу клиенту.
     * @param dto данные о предоставляемой услуге.
     * @return ResponseEntity с кодом 200 (OK) и предоставленной услугой в теле ответа, если данные корректны,
     * или кодом 400 (Bad Request) в случае некорректных данных.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @PostMapping("/provided-service")
    public ResponseEntity<String> provideService(@Valid @RequestBody ProvidedServiceDto dto) throws SQLException {
        serviceService.provideService(DtoConverter.convertDtoToProvidedService(dto, serviceService, clientService));
        return ResponseEntity.ok().body("Service provided successfully");
    }

    /**
     * Получает список услуг для клиента по их стоимости.
     * @param id идентификатор клиента.
     * @return {@link ResponseEntity} с кодом 200 (OK) и списком услуг для клиента в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping("/client-services-by-price/{id}")
    public ResponseEntity<List<ServiceDto>> getClientServicesByPrice(@PathVariable("id") int id) throws SQLException {
        return ResponseEntity.ok().body(DtoConverter.listServicesToDtos(
                serviceService.getClientServicesByPrice(clientService.getClientById(id))));
    }

    /**
     * Получает список услуг для клиента по их времени предоставления.
     * @param id идентификатор клиента.
     * @return {@link ResponseEntity} с кодом 200 (OK) и списком услуг для клиента в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping("/client-services-by-time/{id}")
    public ResponseEntity<List<ServiceDto>> getClientServicesByTime(@PathVariable("id") int id) throws SQLException {
        return ResponseEntity.ok().body(DtoConverter.listServicesToDtos(
                serviceService.getClientServicesByTime(clientService.getClientById(id))));
    }
}
