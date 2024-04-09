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

    @GetMapping("/allServices")
    public ResponseEntity<List<ServiceDto>> getAllServices() throws SQLException {
        List<ServiceDto> services = serviceService.getServices().stream()
                .map(DtoConverter::convertServiceToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(services);
    }

    @GetMapping("/getService/{id}")
    public ResponseEntity<ServiceDto> getServiceById(@PathVariable("id") int id) throws SQLException {
        ServiceDto dto = DtoConverter.convertServiceToDto(serviceService.getServiceById(id));
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/addService")
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

    @PutMapping("/updateService/{id}")
    public ResponseEntity<ServiceDto> updateService(@PathVariable("id") int id, @Valid @RequestBody ServiceDto dto,
                                                    BindingResult bindingResult) throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        boolean updated = serviceService.updateService(DtoConverter.convertDtoToService(dto));
        if (!updated) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(DtoConverter.convertServiceToDto(serviceService.getServiceById(id)));
    }

    @DeleteMapping("/deleteService/{id}")
    public ResponseEntity<String> deleteService(@PathVariable("id") int id) throws SQLException {
        AbstractService service = serviceService.getServiceById(id);
        serviceService.deleteService(service);
        return ResponseEntity.ok().body("Deleted service with ID = " + id);
    }

    @GetMapping("/exportServices")
    public ResponseEntity<Resource> exportServicesToCsv() throws IOException, SQLException {
        List<ServiceDto> services = serviceService.getServices().stream()
                .map(DtoConverter::convertServiceToDto)
                .collect(Collectors.toList());
        return ResponseEntityPreparer.prepareResponseEntity("services.csv", services, roomService,
                serviceService, clientService);
    }

    @PostMapping("/importServices")
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

    @GetMapping("/countPriceService/{id}")
    public ResponseEntity<Integer> countPriceService(@PathVariable("id") int id) throws SQLException {
        int price = serviceService.getFavorPrice((AbstractFavor) serviceService.getServiceById(id));
        return ResponseEntity.ok().body(price);
    }

    @GetMapping("/exportProvidedServices")
    public ResponseEntity<Resource> exportProvidedServicesToCsv() throws IOException, SQLException {
        List<ProvidedServiceDto> providedServices = serviceService.getProvidedServices().stream()
                .map(DtoConverter::convertProvidedServiceToDto)
                .collect(Collectors.toList());
        return ResponseEntityPreparer.prepareResponseEntity("provided.csv", providedServices, roomService,
                serviceService, clientService);
    }

    @PostMapping("/importProvidedServices")
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

    @PostMapping("/provideService")
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

    @GetMapping("/getClientServicesByPrice/{id}")
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

    @GetMapping("/getClientServicesByTime/{id}")
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
