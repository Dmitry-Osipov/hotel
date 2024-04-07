package web;

import dto.ClientDto;
import essence.person.AbstractClient;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import service.ClientService;
import utils.DtoConverter;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/clients")
public class ClientController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/allClients")
    @SneakyThrows
    public String allClients(Model model) {
        List<ClientDto> clients = new ArrayList<>();
        for (AbstractClient client : clientService.getClients()) {
            ClientDto dto = DtoConverter.convertClientToDto(client);
            clients.add(dto);
        }

        model.addAttribute("clients", clients);
        model.addAttribute("title", "All Clients");
        return "clients/all-clients";
    }

    @GetMapping("/countClients")
    @SneakyThrows
    public String countClients(Model model) {
        model.addAttribute("count", clientService.countClients());
        model.addAttribute("title", "Count Clients");
        return "clients/count-clients";
    }

    @GetMapping("/addClient")
    public String saveClientForm(Model model) {
        model.addAttribute("client", new ClientDto());
        model.addAttribute("title", "Add Client");
        return "clients/save-client";
    }

    @PostMapping("/saveClient")
    @SneakyThrows
    public String saveClient(@Valid @ModelAttribute("client") ClientDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "clients/save-client";
        }

        AbstractClient client = DtoConverter.convertDtoToClient(dto);
        clientService.addClient(client);
        return "redirect:/clients/allClients";
    }

    @GetMapping("/updateClient/{id}")
    @SneakyThrows
    public String updateClientForm(@PathVariable("id") int id, Model model) {
        AbstractClient client = clientService.getClientById(id);
        ClientDto dto = DtoConverter.convertClientToDto(client);
        model.addAttribute("client", dto);
        model.addAttribute("title", "Update Client");
        return "clients/update-client";
    }

    @PostMapping("/updateClient/{id}")
    @SneakyThrows
    public String updateClient(@PathVariable("id") int id, @Valid @ModelAttribute("client") ClientDto dto,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "clients/update-client";
        }

        AbstractClient client = DtoConverter.convertDtoToClient(dto);
        clientService.updateClient(client);
        return "redirect:/clients/allClients";
    }

    @PostMapping("/deleteClient/{id}")
    @SneakyThrows
    public String deleteClient(@PathVariable("id") int id) {
        clientService.deleteClient(clientService.getClientById(id));
        return "redirect:/clients/allClients";
    }
}
