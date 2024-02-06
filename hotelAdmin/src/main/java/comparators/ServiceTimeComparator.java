package comparators;

import essence.service.AbstractService;

import java.time.LocalDateTime;
import java.util.Comparator;

public class ServiceTimeComparator implements Comparator<AbstractService> {
    @Override
    public int compare(AbstractService o1, AbstractService o2) {
        LocalDateTime time1 = o1.getServiceTime();
        LocalDateTime time2 = o2.getServiceTime();
        if (time1 == null && time2 == null) {
            return 0;
        }
        if (time1 == null) {
            return -1;
        }
        if (time2 == null) {
            return 1;
        }
        return time1.compareTo(time2);
    }
}
