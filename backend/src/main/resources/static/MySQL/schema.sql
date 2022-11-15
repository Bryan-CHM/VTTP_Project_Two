-- drop database if exists
drop schema if exists library;

create schema library;

use library;

create table user(
    user_id INT AUTO_INCREMENT,
    email VARCHAR(64) NOT NULL,
    password VARCHAR(64) NOT NULL,
    admin boolean DEFAULT false NOT NULL,
    primary key (user_id)
);

create table library(
    book_id INT AUTO_INCREMENT,
    title VARCHAR(128),
    authors VARCHAR(128),
    description VARCHAR(4096),
    thumbnail VARCHAR(512),
    publisher VARCHAR (128),
    published_date VARCHAR (128),
    page_count INT,
    quantity INT,
    likes INT,
    primary key(book_id)
);

create table borrow(
    borrow_id INT AUTO_INCREMENT,
    user_id INT,
    book_id INT,
    due TIMESTAMP,
    returned boolean DEFAULT false,
    PRIMARY KEY(borrow_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (book_id) REFERENCES library(book_id)
);










