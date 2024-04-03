package utils.database;

import essence.person.Client;
import essence.provided.ProvidedService;
import essence.reservation.RoomReservation;
import essence.room.Room;
import essence.service.Service;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Утилитный класс предназначен для создания и предоставления объекта {@link SessionFactory} для работы с сессиями
 * Hibernate.
 */
public final class SessionFactoryCreator {
    private SessionFactoryCreator() {
    }

    /**
     * Возвращает объект {@link SessionFactory} для работы с сессиями Hibernate.
     * @return объект {@link SessionFactory}
     */
    public static SessionFactory getSessionFactory() {
        return new Configuration()
                .configure()
                .addAnnotatedClass(Room.class)
                .addAnnotatedClass(Service.class)
                .addAnnotatedClass(Client.class)
                .addAnnotatedClass(ProvidedService.class)
                .addAnnotatedClass(RoomReservation.class)
                .buildSessionFactory();
    }
}
