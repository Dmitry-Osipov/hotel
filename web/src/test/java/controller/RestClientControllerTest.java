package controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import config.TestConfig;
import dto.ClientDto;
import essence.person.AbstractClient;
import essence.person.Client;
import jakarta.servlet.ServletException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import service.ClientService;
import utils.DtoConverter;
import utils.exceptions.TechnicalException;
import utils.file.ResponseEntityPreparer;
import web.controller.RestClientController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig
@ContextConfiguration(classes = TestConfig.class)
class RestClientControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private List<AbstractClient> clients;
    private MockMvc sut;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ResponseEntityPreparer responsePreparer;

    @BeforeEach
    public void setUp() {
        AbstractClient client1 = new Client(1, "Osipov D. R.", "+7(902)902-98-11");
        client1.setCheckInTime(LocalDateTime.of(2024, 1, 25, 22, 0, 37));
        AbstractClient client2 = new Client(2, "Musofranova N. S.", "+7(961)150-09-97");
        client2.setCheckInTime(LocalDateTime.of(2024, 1, 25, 22, 0, 37));
        AbstractClient client3 = new Client(3, "Belyakova I. S.", "+7(953)180-00-61");
        client3.setCheckInTime(LocalDateTime.of(2024, 1, 25, 22, 0, 37));
        AbstractClient client4 = new Client(4, "Kondrashin E. V.", "+7(991)234-11-00");
        AbstractClient client5 = new Client(5, "Lebedev G.I.", "+7(921)728-21-01");
        clients = new ArrayList<>(List.of(client1, client2, client3, client4, client5));

        sut = MockMvcBuilders.standaloneSetup(new RestClientController(clientService, responsePreparer)).build();
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        clients = null;
        sut = null;
    }

    @Test
    @SneakyThrows
    void getAllClientsDoesNotThrowException() {
        when(clientService.getClients()).thenReturn(clients);
        List<ClientDto> expected = DtoConverter.listClientsToDtos(clients);
        String jsonResponse = sut.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<ClientDto> actual = objectMapper.readValue(jsonResponse, new TypeReference<>() {});

        assertEquals(expected, actual);

        verify(clientService, times(1)).getClients();
    }

    @Test
    @SneakyThrows
    void getAllClientsThrowsServletException() {
        doThrow(TechnicalException.class).when(clientService).getClients();

        assertThrows(ServletException.class, () ->
                sut.perform(get("/api/clients")).andExpect(status().isInternalServerError()));

        verify(clientService, times(1)).getClients();
    }

    @Test
    @SneakyThrows
    void getClientByIdDoesNotThrowException() {
        int id = 1;
        AbstractClient client = clients.get(id - 1);
        when(clientService.getClientById(id)).thenReturn(client);
        ClientDto expected = DtoConverter.convertClientToDto(client);
        String jsonResponse = sut.perform(get("/api/clients/{id}", id))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        ClientDto actual = objectMapper.readValue(jsonResponse, ClientDto.class);

        assertEquals(expected, actual);

        verify(clientService, times(1)).getClientById(id);
    }

    @Test
    @SneakyThrows
    void getClientByIdThrowsServletException() {
        int id = 0;
        doThrow(TechnicalException.class).when(clientService).getClientById(id);

        assertThrows(ServletException.class, () ->
                sut.perform(get("/api/clients/{id}", id)).andExpect(status().isNotFound()));

        verify(clientService, times(1)).getClientById(id);
    }

    @Test
    @SneakyThrows
    void addClientDoesNotThrowException() {
        ClientDto client =
                DtoConverter.convertClientToDto(new Client(6, "Talyx O. G.", "+7(900)009-99-11"));

        assertDoesNotThrow(() -> sut.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isOk())
                .andExpect(content().string("Client " + client.getFio() + " added successfully")));

        verify(clientService, times(1)).addClient(DtoConverter.convertDtoToClient(client));
    }

    @Test
    @SneakyThrows
    void addClientThrowsServletException() {
        ClientDto dto =
                DtoConverter.convertClientToDto(new Client(6, "A", "8(900)009-99-11"));
        doThrow(TechnicalException.class).when(clientService).addClient(DtoConverter.convertDtoToClient(dto));

        assertThrows(ServletException.class, () -> sut.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError()));

        verify(clientService, times(1)).addClient(DtoConverter.convertDtoToClient(dto));
    }

    @Test
    @SneakyThrows
    void updateClientDoesNotThrowException() {
        ClientDto dto =
                DtoConverter.convertClientToDto(new Client(1, "Osipov D. R.", "+7(953)180-00-61"));
        String jsonResponse = sut.perform(put("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        ClientDto actual = objectMapper.readValue(jsonResponse, ClientDto.class);

        assertEquals(dto, actual);

        verify(clientService, times(1)).updateClient(DtoConverter.convertDtoToClient(dto));
    }

    @Test
    @SneakyThrows
    void updateClientThrowsServletException() {
        ClientDto dto =
                DtoConverter.convertClientToDto(new Client(6, "A", "8(900)009-99-11"));
        doThrow(TechnicalException.class).when(clientService).updateClient(DtoConverter.convertDtoToClient(dto));

        assertThrows(ServletException.class, () -> sut.perform(put("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError()));

        verify(clientService, times(1)).updateClient(DtoConverter.convertDtoToClient(dto));
    }

    @Test
    @SneakyThrows
    void deleteClientDoesNotThrowException() {
        int id = 2;
        when(clientService.getClientById(id)).thenReturn(clients.get(id - 1));

        assertDoesNotThrow(() -> sut.perform(delete("/api/clients/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted client with ID = " + id)));

        verify(clientService, times(1)).deleteClient(clients.get(id - 1));
        verify(clientService, times(1)).getClientById(id);
    }

    @Test
    @SneakyThrows
    void deleteClientThrowsServletException() {
        int id = 0;
        doThrow(TechnicalException.class).when(clientService).deleteClient(any());

        assertThrows(ServletException.class, () -> sut.perform(delete("/api/clients/{id}", id))
                .andExpect(status().isOk()));

        verify(clientService, times(0)).deleteClient(clients.get(0));
        verify(clientService, times(0)).deleteClient(clients.get(1));
        verify(clientService, times(0)).deleteClient(clients.get(2));
        verify(clientService, times(0)).deleteClient(clients.get(3));
        verify(clientService, times(0)).deleteClient(clients.get(4));
    }

    @Test
    @SneakyThrows
    void countClientsDoesNotThrowException() {
        int expected = clients.size();
        when(clientService.countClients()).thenReturn(expected);
        String jsonResponse = sut.perform(get("/api/clients/number-clients"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        int actual = objectMapper.readValue(jsonResponse, Integer.class);

        assertEquals(expected, actual);

        verify(clientService, times(1)).countClients();
    }

    @Test
    @SneakyThrows
    void exportClientsDoesNotThrowException() {
        Resource resource = new ByteArrayResource("CSV data".getBytes());
        when(responsePreparer.prepareResponseEntity(anyString(), anyList())).thenReturn(ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=clients.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource));

        assertDoesNotThrow(() -> sut.perform(get("/api/clients/client-file"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=clients.csv"))
                .andExpect(content().contentType("text/csv"))
                .andExpect(content().string("CSV data")));

        verify(responsePreparer, times(1)).prepareResponseEntity(anyString(), anyList());
    }

    @Test
    @SneakyThrows
    void importClientsDoesNotThrowException() {
        MockMultipartFile file = new MockMultipartFile("file", "clients.csv",
                MediaType.TEXT_PLAIN_VALUE, "CSV data".getBytes());

        assertDoesNotThrow(() -> sut.perform(multipart("/api/clients/client-file").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Clients imported successfully")));

        verify(clientService, times(1)).importClients(anyList());
    }
}
