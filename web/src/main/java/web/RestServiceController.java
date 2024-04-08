package web;

import dto.ServiceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.ClientService;
import service.RoomService;
import service.ServiceService;
import utils.DtoConverter;

import java.sql.SQLException;
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
}
