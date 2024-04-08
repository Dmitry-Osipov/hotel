package web;

import dto.ProvidedServiceDto;
import dto.ServiceDto;
import essence.provided.ProvidedService;
import essence.service.AbstractService;
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
 * Контроллер для обработки запросов, связанных с услугами.
 */
@Controller
@RequestMapping("/services")
public class ServiceController {
    private final ServiceService serviceService;
    private final ClientService clientService;
    private final RoomService roomService;

    @Autowired
    public ServiceController(ServiceService serviceService, ClientService clientService, RoomService roomService) {
        this.serviceService = serviceService;
        this.clientService = clientService;
        this.roomService = roomService;
    }

    /**
     * Отображает страницу со списком всех услуг.
     * @param model модель представления
     * @return имя представления для списка всех услуг
     * @throws SQLException если возникает ошибка при взаимодействии с базой данных
     */
    @GetMapping("/allServices")
    public String allServices(Model model) throws SQLException {
        List<ServiceDto> services = new ArrayList<>();
        for (AbstractService service : serviceService.getServices()) {
            ServiceDto serviceDto = DtoConverter.convertServiceToDto(service);
            services.add(serviceDto);
        }

        model.addAttribute("services", services);
        model.addAttribute("title", "All Services");
        return "services/all-services";
    }

    /**
     * Отображает форму для добавления новой услуги.
     * @param model модель представления
     * @return имя представления для формы добавления новой услуги
     */
    @GetMapping("/addService")
    public String saveServiceForm(Model model) {
        model.addAttribute("service", new ServiceDto());
        model.addAttribute("title", "Add Service");
        return "services/save-service";
    }

    /**
     * Сохраняет новую услугу.
     * @param serviceDto DTO новой услуги
     * @param bindingResult результат валидации данных
     * @return перенаправление на страницу со списком всех услуг в случае успешного сохранения; иначе возврат на
     * страницу формы добавления новой услуги
     * @throws SQLException если возникает ошибка при взаимодействии с базой данных
     */
    @PostMapping("/saveService")
    public String saveService(@Valid @ModelAttribute("service") ServiceDto serviceDto, BindingResult bindingResult)
            throws SQLException {
        if (bindingResult.hasErrors()) {
            return "services/save-service";
        }

        AbstractService service = DtoConverter.convertDtoToService(serviceDto);
        serviceService.addService(service);
        return RedirectBasePages.SERVICE.getUrl();
    }

    /**
     * Отображает форму для обновления информации о выбранной услуге.
     * @param id идентификатор услуги, чья информация будет обновлена
     * @param model модель представления
     * @return имя представления для формы обновления информации о услуге
     * @throws SQLException если возникает ошибка при взаимодействии с базой данных
     */
    @GetMapping("/updateService/{id}")
    public String updateServiceForm(@PathVariable("id") int id, Model model) throws SQLException {
        AbstractService service = serviceService.getServiceById(id);
        ServiceDto dto = DtoConverter.convertServiceToDto(service);
        model.addAttribute("service", dto);
        model.addAttribute("title", "Update Service");
        return "services/update-service";
    }

    /**
     * Обновляет информацию о выбранной услуге.
     * @param id идентификатор услуги, чья информация будет обновлена
     * @param serviceDto  с новой информацией о выбранной услуге
     * @param bindingResult результат валидации данных
     * @return перенаправление на страницу со списком всех услуг в случае успешного обновления; иначе возврат на
     * страницу формы обновления информации об услуге
     * @throws SQLException если возникает ошибка при взаимодействии с базой данных
     */
    @PostMapping("/updateService/{id}")
    public String updateService(@PathVariable("id") int id, @Valid @ModelAttribute("service") ServiceDto serviceDto,
                                BindingResult bindingResult) throws SQLException {
        if (bindingResult.hasErrors()) {
            return "services/update-service";
        }

        AbstractService service = DtoConverter.convertDtoToService(serviceDto);
        serviceService.updateService(service);
        return RedirectBasePages.SERVICE.getUrl();
    }

    /**
     * Удаляет выбранную услугу.
     * @param id идентификатор услуги, которая будет удалена
     * @return перенаправление на страницу со списком всех услуг после удаления выбранной услуги
     * @throws SQLException если возникает ошибка при взаимодействии с базой данных
     */
    @PostMapping("/deleteService/{id}")
    public String deleteService(@PathVariable("id") int id) throws SQLException {
        serviceService.deleteClient(serviceService.getServiceById(id));
        return RedirectBasePages.SERVICE.getUrl();
    }

    /**
     * Обрабатывает запрос для экспорта услуг в формат CSV.
     * @param model объект модели
     * @return имя представления для экспорта услуг
     */
    @GetMapping("/exportCsvServices")
    public String exportCsvServices(Model model) {
        model.addAttribute("title", "Export Services");
        return "services/export-services";
    }

