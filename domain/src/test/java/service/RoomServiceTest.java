package service;

import essence.person.AbstractClient;
import essence.person.Client;
import essence.reservation.RoomReservation;
import essence.room.AbstractRoom;
import essence.room.Room;
import essence.room.RoomStatusTypes;
import jakarta.persistence.OptimisticLockException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.ClientRepository;
import repository.RoomRepository;
import repository.RoomReservationRepository;
import utils.comparators.RoomCapacityComparator;
import utils.comparators.RoomPriceComparator;
import utils.exceptions.AccessDeniedException;
import utils.exceptions.EntityContainedException;
import utils.exceptions.InvalidDataException;
import utils.exceptions.TechnicalException;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RoomServiceTest {
    private List<AbstractRoom> rooms;
    private List<RoomReservation> reservations;
    private List<AbstractClient> clients;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private RoomReservationRepository roomReservationRepository;
    @Mock
    private ClientRepository clientRepository;
    @InjectMocks
    private RoomService sut;

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

        AbstractRoom room1 = new Room(1, 1, 3, 3000);
        room1.setStatus(RoomStatusTypes.OCCUPIED);
        room1.setStars(5);
        room1.setCheckInTime(LocalDateTime.of(2024, 1, 25, 22, 0, 37));
        AbstractRoom room2 = new Room(2, 2, 5, 13000);
        room2.setStatus(RoomStatusTypes.OCCUPIED);
        room2.setStars(4);
        room2.setCheckInTime(LocalDateTime.of(2024, 1, 25, 22, 0, 37));
        AbstractRoom room3 = new Room(3, 3, 6, 20000);
        room3.setStars(5);
        AbstractRoom room4 = new Room(4, 4, 7, 30000);
        room4.setStars(5);
        AbstractRoom room5 = new Room(5, 5, 2, 85000);
        room5.setStars(4);
        AbstractRoom room6 = new Room(6, 6, 4, 6000);
        rooms = new ArrayList<>(List.of(room1, room2, room3, room4, room5, room6));

        RoomReservation reservation1 = new RoomReservation(1, rooms.getFirst(),
                LocalDateTime.of(2024, 1, 25, 22, 0, 37),
                LocalDateTime.of(2024, 1, 26, 22, 0, 37),
                List.of(clients.get(0), clients.get(1)));
        RoomReservation reservation2 = new RoomReservation(2, rooms.get(1),
                LocalDateTime.of(2024, 1, 25, 22, 0, 37),
                LocalDateTime.of(2024, 1, 26, 22, 0, 37),
                List.of(clients.get(2)));
        reservations = new ArrayList<>(List.of(reservation1, reservation2));

        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        rooms = null;
        reservations = null;
        clients = null;
    }

    @Test
    @SneakyThrows
    void getRoomsGivesCurrentData() {
        when(roomRepository.getRooms()).thenReturn(rooms);

        assertDoesNotThrow(sut::getRooms);

        verify(roomRepository, times(1)).getRooms();
    }

    @Test
    @SneakyThrows
    void getRoomsThrowsTechnicalException() {
        doThrow(TechnicalException.class).when(roomRepository).getRooms();

        assertThrows(TechnicalException.class, sut::getRooms);

        verify(roomRepository, times(1)).getRooms();
    }

    @Test
    @SneakyThrows
    void addRoomDoesNotThrowException() {
        AbstractRoom room = new Room(7, 7, 3, 9000);

        assertDoesNotThrow(() -> sut.addRoom(room));

        verify(roomRepository, times(1)).save(room);
    }

    @Test
    @SneakyThrows
    void addRoomThrowsEntityContainedException() {
        AbstractRoom room = new Room(1, 1, 3, 3000);
        doThrow(EntityContainedException.class).when(roomRepository).save(room);

        assertThrows(EntityContainedException.class, () -> sut.addRoom(room));

        verify(roomRepository, times(1)).save(room);
    }

    @Test
    @SneakyThrows
    void addRoomThrowsOptimisticLockException() {
        AbstractRoom room = new Room();
        doThrow(OptimisticLockException.class).when(roomRepository).save(room);

        assertThrows(OptimisticLockException.class, () -> sut.addRoom(room));

        verify(roomRepository, times(1)).save(room);
    }

    @Test
    @SneakyThrows
    void updateRoomDoesNotThrowException() {
        changeSetStatusRoom();
        AbstractRoom room = new Room(1, 1, 3, 19000);

        assertDoesNotThrow(() -> sut.updateRoom(room));

        verify(roomRepository, times(1)).update(room);
    }

    @Test
    @SneakyThrows
    void updateRoomThrowsTechnicalException() {
        changeSetStatusRoom();
        AbstractRoom room = new Room(0, 7, 3, 9000);
        doThrow(TechnicalException.class).when(roomRepository).update(room);

        assertThrows(TechnicalException.class, () -> sut.updateRoom(room));

        verify(roomRepository, times(1)).update(room);
    }

    @Test
    @SneakyThrows
    void updateRoomThrowsAccessDeniedException() {
        AbstractRoom room = new Room(1, 1, 3, 19000);

        assertThrows(AccessDeniedException.class, () -> sut.updateRoom(room));

        verify(roomRepository, times(0)).update(room);
    }

    @Test
    @SneakyThrows
    void importRoomsDoesNotThrowException() {
        changeSetStatusRoom();
        AbstractRoom room1 = rooms.get(0);
        AbstractRoom room2 = rooms.get(1);
        AbstractRoom room3 = rooms.get(2);
        AbstractRoom room4 = rooms.get(3);
        AbstractRoom room5 = rooms.get(4);
        AbstractRoom room6 = rooms.get(5);

        assertDoesNotThrow(() -> sut.importRooms(rooms));

        verify(roomRepository, times(1)).update(room1);
        verify(roomRepository, times(1)).update(room2);
        verify(roomRepository, times(1)).update(room3);
        verify(roomRepository, times(1)).update(room4);
        verify(roomRepository, times(1)).update(room5);
        verify(roomRepository, times(1)).update(room6);
    }

    @Test
    @SneakyThrows
    void importRoomsThrowsAccessDeniedException() {
        AbstractRoom room1 = rooms.get(0);
        AbstractRoom room2 = rooms.get(1);
        AbstractRoom room3 = rooms.get(2);
        AbstractRoom room4 = rooms.get(3);
        AbstractRoom room5 = rooms.get(4);
        AbstractRoom room6 = rooms.get(5);

        assertThrows(AccessDeniedException.class, () -> sut.importRooms(rooms));

        verify(roomRepository, times(0)).update(room1);
        verify(roomRepository, times(0)).update(room2);
        verify(roomRepository, times(0)).update(room3);
        verify(roomRepository, times(0)).update(room4);
        verify(roomRepository, times(0)).update(room5);
        verify(roomRepository, times(0)).update(room6);
    }

    @Test
    @SneakyThrows
    void importReservationsDoesNotThrowException() {
        RoomReservation reservation1 = reservations.get(0);
        RoomReservation reservation2 = reservations.get(1);

        assertDoesNotThrow(() -> sut.importReservations(reservations));

        verify(roomReservationRepository, times(1)).update(reservation1);
        verify(roomReservationRepository, times(1)).update(reservation2);
    }

    @Test
    @SneakyThrows
    void addReservationDoesNotThrowException() {
        RoomReservation reservation = new RoomReservation(3, rooms.get(2),
                LocalDateTime.of(2024, 1, 25, 22, 0, 37),
                LocalDateTime.of(2024, 1, 26, 22, 0, 37),
                List.of(clients.get(3), clients.get(4)));

        assertDoesNotThrow(() -> sut.addReservation(reservation));

        verify(roomReservationRepository, times(1)).save(reservation);
    }

    @Test
    @SneakyThrows
    void addReservationThrowsEntityContainedException() {
        RoomReservation reservation = new RoomReservation(1, rooms.getFirst(),
                LocalDateTime.of(2024, 1, 25, 22, 0, 37),
                LocalDateTime.of(2024, 1, 26, 22, 0, 37),
                List.of(clients.get(0), clients.get(1)));
        doThrow(EntityContainedException.class).when(roomReservationRepository).save(reservation);

        assertThrows(EntityContainedException.class, () -> sut.addReservation(reservation));

        verify(roomReservationRepository, times(1)).save(reservation);
    }

    @Test
    @SneakyThrows
    void addReservationThrowsOptimisticLockException() {
        RoomReservation reservation = new RoomReservation();
        doThrow(OptimisticLockException.class).when(roomReservationRepository).save(reservation);

        assertThrows(OptimisticLockException.class, () -> sut.addReservation(reservation));

        verify(roomReservationRepository, times(1)).save(reservation);
    }

    @Test
    @SneakyThrows
    void updateReservationDoesNotThrowException() {
        RoomReservation reservation = new RoomReservation(1, rooms.getFirst(),
                LocalDateTime.of(2024, 1, 25, 22, 0, 37),
                LocalDateTime.of(2024, 1, 26, 22, 0, 37),
                List.of(clients.get(0), clients.get(1), clients.get(2)));

        assertDoesNotThrow(() -> sut.updateReservation(reservation));

        verify(roomReservationRepository, times(1)).update(reservation);
    }

    @Test
    @SneakyThrows
    void updateReservationThrowsTechnicalException() {
        RoomReservation reservation = new RoomReservation(0, rooms.getFirst(),
                LocalDateTime.of(2024, 1, 25, 22, 0, 37),
                LocalDateTime.of(2024, 1, 26, 22, 0, 37),
                List.of(clients.get(0), clients.get(1)));
        doThrow(TechnicalException.class).when(roomReservationRepository).update(reservation);

        assertThrows(TechnicalException.class, () -> sut.updateReservation(reservation));

        verify(roomReservationRepository, times(1)).update(reservation);
    }

    @Test
    @SneakyThrows
    void checkInDoesNotThrowExceptionWithOneClient() {
        changeSetStatusRoom();
        AbstractClient client = clients.get(3);
        AbstractRoom room = rooms.get(3);
        when(roomRepository.getRooms()).thenReturn(rooms);

        assertDoesNotThrow(() -> sut.checkIn(room, client));

        verify(roomRepository, times(1)).update(room);
        verify(clientRepository, times(1)).update(client);
    }

    @Test
    @SneakyThrows
    void checkInDoesNotThrowExceptionWithManyClients() {
        changeSetStatusRoom();
        AbstractClient client1 = clients.get(2);
        AbstractClient client2 = clients.get(3);
        AbstractClient client3 = clients.get(4);
        AbstractRoom room = rooms.get(3);
        when(roomRepository.getRooms()).thenReturn(rooms);

        assertDoesNotThrow(() -> sut.checkIn(room, client1, client2, client3));

        verify(roomRepository, times(1)).update(room);
        verify(clientRepository, times(1)).update(client1);
        verify(clientRepository, times(1)).update(client2);
        verify(clientRepository, times(1)).update(client3);
    }

    @Test
    @SneakyThrows
    void checkInThrowsInvalidDataExceptionWithoutClients() {
        AbstractRoom room = rooms.get(3);
        when(roomRepository.getRooms()).thenReturn(rooms);

        assertThrows(InvalidDataException.class, () -> sut.checkIn(room));

        verify(roomRepository, times(0)).update(room);
    }

    @Test
    @SneakyThrows
    void checkInThrowsInvalidDataExceptionWithSoMuchClients() {
        AbstractClient client1 = clients.get(2);
        AbstractClient client2 = clients.get(3);
        AbstractClient client3 = clients.get(4);
        AbstractRoom room = rooms.get(4);
        when(roomRepository.getRooms()).thenReturn(rooms);

        assertThrows(InvalidDataException.class, () -> sut.checkIn(room, client1, client2, client3));

        verify(roomRepository, times(0)).update(room);
        verify(clientRepository, times(0)).update(client1);
        verify(clientRepository, times(0)).update(client2);
        verify(clientRepository, times(0)).update(client3);
    }

    @Test
    @SneakyThrows
    void checkInThrowsInvalidDataExceptionWithOccupiedRoom() {
        AbstractClient client1 = clients.get(3);
        AbstractClient client2 = clients.get(4);
        AbstractRoom room = rooms.getFirst();
        when(roomRepository.getRooms()).thenReturn(rooms);

        assertThrows(InvalidDataException.class, () -> sut.checkIn(room, client1, client2));

        verify(roomRepository, times(0)).update(room);
        verify(clientRepository, times(0)).update(client1);
        verify(clientRepository, times(0)).update(client2);
    }

    @Test
    @SneakyThrows
    void checkInThrowsInvalidDataExceptionWithNewRoom() {
        AbstractClient client1 = clients.get(3);
        AbstractClient client2 = clients.get(4);
        AbstractRoom room = new Room(7, 7, 2, 3000);
        when(roomRepository.getRooms()).thenReturn(rooms);

        assertThrows(InvalidDataException.class, () -> sut.checkIn(room, client1, client2));

        verify(roomRepository, times(0)).update(room);
        verify(clientRepository, times(0)).update(client1);
        verify(clientRepository, times(0)).update(client2);
    }

    @Test
    @SneakyThrows
    void checkInThrowsAccessDeniedException() {
        AbstractClient client1 = clients.get(3);
        AbstractClient client2 = clients.get(4);
        AbstractRoom room = rooms.get(3);
        when(roomRepository.getRooms()).thenReturn(rooms);

        assertThrows(AccessDeniedException.class, () -> sut.checkIn(room, client1, client2));

        verify(roomRepository, times(0)).update(room);
        verify(clientRepository, times(0)).update(client1);
        verify(clientRepository, times(0)).update(client2);
    }

    @Test
    @SneakyThrows
    void checkInWithReservationDoesNotThrowExceptionWithOneClient() {
        changeSetStatusRoom();
        RoomReservation reservation = new RoomReservation(3, rooms.get(2),
                LocalDateTime.of(2024, 1, 25, 22, 0, 37),
                LocalDateTime.of(2024, 1, 26, 22, 0, 37),
                List.of(clients.get(3), clients.get(4)));
        when(roomRepository.getRooms()).thenReturn(rooms);

        assertDoesNotThrow(() -> sut.checkIn(reservation));

        verify(roomRepository, times(1)).update(reservation.getRoom());
        verify(clientRepository, times(1)).update(reservation.getClients().getFirst());
        verify(clientRepository, times(1)).update(reservation.getClients().getLast());
    }

    @Test
    @SneakyThrows
    void checkInWithReservationDoesNotThrowExceptionWithManyClients() {
        changeSetStatusRoom();
        RoomReservation reservation = new RoomReservation(3, rooms.get(3),
                LocalDateTime.of(2024, 1, 25, 22, 0, 37),
                LocalDateTime.of(2024, 1, 26, 22, 0, 37),
                List.of(clients.get(2), clients.get(3), clients.get(4)));
        when(roomRepository.getRooms()).thenReturn(rooms);

        assertDoesNotThrow(() -> sut.checkIn(reservation));

        verify(roomRepository, times(1)).update(reservation.getRoom());
        verify(clientRepository, times(1)).update(reservation.getClients().get(0));
        verify(clientRepository, times(1)).update(reservation.getClients().get(1));
        verify(clientRepository, times(1)).update(reservation.getClients().get(2));
    }

    @Test
    @SneakyThrows
    void checkInWithReservationThrowsInvalidDataExceptionWithoutClients() {
        RoomReservation reservation = new RoomReservation(3, rooms.get(2),
                LocalDateTime.of(2024, 1, 25, 22, 0, 37),
                LocalDateTime.of(2024, 1, 26, 22, 0, 37),
                List.of());
        when(roomRepository.getRooms()).thenReturn(rooms);

        assertThrows(InvalidDataException.class, () -> sut.checkIn(reservation));

        verify(roomRepository, times(0)).update(reservation.getRoom());
    }

    @Test
    @SneakyThrows
    void checkInWithReservationThrowsInvalidDataExceptionWithSoMuchClients() {
        RoomReservation reservation = new RoomReservation(3, rooms.get(4),
                LocalDateTime.of(2024, 1, 25, 22, 0, 37),
                LocalDateTime.of(2024, 1, 26, 22, 0, 37),
                List.of(clients.get(2), clients.get(3), clients.get(4)));
        when(roomRepository.getRooms()).thenReturn(rooms);

        assertThrows(InvalidDataException.class, () -> sut.checkIn(reservation));

        verify(roomRepository, times(0)).update(reservation.getRoom());
        verify(clientRepository, times(0)).update(reservation.getClients().get(0));
        verify(clientRepository, times(0)).update(reservation.getClients().get(1));
        verify(clientRepository, times(0)).update(reservation.getClients().get(2));
    }

    @Test
    @SneakyThrows
    void checkInWithReservationThrowsInvalidDataExceptionWithOccupiedRoom() {
        RoomReservation reservation = new RoomReservation(3, rooms.getFirst(),
                LocalDateTime.of(2024, 1, 25, 22, 0, 37),
                LocalDateTime.of(2024, 1, 26, 22, 0, 37),
                List.of(clients.get(3), clients.get(4)));
        when(roomRepository.getRooms()).thenReturn(rooms);

        assertThrows(InvalidDataException.class, () -> sut.checkIn(reservation));

        verify(roomRepository, times(0)).update(reservation.getRoom());
        verify(clientRepository, times(0)).update(reservation.getClients().get(0));
        verify(clientRepository, times(0)).update(reservation.getClients().get(1));
    }

    @Test
    @SneakyThrows
    void checkInWithReservationThrowsInvalidDataExceptionWithNewRoom() {
        RoomReservation reservation = new RoomReservation(3, new Room(7, 7, 2, 3000),
                LocalDateTime.of(2024, 1, 25, 22, 0, 37),
                LocalDateTime.of(2024, 1, 26, 22, 0, 37),
                List.of(clients.get(3), clients.get(4)));
        when(roomRepository.getRooms()).thenReturn(rooms);

        assertThrows(InvalidDataException.class, () -> sut.checkIn(reservation));

        verify(roomRepository, times(0)).update(reservation.getRoom());
        verify(clientRepository, times(0)).update(reservation.getClients().get(0));
        verify(clientRepository, times(0)).update(reservation.getClients().get(1));
    }

    @Test
    @SneakyThrows
    void checkInWithReservationThrowsAccessDeniedException() {
        RoomReservation reservation = new RoomReservation(3, rooms.get(3),
                LocalDateTime.of(2024, 1, 25, 22, 0, 37),
                LocalDateTime.of(2024, 1, 26, 22, 0, 37),
                List.of(clients.get(3), clients.get(4)));
        when(roomRepository.getRooms()).thenReturn(rooms);

        assertThrows(AccessDeniedException.class, () -> sut.checkIn(reservation));

        verify(roomRepository, times(0)).update(reservation.getRoom());
        verify(clientRepository, times(0)).update(reservation.getClients().get(0));
        verify(clientRepository, times(0)).update(reservation.getClients().get(1));
    }

    @Test
    @SneakyThrows
    void addStarsToRoomDoesNotThrowException() {
        changeSetStatusRoom();
        AbstractRoom room = rooms.get(3);
        when(roomRepository.getRooms()).thenReturn(rooms);

        assertDoesNotThrow(() -> sut.addStarsToRoom(room, 3));

        verify(roomRepository, times(1)).update(room);
    }

    @Test
    @SneakyThrows
    void addStarsToRoomThrowsInvalidDataExceptionWithZeroOrLessStars() {
        AbstractRoom room = rooms.get(3);

        assertThrows(InvalidDataException.class, () -> sut.addStarsToRoom(room, 0));

        verify(roomRepository, times(0)).update(room);
    }

    @Test
    @SneakyThrows
    void addStarsToRoomThrowsInvalidDataExceptionWithSixOrGraterStars() {
        AbstractRoom room = rooms.get(3);

        assertThrows(InvalidDataException.class, () -> sut.addStarsToRoom(room, 6));

        verify(roomRepository, times(0)).update(room);
    }

    @Test
    @SneakyThrows
    void addStarsToRoomThrowsAccessDeniedException() {
        AbstractRoom room = rooms.get(3);
        when(roomRepository.getRooms()).thenReturn(rooms);

        assertThrows(AccessDeniedException.class, () -> sut.addStarsToRoom(room, 3));

        verify(roomRepository, times(0)).update(room);
    }

    @Test
    @SneakyThrows
    void evictDoesNotThrowException() {
        changeSetStatusRoom();
        AbstractRoom room = rooms.getFirst();
        AbstractClient client1 = clients.get(0);
        AbstractClient client2 = clients.get(1);
        when(roomRepository.getRooms()).thenReturn(rooms);
        when(roomReservationRepository.getReservations()).thenReturn(reservations);

        assertDoesNotThrow(() -> sut.evict(room, client1, client2));

        verify(roomRepository, times(1)).update(room);
        verify(roomReservationRepository, times(1)).getReservations();
        verify(clientRepository, times(1)).update(client1);
        verify(clientRepository, times(1)).update(client2);
    }

    @Test
    @SneakyThrows
    void evictWithSoMuchClientsThrowsInvalidDataException() {
        AbstractRoom room = rooms.getFirst();
        AbstractClient client1 = clients.get(0);
        AbstractClient client2 = clients.get(1);
        AbstractClient client3 = clients.get(2);
        AbstractClient client4 = clients.get(3);
        when(roomRepository.getRooms()).thenReturn(rooms);
        when(roomReservationRepository.getReservations()).thenReturn(reservations);

        assertThrows(InvalidDataException.class, () -> sut.evict(room, client1, client2, client3, client4));

        verify(roomRepository, times(0)).update(room);
        verify(roomReservationRepository, times(0)).getReservations();
        verify(clientRepository, times(0)).update(client1);
        verify(clientRepository, times(0)).update(client2);
        verify(clientRepository, times(0)).update(client3);
        verify(clientRepository, times(0)).update(client4);
    }

    @Test
    @SneakyThrows
    void evictWithoutClientThrowsInvalidDataException() {
        AbstractRoom room = rooms.getFirst();
        when(roomRepository.getRooms()).thenReturn(rooms);
        when(roomReservationRepository.getReservations()).thenReturn(reservations);

        assertThrows(InvalidDataException.class, () -> sut.evict(room));

        verify(roomRepository, times(0)).update(room);
        verify(roomReservationRepository, times(0)).getReservations();
    }

    @Test
    @SneakyThrows
    void evictFromAvailableRoomThrowsInvalidDataException() {
        AbstractRoom room = rooms.get(2);
        AbstractClient client = clients.get(3);
        when(roomRepository.getRooms()).thenReturn(rooms);
        when(roomReservationRepository.getReservations()).thenReturn(reservations);

        assertThrows(InvalidDataException.class, () -> sut.evict(room, client));

        verify(roomRepository, times(0)).update(room);
        verify(roomReservationRepository, times(0)).getReservations();
        verify(clientRepository, times(0)).update(client);
    }

    @Test
    @SneakyThrows
    void evictThrowsAccessDeniedException() {
        AbstractRoom room = rooms.getFirst();
        AbstractClient client1 = clients.get(0);
        AbstractClient client2 = clients.get(1);
        when(roomRepository.getRooms()).thenReturn(rooms);
        when(roomReservationRepository.getReservations()).thenReturn(reservations);

        assertThrows(AccessDeniedException.class, () -> sut.evict(room, client1, client2));

        verify(roomRepository, times(0)).update(room);
        verify(roomReservationRepository, times(0)).getReservations();
        verify(clientRepository, times(0)).update(client1);
        verify(clientRepository, times(0)).update(client2);
    }

    @Test
    @SneakyThrows
    void evictWithReservationDoesNotThrowException() {
        changeSetStatusRoom();
        RoomReservation reservation = reservations.getFirst();
        when(roomRepository.getRooms()).thenReturn(rooms);
        when(roomReservationRepository.getReservations()).thenReturn(reservations);

        assertDoesNotThrow(() -> sut.evict(reservation));

        verify(roomRepository, times(1)).update(reservation.getRoom());
        verify(roomReservationRepository, times(1)).getReservations();
        verify(clientRepository, times(1)).update(reservation.getClients().getFirst());
        verify(clientRepository, times(1)).update(reservation.getClients().getLast());
    }

    @Test
    @SneakyThrows
    void evictWithReservationWithSoMuchClientsThrowsInvalidDataException() {
        RoomReservation reservation = new RoomReservation(3, rooms.getFirst(),
                LocalDateTime.of(2024, 1, 25, 22, 0, 37),
                LocalDateTime.of(2024, 1, 26, 22, 0, 37),
                List.of(clients.get(0), clients.get(1), clients.get(2), clients.get(3)));
        when(roomRepository.getRooms()).thenReturn(rooms);
        when(roomReservationRepository.getReservations()).thenReturn(reservations);

        assertThrows(InvalidDataException.class, () -> sut.evict(reservation));

        verify(roomRepository, times(0)).update(reservation.getRoom());
        verify(roomReservationRepository, times(0)).getReservations();
        verify(clientRepository, times(0)).update(reservation.getClients().get(0));
        verify(clientRepository, times(0)).update(reservation.getClients().get(1));
        verify(clientRepository, times(0)).update(reservation.getClients().get(2));
        verify(clientRepository, times(0)).update(reservation.getClients().get(3));
    }

    @Test
    @SneakyThrows
    void evictWithReservationWithoutClientThrowsInvalidDataException() {
        RoomReservation reservation = new RoomReservation(3, rooms.getFirst(),
                LocalDateTime.of(2024, 1, 25, 22, 0, 37),
                LocalDateTime.of(2024, 1, 26, 22, 0, 37),
                List.of());
        when(roomRepository.getRooms()).thenReturn(rooms);
        when(roomReservationRepository.getReservations()).thenReturn(reservations);

        assertThrows(InvalidDataException.class, () -> sut.evict(reservation));

        verify(roomRepository, times(0)).update(reservation.getRoom());
        verify(roomReservationRepository, times(0)).getReservations();
    }

    @Test
    @SneakyThrows
    void evictWithReservationFromAvailableRoomThrowsInvalidDataException() {
        RoomReservation reservation = new RoomReservation(3, rooms.get(2),
                LocalDateTime.of(2024, 1, 25, 22, 0, 37),
                LocalDateTime.of(2024, 1, 26, 22, 0, 37),
                List.of(clients.get(3)));
        when(roomRepository.getRooms()).thenReturn(rooms);
        when(roomReservationRepository.getReservations()).thenReturn(reservations);

        assertThrows(InvalidDataException.class, () -> sut.evict(reservation));

        verify(roomRepository, times(0)).update(reservation.getRoom());
        verify(roomReservationRepository, times(0)).getReservations();
        verify(clientRepository, times(0)).update(reservation.getClients().getFirst());
    }

    @Test
    @SneakyThrows
    void evictWithReservationThrowsAccessDeniedException() {
        RoomReservation reservation = new RoomReservation(3, rooms.getFirst(),
                LocalDateTime.of(2024, 1, 25, 22, 0, 37),
                LocalDateTime.of(2024, 1, 26, 22, 0, 37),
                List.of(clients.get(0), clients.get(1)));
        when(roomRepository.getRooms()).thenReturn(rooms);
        when(roomReservationRepository.getReservations()).thenReturn(reservations);

        assertThrows(AccessDeniedException.class, () -> sut.evict(reservation));

        verify(roomRepository, times(0)).update(reservation.getRoom());
        verify(roomReservationRepository, times(0)).getReservations();
        verify(clientRepository, times(0)).update(reservation.getClients().getFirst());
        verify(clientRepository, times(0)).update(reservation.getClients().getLast());
    }

    @Test
    @SneakyThrows
    void roomsByStarsDoesNotThrowException() {
        when(roomRepository.getRooms()).thenReturn(rooms);
        List<AbstractRoom> expected = rooms.stream().sorted().toList();

        assertEquals(expected, sut.roomsByStars());

        verify(roomRepository, times(1)).getRooms();
    }

    @Test
    @SneakyThrows
    void roomsByPriceDoesNotThrowException() {
        when(roomRepository.getRooms()).thenReturn(rooms);
        List<AbstractRoom> expected = rooms.stream()
                .sorted(new RoomPriceComparator())
                .toList();

        assertEquals(expected, sut.roomsByPrice());

        verify(roomRepository, times(1)).getRooms();
    }

    @Test
    @SneakyThrows
    void roomsByCapacityDoesNotThrowException() {
        when(roomRepository.getRooms()).thenReturn(rooms);
        List<AbstractRoom> expected = rooms.stream()
                .sorted(new RoomCapacityComparator())
                .toList();

        assertEquals(expected, sut.roomsByCapacity());

        verify(roomRepository, times(1)).getRooms();
    }

    @Test
    @SneakyThrows
    void availableRoomsByStars() {
        when(roomRepository.getRooms()).thenReturn(rooms);
        List<AbstractRoom> expected = rooms.stream()
                .filter(room -> room.getStatus() == RoomStatusTypes.AVAILABLE)
                .sorted()
                .toList();

        assertEquals(expected, sut.availableRoomsByStars());

        verify(roomRepository, times(1)).getRooms();
    }

    @Test
    @SneakyThrows
    void availableRoomsByPriceDoesNotThrowException() {
        when(roomRepository.getRooms()).thenReturn(rooms);
        List<AbstractRoom> expected = rooms.stream()
                .filter(room -> room.getStatus() == RoomStatusTypes.AVAILABLE)
                .sorted(new RoomPriceComparator())
                .toList();

        assertEquals(expected, sut.availableRoomsByPrice());

        verify(roomRepository, times(1)).getRooms();
    }

    @Test
    @SneakyThrows
    void availableRoomsByCapacityDoesNotThrowException() {
        when(roomRepository.getRooms()).thenReturn(rooms);
        List<AbstractRoom> expected = rooms.stream()
                .filter(room -> room.getStatus() == RoomStatusTypes.AVAILABLE)
                .sorted(new RoomCapacityComparator())
                .toList();

        assertEquals(expected, sut.availableRoomsByCapacity());

        verify(roomRepository, times(1)).getRooms();
    }

    @Test
    @SneakyThrows
    void countAvailableRoomsGivesCurrentData() {
        when(roomRepository.getRooms()).thenReturn(rooms);
        int count = 4;

        assertEquals(count, sut.countAvailableRooms());

        verify(roomRepository, times(1)).getRooms();
    }

    @Test
    @SneakyThrows
    void getRoomLastClientGivesCurrentDataWithNormalData() {
        when(roomReservationRepository.getReservations()).thenReturn(reservations);
        List<AbstractClient> expected = List.of(clients.get(0), clients.get(1));

        assertEquals(expected, sut.getRoomLastClients(rooms.getFirst(), 2));

        verify(roomReservationRepository, times(2)).getReservations();
    }

    @Test
    @SneakyThrows
    void getRoomLastClientGivesCurrentDataWithIncorrectData() {
        when(roomReservationRepository.getReservations()).thenReturn(reservations);
        List<AbstractClient> expected = List.of(clients.getFirst());

        assertEquals(expected, sut.getRoomLastClients(rooms.getFirst(), 0));

        verify(roomReservationRepository, times(2)).getReservations();
    }

    @Test
    @SneakyThrows
    void getClientRoomsByNumbersDoesNotThrowException() {
        when(roomReservationRepository.getReservations()).thenReturn(reservations);
        List<AbstractRoom> expected = List.of(rooms.getFirst());

        assertEquals(expected, sut.getClientRoomsByNumbers(clients.getFirst()));

        verify(roomReservationRepository, times(1)).getReservations();
    }

    @Test
    @SneakyThrows
    void getClientRoomsByCheckOutTimeDoesNotThrowException() {
        when(roomReservationRepository.getReservations()).thenReturn(reservations);
        List<AbstractRoom> expected = List.of(rooms.getFirst());

        assertEquals(expected, sut.getClientRoomsByCheckOutTime(clients.getFirst()));

        verify(roomReservationRepository, times(1)).getReservations();
    }

    @Test
    @SneakyThrows
    void getAvailableRoomsByTimeWithLocalDateTimeGivesCurrentData() {
        when(roomRepository.getRooms()).thenReturn(rooms);
        List<AbstractRoom> expected = rooms;

        assertEquals(expected, sut.getAvailableRoomsByTime(LocalDateTime.now()));

        verify(roomRepository, times(1)).getRooms();
    }

    @Test
    @SneakyThrows
    void getAvailableRoomsByTimeWithStringTimeGivesCurrentData() {
        when(roomRepository.getRooms()).thenReturn(rooms);
        List<AbstractRoom> expected = rooms;

        assertEquals(expected, sut.getAvailableRoomsByTime("2024-05-04-15-53-00"));

        verify(roomRepository, times(1)).getRooms();
    }

    @Test
    @SneakyThrows
    void getAvailableRoomsByTimeWithStringTimeThrowsDateTimeParseException() {
        when(roomRepository.getRooms()).thenReturn(rooms);

        assertThrows(DateTimeParseException.class, () -> sut.getAvailableRoomsByTime("04-05-2024-15-53-00"));

        verify(roomRepository, times(0)).getRooms();
    }

    @SneakyThrows
    private void changeSetStatusRoom() {
        for (Field filed : sut.getClass().getDeclaredFields()) {
            filed.setAccessible(true);
            if (filed.getName().equals("allowSetStatusRoom")) {
                filed.set(sut, "true");
            }
        }
    }
}
