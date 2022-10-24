DELETE
FROM user_roles;
DELETE
FROM users;
DELETE
FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100000, '2022-10-20 15:10:45.120120', 'Картошка', 200),
       (100000, '2022-10-22 12:15:32.256248', 'Обед', 250),
       (100000, '2022-10-02 09:13:15.123698', 'Завтрак', 345),
       (100001, '2022-10-15 17:55:36.963197', 'Перекус', 220),
       (100001, '2022-10-06 20:01:28.698573', 'Мясо', 550),
       (100001, '2022-10-05 18:02:19.123321', 'Суп', 150);