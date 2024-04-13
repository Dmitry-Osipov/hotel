package web.controller;

import dto.RoomDto;
import dto.RoomReservationDto;
import essence.room.AbstractRoom;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import utils.DtoConverter;
import utils.file.ImportCSV;
import utils.file.ResponseEntityPreparer;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Контроллер для работы с комнатами через REST API.
 */
@RestController
@RequestMapping("${api.path}/rooms")
@Validated
public class RestRoomController {
    private final RoomService roomService;
    private final ClientService clientService;
    private final ResponseEntityPreparer responseEntityPreparer;

    @Autowired
    public RestRoomController(RoomService roomService, ClientService clientService,
                              ResponseEntityPreparer responseEntityPreparer) {
        this.roomService = roomService;
        this.clientService = clientService;
        this.responseEntityPreparer = responseEntityPreparer;
    }

    /**
     * Получает список всех номеров.
     * @return {@link ResponseEntity} с кодом 200 (OK) и списком всех номеров в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping
    public ResponseEntity<List<RoomDto>> getAllRooms() throws SQLException {
        return ResponseEntity.ok().body(DtoConverter.listRoomsToDtos(roomService.getRooms()));
    }

    /**
     * Получает информацию о номере по его идентификатору.
     * @param id идентификатор номера.
     * @return {@link ResponseEntity} с кодом 200 (OK) и найденным номером в теле ответа, если идентификатор больше 0,
     * или кодом 400 (Bad Request) в случае некорректного идентификатора.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable("id") int id) throws SQLException {
        return ResponseEntity.ok().body(DtoConverter.convertRoomToDto(roomService.getRoomById(id)));
    }

    /**
     * Добавляет новый номер.
     * @param dto данные нового номера.
     * @return {@link ResponseEntity} с кодом 200 (OK) и добавленным номером в теле ответа, если данные корректны,
     * или кодом 400 (Bad Request) в случае некорректных данных.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @PostMapping
    public ResponseEntity<String> addRoom(@Valid @RequestBody RoomDto dto)
            throws SQLException {
        roomService.addRoom(DtoConverter.convertDtoToRoom(dto));
        return ResponseEntity.ok().body("Room " + dto.getNumber() + " added successfully");
    }

    /**
     * Обновляет информацию о номере.
     * @param dto данные обновляемого номера.
     * @return {@link ResponseEntity} с кодом 200 (OK) и обновленным номером в теле ответа, если данные корректны,
     * или кодом 400 (Bad Request) в случае некорректных данных.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @PutMapping
    public ResponseEntity<RoomDto> updateRoom(@Valid @RequestBody RoomDto dto) throws SQLException {
        roomService.updateRoom(DtoConverter.convertDtoToRoom(dto));
        return ResponseEntity.ok().body(dto);
    }

    /**
     * Удаляет номер по его идентификатору.
     * @param id идентификатор удаляемого номера.
     * @return {@link ResponseEntity} с кодом 200 (OK) и сообщением об успешном удалении номера в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable("id") int id) throws SQLException {
        roomService.deleteRoom(roomService.getRoomById(id));
        return ResponseEntity.ok().body("Deleted room with ID = " + id);
    }

    /**
     * Экспортирует данные о номерах в формате CSV.
     * @return {@link ResponseEntity} с кодом 200 (OK) и файлом CSV в теле ответа.
     * @throws IOException  если возникает ошибка ввода-вывода при экспорте данных.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping("/room-file")
    public ResponseEntity<Resource> exportRoomsToCsv() throws IOException, SQLException {
        return responseEntityPreparer.prepareResponseEntity("rooms.csv",
                DtoConverter.listRoomsToDtos(roomService.getRooms()));
    }

    /**
     * Импортирует данные о номерах из файла CSV.
     * @param file файл CSV с данными о номерах.
     * @return {@link ResponseEntity} с кодом 200 (OK) и сообщением об успешном импорте данных в теле ответа.
     * @throws IOException  если возникает ошибка ввода-вывода при чтении файла.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @PostMapping("/room-file")
    public ResponseEntity<String> importRoomsFromCsv(@RequestParam("file") MultipartFile file) throws IOException,
            SQLException {
        roomService.importRooms(DtoConverter.listRoomsFromDtos(ImportCSV.parseEntityDtoFromCsv(file, RoomDto.class)));
        return ResponseEntity.ok().body("Rooms imported successfully");
    }

    /**
     * Получает стоимость номера.
     * @param id идентификатор номера.
     * @return {@link ResponseEntity} с кодом 200 (OK) и стоимостью номера в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping("/room-price/{id}")
    public ResponseEntity<Integer> countPriceService(@PathVariable("id") int id) throws SQLException {
        return ResponseEntity.ok().body(roomService.getFavorPrice(id));
    }

    /**
     * Экспортирует данные о бронированиях номеров в формате CSV.
     * @return {@link ResponseEntity} с кодом 200 (OK) и файлом CSV в теле ответа.
     * @throws IOException  если возникает ошибка ввода-вывода при экспорте данных.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping("/reservation-file")
    public ResponseEntity<Resource> exportRoomReservationsToCsv() throws IOException, SQLException {
        return responseEntityPreparer.prepareResponseEntity("reservations.csv",
                DtoConverter.listReservationsToDtos(roomService.getReservations()));
    }

    /**
     * Импортирует данные о бронированиях номеров из файла CSV.
     * @param file файл CSV с данными о бронированиях номеров.
     * @return {@link ResponseEntity} с кодом 200 (OK) и сообщением об успешном импорте данных в теле ответа.
     * @throws IOException  если возникает ошибка ввода-вывода при чтении файла.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @PostMapping("/reservation-file")
    public ResponseEntity<String> importRoomReservationsFromCsv(@RequestParam("file") MultipartFile file)
            throws IOException, SQLException {
        roomService.importReservations(DtoConverter.listReservationsFromDtos(
                ImportCSV.parseEntityDtoFromCsv(file, RoomReservationDto.class), roomService, clientService));
        return ResponseEntity.ok().body("Room reservations imported successfully");
    }

    /**
     * Добавляет звезды к номеру.
     * @param dto данные номера.
     * @return {@link ResponseEntity} с кодом 200 (OK) и обновленным объектом RoomDto в теле ответа, если данные
     * корректны, или кодом 400 (Bad Request) в случае некорректных данных.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @PostMapping("/room-stars")
    public ResponseEntity<RoomDto> addStars(@Valid @RequestBody RoomDto dto) throws SQLException {
        AbstractRoom room = roomService.getRoomById(dto.getId());
        roomService.addStarsToRoom(room, dto.getStars() - room.getStars());
        return ResponseEntity.ok().body(dto);
    }

    /**
     * Регистрирует заезд клиентов в номер.
     * @param dto данные бронирования номера.
     * @return {@link ResponseEntity} с кодом 200 (OK) и объектом RoomReservationDto в теле ответа, если данные
     * корректны, или кодом 400 (Bad Request) в случае некорректных данных.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @PostMapping("/check-in")
    public ResponseEntity<String> checkIn(@Valid @RequestBody RoomReservationDto dto) throws SQLException {
        roomService.checkIn(DtoConverter.convertDtoToRoomReservation(dto, roomService, clientService));
        return ResponseEntity.ok().body("Room checked in successfully");
    }

    /**
     * Выселяет клиентов из номера.
     * @param dto данные бронирования номера.
     * @return {@link ResponseEntity} с кодом 200 (OK) и объектом RoomReservationDto в теле ответа, если данные
     * корректны, или кодом 400 (Bad Request) в случае некорректных данных.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @PutMapping("/eviction")
    public ResponseEntity<RoomReservationDto> evict(@Valid @RequestBody RoomReservationDto dto) throws SQLException {
        roomService.evict(DtoConverter.convertDtoToRoomReservation(dto, roomService, clientService));
        return ResponseEntity.ok().body(dto);
    }

    /**
     * Получает список номеров отсортированных по количеству звезд.
     * @return {@link ResponseEntity} с кодом 200 (OK) и списком номеров в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping("/rooms-by-stars")
    public ResponseEntity<List<RoomDto>> getRoomsByStars() throws SQLException {
        return ResponseEntity.ok().body(DtoConverter.listRoomsToDtos(roomService.roomsByStars()));
    }

    /**
     * Получает список номеров отсортированных по цене.
     * @return {@link ResponseEntity} с кодом 200 (OK) и списком номеров в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping("/rooms-by-price")
    public ResponseEntity<List<RoomDto>> getRoomsByPrice() throws SQLException {
        return ResponseEntity.ok().body(DtoConverter.listRoomsToDtos(roomService.roomsByPrice()));
    }

    /**
     * Получает список номеров отсортированных по вместимости.
     * @return {@link ResponseEntity} с кодом 200 (OK) и списком номеров в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping("/rooms-by-capacity")
    public ResponseEntity<List<RoomDto>> getRoomsByCapacity() throws SQLException {
        return ResponseEntity.ok().body(DtoConverter.listRoomsToDtos(roomService.roomsByCapacity()));
    }

    /**
     * Получает список доступных номеров отсортированных по количеству звезд.
     * @return {@link ResponseEntity} с кодом 200 (OK) и списком номеров в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping("/available-rooms-by-stars")
    public ResponseEntity<List<RoomDto>> getAvailableRoomsByStars() throws SQLException {
        return ResponseEntity.ok().body(DtoConverter.listRoomsToDtos(roomService.availableRoomsByStars()));
    }

    /**
     * Получает список доступных номеров отсортированных по цене.
     * @return {@link ResponseEntity} с кодом 200 (OK) и списком номеров в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping("/available-rooms-by-price")
    public ResponseEntity<List<RoomDto>> getAvailableRoomsByPrice() throws SQLException {
        return ResponseEntity.ok().body(DtoConverter.listRoomsToDtos(roomService.availableRoomsByPrice()));
    }

    /**
     * Получает список доступных номеров отсортированных по вместимости.
     * @return {@link ResponseEntity} с кодом 200 (OK) и списком номеров в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping("/available-rooms-by-capacity")
    public ResponseEntity<List<RoomDto>> getAvailableRoomsByCapacity() throws SQLException {
        return ResponseEntity.ok().body(DtoConverter.listRoomsToDtos(roomService.availableRoomsByCapacity()));
    }

    /**
     * Получает количество доступных номеров.
     * @return {@link ResponseEntity} с кодом 200 (OK) и числом доступных номеров в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping("/number-available-rooms")
    public ResponseEntity<Integer> countAvailableRooms() throws SQLException {
        return ResponseEntity.ok().body(roomService.countAvailableRooms());
    }

    /**
     * Получает информацию о номере по его идентификатору.
     * @param id идентификатор номера.
     * @return {@link ResponseEntity} с кодом 200 (OK) и информацией о номере в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping("/room-info/{id}")
    public ResponseEntity<String> getRoomInfo(@PathVariable("id") int id) throws SQLException {
        return ResponseEntity.ok().body(roomService.getRoomInfo(roomService.getRoomById(id)));
    }

    /**
     * Получает список номеров клиента, отсортированных по номерам.
     * @param id идентификатор клиента.
     * @return {@link ResponseEntity} с кодом 200 (OK) и списком номеров клиента в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping("/client-rooms-by-numbers/{id}")
    public ResponseEntity<List<RoomDto>> getClientRoomsByNumbers(@PathVariable("id") int id) throws SQLException {
        return ResponseEntity.ok()
                .body(DtoConverter.listRoomsToDtos(roomService.getClientRoomsByNumbers(clientService.getClientById(id))));
    }

    /**
     * Получает список номеров клиента, отсортированных по времени выезда.
     * @param id идентификатор клиента.
     * @return {@link ResponseEntity} с кодом 200 (OK) и списком номеров в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     */
    @GetMapping("/client-rooms-by-check-out-time/{id}")
    public ResponseEntity<List<RoomDto>> getClientRoomsByCheckOutTime(@PathVariable("id") int id) throws SQLException {
        return ResponseEntity.ok()
                .body(DtoConverter.listRoomsToDtos(
                        roomService.getClientRoomsByCheckOutTime(clientService.getClientById(id))));
    }

    /**
     * Получает список доступных номеров к указанному времени.
     * @param time строка времени в формате ГГГГ-ММ-ДД-чч-мм-сс, например: 2024-04-12-15-30-00.
     * @return {@link ResponseEntity} с кодом 200 (OK) и списком доступных номеров в теле ответа.
     * @throws SQLException если возникает ошибка при выполнении запроса к базе данных.
     * @throws DateTimeParseException если строка времени имеет неправильный формат.
     */
    @GetMapping("/available-rooms-by-time/{time}")
    public ResponseEntity<List<RoomDto>> getAvailableRoomsByTime(@PathVariable("time") String time)
            throws SQLException {
        return ResponseEntity.ok().body(DtoConverter.listRoomsToDtos(roomService.getAvailableRoomsByTime(time)));
    }
}
