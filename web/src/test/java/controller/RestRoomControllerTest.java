package controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import config.TestConfig;
import dto.RoomDto;
import dto.RoomReservationDto;
import essence.person.AbstractClient;
import essence.person.Client;
import essence.reservation.RoomReservation;
import essence.room.AbstractRoom;
import essence.room.Room;
import essence.room.RoomStatusTypes;
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
import service.RoomService;
import utils.DtoConverter;
import utils.comparators.RoomCapacityComparator;
import utils.comparators.RoomCheckOutTimeComparator;
import utils.comparators.RoomNumberComparator;
import utils.comparators.RoomPriceComparator;
import utils.exceptions.InvalidDataException;
import utils.exceptions.TechnicalException;
import utils.file.ResponseEntityPreparer;
import web.controller.RestRoomController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
class RestRoomControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private List<AbstractClient> clients;
    private List<AbstractRoom> rooms;
    private List<RoomReservation> reservations;
    private MockMvc sut;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ResponseEntityPreparer responsePreparer;
    @Autowired
    private RoomService roomService;

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

        sut = MockMvcBuilders.standaloneSetup(
                new RestRoomController(roomService, clientService, responsePreparer)).build();
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        clients = null;
        rooms = null;
        reservations = null;
        sut = null;
    }

    @Test
    @SneakyThrows
    void getAllRoomsDoesNotThrowException() {
        when(roomService.getRooms()).thenReturn(rooms);
        List<RoomDto> expected = DtoConverter.listRoomsToDtos(rooms);
        String jsonResponse = sut.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<RoomDto> actual = objectMapper.readValue(jsonResponse, new TypeReference<>() {});

        assertEquals(expected, actual);

        verify(roomService, times(1)).getRooms();
    }

    @Test
    @SneakyThrows
    void getAllRoomsThrowsServletException() {
        doThrow(TechnicalException.class).when(roomService).getRooms();

        assertThrows(ServletException.class,
                () -> sut.perform(get("/api/rooms")).andExpect(status().isInternalServerError()));

        verify(roomService, times(1)).getRooms();
    }

    @Test
    @SneakyThrows
    void getRoomByIdDoesNotThrowException() {
        int id = 1;
        AbstractRoom room = rooms.get(id - 1);
        when(roomService.getRoomById(id)).thenReturn(room);
        RoomDto expected = DtoConverter.convertRoomToDto(room);
        String jsonResponse = sut.perform(get("/api/rooms/{id}", id))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        RoomDto actual = objectMapper.readValue(jsonResponse, RoomDto.class);

        assertEquals(expected, actual);

        verify(roomService, times(1)).getRoomById(anyInt());
    }

    @Test
    @SneakyThrows
    void getRoomByIdThrowsServletException() {
        int id = 0;
        doThrow(TechnicalException.class).when(roomService).getRoomById(anyInt());

        assertThrows(ServletException.class, () ->
                sut.perform(get("/api/rooms/{id}", id)).andExpect(status().isNotFound()));

        verify(roomService, times(1)).getRoomById(anyInt());
    }

    @Test
    @SneakyThrows
    void addRoomDoesNotThrowException() {
        RoomDto dto =
                DtoConverter.convertRoomToDto(new Room(7, 7, 2, 5000));

        assertDoesNotThrow(() -> sut.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Room " + dto.getNumber() + " added successfully")));

        verify(roomService, times(1)).addRoom(any());
    }

    @Test
    @SneakyThrows
    void addRoomThrowsServletException() {
        RoomDto dto =
                DtoConverter.convertRoomToDto(rooms.getFirst());
        doThrow(TechnicalException.class).when(roomService).addRoom(any());

        assertThrows(ServletException.class, () -> sut.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError()));

        verify(roomService, times(1)).addRoom(any());
    }

    @Test
    @SneakyThrows
    void updateRoomDoesNotThrowException() {
        RoomDto dto =
                DtoConverter.convertRoomToDto(new Room(1, 1, 2, 15000));
        String jsonResponse = sut.perform(put("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        RoomDto actual = objectMapper.readValue(jsonResponse, RoomDto.class);

        assertEquals(dto, actual);

        verify(roomService, times(1)).updateRoom(any());
    }

    @Test
    @SneakyThrows
    void updateRoomThrowsServletException() {
        RoomDto dto =
                DtoConverter.convertRoomToDto(new Room(10, 1, 2, 15000));
        doThrow(TechnicalException.class).when(roomService).updateRoom(any());

        assertThrows(ServletException.class, () -> sut.perform(put("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError()));

        verify(roomService, times(1)).updateRoom(any());
    }

    @Test
    @SneakyThrows
    void deleteRoomDoesNotThrowException() {
        int id = 2;
        AbstractRoom room = rooms.get(id - 1);
        when(roomService.getRoomById(id)).thenReturn(room);

        assertDoesNotThrow(() -> sut.perform(delete("/api/rooms/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted room with ID = " + id)));

        verify(roomService, times(1)).deleteRoom(any());
        verify(roomService, times(1)).getRoomById(anyInt());
    }

    @Test
    @SneakyThrows
    void deleteRoomThrowsServletException() {
        int id = 0;
        doThrow(TechnicalException.class).when(roomService).deleteRoom(any());

        assertThrows(ServletException.class, () -> sut.perform(delete("/api/rooms/{id}", id))
                .andExpect(status().isInternalServerError()));

        verify(roomService, times(1)).deleteRoom(any());
    }

    @Test
    @SneakyThrows
    void exportRoomsDoesNotThrowException() {
        Resource resource = new ByteArrayResource("CSV data".getBytes());
        when(responsePreparer.prepareResponseEntity(anyString(), anyList())).thenReturn(ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=rooms.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource));

        assertDoesNotThrow(() -> sut.perform(get("/api/rooms/room-file"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=rooms.csv"))
                .andExpect(content().contentType("text/csv"))
                .andExpect(content().string("CSV data")));

        verify(responsePreparer, times(1)).prepareResponseEntity(anyString(), anyList());
    }

    @Test
    @SneakyThrows
    void importRoomsDoesNotThrowException() {
        MockMultipartFile file = new MockMultipartFile("file", "rooms.csv",
                MediaType.TEXT_PLAIN_VALUE, "CSV data".getBytes());

        assertDoesNotThrow(() -> sut.perform(multipart("/api/rooms/room-file").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Rooms imported successfully")));

        verify(roomService, times(1)).importRooms(anyList());
    }

    @Test
    @SneakyThrows
    void countPriceRoomDoesNotThrowException() {
        int id = 1;
        when(roomService.getFavorPrice(id)).thenReturn(rooms.get(id - 1).getPrice());
        int expected = 3000;
        String jsonResponse = sut.perform(get("/api/rooms/room-price/{id}", id))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        int actual = objectMapper.readValue(jsonResponse, Integer.class);

        assertEquals(expected, actual);

        verify(roomService, times(1)).getFavorPrice(anyInt());
    }

    @Test
    @SneakyThrows
    void countPriceRoomThrowsServletException() {
        int id = 10;
        doThrow(TechnicalException.class).when(roomService).getFavorPrice(anyInt());

        assertThrows(ServletException.class,
                () -> sut.perform(get("/api/rooms/room-price/{id}", id))
                        .andExpect(status().isNotFound()));

        verify(roomService, times(1)).getFavorPrice(anyInt());
    }

    @Test
    @SneakyThrows
    void exportReservationsDoesNotThrowException() {
        Resource resource = new ByteArrayResource("CSV data".getBytes());
        when(responsePreparer.prepareResponseEntity(anyString(), anyList())).thenReturn(ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reservations.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource));

        assertDoesNotThrow(() -> sut.perform(get("/api/rooms/reservation-file"))
                .andExpect(status().isOk())
                .andExpect(header()
                        .string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reservations.csv"))
                .andExpect(content().contentType("text/csv"))
                .andExpect(content().string("CSV data")));

        verify(responsePreparer, times(1)).prepareResponseEntity(anyString(), anyList());
    }

    @Test
    @SneakyThrows
    void importReservationsDoesNotThrowException() {
        MockMultipartFile file = new MockMultipartFile("file", "reservations.csv",
                MediaType.TEXT_PLAIN_VALUE, "CSV data".getBytes());

        assertDoesNotThrow(() -> sut.perform(multipart("/api/rooms/reservation-file").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Room reservations imported successfully")));

        verify(roomService, times(1)).importReservations(anyList());
    }

    @Test
    @SneakyThrows
    void addStarsDoesNotThrowException() {
        int id = 1;
        AbstractRoom room = rooms.get(id - 1);
        RoomDto dto = DtoConverter.convertRoomToDto(room);
        when(roomService.getRoomById(id)).thenReturn(room);
        String jsoResponse = sut.perform(post("/api/rooms/room-stars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        RoomDto actual = objectMapper.readValue(jsoResponse, RoomDto.class);

        assertEquals(dto.getStars(), actual.getStars());

        verify(roomService, times(1)).getRoomById(anyInt());
        verify(roomService, times(1)).addStarsToRoom(any(), anyInt());
    }

    @Test
    @SneakyThrows
    void addStarsThrowsServletException() {
        int id = 1;
        AbstractRoom room = rooms.get(id - 1);
        RoomDto dto = DtoConverter.convertRoomToDto(room);
        doThrow(InvalidDataException.class).when(roomService).getRoomById(anyInt());

        assertThrows(ServletException.class,
                () -> sut.perform(post("/api/rooms/room-stars")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                        .andExpect(status().isInternalServerError()));

        verify(roomService, times(1)).getRoomById(anyInt());
    }

    @Test
    @SneakyThrows
    void checkInDoesNotThrowException() {
        RoomReservationDto dto =
                DtoConverter.convertRoomReservationToDto(
                        new RoomReservation(3, rooms.get(2), LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                                List.of(clients.get(2))));

        assertDoesNotThrow(() -> sut.perform(post("/api/rooms/check-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Room checked in successfully")));

        verify(roomService, times(1)).checkIn(any());
    }

    @Test
    @SneakyThrows
    void checkInThrowsServletException() {
        RoomReservationDto dto = DtoConverter.convertRoomReservationToDto(reservations.getFirst());
        doThrow(TechnicalException.class).when(roomService).checkIn(any());

        assertThrows(ServletException.class, () -> sut.perform(post("/api/rooms/check-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError()));

        verify(roomService, times(1)).checkIn(any());
    }

    @Test
    @SneakyThrows
    void evictDoesNotThrowException() {
        RoomReservationDto dto = DtoConverter.convertRoomReservationToDto(reservations.getFirst());
        String expected = objectMapper.writeValueAsString(dto);
        String jsonResponse = sut.perform(put("/api/rooms/eviction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(expected, jsonResponse);

        verify(roomService, times(1)).evict(any());
    }

    @Test
    @SneakyThrows
    void evictThrowsServletException() {
        RoomReservationDto dto = DtoConverter.convertRoomReservationToDto(reservations.getFirst());
        doThrow(TechnicalException.class).when(roomService).evict(any());

        assertThrows(ServletException.class, () -> sut.perform(put("/api/rooms/eviction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError()));

        verify(roomService, times(1)).evict(any());
    }

    @Test
    @SneakyThrows
    void getRoomsByStarsDoesNotThrowException() {
        List<AbstractRoom> expected = rooms.stream()
                .sorted()
                .toList();
        when(roomService.roomsByStars()).thenReturn(expected);
        String jsonResponse = sut.perform(get("/api/rooms/rooms-by-stars"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expected), jsonResponse);

        verify(roomService, times(1)).roomsByStars();
    }

    @Test
    @SneakyThrows
    void getRoomsByPriceDoesNotThrowException() {
        List<AbstractRoom> expected = rooms.stream()
                .sorted(new RoomPriceComparator())
                .toList();
        when(roomService.roomsByPrice()).thenReturn(expected);
        String jsonResponse = sut.perform(get("/api/rooms/rooms-by-price"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expected), jsonResponse);

        verify(roomService, times(1)).roomsByPrice();
    }

    @Test
    @SneakyThrows
    void getRoomsByCapacityDoesNotThrowException() {
        List<AbstractRoom> expected = rooms.stream()
                .sorted(new RoomCapacityComparator())
                .toList();
        when(roomService.roomsByCapacity()).thenReturn(expected);
        String jsonResponse = sut.perform(get("/api/rooms/rooms-by-capacity"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expected), jsonResponse);

        verify(roomService, times(1)).roomsByCapacity();
    }

    @Test
    @SneakyThrows
    void getAvailableRoomsByStarsDoesNotThrowException() {
        List<AbstractRoom> expected = rooms.stream()
                .filter(o -> o.getStatus() == RoomStatusTypes.AVAILABLE)
                .sorted()
                .toList();
        when(roomService.availableRoomsByStars()).thenReturn(expected);
        String jsonResponse = sut.perform(get("/api/rooms/available-rooms-by-stars"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expected), jsonResponse);

        verify(roomService, times(1)).availableRoomsByStars();
    }

    @Test
    @SneakyThrows
    void getAvailableRoomsByPriceDoesNotThrowException() {
        List<AbstractRoom> expected = rooms.stream()
                .filter(o -> o.getStatus() == RoomStatusTypes.AVAILABLE)
                .sorted()
                .toList();
        when(roomService.availableRoomsByPrice()).thenReturn(expected);
        String jsonResponse = sut.perform(get("/api/rooms/available-rooms-by-price"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expected), jsonResponse);

        verify(roomService, times(1)).availableRoomsByPrice();
    }

    @Test
    @SneakyThrows
    void getAvailableRoomsByCapacityDoesNotThrowException() {
        List<AbstractRoom> expected = rooms.stream()
                .filter(o -> o.getStatus() == RoomStatusTypes.AVAILABLE)
                .sorted()
                .toList();
        when(roomService.availableRoomsByCapacity()).thenReturn(expected);
        String jsonResponse = sut.perform(get("/api/rooms/available-rooms-by-capacity"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expected), jsonResponse);

        verify(roomService, times(1)).availableRoomsByCapacity();
    }

    @Test
    @SneakyThrows
    void countAvailableRoomsDoesNotThrowException() {
        int expected = rooms.stream()
                .filter(o -> o.getStatus() == RoomStatusTypes.AVAILABLE)
                .sorted()
                .toList().size();
        when(roomService.countAvailableRooms()).thenReturn(expected);
        String jsonResponse = sut.perform(get("/api/rooms/number-available-rooms"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expected), jsonResponse);

        verify(roomService, times(1)).countAvailableRooms();
    }

    @Test
    @SneakyThrows
    void getClientRoomsByNumberDoesNotThrowException() {
        int id = 1;
        AbstractClient client = clients.get(id - 1);
        List<AbstractRoom> expected = reservations
                .stream()
                .filter(reservation -> reservation.getClients().contains(client))
                .sorted(new RoomNumberComparator())
                .map(RoomReservation::getRoom)
                .toList();
        when(clientService.getClientById(id)).thenReturn(client);
        when(roomService.getClientRoomsByNumbers(client)).thenReturn(expected);
        String jsonResponse = sut.perform(get("/api/rooms/client-rooms-by-numbers/{id}", id))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expected), jsonResponse);

        verify(clientService, times(1)).getClientById(anyInt());
        verify(roomService, times(1)).getClientRoomsByNumbers(any());
    }

    @Test
    @SneakyThrows
    void getClientRoomsByNumberThrowsServletException() {
        int id = 0;
        doThrow(TechnicalException.class).when(roomService).getClientRoomsByNumbers(any());

        assertThrows(ServletException.class,
                () -> sut.perform(get("/api/rooms/client-rooms-by-numbers/{id}", id))
                        .andExpect(status().isNotFound()));

        verify(roomService, times(1)).getClientRoomsByNumbers(any());
    }

    @Test
    @SneakyThrows
    void getClientRoomsByCheckOutTimeDoesNotThrowException() {
        int id = 1;
        AbstractClient client = clients.get(id - 1);
        List<AbstractRoom> expected = reservations
                .stream()
                .filter(reservation -> reservation.getClients().contains(client))
                .sorted(new RoomCheckOutTimeComparator())
                .map(RoomReservation::getRoom)
                .toList();
        when(clientService.getClientById(id)).thenReturn(client);
        when(roomService.getClientRoomsByCheckOutTime(client)).thenReturn(expected);
        String jsonResponse = sut.perform(get("/api/rooms/client-rooms-by-check-out-time/{id}", id))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expected), jsonResponse);

        verify(clientService, times(1)).getClientById(anyInt());
        verify(roomService, times(1)).getClientRoomsByCheckOutTime(any());
    }

    @Test
    @SneakyThrows
    void getClientRoomsByCheckOutTimeThrowsServletException() {
        int id = 0;
        doThrow(TechnicalException.class).when(roomService).getClientRoomsByCheckOutTime(any());

        assertThrows(ServletException.class,
                () -> sut.perform(get("/api/rooms/client-rooms-by-check-out-time/{id}", id))
                        .andExpect(status().isNotFound()));

        verify(roomService, times(1)).getClientRoomsByCheckOutTime(any());
    }

    @Test
    @SneakyThrows
    void getAvailableRoomsByTimeDoesNotThrowException() {
        String time = "2024-05-06-12-00-00";
        List<AbstractRoom> expected = rooms
                .stream()
                .filter(room -> room.getCheckOutTime() == null || room.getCheckOutTime().isAfter(
                        LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"))))
                .toList();
        when(roomService.getAvailableRoomsByTime(time)).thenReturn(expected);
        String jsonResponse = sut.perform(get("/api/rooms/available-rooms-by-time/{time}", time))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expected), jsonResponse);

        verify(roomService, times(1)).getAvailableRoomsByTime(anyString());
    }

    @Test
    @SneakyThrows
    void getAvailableRoomsByTimeThrowsServletException() {
        doThrow(DateTimeParseException.class).when(roomService).getAvailableRoomsByTime(anyString());

        assertThrows(ServletException.class,
                () -> sut.perform(get("/api/rooms/available-rooms-by-time/{time}", LocalDateTime.now()))
                        .andExpect(status().isInternalServerError()));

        verify(roomService, times(1)).getAvailableRoomsByTime(anyString());
    }
}
