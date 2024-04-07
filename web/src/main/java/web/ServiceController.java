package web;

import dto.ServiceDto;
import essence.service.AbstractService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import service.ServiceService;
import utils.DtoConverter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Контроллер для обработки запросов, связанных с услугами.
 */
@Controller
@RequestMapping("/services")
public class ServiceController {
    private final ServiceService serviceService;

    @Autowired
    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
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
        return "redirect:/services/allServices";
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
     * страницу формы обновления информации о услуге
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
        return "redirect:/services/allServices";
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
        return "redirect:/services/allServices";
    }
}
