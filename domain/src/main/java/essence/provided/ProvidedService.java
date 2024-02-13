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
@Setter
public class ProvidedService implements Identifiable {
    private int id;
    private List<AbstractClient> beneficiaries = new ArrayList<>();
    private AbstractService service;
    private LocalDateTime serviceTime;

    public ProvidedService() {
    }

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
