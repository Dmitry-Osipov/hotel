package essence.service;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс услуги.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "hotel", name = "service")
public class Service extends AbstractFavor implements AbstractService, Comparable<AbstractService> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "price")
    private int price;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private ServiceNames name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ServiceStatusTypes status = ServiceStatusTypes.UNPAID;

    @Column(name = "service_time")
    private LocalDateTime serviceTime;

    public Service(int id, ServiceNames name, int price) {
        this.id = id;
        this.price = price;
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
