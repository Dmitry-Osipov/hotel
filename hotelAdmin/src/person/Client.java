package person;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class Client implements AbstractClient, Comparable<AbstractClient> {
    private final int id;
    private final String fio;
    @Setter
    private String phoneNumber;

    public Client(int id, String fio, String phoneNumber) {
        this.id = id;
        this.fio = fio;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public int compareTo(AbstractClient o) {
        return fio.compareTo(o.getFio());
    }
}
