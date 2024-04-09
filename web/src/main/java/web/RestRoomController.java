package web;

import dto.RoomDto;
import dto.RoomReservationDto;
import essence.person.AbstractClient;
import essence.reservation.RoomReservation;
import essence.room.AbstractRoom;
import essence.service.AbstractFavor;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
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
import utils.ListToArrayConverter;
import utils.file.ImportCSV;
import utils.file.ResponseEntityPreparer;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rooms")
public class RestRoomController {
    private final RoomService roomService;
    private final ServiceService serviceService;
    private final ClientService clientService;

    @Autowired
    public RestRoomController(RoomService roomService, ServiceService serviceService, ClientService clientService) {
        this.roomService = roomService;
        this.serviceService = serviceService;
        this.clientService = clientService;
    }

    @GetMapping("/allRooms")
    public ResponseEntity<List<RoomDto>> getAllRooms() throws SQLException {
        List<RoomDto> rooms = roomService.getRooms().stream()
                .map(DtoConverter::convertRoomToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(rooms);
    }

    @GetMapping("/getRoom/{id}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable("id") int id) throws SQLException {
        if (id < 1) {
            return ResponseEntity.badRequest().build();
        }

        RoomDto dto = DtoConverter.convertRoomToDto(roomService.getRoomById(id));
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/addRoom")
    public ResponseEntity<RoomDto> addRoom(@Valid @RequestBody RoomDto dto, BindingResult bindingResult)
            throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        AbstractRoom room = DtoConverter.convertDtoToRoom(dto);
        roomService.addRoom(room);
        List<AbstractRoom> rooms = roomService.getRooms();
        return ResponseEntity.ok().body(DtoConverter.convertRoomToDto(rooms.get(rooms.size() - 1)));
    }

    @PutMapping("/updateRoom")
    public ResponseEntity<RoomDto> updateRoom(@Valid @RequestBody RoomDto dto, BindingResult bindingResult)
            throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        boolean updated = roomService.updateRoom(DtoConverter.convertDtoToRoom(dto));
        if (!updated) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/deleteRoom/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable("id") int id) throws SQLException {
        AbstractRoom room = roomService.getRoomById(id);
        roomService.deleteRoom(room);
        return ResponseEntity.ok().body("Deleted room with ID = " + id);
    }

    @GetMapping("/exportRooms")
    public ResponseEntity<Resource> exportRoomsToCsv() throws IOException, SQLException {
        List<RoomDto> rooms = roomService.getRooms().stream()
                .map(DtoConverter::convertRoomToDto)
                .collect(Collectors.toList());
        return ResponseEntityPreparer.prepareResponseEntity("rooms.csv", rooms, roomService,
                serviceService, clientService);
    }

    @PostMapping("/importRooms")
    public ResponseEntity<String> importRoomsFromCsv(@RequestParam("file") MultipartFile file)
            throws IOException, SQLException {
        List<AbstractRoom> rooms = ImportCSV.parseEntityDtoFromCsv(file, RoomDto.class).stream()
                .map(DtoConverter::convertDtoToRoom)
                .collect(Collectors.toList());
        for (AbstractRoom room : rooms) {
            boolean updated = roomService.updateRoom(room);
            if (!updated) {
                roomService.addRoom(room);
            }
        }

        return ResponseEntity.ok().body("Rooms imported successfully");
    }

    @GetMapping("/countPriceRoom/{id}")
    public ResponseEntity<Integer> countPriceService(@PathVariable("id") int id) throws SQLException {
        int price = roomService.getFavorPrice((AbstractFavor) roomService.getRoomById(id));
        return ResponseEntity.ok().body(price);
    }

    @GetMapping("/exportRoomReservations")
    public ResponseEntity<Resource> exportRoomReservationsToCsv() throws IOException, SQLException {
        List<RoomReservationDto> roomReservations = roomService.getReservations().stream()
                .map(DtoConverter::convertRoomReservationToDto)
                .collect(Collectors.toList());
        return ResponseEntityPreparer.prepareResponseEntity("reservations.csv", roomReservations, roomService,
                serviceService, clientService);
    }

    @PostMapping("/importRoomReservations")
    public ResponseEntity<String> importRoomReservationsFromCsv(@RequestParam("file") MultipartFile file)
            throws IOException, SQLException {
        List<RoomReservationDto> dtos = ImportCSV.parseEntityDtoFromCsv(file, RoomReservationDto.class);
        List<RoomReservation> reservations = new ArrayList<>(dtos.size());

        for (RoomReservationDto dto : dtos) {
            RoomReservation reservation = DtoConverter.convertDtoToRoomReservation(dto, roomService, clientService);
            reservations.add(reservation);
        }

        for (RoomReservation reservation : reservations) {
            boolean updated = roomService.updateReservation(reservation);
            if (!updated) {
                roomService.addReservation(reservation);
            }
        }

        return ResponseEntity.ok().body("Room reservations imported successfully");
    }

