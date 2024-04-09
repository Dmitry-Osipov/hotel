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

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public final class ResponseEntityPreparer {
    private ResponseEntityPreparer() {
    }

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
