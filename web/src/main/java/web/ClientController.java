package web;

import dto.ClientDto;
import essence.person.AbstractClient;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import service.ClientService;
import service.RoomService;
import service.ServiceService;
import utils.DtoConverter;
import utils.RedirectBasePages;
import utils.file.ExportCSV;
import utils.file.ImportCSV;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер для обработки запросов, связанных с клиентами.
 */
@Controller
@RequestMapping("/clients")
public class ClientController {
    private final ClientService clientService;
    private final RoomService roomService;
    private final ServiceService serviceService;

    @Autowired
    public ClientController(ClientService clientService, RoomService roomService, ServiceService serviceService) {
        this.clientService = clientService;
        this.roomService = roomService;
        this.serviceService = serviceService;
    }

    /**
     * Обрабатывает GET запрос для отображения всех клиентов. Загружает всех клиентов из базы данных и передает их на
     * представление.
     * @param model объект модели Spring MVC
     * @return имя представления
     * @throws SQLException если возникла ошибка при работе с базой данных
     */
    @GetMapping("/allClients")
    public String allClients(Model model) throws SQLException {
        List<ClientDto> clients = new ArrayList<>();
        for (AbstractClient client : clientService.getClients()) {
            ClientDto dto = DtoConverter.convertClientToDto(client);
            clients.add(dto);
        }

        model.addAttribute("clients", clients);
        model.addAttribute("title", "All Clients");
        return "clients/all-clients";
    }

    /**
     * Обрабатывает GET запрос для отображения количества клиентов. Загружает количество клиентов из базы данных и
     * передает его на представление.
     * @param model объект модели Spring MVC
     * @return имя представления
     * @throws SQLException если возникла ошибка при работе с базой данных
     */
    @GetMapping("/countClients")
    public String countClients(Model model) throws SQLException {
        model.addAttribute("count", clientService.countClients());
        model.addAttribute("title", "Count Clients");
        return "clients/count-clients";
    }

    /**
     * Отображает форму для добавления нового клиента. Создает новый объект ClientDto и передает его на представление
     * вместе с заголовком.
     * @param model объект модели Spring MVC
     * @return имя представления для добавления нового клиента
     */
    @GetMapping("/addClient")
    public String saveClientForm(Model model) {
        model.addAttribute("client", new ClientDto());
        model.addAttribute("title", "Add Client");
        return "clients/save-client";
    }

    /**
     * Сохраняет нового клиента.
     * Принимает данные нового клиента из формы, проводит их валидацию, затем, если данные валидны,
     * преобразует их в объект AbstractClient и добавляет его в базу данных. После успешного добавления
     * клиента выполняется перенаправление на страницу, отображающую список всех клиентов.
     * Если данные не прошли валидацию, возвращает обратно на форму добавления клиента с отображением ошибок.
     * @param dto объект ClientDto с данными нового клиента
     * @param bindingResult объект для хранения результатов валидации
     * @return если данные валидны, перенаправляет на страницу со списком всех клиентов, в противном случае возвращает
     * на страницу для добавления клиента с отображением ошибок
     * @throws SQLException если возникает ошибка при взаимодействии с базой данных
     */
    @PostMapping("/saveClient")
    public String saveClient(@Valid @ModelAttribute("client") ClientDto dto, BindingResult bindingResult)
            throws SQLException {
        if (bindingResult.hasErrors()) {
            return "clients/save-client";
        }

        AbstractClient client = DtoConverter.convertDtoToClient(dto);
        clientService.addClient(client);
        return RedirectBasePages.CLIENT.getUrl();
    }

    /**
     * Отображает форму для обновления информации о клиенте.
     * Получает идентификатор клиента из URL и использует его для получения информации о клиенте
     * из базы данных. Затем преобразует информацию о клиенте в объект ClientDto и передает его
     * в модель для отображения на форме обновления клиента. Возвращает страницу для обновления клиента.
     * @param id идентификатор клиента, чья информация будет обновлена
     * @param model объект модели для передачи данных на страницу
     * @return страница для обновления информации о клиенте
     * @throws SQLException если возникает ошибка при взаимодействии с базой данных
     */
    @GetMapping("/updateClient/{id}")
    public String updateClientForm(@PathVariable("id") int id, Model model) throws SQLException {
        AbstractClient client = clientService.getClientById(id);
        ClientDto dto = DtoConverter.convertClientToDto(client);
        model.addAttribute("client", dto);
        model.addAttribute("title", "Update Client");
        return "clients/update-client";
    }

