package repository.service;

import lombok.Getter;
import lombok.ToString;
import essence.service.AbstractService;

import java.util.HashSet;
import java.util.Set;

/**
 * Класс отвечает за хранение услуг отеля.
 */
@Getter
@ToString
public class ServiceRepository {
    private final Set<AbstractService> services = new HashSet<>();
}
