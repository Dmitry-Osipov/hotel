INSERT INTO hotel.client (fio, phone_number, check_in_time, check_out_time) VALUES
('Osipov D. R.', '+7(902)902-98-11', '2024-01-25 22:00:37', NULL),
('Musofranova N. S.', '+7(961)150-09-97', '2024-01-25 22:00:37', NULL),
('Belyakova I. S.', '+7(953)180-00-61', '2024-01-25 22:00:37', NULL),
('Kondrashin E. V.', '+7(991)234-11-00', NULL, NULL),
('Lebedev G.I.', '+7(921)728-21-01', NULL, NULL);

INSERT INTO hotel.room (number, capacity, price, status, stars, check_in_time, check_out_time) VALUES
(1, 3, 3000, 'OCCUPIED', 5, '2024-01-25 22:00:37', NULL),
(2, 5, 13000, 'OCCUPIED', 4, '2024-01-25 22:00:37', NULL),
(3, 6, 20000, 'AVAILABLE', 5, NULL, NULL),
(4, 7, 30000, 'AVAILABLE', 5, NULL, NULL),
(5, 2, 85000, 'AVAILABLE', 4, NULL, NULL),
(6, 4, 6000, 'AVAILABLE', 0, NULL, NULL);

INSERT INTO hotel.service (name, price, status, service_time) VALUES
('CONFERENCE', 2000, 'RENDERED', '2024-02-18 13:17:27'),
('BREAKFAST', 1500, 'UNPAID', NULL),
('EXCURSION', 20000, 'UNPAID', NULL),
('MINIBAR', 5000, 'RENDERED', '2024-01-25 22:00:37'),
('PARKING', 3000, 'RENDERED', '2024-03-05 19:02:47');

INSERT INTO hotel.room_reservation (room_id, check_in_time, check_out_time) VALUES
(1, '2024-01-25 22:00:37', '2024-01-26 20:00:37'),
(2, '2024-01-25 22:00:37', '2024-01-26 20:00:37');

INSERT INTO hotel.reservation_client (reservation_id, client_id) VALUES
(1, 1),
(1, 2),
(2, 3);

INSERT INTO hotel.provided_service (client_id, service_id, service_time) VALUES
(1, 1, '2024-01-25 22:00:37'),
(3, 4, '2024-01-25 22:00:37'),
(1, 1, '2024-02-18 13:17:27'),
(3, 5, '2024-03-05 19:02:47');
