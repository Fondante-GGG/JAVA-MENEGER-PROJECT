create table users (
    id varchar(36) primary key,
    email varchar(320) not null unique,
    password_hash varchar(200) not null,
    role varchar(20) not null,
    enabled boolean not null
);
