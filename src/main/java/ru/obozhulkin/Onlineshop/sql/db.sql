create table person
(
    id            integer generated by default as identity
        primary key,
    username      varchar(100) not null,
    year_of_birth integer      not null,
    password      varchar      not null,
    phone         char(11)     not null,
    role          varchar(100) not null
);

create table product
(
    product_id  integer default nextval('products_product_id_seq'::regclass) not null
        constraint products_pkey
            primary key,
    title       varchar(255)                                                 not null,
    description text,
    category_id varchar                                                      not null,
    price       integer                                                      not null,
    quantity    integer                                                      not null,
    image_url   varchar(255)                                                 not null
);


create table person_product
(
    id         serial
        primary key,
    person_id  integer not null
        references person
            on delete cascade,
    product_id integer not null
        references product
);