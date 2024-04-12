package web;

import dto.ClientDto;
import essence.person.AbstractClient;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер для работы с клиентами через REST API.
 */
@RestController
@RequestMapping("/api/clients")
public class RestClientController {
    private final ClientService clientService;
    private final RoomService roomService;
    private final ServiceService serviceService;

    @Autowired
    public RestClientController(ClientService clientService, RoomService roomService, ServiceService serviceService) {
        this.clientService = clientService;
        this.roomService = roomService;
        this.serviceService = serviceService;
    }

    /**
     * Получает список всех клиентов.
     * @return {@link ResponseEntity} с кодом 200 (OK) и списком всех клиентов в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping
    public ResponseEntity<List<ClientDto>> getAllClients() throws SQLException {
        List<ClientDto> clients = clientService.getClients().stream()
                .map(DtoConverter::convertClientToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(clients);
    }

    /**
     * Получает клиента по его идентификатору.
     * @param id идентификатор клиента.
     * @return {@link ResponseEntity} с кодом 200 (OK) и найденным клиентом в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable("id") int id) throws SQLException {
        ClientDto dto = DtoConverter.convertClientToDto(clientService.getClientById(id));
        return ResponseEntity.ok().body(dto);
    }

    /**
     * Добавляет нового клиента.
     * @param dto данные нового клиента.
     * @param bindingResult результат валидации данных.
     * @return {@link ResponseEntity} с кодом 200 (OK) и добавленным клиентом в теле ответа, если данные корректны,
     * или кодом 400 (Bad Request) в случае некорректных данных.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @PostMapping
    public ResponseEntity<ClientDto> addClient(@Valid @RequestBody ClientDto dto, BindingResult bindingResult)
            throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        AbstractClient client = DtoConverter.convertDtoToClient(dto);
        clientService.addClient(client);
        List<AbstractClient> clients = clientService.getClients();
        return ResponseEntity.ok().body(DtoConverter.convertClientToDto(clients.get(clients.size() - 1)));
    }

    /**
     * Обновляет информацию о клиенте.
     * @param dto данные обновляемого клиента.
     * @param bindingResult результат валидации данных.
     * @return {@link ResponseEntity} с кодом 200 (OK) и обновленным клиентом в теле ответа, если данные корректны,
     * или кодом 400 (Bad Request) в случае некорректных данных.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @PutMapping
    public ResponseEntity<ClientDto> updateClient(@Valid @RequestBody ClientDto dto, BindingResult bindingResult)
            throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        boolean updated = clientService.updateClient(DtoConverter.convertDtoToClient(dto));
        if (!updated) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(dto);
    }

    /**
     * Удаляет клиента по его идентификатору.
     * @param id идентификатор удаляемого клиента.
     * @return {@link ResponseEntity} с кодом 200 (OK) и сообщением об успешном удалении клиента в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable("id") int id) throws SQLException {
        AbstractClient client = clientService.getClientById(id);
        clientService.deleteClient(client);
        return ResponseEntity.ok().body("Deleted client with ID = " + id);
    }

    /**
     * Получает количество клиентов.
     * @return {@link ResponseEntity} с кодом 200 (OK) и количеством клиентов в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping("/number-clients")
    public ResponseEntity<Integer> countClients() throws SQLException {
        Integer count = clientService.countClients();
        return ResponseEntity.ok().body(count);
    }

    /**
     * Экспортирует данные о клиентах в формате CSV.
     * @return {@link ResponseEntity} с кодом 200 (OK) и файлом CSV в теле ответа.
     * @throws IOException  если возникает ошибка ввода-вывода при экспорте данных.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping("/client-file")
    public ResponseEntity<Resource> exportClientsToCsv() throws IOException, SQLException {
        List<ClientDto> clients = clientService.getClients().stream()
                .map(DtoConverter::convertClientToDto)
                .collect(Collectors.toList());
        return ResponseEntityPreparer.prepareResponseEntity("clients.csv", clients, roomService,
                serviceService, clientService);
    }

    /**
     * Импортирует данные о клиентах из файла CSV.
     * @param file файл CSV с данными о клиентах.
     * @return {@link ResponseEntity} с кодом 200 (OK) и сообщением об успешном импорте данных в теле ответа.
     * @throws IOException  если возникает ошибка ввода-вывода при чтении файла.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @PostMapping("/client-file")
    public ResponseEntity<String> importsClientsFromCsv(@RequestParam("file") MultipartFile file)
            throws IOException, SQLException {
        List<AbstractClient> clients = ImportCSV.parseEntityDtoFromCsv(file, ClientDto.class).stream()
                .map(DtoConverter::convertDtoToClient)
                .collect(Collectors.toList());
        for (AbstractClient client : clients) {
            boolean updated = clientService.updateClient(client);
            if (!updated) {
                clientService.addClient(client);
            }
        }

        return ResponseEntity.ok().body("Clients imported successfully");
    }
}
