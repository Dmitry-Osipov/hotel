package repository.service;

import essence.provided.ProvidedService;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс отвечает за хранение списка оказанных услуг.
 */
@Getter
public class ProvidedServicesRepository {
    List<ProvidedService> services = new ArrayList<>();
}
