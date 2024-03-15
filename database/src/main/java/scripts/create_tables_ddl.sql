CREATE SCHEMA `hotel` DEFAULT CHARACTER SET utf8mb4;

USE hotel;

CREATE TABLE room_status (
    id INT PRIMARY KEY,
    status_name VARCHAR(16) NOT NULL
);

CREATE TABLE service_name (
    id INT PRIMARY KEY,
    name VARCHAR(16) NOT NULL
);

CREATE TABLE service_status (
    id INT PRIMARY KEY,
    status_name VARCHAR(16) NOT NULL
);

CREATE TABLE client (
    id INT PRIMARY KEY,
    fio VARCHAR(256) NOT NULL,
    phone_number VARCHAR(32) NOT NULL,
    check_in_time TIMESTAMP,
    check_out_time TIMESTAMP
);

CREATE TABLE room (
    id INT PRIMARY KEY,
    number INT NOT NULL,
    capacity INT NOT NULL,
    price INT NOT NULL,
    status_id INT NOT NULL,
    stars INT NOT NULL,
    check_in_time TIMESTAMP,
    check_out_time TIMESTAMP,
    FOREIGN KEY (status_id) REFERENCES room_status(id)
);

CREATE TABLE service (
    id INT PRIMARY KEY,
    name_id INT NOT NULL,
    price INT NOT NULL,
    status_id INT NOT NULL,
    service_time TIMESTAMP,
    FOREIGN KEY (name_id) REFERENCES service_name(id),
    FOREIGN KEY (status_id) REFERENCES service_status(id)
);

CREATE TABLE room_reservation (
    id INT PRIMARY KEY,
    room_id INT NOT NULL,
    check_in_time TIMESTAMP NOT NULL,
    check_out_time TIMESTAMP NOT NULL,
    FOREIGN KEY (room_id) REFERENCES room(id)
);

CREATE TABLE reservation_client (
    reservation_id INT,
    client_id INT,
    PRIMARY KEY (reservation_id, client_id),
    FOREIGN KEY (reservation_id) REFERENCES room_reservation(id),
    FOREIGN KEY (client_id) REFERENCES client(id)
);

CREATE TABLE provided_service (
    id INT PRIMARY KEY,
    client_id INT NOT NULL,
    service_id INT NOT NULL,
    service_time TIMESTAMP NOT NULL,
    FOREIGN KEY (client_id) REFERENCES client(id),
    FOREIGN KEY (service_id) REFERENCES service(id)
);
