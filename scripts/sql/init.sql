use long_taxi;
DROP TABLE IF EXISTS orders_locations;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS cars;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       id varchar(255) PRIMARY KEY,
                       phone varchar(255) NOT NULL UNIQUE,
                       password varchar(255) NOT NULL,
                       username varchar(255) NOT NULL,
                       type varchar(255) NOT NULL default "user"
);
INSERT INTO users (id, phone, password, username, type)
VALUES
    ('u001', '12345678901', 'password1', 'user1', 'customer'),
    ('u002', '23456789012', 'password2', 'user2', 'customer'),
    ('u003', '34567890123', 'password3', 'user3', 'driver'),
    ('u004', '45678901234', 'password4', 'user4', 'customer');
CREATE TABLE cars (
                      id VARCHAR(20) PRIMARY KEY,
                      status tinyint NOT NULL DEFAULT 0,
                      longitude DECIMAL(10, 6) NOT NULL,
                      latitude DECIMAL(10, 6) NOT NULL
);
INSERT INTO cars (id, status, longitude, latitude) VALUES
                                                       ('c001', 1, 121.4737, 31.2304),
                                                       ('c002', 0, 121.4797, 31.2344),
                                                       ('c003', 1, 121.4787, 31.2324),
                                                       ('c004', 1, 121.4777, 31.2314);

CREATE TABLE orders (
                        id varchar(20) PRIMARY KEY,
                        user_id varchar(20) NOT NULL,
                        car_id varchar(20) DEFAULT NULL,
                        status int NOT NULL,
                        created_at datetime NOT NULL,
                        start_lon decimal(10, 6) NOT NULL,
                        start_lat decimal(10, 6) NOT NULL,
                        end_lon decimal(10, 6) NOT NULL,
                        end_lat decimal(10, 6) NOT NULL,
                        FOREIGN KEY (user_id) REFERENCES users(id),
                        FOREIGN KEY (car_id) REFERENCES cars(id)
);

INSERT INTO orders (id, user_id, car_id, status, created_at, start_lon, start_lat, end_lon, end_lat)
VALUES
    ('o001', 'u001', 'c001', 1, '2023-06-30 12:30:00', 121.4737, 31.2304, 121.4737, 31.2304),
    ('o002', 'u002', 'c002', 2, '2023-06-30 13:00:00', 121.4797, 31.2344, 121.4797, 31.2344),
    ('o003', 'u003', 'c003', 3, '2023-06-30 14:00:00', 121.4787, 31.2324, 121.4787, 31.2324),
    ('o004', 'u001', 'c004', 4, '2023-06-30 15:00:00', 121.4777, 31.2314, 121.4777, 31.2314);