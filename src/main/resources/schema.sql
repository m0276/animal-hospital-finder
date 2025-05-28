create table favorite(
    id bigint primary key,
    user_id bigint not null,
    hospital_id varchar(225) not null
);

create table hospital(
    location_id varchar(225) primary key ,
    place_name varchar(225) not null ,
    phone varchar(225),
    address_name varchar(225),
    road_address_name varchar(225),
    x double not null ,
    y double not null ,
    place_url varchar(225),
    tag varchar(225),
    tag2 varchar(225),
    tag3 varchar(225),
    loc point srid 4326,
    index inx_loc (loc),
    SPATIAL INDEX(loc)
);

create table user(
    id bigint primary key ,
    username varchar(225) unique not null ,
    password varchar(225) not null ,
    type varchar(225),
    type_id varchar(225)
);

