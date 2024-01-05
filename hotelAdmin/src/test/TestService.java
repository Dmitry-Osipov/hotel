package test;

import service.Service;
import service.ServiceStatusTypes;

public class TestService {
    public static void main(String[] args) {
        Service service = new Service(1, "Завтрак в номер", 2000);
        service.setStatus(ServiceStatusTypes.PAID);
        System.out.println(service.getStatus());
        service.setPrice(3000);
        System.out.println(service.getPrice());
    }
}
