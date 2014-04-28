-- test data

DELETE FROM login_user;
ALTER SEQUENCE login_user_user_id_seq RESTART WITH 1;
INSERT INTO login_user(username, password, role, related_id) VALUES ('apatsche1',E'\\x7f222a9e742f0afaa4c569059e87e1c15bf78392838755c4f28cdd3981b45e19f818c0ae7ac397709916199e9f192466ec39a6c6746c8a9155a60a35adbb068c','hub',NULL);
INSERT INTO login_user(username, password, role, related_id) VALUES ('apatsche2',E'\\x8cccff5ff7cf4d0f83b76856dbe7048d1a9ecead845d8dba49f65aff3d93dd37678944e98ec33e5a7487761a5e96d94a102c5d13b2748c5d63cc1bd4195fbc22','admin',NULL);

DELETE FROM site;
ALTER SEQUENCE site_site_id_seq RESTART WITH 1;
INSERT INTO site(latitude, longitude, altitude, site_name) VALUES (55.85546,-4.232459, 16.0,'catherinefield');
INSERT INTO site(latitude, longitude, altitude, site_name) VALUES (55.09906,-3.583382, 13.0,'annan');
INSERT INTO site(latitude, longitude, altitude, site_name) VALUES (55.85546,-4.232459, 16.0,'catherinefield2');
INSERT INTO site(latitude, longitude, altitude, site_name) VALUES (55.09906,-3.583382, 13.0,'annan2');

DELETE FROM status_events;
ALTER SEQUENCE status_events_status_id_seq RESTART WITH 1;
INSERT INTO status_events VALUES (0, 'broadband failure');
INSERT INTO status_events VALUES (1, 'power interuption');
INSERT INTO status_events VALUES (2, 'sensor timestamp skew');

DELETE FROM deployed_sensor;
ALTER SEQUENCE deployed_sensor_type_id_seq RESTART WITH 1;
INSERT INTO deployed_sensor(description, measurement_units) VALUES ('Kamstrup Flow Meter','m3/s');
INSERT INTO deployed_sensor(description, measurement_units) VALUES ('AlertMe Current Clamp Advance','kWh');
INSERT INTO deployed_sensor(description, measurement_units) VALUES ('AlertMe Current Clamp Power','kW');
INSERT INTO deployed_sensor(description, measurement_units) VALUES ('AlertMe Occupancy Sensor','counts');
INSERT INTO deployed_sensor(description, measurement_units) VALUES ('AlertMe Plug Meter','kWh');
INSERT INTO deployed_sensor(description, measurement_units) VALUES ('HW Joel-o-matic','Occupants');

DELETE FROM house;
ALTER SEQUENCE house_house_id_seq RESTART WITH 1;
INSERT INTO house(site_id, rooms, floors, occupants, central_heating_gas, cooking_gas) VALUES (1, 7, 2, 4, true, true);
INSERT INTO house(site_id, rooms, floors, occupants, central_heating_gas, cooking_gas) VALUES (1, 8, 2, 4, false, false);
INSERT INTO house(site_id, rooms, floors, occupants, central_heating_gas, cooking_gas) VALUES (2, 8, 2, 4, false, false);
INSERT INTO house(site_id, rooms, floors, occupants, central_heating_gas, cooking_gas) VALUES (2, 2, 1, 1, true, false);
INSERT INTO house(site_id, rooms, floors, occupants, central_heating_gas, cooking_gas) VALUES (3, 3, 1, 1, true, true);
INSERT INTO house(site_id, rooms, floors, occupants, central_heating_gas, cooking_gas) VALUES (3, 3, 1, 2, true, true);
INSERT INTO house(site_id, rooms, floors, occupants, central_heating_gas, cooking_gas) VALUES (4, 8, 2, 4, true, false);
INSERT INTO house(site_id, rooms, floors, occupants, central_heating_gas, cooking_gas) VALUES (4, 4, 1, 2, true, true);
INSERT INTO house(site_id, rooms, floors, occupants, central_heating_gas, cooking_gas) VALUES (4, 8, 2, 4, true, false);

DELETE FROM hub;
ALTER SEQUENCE hub_hub_id_seq RESTART WITH 1;
INSERT INTO hub(house_id, last_update, free_storage) VALUES (1, '2013-07-05 00:01:00', 0.6);
INSERT INTO hub(house_id, last_update, free_storage) VALUES (2, '2013-07-14 01:01:00', 0.1);
INSERT INTO hub(house_id, last_update, free_storage) VALUES (3, '2013-07-12 13:25:00', 0.4);
INSERT INTO hub(house_id, last_update, free_storage) VALUES (4, '2013-07-05 00:01:00', 0.66);
INSERT INTO hub(house_id, last_update, free_storage) VALUES (5, '2013-07-30 14:01:00', 0.25);
INSERT INTO hub(house_id, last_update, free_storage) VALUES (6, '2013-07-11 11:01:59', 0.3);
INSERT INTO hub(house_id, last_update, free_storage) VALUES (7, '2013-07-02 10:01:36', 0.3);
INSERT INTO hub(house_id, last_update, free_storage) VALUES (8, '2013-07-01 23:59:49', 0.01);