    /**
     * Экспортирует услуги в формате CSV для пользователя.
     * @return ответ с ресурсом для скачивания CSV файла
     * @throws SQLException в случае возникновения ошибки при работе с базой данных
     * @throws IOException в случае возникновения ошибки ввода-вывода
     */
    @GetMapping("/exportServices")
    public ResponseEntity<Resource> exportServicesToCsv() throws SQLException, IOException {
        List<ServiceDto> services = serviceService.getServices().stream()
                .map(DtoConverter::convertServiceToDto)
                .collect(Collectors.toList());
        ByteArrayResource resource = new ByteArrayResource(
                ExportCSV.exportEntitiesDtoDataToBytes(services, roomService, clientService, serviceService));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=service.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }

    /**
     * Обрабатывает запрос для импорта услуг из файла CSV.
     * @param model объект модели
     * @return имя представления для импорта услуг
     */
    @GetMapping("/importCsvServices")
    public String importCsvServices(Model model) {
        model.addAttribute("title", "Import Services");
        return "services/import-services";
    }

    /**
     * Обрабатывает запрос для услуг клиентов из файла CSV от пользователя.
     * @param file загружаемый файл CSV с данными услуг
     * @return перенаправление на страницу со списком всех услуг
     * @throws IOException в случае возникновения ошибки ввода-вывода
     * @throws SQLException в случае возникновения ошибки при работе с базой данных
     */
    @PostMapping(value = "/importServices", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String importServicesFromCsv(@RequestParam("file") MultipartFile file) throws IOException, SQLException {
        for (ServiceDto dto : ImportCSV.parseEntityDtoFromCsv(file, ServiceDto.class)) {
            AbstractService service = DtoConverter.convertDtoToService(dto);
            boolean updated = serviceService.updateService(service);
            if (!updated) {
                serviceService.addService(service);
            }
        }

        return RedirectBasePages.SERVICE.getUrl();
    }

    // -------
    /**
     * Обрабатывает запрос для экспорта проведённых в формат CSV.
     * @param model объект модели
     * @return имя представления для экспорта проведённых услуг
     */
    @GetMapping("/exportCsvProvidedServices")
    public String exportCsvClients(Model model) {
        model.addAttribute("title", "Export Provided Services");
        return "services/export-provided-services";
    }

    /**
     * Экспортирует проведённых услуг в формате CSV для пользователя.
     * @return ответ с ресурсом для скачивания CSV файла
     * @throws SQLException в случае возникновения ошибки при работе с базой данных
     * @throws IOException в случае возникновения ошибки ввода-вывода
     */
    @GetMapping("/exportProvidedServices")
    public ResponseEntity<Resource> exportClientsToCsv() throws SQLException, IOException {
        List<ProvidedServiceDto> services = serviceService.getProvidedServices().stream()
                .map(DtoConverter::convertProvidedServiceToDto)
                .collect(Collectors.toList());
        ByteArrayResource resource = new ByteArrayResource(
                ExportCSV.exportEntitiesDtoDataToBytes(services, roomService, clientService, serviceService));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=provided.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }

    /**
     * Обрабатывает запрос для импорта проведённых услуг из файла CSV.
     * @param model объект модели
     * @return имя представления для импорта проведённых услуг
     */
    @GetMapping("/importCsvProvidedServices")
    public String importCsvClients(Model model) {
        model.addAttribute("title", "Import Provided Services");
        return "services/import-provided-services";
    }

    /**
     * Обрабатывает запрос для импорта проведённых услуг из файла CSV от пользователя.
     * @param file загружаемый файл CSV с данными проведённых услуг
     * @return перенаправление на страницу со списком всех проведённых услуг
     * @throws IOException в случае возникновения ошибки ввода-вывода
     * @throws SQLException в случае возникновения ошибки при работе с базой данных
     */
    @PostMapping(value = "/importProvidedServices", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String importClientsFromCsv(@RequestParam("file") MultipartFile file) throws IOException, SQLException {
        for (ProvidedServiceDto dto : ImportCSV.parseEntityDtoFromCsv(file, ProvidedServiceDto.class)) {
            ProvidedService providedService =
                    DtoConverter.convertDtoToProvidedService(dto, serviceService, clientService);

            boolean updated = serviceService.updateProvidedService(providedService);
            if (!updated) {
                serviceService.addProvidedService(providedService);
            }
        }

        return RedirectBasePages.SERVICE.getUrl();
    }

    @GetMapping("/countPriceService/{id}")
    public String countPriceService(@PathVariable("id") int id, Model model) throws SQLException {
        int price = DtoConverter.convertServiceToDto(serviceService.getServiceById(id)).getPrice();
        model.addAttribute("title", "Service Price");
        model.addAttribute("id", id);
        model.addAttribute("price", price);
        return "services/count-price-service";
    }
}
