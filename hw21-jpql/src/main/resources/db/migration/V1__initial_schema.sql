-- ADDRESS
create sequence address_SEQ start with 1 increment by 1;
create table address
(
    id     bigint not null primary key,
    street varchar(256)
);

-- CLIENT
create sequence client_SEQ start with 1 increment by 1;
create table client
(
    id         bigint not null primary key,
    address_id bigint not null,
    name       varchar(50),
    constraint fk_address
        foreign key (address_id)
            references address (id)
);

-- PHONE
create sequence phone_SEQ start with 1 increment by 1;
create table phone
(
    id        bigint not null primary key,
    number    varchar(50),
    client_id bigint not null,
    constraint fk_client
        foreign key (client_id)
            references client (id)
);