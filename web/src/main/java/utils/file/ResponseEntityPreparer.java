package utils.file;

import essence.Identifiable;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import service.ClientService;
import service.RoomService;
import service.ServiceService;
import utils.exceptions.TechnicalException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Утилитный класс для подготовки объектов {@link ResponseEntity<Resource>} с данными, экспортированными в CSV.
 */
public final class ResponseEntityPreparer {
    private ResponseEntityPreparer() {
    }

    /**
     * Подготавливает объект {@link ResponseEntity<Resource>} для ответа с данными, экспортированными в CSV.
     * @param fileName Имя файла для вложения.
     * @param entities Список дто-сущностей для экспорта в CSV.
     * @param roomService Сервис комнат.
     * @param serviceService Сервис услуг.
     * @param clientService Сервис клиентов.
     * @return Объект {@link ResponseEntity<Resource>}, готовый для отправки в ответ на запрос.
     * @throws SQLException Если возникает ошибка SQL.
     * @throws IOException Если возникает ошибка ввода-вывода.
     * @throws TechnicalException Если нет обработки для класса предоставленных сущностей.
     */
    public static <T extends Identifiable> ResponseEntity<Resource> prepareResponseEntity(String fileName,
                                                                                          List<T> entities,
                                                                                          RoomService roomService,
                                                                                          ServiceService serviceService,
                                                                                          ClientService clientService)
            throws SQLException, IOException {
        ByteArrayResource resource = new ByteArrayResource(
                ExportCSV.exportEntitiesDtoDataToBytes(entities, roomService, clientService, serviceService));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }
}