DELETE FROM hub_log;
ALTER SEQUENCE hub_log_hub_log_id_seq RESTART WITH 1;
INSERT INTO hub_log(hub_id, hub_log_message, hub_log_time, hub_log_code) VALUES (1,'Broadband connection timed out','2013-07-21 00:00:00', 2);
INSERT INTO hub_log(hub_id, hub_log_message, hub_log_time, hub_log_code) VALUES (1,'Broadband access restored','2013-07-21 00:50:00', 1);
INSERT INTO hub_log(hub_id, hub_log_message, hub_log_time, hub_log_code) VALUES (1,'Broadband connection timed out','2013-07-21 00:55:00', 2);

DELETE FROM sensor;
ALTER SEQUENCE sensor_sensor_id_seq RESTART WITH 1;
INSERT INTO sensor(hub_id, type_id, description) VALUES (1, 2, 'Property Incomer');
INSERT INTO sensor(hub_id, type_id, description) VALUES (1, 4, 'Living Room');
INSERT INTO sensor(hub_id, type_id, description) VALUES (1, 5, 'Dishwasher');
INSERT INTO sensor(hub_id, type_id, description) VALUES (1, 5, 'Washing Machine');
INSERT INTO sensor(hub_id, type_id, description) VALUES (2, 2, 'Property Incomer');
INSERT INTO sensor(hub_id, type_id, description) VALUES (3, 2, 'Property Incomer');
INSERT INTO sensor(hub_id, type_id, description) VALUES (4, 2, 'Property Incomer');
INSERT INTO sensor(hub_id, type_id, description) VALUES (5, 2, 'Property Incomer');

DELETE FROM measurement;
ALTER SEQUENCE measurement_measurement_id_seq RESTART WITH 1;
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 00:00:00',0.1);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 00:30:00',0.12);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 01:00:00',0.09);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 01:30:00',0.1);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 02:00:00',0.12);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 02:30:00',0.13);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 03:00:00',0.124);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 03:30:00',0.11);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 04:00:00',0.11);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 04:30:00',0.11);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 05:00:00',0.13);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 05:30:00',0.14);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 06:00:00',0.2);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 06:30:00',0.25);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 07:00:00',1.1);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 07:30:00',1.25);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 08:00:00',1.7);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 08:30:00',0.5);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 09:00:00',0.35);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 09:30:00',0.2);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 10:00:00',0.2);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 10:30:00',0.1);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 11:00:00',0.1);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 11:30:00',0.11);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 12:00:00',0.1);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 12:30:00',0.1);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 13:00:00',0.1);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 13:30:00',0.1);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 14:00:00',0.1);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 14:30:00',0.1);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 15:00:00',0.3);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 15:30:00',0.4);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 16:00:00',0.6);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 16:30:00',0.65);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 17:00:00',0.7);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 17:30:00',1.68);
INSERT INTO measurement(sensor_id, observation_time, observation) VALUES (1,'2013-07-13 18:00:00',2.5);

DELETE FROM weather;
ALTER SEQUENCE weather_weather_observation_id_seq RESTART WITH 1;
INSERT INTO weather(observation_time, site_id, wind_speed, wind_direction, ambient_temperature, humidity, uv, precipitation) VALUES ('2013-07-13 18:00:00',2,3.5,345.0,16.5,56.0,1.1,0.2);
INSERT INTO weather(observation_time, site_id, wind_speed, wind_direction, ambient_temperature, humidity, uv, precipitation) VALUES ('2013-07-13 18:30:00',2,3.25,340.0,14.5,56.0,1.0,0.2);
INSERT INTO weather(observation_time, site_id, wind_speed, wind_direction, ambient_temperature, humidity, uv, precipitation) VALUES ('2013-07-13 19:00:00',2,3.35,342.0,13.5,56.0,0.8,0.2);
INSERT INTO weather(observation_time, site_id, wind_speed, wind_direction, ambient_temperature, humidity, uv, precipitation) VALUES ('2013-07-13 19:30:00',2,3.45,342.0,12.5,56.0,0.75,0.2);
INSERT INTO weather(observation_time, site_id, wind_speed, wind_direction, ambient_temperature, humidity, uv, precipitation) VALUES ('2013-07-13 20:00:00',2,3.25,347.0,11.5,56.0,0.6,0.2);
INSERT INTO weather(observation_time, site_id, wind_speed, wind_direction, ambient_temperature, humidity, uv, precipitation) VALUES ('2013-07-13 20:30:00',2,3.15,348.0,11.5,56.0,0.4,0.2);
INSERT INTO weather(observation_time, site_id, wind_speed, wind_direction, ambient_temperature, humidity, uv, precipitation) VALUES ('2013-07-13 21:00:00',2,3.35,347.0,10.5,50.0,0.4,0.2);
INSERT INTO weather(observation_time, site_id, wind_speed, wind_direction, ambient_temperature, humidity, uv, precipitation) VALUES ('2013-07-13 21:30:00',2,3.15,347.0,11.5,50.0,0.4,0.2);
INSERT INTO weather(observation_time, site_id, wind_speed, wind_direction, ambient_temperature, humidity, uv, precipitation) VALUES ('2013-07-13 22:00:00',2,3.0,346.0,11.5,50.0,0.4,0.0);
INSERT INTO weather(observation_time, site_id, wind_speed, wind_direction, ambient_temperature, humidity, uv, precipitation) VALUES ('2013-07-13 22:30:00',2,3.01,345.0,11.5,50.0,0.4,0.0);
INSERT INTO weather(observation_time, site_id, wind_speed, wind_direction, ambient_temperature, humidity, uv, precipitation) VALUES ('2013-07-13 23:00:00',2,3.0,344.0,11.5,50.0,0.4,0.0);

