package test;

import essence.service.Service;
import essence.service.ServiceNames;
import essence.service.ServiceStatusTypes;

public class TestService {
    public static void main(String[] args) {
        Service service = new Service(ServiceNames.FITNESS, 2000);
        service.setStatus(ServiceStatusTypes.PAID);
        System.out.println(service.getStatus());
        service.setPrice(3000);
        System.out.println(service.getPrice());
    }
}
