package essence.provided;

import essence.Identifiable;
import essence.person.AbstractClient;
import essence.person.Client;
import essence.service.AbstractService;
import essence.service.Service;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Класс проведённой услуги.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "hotel", name = "provided_service")
public class ProvidedService implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
            targetEntity = Client.class)
    @JoinColumn(name = "client_id")
    private AbstractClient client;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
            targetEntity = Service.class)
    @JoinColumn(name = "service_id")
    private AbstractService service;

    @Column(name = "service_time")
    private LocalDateTime serviceTime;

    public ProvidedService(int id, AbstractService service, LocalDateTime serviceTime, AbstractClient client) {
        this.id = id;
        this.service = service;
        this.serviceTime = serviceTime;
        this.client = client;
    }

    @Override
    public String toString() {
        return "ProvidedService{" +
                "id=" + id + "; " +
                "client=" + client + "; " +
                "service=" + service + "; " +
                "serviceTime=" + serviceTime +
                '}';
    }
}
