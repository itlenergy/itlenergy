-- helper functions to simplify migration
--schema public;

/**
 * Checks if a column exists on a table.
 */
create or replace function columnExists(tbl varchar, col varchar) returns boolean as
    $$ select exists (select column_name from information_schema.columns where table_name=$1 and column_name=$2); $$ language sql;


/**
 * Represents a measurement site with one or more houses.
 */
create table if not exists site
(
    site_id             serial
        constraint site_pkey primary key,
    latitude            real,
    longitude           real,
    altitude            real,
    site_name           varchar(32)     not null
);


/**
 * Represents a house which contains measuring equipment.
 */
create table if not exists house
(
    house_id            serial
        constraint house_pkey primary key,
    site_id             integer         not null
        constraint house_site_fkey references site on delete cascade,
    rooms               integer         not null,
    floors              integer         not null,
    occupants           integer         not null,
    central_heating_gas boolean         not null,
    cooking_gas         boolean         not null
);


/**
 * Represents a measuring hub within a house.
 * REVIEW: Does this need to be separate from house?
 */
create table if not exists hub
(
    hub_id              serial
        constraint hub_pkey primary key,
    house_id            integer         not null
        constraint hub_house_fkey references house on delete cascade,
    last_update         date            not null,
    free_storage        integer         not null
);


/**
 * A list of types of status events which may occur in the hub log.
 */
create table if not exists status_events
(
    status_id           serial
        constraint status_events_pkey primary key,
    status_description  varchar(128)    not null 
);


/**
 * An event log for a hub, containing messages which of a type defined in
 * table `status_events`.
 */
create table if not exists hub_log
(
    hub_log_id          serial
        constraint hub_log_pkey primary key,
    hub_id              integer         not null
        constraint hub_log_hub_fkey references hub on delete cascade,
    hub_log_message     varchar(32)     not null,
    hub_log_time        timestamp       not null,
    hub_log_code        integer         not null
        constraint hub_log_code_fkey references status_events
);


/**
 * Describes a type of sensor which may be deployed.
 */
create table if not exists deployed_sensor
(
    type_id             serial
        constraint deployed_sensor_pkey primary key,
    description         varchar(128)    not null,
    measurement_units   varchar(16)
);


/**
 * Represents a sensor which has been deployed to a house (attached to a hub)
 * with a given sensor type.
 */
create table if not exists sensor
(
    sensor_id           serial
        constraint sensor_pkey primary key,
    hub_id              integer         not null
        constraint sensor_hub_fkey references hub on delete cascade,
    type_id             integer         not null
        constraint sensor_type_fkey references deployed_sensor on delete cascade,
    description         varchar(64)     not null
);


/**
 * Represents a measurement taken by a sensor at a given time.
 */
create table if not exists measurement
(
    measurement_id      serial
        constraint measurement_pkey primary key,
    sensor_id           integer         not null
        constraint measurement_sensor_fkey references sensor on delete cascade,
    observation_time    timestamp       not null,
    observation         real
);


/**
 * Represents a set of weather observations for a site at 
 * a point in time.
 */
create table if not exists weather
(
    weather_observation_id serial
        constraint weather_pkey primary key,
    observation_time    timestamp       not null,    
    site_id             integer         not null
        constraint weather_site_fkey references site on delete cascade,
    wind_speed          real,
    wind_direction      real,
    ambient_temperature real,
    humidity            real,
    uv                  real,
    precipitation       real    
);