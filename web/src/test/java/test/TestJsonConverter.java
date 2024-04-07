package test;

import dto.ClientDto;
import dto.ProvidedServiceDto;
import dto.RoomDto;
import dto.RoomReservationDto;
import dto.ServiceDto;
import lombok.SneakyThrows;
import utils.JsonConverter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestJsonConverter {
    @SneakyThrows
    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();

        ClientDto clientDto = new ClientDto();
        clientDto.setId(1);
        clientDto.setFio("Osipov D. R.");
        clientDto.setPhoneNumber("+7(902)902-98-11");
        clientDto.setCheckInTime(now);
        clientDto.setCheckOutTime(now.plusHours(2));
        String jsonClientDto = JsonConverter.toJson(clientDto);
        System.out.println(jsonClientDto);
        System.out.println(JsonConverter.fromJson(jsonClientDto, ClientDto.class));

        ProvidedServiceDto providedServiceDto = new ProvidedServiceDto();
        providedServiceDto.setId(1);
        providedServiceDto.setClientId(1);
        providedServiceDto.setServiceId(1);
        providedServiceDto.setServiceTime(now);
        String jsonProvidedServiceDto = JsonConverter.toJson(providedServiceDto);
        System.out.println(jsonProvidedServiceDto);
        System.out.println(JsonConverter.fromJson(jsonProvidedServiceDto, ProvidedServiceDto.class));

        RoomDto roomDto = new RoomDto();
        roomDto.setId(1);
        roomDto.setNumber(1);
        roomDto.setCapacity(1);
        roomDto.setPrice(20000);
        roomDto.setStars(3);
        roomDto.setStatus("OCCUPIED");
        roomDto.setCheckInTime(now);
        roomDto.setCheckOutTime(now.plusHours(2));
        String jsonRoomDto = JsonConverter.toJson(roomDto);
        System.out.println(jsonRoomDto);
        System.out.println(JsonConverter.fromJson(jsonRoomDto, RoomDto.class));

        RoomReservationDto roomReservationDto = new RoomReservationDto();
        roomReservationDto.setId(1);
        roomReservationDto.setRoomId(1);
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        roomReservationDto.setClientIds(list);
        roomReservationDto.setCheckInTime(now);
        roomReservationDto.setCheckOutTime(now.plusHours(2));
        String jsonRoomReservationDto = JsonConverter.toJson(roomReservationDto);
        System.out.println(jsonRoomReservationDto);
        System.out.println(JsonConverter.fromJson(jsonRoomReservationDto, RoomReservationDto.class));

        ServiceDto serviceDto = new ServiceDto();
        serviceDto.setId(1);
        serviceDto.setPrice(5000);
        serviceDto.setStatus("UNPAID");
        serviceDto.setName("FITNESS");
        serviceDto.setServiceTime(now);
        String jsonServiceDto = JsonConverter.toJson(serviceDto);
        System.out.println(jsonServiceDto);
        System.out.println(JsonConverter.fromJson(jsonServiceDto, ServiceDto.class));
    }
}
