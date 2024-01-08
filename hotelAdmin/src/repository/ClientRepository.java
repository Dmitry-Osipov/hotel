package repository;

import lombok.Getter;
import lombok.ToString;
import essence.person.AbstractClient;

import java.util.HashSet;
import java.util.Set;

/**
 * Класс отвечает за хранение клиентов отеля.
 */
@Getter
@ToString
public class ClientRepository {
    private final Set<AbstractClient> clients = new HashSet<>();
}
