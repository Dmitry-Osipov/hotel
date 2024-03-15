-- Задание 1 (1): Создать DDL скрипты для создания базы данных описанной ниже.
CREATE DATABASE technique;

USE technique;

CREATE TABLE product (
    maker VARCHAR(10) NOT NULL,
    model VARCHAR(50) PRIMARY KEY,
    type VARCHAR(50) NOT NULL
);

CREATE TABLE pc (
    code INT PRIMARY KEY,
    model VARCHAR(50) NOT NULL,
    speed SMALLINT NOT NULL,
    ram SMALLINT NOT NULL,
    hd REAL NOT NULL,
    cd VARCHAR(10) NOT NULL,
    price DECIMAL(10, 2),  -- Типа данных MONEY нет в MySQL, использовал DECIMAL для наибольшей точности.
    FOREIGN KEY (model) REFERENCES product(model)
);

CREATE TABLE laptop (
    code INT PRIMARY KEY,
    model VARCHAR(50) NOT NULL,
    speed SMALLINT NOT NULL,
    ram SMALLINT NOT NULL,
    hd REAL NOT NULL,
    screen TINYINT NOT NULL,
    price DECIMAL(10, 2),
    FOREIGN KEY (model) REFERENCES product(model)
);

CREATE TABLE printer (
    code INT PRIMARY KEY,
    model VARCHAR(50) NOT NULL,
    color CHAR(1) NOT NULL,
    type VARCHAR(10) NOT NULL,
    price DECIMAL(10, 2),
    FOREIGN KEY (model) REFERENCES product(model)
);
