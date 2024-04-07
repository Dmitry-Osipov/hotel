package utils;

import dto.ClientDto;
import dto.ProvidedServiceDto;
import dto.RoomDto;
import dto.RoomReservationDto;
import dto.ServiceDto;
import essence.Identifiable;
import essence.person.AbstractClient;
import essence.person.Client;
import essence.provided.ProvidedService;
import essence.reservation.RoomReservation;
import essence.room.Room;
import essence.room.RoomStatusTypes;
import essence.service.AbstractService;
import essence.service.Service;
import essence.service.ServiceNames;
import essence.service.ServiceStatusTypes;
import service.ClientService;
import service.RoomService;
import service.ServiceService;

import java.sql.SQLException;
import java.util.stream.Collectors;

public final class DtoConverter {
    private DtoConverter() {
    }

    public static ClientDto convertClientToDto(AbstractClient client) {
        ClientDto dto = new ClientDto();
        dto.setId(client.getId());
        dto.setFio(client.getFio());
        dto.setPhoneNumber(client.getPhoneNumber());
        dto.setCheckInTime(client.getCheckInTime());
        dto.setCheckOutTime(client.getCheckOutTime());
        return dto;
    }

    public static AbstractClient convertDtoToClient(ClientDto dto) {
        Client client = new Client();
        client.setId(dto.getId());
        client.setFio(dto.getFio());
        client.setPhoneNumber(dto.getPhoneNumber());
        client.setCheckInTime(dto.getCheckInTime());
        client.setCheckOutTime(dto.getCheckOutTime());
        return client;
    }

    public static ServiceDto convertServiceToDto(AbstractService service) {
        ServiceDto dto = new ServiceDto();
        dto.setId(service.getId());
        dto.setName(String.valueOf(service.getName()));
        dto.setPrice(service.getPrice());
        dto.setStatus(String.valueOf(service.getStatus()));
        dto.setServiceTime(service.getServiceTime());
        return dto;
    }

    public static AbstractService convertDtoToService(ServiceDto dto) {
        Service service = new Service();
        service.setId(dto.getId());
        service.setName(ServiceNames.valueOf(dto.getName()));
        service.setPrice(dto.getPrice());
        service.setStatus(ServiceStatusTypes.valueOf(dto.getStatus()));
        service.setServiceTime(dto.getServiceTime());
        return service;
    }

    public static RoomDto convertRoomToDto(Room room) {
        RoomDto dto = new RoomDto();
        dto.setId(room.getId());
        dto.setNumber(room.getNumber());
        dto.setCapacity(room.getCapacity());
        dto.setStars(room.getStars());
        dto.setStatus(String.valueOf(room.getStatus()));
        dto.setPrice(room.getPrice());
        dto.setCheckInTime(room.getCheckInTime());
        dto.setCheckOutTime(room.getCheckOutTime());
        return dto;
    }

    public static Room convertDtoToRoom(RoomDto dto) {
        Room room = new Room();
        room.setId(room.getId());
        room.setNumber(dto.getNumber());
        room.setCapacity(dto.getCapacity());
        room.setStars(dto.getStars());
        room.setStatus(RoomStatusTypes.valueOf(dto.getStatus()));
        room.setPrice(dto.getPrice());
        room.setCheckInTime(dto.getCheckInTime());
        room.setCheckOutTime(dto.getCheckOutTime());
        return room;
    }

    public static ProvidedServiceDto convertProvidedServiceToDto(ProvidedService service) {
        ProvidedServiceDto dto = new ProvidedServiceDto();
        dto.setId(service.getId());
        dto.setServiceId(service.getService().getId());
        dto.setClientId(service.getClient().getId());
        dto.setServiceTime(service.getServiceTime());
        return dto;
    }

    public static ProvidedService convertDtoToProvidedService(ProvidedServiceDto dto, ServiceService serviceService,
                                                              ClientService clientService) throws SQLException {
        ProvidedService providedService = new ProvidedService();
        providedService.setId(dto.getId());
        providedService.setService(serviceService.getServiceById(dto.getServiceId()));
        providedService.setClient(clientService.getClientById(dto.getClientId()));
        providedService.setServiceTime(dto.getServiceTime());
        return providedService;
    }

    public static RoomReservationDto convertRoomReservationToDto(RoomReservation reservation) {
        RoomReservationDto dto = new RoomReservationDto();
        dto.setId(reservation.getId());
        dto.setRoomId(reservation.getRoom().getId());
        dto.setClientIds(reservation.getClients().stream().map(Identifiable::getId).collect(Collectors.toList()));
        dto.setCheckInTime(reservation.getCheckInTime());
        dto.setCheckOutTime(reservation.getCheckOutTime());
        return dto;
    }

    public static RoomReservation convertDtoToRoomReservation(RoomReservationDto dto, RoomService roomService,
                                                              ClientService clientService) throws SQLException {
        RoomReservation reservation = new RoomReservation();
        reservation.setId(dto.getId());
        // TODO: получение комнаты по id в сервисе и репозитории + остальные методы
        return reservation;
    }
}