    /**
     * Обновляет информацию о клиенте.
     * Получает идентификатор клиента из URL и использует его для получения информации о клиенте
     * из базы данных. Проверяет, если есть ошибки валидации данных, возвращает страницу для
     * обновления информации о клиенте. Если данные прошли валидацию, конвертирует объект ClientDto
     * в соответствующий объект клиента и обновляет информацию о клиенте в базе данных. Затем
     * перенаправляет на страницу со списком всех клиентов.
     * @param id идентификатор клиента, чья информация будет обновлена
     * @param dto объект ClientDto с новой информацией о клиенте
     * @param bindingResult результат валидации данных
     * @return страница для обновления информации о клиенте, если есть ошибки валидации; иначе перенаправление на
     * страницу со списком всех клиентов
     * @throws SQLException если возникает ошибка при взаимодействии с базой данных
     */
    @PostMapping("/updateClient/{id}")
    public String updateClient(@PathVariable("id") int id, @Valid @ModelAttribute("client") ClientDto dto,
                               BindingResult bindingResult) throws SQLException {
        if (bindingResult.hasErrors()) {
            return "clients/update-client";
        }

        AbstractClient client = DtoConverter.convertDtoToClient(dto);
        clientService.updateClient(client);
        return RedirectBasePages.CLIENT.getUrl();
    }

    /**
     * Удаляет клиента.
     * Получает идентификатор клиента из URL и использует его для получения информации о клиенте
     * из базы данных. Затем удаляет клиента из базы данных и перенаправляет на страницу со списком
     * всех клиентов.
     * @param id идентификатор клиента, который будет удален
     * @return перенаправление на страницу со списком всех клиентов после удаления клиента
     * @throws SQLException если возникает ошибка при взаимодействии с базой данных
     */
    @PostMapping("/deleteClient/{id}")
    public String deleteClient(@PathVariable("id") int id) throws SQLException {
        clientService.deleteClient(clientService.getClientById(id));
        return RedirectBasePages.CLIENT.getUrl();
    }

    /**
     * Обрабатывает запрос для экспорта клиентов в формат CSV.
     * @param model объект модели
     * @return имя представления для экспорта клиентов
     */
    @GetMapping("/exportCsvClients")
    public String exportCsvClients(Model model) {
        model.addAttribute("title", "Export Clients");
        return "clients/export-clients";
    }

    /**
     * Экспортирует клиентов в формате CSV для пользователя.
     * @return ответ с ресурсом для скачивания CSV файла
     * @throws SQLException в случае возникновения ошибки при работе с базой данных
     * @throws IOException в случае возникновения ошибки ввода-вывода
     */
    @GetMapping("/exportClients")
    public ResponseEntity<Resource> exportClientsToCsv() throws SQLException, IOException {
        List<ClientDto> clients = clientService.getClients().stream()
                .map(DtoConverter::convertClientToDto)
                .collect(Collectors.toList());
        ByteArrayResource resource = new ByteArrayResource(
                ExportCSV.exportEntitiesDtoDataToBytes(clients, roomService, clientService, serviceService));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=client.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }

    /**
     * Обрабатывает запрос для импорта клиентов из файла CSV.
     * @param model объект модели
     * @return имя представления для импорта клиентов
     */
    @GetMapping("/importCsvClients")
    public String importCsvClients(Model model) {
        model.addAttribute("title", "Import Clients");
        return "clients/import-clients";
    }

    /**
     * Обрабатывает запрос для импорта клиентов из файла CSV от пользователя.
     * @param file загружаемый файл CSV с данными клиентов
     * @return перенаправление на страницу со списком всех клиентов
     * @throws IOException в случае возникновения ошибки ввода-вывода
     * @throws SQLException в случае возникновения ошибки при работе с базой данных
     */
    @PostMapping(value = "/importClients", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String importClientsFromCsv(@RequestParam("file") MultipartFile file) throws IOException, SQLException {
        for (ClientDto dto : ImportCSV.parseEntityDtoFromCsv(file, ClientDto.class)) {
            AbstractClient client = DtoConverter.convertDtoToClient(dto);
            boolean updated = clientService.updateClient(client);
            if (!updated) {
                clientService.addClient(client);
            }
        }

        return RedirectBasePages.CLIENT.getUrl();
    }
}
