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
import utils.file.ExportCSV;
import utils.file.ImportCSV;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/allClients")
    public ResponseEntity<List<ClientDto>> getAllClients() throws SQLException {
        List<ClientDto> clients = clientService.getClients().stream()
                .map(DtoConverter::convertClientToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(clients);
    }

    @GetMapping("/getClient/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable("id") int id) throws SQLException {
        ClientDto dto = DtoConverter.convertClientToDto(clientService.getClientById(id));
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/addClient")
    public ResponseEntity<ClientDto> addClient(@Valid @RequestBody ClientDto dto, BindingResult bindingResult)
            throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        AbstractClient client = DtoConverter.convertDtoToClient(dto);
        clientService.addClient(client);
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/updateClient/{id}")
    public ResponseEntity<ClientDto> updateClient(@PathVariable("id") int id, @Valid @RequestBody ClientDto dto,
                                                  BindingResult bindingResult) throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        boolean updated = clientService.updateClient(DtoConverter.convertDtoToClient(dto));
        if (!updated) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(DtoConverter.convertClientToDto(clientService.getClientById(id)));
    }

    @DeleteMapping("/deleteClient/{id}")
    public ResponseEntity<ClientDto> deleteClient(@PathVariable("id") int id) throws SQLException {
        AbstractClient client = clientService.getClientById(id);
        clientService.deleteClient(client);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/countClients")
    public ResponseEntity<Integer> countClients() throws SQLException {
        Integer count = clientService.countClients();
        return ResponseEntity.ok().body(count);
    }

    @GetMapping("/exportClients")
    public ResponseEntity<Resource> exportClientsToCsv() throws IOException, SQLException {
        List<ClientDto> clients = clientService.getClients().stream()
                .map(DtoConverter::convertClientToDto)
                .collect(Collectors.toList());
        ByteArrayResource resource = new ByteArrayResource(
                ExportCSV.exportEntitiesDtoDataToBytes(clients, roomService, clientService, serviceService));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=clients.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }

    @PostMapping("/importClients")
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
