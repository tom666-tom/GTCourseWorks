CREATE DATABASE team06_project;
USE team06_project;

CREATE TABLE Manufacturer (
    manufacturer_id INT PRIMARY KEY,
    manufacturer_name VARCHAR(255) NOT NULL,
    max_discount DECIMAL(5, 2)
);

CREATE TABLE Category (
    category_name VARCHAR(255) PRIMARY KEY
);

CREATE TABLE Date (
    date DATE PRIMARY KEY
);

CREATE TABLE Holiday (
    date DATE PRIMARY KEY,
    holiday_name VARCHAR(255) NOT NULL,
    FOREIGN KEY (date) REFERENCES Date(date)
);

CREATE TABLE City (
    city_name VARCHAR(255) NOT NULL,
    state VARCHAR(50) NOT NULL,
    population INT,
    PRIMARY KEY (city_name, state)
);

CREATE TABLE Membership_Level (
    level_name VARCHAR(255) PRIMARY KEY
);

CREATE TABLE Store (
    store_number INT PRIMARY KEY,
    phone_number VARCHAR(20),
    street_address VARCHAR(255),
    zipcode VARCHAR(10),
    city_name VARCHAR(255) NOT NULL,
    state VARCHAR(50) NOT NULL,
    FOREIGN KEY (city_name, state) REFERENCES City(city_name, state)
);

CREATE TABLE Product (
    product_id INT PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    retail_price DECIMAL(10, 2),
    manufacturer_id INT NOT NULL,
    FOREIGN KEY (manufacturer_id) REFERENCES Manufacturer(manufacturer_id)
);

CREATE TABLE Belong (
    product_id INT NOT NULL,
    category_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (product_id, category_name),
    FOREIGN KEY (product_id) REFERENCES Product(product_id),
    FOREIGN KEY (category_name) REFERENCES Category(category_name)
);

CREATE TABLE Has_Sale_Event (
    date DATE NOT NULL,
    product_id INT NOT NULL,
    sale_price DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (date, product_id),
    FOREIGN KEY (date) REFERENCES Date(date),
    FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

CREATE TABLE Membership (
    member_id INT PRIMARY KEY,
    signup_store INT NOT NULL,
    level_name VARCHAR(50) NOT NULL,
    signup_date DATE NOT NULL,
    FOREIGN KEY (signup_store) REFERENCES Store(store_number),
    FOREIGN KEY (level_name) REFERENCES Membership_Level(level_name),
    FOREIGN KEY (signup_date) REFERENCES Date(date)
);

CREATE TABLE Sold (
    store_number INT NOT NULL,
    product_id INT NOT NULL,
    date DATE NOT NULL,
    quantity INT,
    sold_price DECIMAL(10, 2),
    PRIMARY KEY (store_number, product_id, date),
    FOREIGN KEY (store_number) REFERENCES Store(store_number),
    FOREIGN KEY (product_id) REFERENCES Product(product_id),
    FOREIGN KEY (date) REFERENCES Date(date)
);
