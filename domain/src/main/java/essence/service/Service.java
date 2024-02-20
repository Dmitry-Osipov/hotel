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

    public Service() {
    }

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

        return getId() == ((Service) obj).getId();
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
                "status=" + status + "; " +
                "service time=" + serviceTime +
                '}';
    }

    @Override
    public int compareTo(AbstractService o) {
        return getPrice() - o.getPrice();
    }
}
