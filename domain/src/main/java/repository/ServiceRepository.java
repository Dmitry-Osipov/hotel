package repository;

import annotations.annotation.Component;
import essence.service.AbstractService;
import lombok.Getter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

/**
 * Класс отвечает за хранение услуг отеля. Реализован с использованием паттерна Singleton для обеспечения
 * единственного экземпляра репозитория услуг.
 */
@Component
@Getter
@ToString
public class ServiceRepository {
    private final Set<AbstractService> services = new HashSet<>();
}
