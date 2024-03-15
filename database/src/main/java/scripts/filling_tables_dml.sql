USE hotel;

INSERT INTO room_status (id, status_name) VALUES
(1, "OCCUPIED"),
(2, "REPAIR"),
(3, "AVAILABLE");

INSERT INTO service_name (id, name) VALUES
(1, "WI_FI"),
(2, "PARKING"),
(3, "BREAKFAST"),
(4, "CLEANING"),
(5, "LAUNDRY"),
(6, "SPA"),
(7, "FITNESS"),
(8, "CONCIERGE"),
(9, "RESTAURANT"),
(10, "TRANSPORTATION"),
(11, "EXCURSION"),
(12, "CONFERENCE"),
(13, "MINIBAR");

INSERT INTO service_status (id, status_name) VALUES
(1, "UNPAID"),
(2, "PAID"),
(3, "RENDERED"),
(4, "CANCELED");

INSERT INTO client (id, fio, phone_number, check_in_time, check_out_time) VALUES
(1, "Osipov D. R.", "+7(902)902-98-11", "2024-01-25 22:00:37", NULL),
(2, "Musofranova N. S.", "+7(961)150-09-97", "2024-01-25 22:00:37", NULL),
(3, "Belyakova I. S.", "+7(953)180-00-61", "2024-01-25 22:00:37", NULL),
(4, "Kondrashin E. V.", "+7(991)234-11-00", NULL, NULL),
(5, "Lebedev G.I.", "+7(921)728-21-01", NULL, NULL);

INSERT INTO room (id, number, capacity, price, status_id, stars, check_in_time, check_out_time) VALUES
(1, 1, 3, 3000, 1, 5, "2024-01-25 22:00:37", NULL),
(2, 2, 5, 13000, 1, 4, "2024-01-25 22:00:37", NULL),
(3, 3, 6, 20000, 3, 5, NULL, NULL),
(4, 4, 7, 30000, 3, 5, NULL, NULL),
(5, 5, 2, 85000, 3, 4, NULL, NULL),
(15, 6, 4, 6000, 3, 0, NULL, NULL);

INSERT INTO service (id, name_id, price, status_id, service_time) VALUES
(1, 12, 2000, 3, "2024-02-18 13:17:27"),
(2, 3, 1500, 1, NULL),
(3, 11, 20000, 1, NULL),
(4, 13, 5000, 3, "2024-01-25 22:00:37"),
(5, 2, 3000, 3, "2024-03-05 19:02:47");

INSERT INTO room_reservation (id, room_id, check_in_time, check_out_time) VALUES
(1, 1, "2024-01-25 22:00:37", "2024-01-26 20:00:37"),
(2, 2, "2024-01-25 22:00:37", "2024-01-26 20:00:37");

INSERT INTO reservation_client (reservation_id, client_id) VALUES
(1, 1),
(1, 2),
(2, 3);

INSERT INTO provided_service (id, client_id, service_id, service_time) VALUES
(1, 1, 1, "2024-01-25 22:00:37"),
(2, 3, 4, "2024-01-25 22:00:37"),
(86, 1, 1, "2024-02-18 13:17:27"),
(87, 3, 5, "2024-03-05 19:02:47");
