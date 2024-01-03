package task_4;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Service extends AbstractFavor implements AbstractService {
    private final String name;
    @Setter
    private ServiceStatusTypes status = ServiceStatusTypes.UNPAID;

    public Service(int id, String name, int price) {
        super(id, price);
        this.name = name;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + getId() + "; " +
                "name=" + name + "; " +
                "price=" + getPrice() + "; " +
                "status=" + status +
                '}';
    }
}
