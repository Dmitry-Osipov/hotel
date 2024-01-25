package essence.provided;

import essence.Identifiable;
import essence.person.AbstractClient;
import essence.service.AbstractService;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ProvidedService implements Identifiable {
    private final int id;
    private final List<AbstractClient> beneficiaries = new ArrayList<>();
    @Setter
    private AbstractService service;
    private final LocalDateTime serviceTime;

    public ProvidedService(int id, AbstractService service, LocalDateTime serviceTime, AbstractClient client) {
        this.id = id;
        this.service = service;
        this.serviceTime = serviceTime;
        this.beneficiaries.add(client);
    }

    @Override
    public String toString() {
        return "ProvidedService{" +
                "id=" + id + "; " +
                "beneficiaries=" + beneficiaries + "; " +
                "service=" + service + "; " +
                "serviceTime=" + serviceTime +
                '}';
    }
}
