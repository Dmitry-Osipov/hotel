package service;

import essence.Identifiable;
import essence.person.AbstractClient;
import essence.person.Client;
import jakarta.persistence.OptimisticLockException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.ClientRepository;
import utils.exceptions.EntityContainedException;
import utils.exceptions.TechnicalException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ClientServiceTest {
    private List<AbstractClient> clients;
    @Mock
    private ClientRepository clientRepository;
    @InjectMocks
    private ClientService sut;

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

        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        clients = null;
    }

    @Test
    @SneakyThrows
    void addClientDoesNotThrowException() {
        AbstractClient client = new Client(1, "Osipov D. R.", "+7(902)902-98-11");

        assertDoesNotThrow(() -> sut.addClient(client));

        verify(clientRepository, times(1)).save(client);
    }

    @Test
    @SneakyThrows
    void addClientThrowsEntityContainedException() {
        AbstractClient client = new Client(1, "Osipov D. R.", "+7(902)902-98-11");
        doThrow(EntityContainedException.class).when(clientRepository).save(client);

        assertThrows(EntityContainedException.class, () -> sut.addClient(client));

        verify(clientRepository, times(1)).save(client);
    }

    @Test
    @SneakyThrows
    void addClientThrowsOptimisticLockException() {
        AbstractClient client = new Client();
        doThrow(OptimisticLockException.class).when(clientRepository).save(client);

        assertThrows(OptimisticLockException.class, () -> sut.addClient(client));

        verify(clientRepository, times(1)).save(client);
    }

    @Test
    @SneakyThrows
    void getClientsGivesCurrentData() {
        when(clientRepository.getClients()).thenReturn(clients);

        assertDoesNotThrow(sut::getClients);

        verify(clientRepository, times(1)).getClients();
    }

    @Test
    @SneakyThrows
    void getClientsThrowsTechnicalException() {
        doThrow(TechnicalException.class).when(clientRepository).getClients();

        assertThrows(TechnicalException.class, sut::getClients);

        verify(clientRepository, times(1)).getClients();
    }

    @Test
    @SneakyThrows
    void countClientsGivesCurrentData() {
        when(clientRepository.getClients()).thenReturn(clients);

        int result = sut.countClients();

        assertEquals(5, result);
        verify(clientRepository, times(1)).getClients();
    }

    @Test
    @SneakyThrows
    void updateClientDoesNotThrowException() {
        AbstractClient client = new Client(1, "Osipov D. R.", "+7(953)180-00-61");

        assertDoesNotThrow(() -> sut.updateClient(client));

        verify(clientRepository, times(1)).update(client);
    }

    @Test
    @SneakyThrows
    void updateClientThrowsTechnicalException() {
        AbstractClient client = new Client(0, "Osipov D.R.", "+7(902)902-98-11");
        doThrow(TechnicalException.class).when(clientRepository).update(client);

        assertThrows(TechnicalException.class, () -> sut.updateClient(client));

        verify(clientRepository, times(1)).update(client);
    }

    @Test
    @SneakyThrows
    void importClientsDoesNotThrowException() {
        AbstractClient client1 = clients.get(0);
        AbstractClient client2 = clients.get(1);
        AbstractClient client3 = clients.get(2);
        AbstractClient client4 = clients.get(3);
        AbstractClient client5 = clients.get(4);

        assertDoesNotThrow(() -> sut.importClients(clients));

        verify(clientRepository, times(1)).update(client1);
        verify(clientRepository, times(1)).update(client2);
        verify(clientRepository, times(1)).update(client3);
        verify(clientRepository, times(1)).update(client4);
        verify(clientRepository, times(1)).update(client5);
    }

    @Test
    @SneakyThrows
    void getClientByIdGivesCurrentData() {
        int id = 1;
        doReturn(clients.stream()
                .sorted(Comparator.comparingInt(Identifiable::getId))
                .toList().get(id - 1)).when(clientRepository).getClientById(id);

        AbstractClient client = assertDoesNotThrow(() -> sut.getClientById(id));

        assertEquals(client, clients.get(id - 1));
        verify(clientRepository, times(1)).getClientById(id);
    }

    @Test
    @SneakyThrows
    void getClientByIdThrowsTechnicalException() {
        int id = 5;
        doThrow(TechnicalException.class).when(clientRepository).getClientById(id);

        assertThrows(TechnicalException.class, () -> sut.getClientById(id));

        verify(clientRepository, times(1)).getClientById(id);
    }

    @Test
    @SneakyThrows
    void deleteClientDoesNotThrowException() {
        AbstractClient client = clients.getFirst();

        assertDoesNotThrow(() -> sut.deleteClient(client));

        verify(clientRepository, times(1)).deleteClient(client);
    }

    @Test
    @SneakyThrows
    void deleteClientThrowsTechnicalException() {
        AbstractClient client = new Client(6, "Talyx O. G.", "+7(902)901-90-09");
        doThrow(TechnicalException.class).when(clientRepository).deleteClient(client);

        assertThrows(TechnicalException.class, () -> sut.deleteClient(client));

        verify(clientRepository, times(1)).deleteClient(client);
    }
}
