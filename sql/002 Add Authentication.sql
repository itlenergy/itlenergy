-- authentication tables --

create table if not exists login_user (
    user_id		        serial
        constraint user_pkey primary key,
    username            varchar(255)    not null
        constraint user_username_uq unique,
    password            bytea           not null,
    role                varchar(255)    not null,
    related_id          int             null
);


