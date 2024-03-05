package repository;

import annotations.annotation.Component;
import essence.provided.ProvidedService;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс отвечает за хранение списка оказанных услуг. Реализован с использованием паттерна Singleton для обеспечения
 * единственного экземпляра репозитория оказанных услуг.
 */
@Component
@Getter
@ToString
public class ProvidedServicesRepository {
    private final List<ProvidedService> providedServices = new ArrayList<>();
}
