package essence.service;

import lombok.Getter;
import lombok.Setter;
import essence.person.AbstractClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Service extends AbstractFavor implements AbstractService, Comparable<AbstractService> {
    private final ServiceNames name;
    private static int count = 1;
    @Setter
    private ServiceStatusTypes status = ServiceStatusTypes.UNPAID;
    @Setter
    private LocalDateTime serviceTime;

    public Service(ServiceNames name, int price) {
        super(count++, price);
        this.name = name;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + getId() + "; " +
                "name=" + name + "; " +
                "price=" + getPrice() + "; " +
                "status=" + status +
                '}';
    }

    @Override
    public int compareTo(AbstractService o) {
        return getPrice() - o.getPrice();
    }
}
