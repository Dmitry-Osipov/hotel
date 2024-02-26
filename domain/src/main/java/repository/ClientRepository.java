package repository;

import annotations.annotation.Component;
import essence.person.AbstractClient;
import lombok.Getter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

/**
 * Класс отвечает за хранение клиентов отеля. Реализован с использованием паттерна Singleton для обеспечения
 * единственного экземпляра репозитория клиентов.
 */
@Component
@Getter
@ToString
public class ClientRepository {
    private final Set<AbstractClient> clients = new HashSet<>();
}
