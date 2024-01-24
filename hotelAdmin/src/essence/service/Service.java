package essence.service;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
public class Service extends AbstractFavor implements AbstractService, Comparable<AbstractService> {
    private ServiceNames name;
    private ServiceStatusTypes status = ServiceStatusTypes.UNPAID;
    private LocalDateTime serviceTime;

    public Service(int id, ServiceNames name, int price) {
        super(id, price);
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Service service = (Service) obj;
        return getId() == service.getId()
                && getPrice() == service.getPrice()
                && status == service.status
                && name == service.name
                && Objects.equals(serviceTime, service.serviceTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
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
