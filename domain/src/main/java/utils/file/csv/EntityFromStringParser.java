package utils.file.csv;

import essence.person.AbstractClient;
import essence.person.Client;
import essence.room.AbstractRoom;
import essence.room.Room;
import essence.room.RoomStatusTypes;
import essence.service.AbstractService;
import essence.service.Service;
import essence.service.ServiceNames;
import essence.service.ServiceStatusTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EntityFromStringParser {
    private EntityFromStringParser() {
    }

    /**
     * Метод парсит строковое время и дату в экземпляр класса LocalDateTime.
     * @param dateTime Строка времени и даты.
     * @return Время, если строка или значение строки не равно null, иначе возвращается null.
     */
    public static LocalDateTime parseDateTime(String dateTime) {
        return dateTime != null && !dateTime.equals("null") ? LocalDateTime.parse(dateTime) : null;
    }

    /**
     * Метод парсит строковое представление комнаты в объект комнаты.
     * @param room Строка комнаты.
     * @return Комната.
     */
    public static AbstractRoom parseRoom(String room) {
        room = room.substring(5, room.length() - 1);
        Map<String, String> roomMap = new HashMap<>();
        updateMapByArray(roomMap, room.split("; "));

        int id = Integer.parseInt(roomMap.get("id"));
        int number = Integer.parseInt(roomMap.get("number"));
        int capacity = Integer.parseInt(roomMap.get("capacity"));
        int price = Integer.parseInt(roomMap.get("price"));
        RoomStatusTypes status = RoomStatusTypes.valueOf(roomMap.get("status"));
        int stars = Integer.parseInt(roomMap.get("stars"));
        LocalDateTime checkInTime = parseDateTime(roomMap.get("check-in time"));
        LocalDateTime checkOutTime = parseDateTime(roomMap.get("check-out time"));

        AbstractRoom newRoom = new Room(id, number, capacity, price);
        newRoom.setStatus(status);
        newRoom.setStars(stars);
        newRoom.setCheckInTime(checkInTime);
        newRoom.setCheckOutTime(checkOutTime);

        return newRoom;
    }

    /**
     * Метод парсит строковый список клиентов в обычный список клиентов.
     * @param client Список клиентов в виде строки.
     * @return Список клиентов.
     */
    public static List<AbstractClient> parseClients(String client) {
        List<AbstractClient> guests = new ArrayList<>();
        client = client.substring(1, client.length()-1);
        Map<String, String> clientMap = new HashMap<>();

        for (String clientString : client.split(", ")) {
            updateMapByArray(clientMap, clientString.substring(7, clientString.length() - 1).split("; "));

            int id = Integer.parseInt(clientMap.get("id"));
            String fio = clientMap.get("fio");
            String phoneNumber = clientMap.get("phoneNumber");
            LocalDateTime checkInTime = parseDateTime(clientMap.get("checkInTime"));
            LocalDateTime checkOutTime = parseDateTime(clientMap.get("checkOutTime"));

            AbstractClient guest = new Client(id, fio, phoneNumber);
            guest.setCheckInTime(checkInTime);
            guest.setCheckOutTime(checkOutTime);

            guests.add(guest);
        }

        return guests;
    }

    /**
     * Метод обновляет переданную мапу ключом и значением переданного массива. Ключ - первый элемент массива,
     * значение - второй элемент массива.
     * @param map Мапа, которую необходимо обновить.
     * @param array Массив, откуда будут браться данные для обновления.
     */
    public static void updateMapByArray(Map<String, String> map, String[] array) {
        for (String data : array) {
            String[] keyValue = data.split("=");
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();
            map.put(key, value);
        }
    }

    /**
     * Метод парсит строковое представление услуги в экземпляр класса услуги.
     * @param service Строка услуги.
     * @return Услуга.
     */
    public static AbstractService parseService(String service) {
        service = service.substring(8, service.length() - 1);
        Map<String, String> serviceMap = new HashMap<>();
        updateMapByArray(serviceMap, service.split("; "));

        int id = Integer.parseInt(serviceMap.get("id"));
        ServiceNames name = ServiceNames.valueOf(serviceMap.get("name"));
        int price = Integer.parseInt(serviceMap.get("price"));
        ServiceStatusTypes status = ServiceStatusTypes.valueOf(serviceMap.get("status"));
        LocalDateTime serviceTime = parseDateTime(serviceMap.get("service time"));

        AbstractService newService = new Service(id, name, price);
        newService.setStatus(status);
        newService.setServiceTime(serviceTime);

        return newService;
    }
}