    @PostMapping("/addStars")
    public ResponseEntity<RoomDto> addStars(@Valid @RequestBody RoomDto dto, BindingResult bindingResult)
            throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        roomService.addStarsToRoom(DtoConverter.convertDtoToRoom(dto), dto.getStars());
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/checkIn")
    public ResponseEntity<RoomReservationDto> checkIn(@Valid @RequestBody RoomReservationDto dto,
                                                      BindingResult bindingResult) throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        RoomReservation reservation = DtoConverter.convertDtoToRoomReservation(dto, roomService, clientService);
        AbstractClient[] clients = ListToArrayConverter.convertListToArray(reservation.getClients(),
                AbstractClient.class);
        roomService.checkIn(reservation.getRoom(), clients);
        List<RoomReservation> reservations = roomService.getReservations();
        return ResponseEntity.ok()
                .body(DtoConverter.convertRoomReservationToDto(reservations.get(reservations.size() - 1)));
    }

    @PutMapping("/evict")
    public ResponseEntity<RoomReservationDto> evict(@Valid @RequestBody RoomReservationDto dto,
                                                       BindingResult bindingResult) throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        RoomReservation reservation = DtoConverter.convertDtoToRoomReservation(dto, roomService, clientService);
        AbstractClient[] clients = ListToArrayConverter.convertListToArray(reservation.getClients(),
                AbstractClient.class);
        roomService.evict(reservation.getRoom(), clients);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/getRoomsByStars")
    public ResponseEntity<List<RoomDto>> getRoomsByStars() throws SQLException {
        List<RoomDto> rooms = roomService.roomsByStars().stream()
                .map(DtoConverter::convertRoomToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(rooms);
    }

    @GetMapping("/getRoomsByPrice")
    public ResponseEntity<List<RoomDto>> getRoomsByPrice() throws SQLException {
        List<RoomDto> rooms = roomService.roomsByPrice().stream()
                .map(DtoConverter::convertRoomToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(rooms);
    }

    @GetMapping("/getRoomsByCapacity")
    public ResponseEntity<List<RoomDto>> getRoomsByCapacity() throws SQLException {
        List<RoomDto> rooms = roomService.roomsByCapacity().stream()
                .map(DtoConverter::convertRoomToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(rooms);
    }

    @GetMapping("/getAvailableRoomsByStars")
    public ResponseEntity<List<RoomDto>> getAvailableRoomsByStars() throws SQLException {
        List<RoomDto> rooms = roomService.availableRoomsByStars().stream()
                .map(DtoConverter::convertRoomToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(rooms);
    }

    @GetMapping("/getAvailableRoomsByPrice")
    public ResponseEntity<List<RoomDto>> getAvailableRoomsByPrice() throws SQLException {
        List<RoomDto> rooms = roomService.availableRoomsByPrice().stream()
                .map(DtoConverter::convertRoomToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(rooms);
    }

    @GetMapping("/getAvailableRoomsByCapacity")
    public ResponseEntity<List<RoomDto>> getAvailableRoomsByCapacity() throws SQLException {
        List<RoomDto> rooms = roomService.availableRoomsByCapacity().stream()
                .map(DtoConverter::convertRoomToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(rooms);
    }

    @GetMapping("/countAvailableRooms")
    public ResponseEntity<Integer> countAvailableRooms() throws SQLException {
        return ResponseEntity.ok().body(roomService.countAvailableRooms());
    }

    @GetMapping("/getRoomInfo/{id}")
    public ResponseEntity<String> getRoomInfo(@PathVariable("id") int id) throws SQLException {
        return ResponseEntity.ok().body(roomService.getRoomInfo(roomService.getRoomById(id)));
    }

    @GetMapping("/getClientRoomsByNumbers/{id}")
    public ResponseEntity<List<RoomDto>> getClientRoomsByNumbers(@PathVariable("id") int id) throws SQLException {
        List<RoomDto> rooms = roomService.getClientRoomsByNumbers(clientService.getClientById(id)).stream()
                .map(DtoConverter::convertRoomToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(rooms);
    }

    @GetMapping("/getClientRoomsByCheckOutTime/{id}")
    public ResponseEntity<List<RoomDto>> getClientRoomsByCheckOutTime(@PathVariable("id") int id) throws SQLException {
        List<RoomDto> rooms = roomService.getClientRoomsByCheckOutTime(clientService.getClientById(id)).stream()
                .map(DtoConverter::convertRoomToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(rooms);
    }

    @GetMapping("/getAvailableRoomsByTime")
    public ResponseEntity<List<RoomDto>> getAvailableRoomsByTime(@RequestParam("time")
                                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                     LocalDateTime time) throws SQLException {
        if (time == null) {
            return ResponseEntity.badRequest().build();
        }

        List<RoomDto> rooms = roomService.getAvailableRoomsByTime(time).stream()
                .map(DtoConverter::convertRoomToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(rooms);
    }
}
