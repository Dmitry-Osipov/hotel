package web.controller;

import dto.ClientDto;
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
import utils.DtoConverter;
import utils.file.ImportCSV;
import utils.file.ResponseEntityPreparer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Контроллер для работы с клиентами через REST API.
 */
@RestController
@RequestMapping("${api.path}/clients")
@Validated
public class RestClientController {
    private final ClientService clientService;
    private final ResponseEntityPreparer responseEntityPreparer;

    @Autowired
    public RestClientController(ClientService clientService, ResponseEntityPreparer responseEntityPreparer) {
        this.clientService = clientService;
        this.responseEntityPreparer = responseEntityPreparer;
    }

    /**
     * Получает список всех клиентов.
     * @return {@link ResponseEntity} с кодом 200 (OK) и списком всех клиентов в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping
    public ResponseEntity<List<ClientDto>> getAllClients() throws SQLException {
        return ResponseEntity.ok().body(DtoConverter.listClientsToDtos(clientService.getClients()));
    }

    /**
     * Получает клиента по его идентификатору.
     * @param id идентификатор клиента.
     * @return {@link ResponseEntity} с кодом 200 (OK) и найденным клиентом в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable("id") int id) throws SQLException {
        return ResponseEntity.ok().body(DtoConverter.convertClientToDto(clientService.getClientById(id)));
    }

    /**
     * Добавляет нового клиента.
     * @param dto данные нового клиента.
     * @return {@link ResponseEntity} с кодом 200 (OK) и добавленным клиентом в теле ответа, если данные корректны,
     * или кодом 400 (Bad Request) в случае некорректных данных.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @PostMapping
    public ResponseEntity<String> addClient(@Valid @RequestBody ClientDto dto)
            throws SQLException {
        clientService.addClient(DtoConverter.convertDtoToClient(dto));
        return ResponseEntity.ok().body("Client " + dto.getFio() + " added successfully");
    }

    /**
     * Обновляет информацию о клиенте.
     * @param dto данные обновляемого клиента.
     * @return {@link ResponseEntity} с кодом 200 (OK) и обновленным клиентом в теле ответа, если данные корректны,
     * или кодом 400 (Bad Request) в случае некорректных данных.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @PutMapping
    public ResponseEntity<ClientDto> updateClient(@Valid @RequestBody ClientDto dto)
            throws SQLException {
        clientService.updateClient(DtoConverter.convertDtoToClient(dto));
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
        clientService.deleteClient(clientService.getClientById(id));
        return ResponseEntity.ok().body("Deleted client with ID = " + id);
    }

    /**
     * Получает количество клиентов.
     * @return {@link ResponseEntity} с кодом 200 (OK) и количеством клиентов в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping("/number-clients")
    public ResponseEntity<Integer> countClients() throws SQLException {
        return ResponseEntity.ok().body(clientService.countClients());
    }

    /**
     * Экспортирует данные о клиентах в формате CSV.
     * @return {@link ResponseEntity} с кодом 200 (OK) и файлом CSV в теле ответа.
     * @throws IOException  если возникает ошибка ввода-вывода при экспорте данных.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping("/client-file")
    public ResponseEntity<Resource> exportClientsToCsv() throws IOException, SQLException {
        return responseEntityPreparer.prepareResponseEntity("clients.csv",
                DtoConverter.listClientsToDtos(clientService.getClients()));
    }

    /**
     * Импортирует данные о клиентах из файла CSV.
     * @param file файл CSV с данными о клиентах.
     * @return {@link ResponseEntity} с кодом 200 (OK) и сообщением об успешном импорте данных в теле ответа.
     * @throws IOException  если возникает ошибка ввода-вывода при чтении файла.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @PostMapping("/client-file")
    public ResponseEntity<String> importsClientsFromCsv(@RequestParam("file") MultipartFile file) throws IOException,
            SQLException {
        clientService.importClients(DtoConverter.listClientsFromDto(
                ImportCSV.parseEntityDtoFromCsv(file, ClientDto.class)));
        return ResponseEntity.ok().body("Clients imported successfully");
    }
}
