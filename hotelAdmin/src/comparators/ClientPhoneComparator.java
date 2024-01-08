package comparators;

import essence.person.AbstractClient;

import java.util.Comparator;

public class ClientPhoneComparator implements Comparator<AbstractClient> {

    @Override
    public int compare(AbstractClient o1, AbstractClient o2) {
        return o1.getPhoneNumber().compareTo(o2.getPhoneNumber());
    }
}
