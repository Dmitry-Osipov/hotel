package test;

import comparators.ClientPhoneComparator;
import person.Client;

import java.util.ArrayList;
import java.util.List;

public class TestClient {
    public static void main(String[] args) {
        Client client = new Client(1, "Osipov Dmitry Romanovich", "8-902-902-98-11");
        Client client1 = new Client(2, "Kondrashin Evgeny Viktorovich", "8-953-180-00-61");
        List<Client> clients = new ArrayList<>();
        clients.add(client);
        clients.add(client1);
        System.out.println(clients.stream().sorted().toList());
        System.out.println(clients.stream().sorted(new ClientPhoneComparator()).toList());
    }
}
