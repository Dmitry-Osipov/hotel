-- Задание 1 (2): Создать DML скрипт для заполнения полученной БД тестовыми данными.
USE technique;

INSERT INTO product (maker, model, type) VALUES
('A', '1', 'PC'),
('B', '2', 'PC'),
('C', '3', 'PC'),
('A', '11', 'PC'),
('E', '12', 'PC'),
('C', '13', 'PC'),
('D', '16', 'PC'),
('D', '17', 'PC'),
('A', '20', 'PC'),
('B', '24', 'PC'),
('D', '25', 'PC'),
('B', '26', 'PC'),
('B', '27', 'PC'),
('D', '28', 'PC'),
('A', '29', 'PC'),
('B', '33', 'PC'),
('A', '34', 'PC'),
('A', '4', 'Laptop'),
('C', '5', 'Laptop'),
('B', '9', 'Laptop'),
('E', '14', 'Laptop'),
('E', '19', 'Laptop'),
('A', '21', 'Laptop'),
('A', '22', 'Laptop'),
('A', '23', 'Laptop'),
('C', '30', 'Laptop'),
('D', '6', 'Printer'),
('A', '7', 'Printer'),
('E', '10', 'Printer'),
('A', '8', 'Printer'),
('B', '15', 'Printer'),
('D', '18', 'Printer'),
('B', '31', 'Printer'),
('B', '32', 'Printer');

INSERT INTO pc (code, model, speed, ram, hd, cd, price) VALUES
(1, '1', 300, 4, 30, '2x', 200),
(2, '2', 100, 8, 50, '4x', 400),
(3, '3', 3000, 16, 100, '8x', 1500),
(4, '11', 1000, 32, 200, '12x', 500),
(5, '12', 1200, 8, 40, '24x', 300),
(6, '13', 2500, 4, 80, '24x', 800),
(7, '16', 5000, 32, 300, '12x', 3000),
(8, '17', 450, 8, 200, '8x', 800),
(9, '20', 200, 16, 150, '12x', 650),
(10, '24', 300, 4, 200, '8x', 1000),
(11, '25', 500, 32, 100, '4x', 500),
(12, '26', 64, 64, 250, '8x', 100),
(13, '27', 64, 64, 250, '4x', 50),
(14, '28', 32, 32, 150, '2x', 30),
(15, '29', 32, 32, 100, '12x', 130),
(16, '34', 3000, 2, 100, '12x', 400),
(17, '33', 8000, 2, 400, '24x', 500);

INSERT INTO laptop (code, model, speed, ram, hd, screen, price) VALUES
(1, '4', 200, 8, 30, 15, 700),
(2, '5', 2500, 16, 50, 17, 1000),
(3, '9', 1200, 32, 100, 25, 3000),
(4, '14', 3000, 16, 300, 25, 6500),
(5, '19', 1000, 16, 80, 13, 300),
(6, '21', 300, 8, 20, 15, 600),
(7, '22', 100, 8, 30, 17, 850),
(8, '23', 2500, 16, 100, 17, 1050),
(9, '30', 20, 20, 10, 11, 50);

INSERT INTO printer (code, model, color, type, price) VALUES
(1, '6', 'y', 'Laser', 300),
(2, '7', 'n', 'Jet', 200),
(3, '10', 'n', 'Laser', 350),
(4, '8', 'y', 'Matrix', 500),
(5, '15', 'n', 'Matrix', 700),
(6, '18', 'y', 'Jet', 700),
(7, '31', 'y', 'Laser', 200),
(8, '32', 'y', 'Matrix', 6500);
