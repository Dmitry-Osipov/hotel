CREATE SCHEMA `hotel` DEFAULT CHARACTER SET utf8mb4;

CREATE TABLE hotel.client (
    id INT NOT NULL AUTO_INCREMENT,
    fio VARCHAR(256) NOT NULL,
    phone_number VARCHAR(32) NOT NULL,
    check_in_time TIMESTAMP,
    check_out_time TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE hotel.room (
    id INT NOT NULL AUTO_INCREMENT,
    number INT NOT NULL,
    capacity INT NOT NULL,
    price INT NOT NULL,
    status VARCHAR(16) NOT NULL,
    stars INT NOT NULL,
    check_in_time TIMESTAMP,
    check_out_time TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE hotel.service (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(16) NOT NULL,
    price INT NOT NULL,
    status VARCHAR(16) NOT NULL,
    service_time TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE hotel.room_reservation (
    id INT NOT NULL AUTO_INCREMENT,
    room_id INT NOT NULL,
    check_in_time TIMESTAMP NOT NULL,
    check_out_time TIMESTAMP NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (room_id) REFERENCES hotel.room(id)
);

CREATE TABLE hotel.reservation_client (
    reservation_id INT,
    client_id INT,
    PRIMARY KEY (reservation_id, client_id),
    FOREIGN KEY (reservation_id) REFERENCES hotel.room_reservation(id),
    FOREIGN KEY (client_id) REFERENCES hotel.client(id)
);

CREATE TABLE hotel.provided_service (
    id INT NOT NULL AUTO_INCREMENT,
    client_id INT NOT NULL,
    service_id INT NOT NULL,
    service_time TIMESTAMP NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (client_id) REFERENCES hotel.client(id),
    FOREIGN KEY (service_id) REFERENCES hotel.service(id)
);
