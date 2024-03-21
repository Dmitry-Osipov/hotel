-- Задание 2:
USE technique;

-- Задание 1: Найдите номер модели, скорость и размер жёсткого диска для всех ПК стоимостью менее 500$.
-- Вывести: model, speed и hd:
SELECT model, speed, hd
FROM pc
WHERE price < 500;

-- Задание 2: Найдите производителей принтеров. Вывести: maker:
SELECT DISTINCT maker
FROM printer
    JOIN product ON printer.model = product.model;

-- Задание 3: Найдите номер модели, объём памяти и размеры экранов ПК-блокнотов, цена которых превышает 1000$:
SELECT model, ram, screen
FROM laptop
WHERE price > 1000;

-- Задание 4: Найдите все записи таблицы printer для цветных принтеров:
SELECT *
FROM printer
WHERE color = 'y';

-- Задание 5: Найдите номер модели, скорость и размер жёсткого диска ПК, имеющих 12x или 24x CD и цену менее 600$:
SELECT model, speed, hd
FROM pc
WHERE (cd = '12x' OR cd = '24x') AND price < 600;

-- Задание 6: Укажите производителя и скорость для тех ПК-блокнотов, которые имеют жёсткий диск объёмом не менее 100 ГБ:
SELECT product.maker, laptop.speed
FROM laptop
    JOIN product ON product.model = laptop.model
WHERE laptop.hd >= 100;

-- Задание 7: Найдите номера моделей и цены всех продуктов (любого типа), выпущенных производителем B (латинская буква):
SELECT product.model, COALESCE(pc.price, laptop.price, printer.price) AS price
FROM product
    LEFT JOIN pc ON product.model = pc.model AND product.type = 'PC'
    LEFT JOIN laptop ON product.model = laptop.model AND product.type = 'Laptop'
    LEFT JOIN printer ON product.model = printer.model AND product.type = 'Printer'
WHERE product.maker = 'B';

-- Задание 8: Найдите производителя, выпускающего ПК, но не ПК-блокноты:
SELECT DISTINCT maker
FROM product
WHERE maker NOT IN (SELECT maker FROM product WHERE type = 'Laptop');

-- Задание 9: Найдите производителей ПК с процессором не менее 450 Мгц. Вывести: Maker:
SELECT DISTINCT product.maker AS Maker
FROM product
    JOIN pc ON product.model = pc.model
WHERE pc.speed >= 450;

-- Задание 10: Найдите принтеры, имеющие самую высокую цену. Вывести: model, price:
SELECT model, price
FROM printer
WHERE price = (SELECT MAX(price) FROM printer);

-- Задание 11: Найдите среднюю стоимость ПК:
SELECT AVG(price) AS pc_avg_price
FROM pc;

-- Задание 12: Найдите среднюю стоимость ПК-блокнотов, цена которых превышает 1000$:
SELECT AVG(price) AS laptop_avg_price
FROM laptop
WHERE price > 1000;

-- Задание 13: Найдите среднюю скорость ПК, выпущенных производителем A:
SELECT AVG(pc.speed) AS pc_avg_speed
FROM pc
    JOIN product ON pc.model = product.model
WHERE product.maker = 'A';

-- Задание 14: Для каждого значения скорости найдите среднюю стоимость ПК с такой же скоростью процессора.
-- Вывести: скорость, средняя цена:
SELECT speed, AVG(price) AS pc_avg_price
FROM pc
GROUP BY speed;

-- Задание 15: Найдите размеры жёстких дисков, совпадающих у двух и более PC:
SELECT hd
FROM pc
GROUP BY hd
HAVING COUNT(*) >= 2;

-- Задание 16: Найдите пары моделей PC, имеющих одинаковые скорость и RAM. В результате каждая пара указывается только
-- один раз, т.е. (i,j), но не (j,i).
-- Порядок вывода: модель с большим номером, модель с меньшим номером, скорость и RAM:
SELECT DISTINCT pc1.model, pc2.model, pc1.speed, pc1.ram
FROM pc pc1, pc pc2
WHERE pc1.model < pc2.model
    AND pc1.speed = pc2.speed
    AND pc1.ram = pc2.ram;

-- Задание 17: Найдите модели ПК-блокнотов, скорость которых меньше скорости любого из ПК:
SELECT DISTINCT model
FROM laptop
WHERE speed < ANY (SELECT speed FROM pc);

-- Задание 18: Найдите производителей самых дешёвых цветных принтеров. Вывести: maker, price:
SELECT product.maker, printer.price
FROM printer
    JOIN product ON product.model = printer.model
WHERE printer.price = (SELECT MIN(price) FROM printer) AND printer.color = 'y';

-- Задание 19: Для каждого производителя найдите средний размер экрана выпускаемых им ПК-блокнотов.
-- Вывести: maker, средний размер экрана:
SELECT product.maker, AVG(laptop.screen) AS laptop_avg_screen
FROM product
    JOIN laptop ON product.model = laptop.model
WHERE product.type = 'Laptop'
GROUP BY product.maker;

-- Задание 20: Найдите производителей, выпускающих по меньшей мере три различных модели ПК.
-- Вывести: Maker, число моделей:
SELECT maker AS Maker, COUNT(DISTINCT model) AS count_models
FROM product
WHERE type = 'PC'
GROUP BY maker
HAVING COUNT(DISTINCT model) >= 3;

-- Задание 21: Найдите максимальную цену ПК, выпускаемых каждым производителем. Вывести: maker, максимальная цена:
SELECT product.maker, MAX(pc.price) AS pc_max_price
FROM product
    JOIN pc ON product.model = pc.model
GROUP BY product.maker;

-- Задание 22: Для каждого значения скорости ПК, превышающего 600 МГц, определите среднюю цену ПК с такой же скоростью.
-- Вывести: speed, средняя цена:
SELECT speed, AVG(price) AS pc_avg_price
FROM pc
WHERE speed > 600
GROUP BY speed;

-- Задание 23: Найдите производителей, которые производили бы как ПК со скоростью не менее 750 МГц, так и ПК-блокноты
-- со скоростью не менее 750 МГц. Вывести: Maker:
SELECT DISTINCT p1.maker AS Maker
FROM product p1
    JOIN pc ON p1.model = pc.model
    JOIN product p2 ON p2.maker = p1.maker
    JOIN laptop ON p2.model = laptop.model
WHERE pc.speed >= 750 AND laptop.speed >= 750;

-- Задание 24: Перечислите номера моделей любых типов, имеющих самую высокую цену по всей имеющейся в БД продукции:
SELECT model
FROM (
    SELECT model, price
    FROM pc
    UNION ALL
    SELECT model, price
    FROM laptop
    UNION ALL
    SELECT model, price
    FROM printer
    ) AS all_products
WHERE price = (
    SELECT MAX(price)
    FROM (
        SELECT price
        FROM pc
        UNION ALL
        SELECT price
        FROM laptop
        UNION ALL
        SELECT price
        FROM printer
        ) AS all_prices
    );

-- Задание 25: Найдите производителей принтеров, которые производят ПК с наименьшим объёмом RAM и с самым быстрым
-- процессором среди всех ПК, имеющих наименьший объём RAM. Вывести: Maker:
SELECT product.maker AS Maker
FROM product
    JOIN pc ON product.model = pc.model
WHERE pc.speed = (SELECT MAX(speed) FROM pc)
    AND pc.ram = (SELECT MIN(ram) FROM pc)
    AND product.maker IN (SELECT DISTINCT maker FROM printer);
