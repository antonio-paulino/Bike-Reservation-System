DROP TABLE IF EXISTS GPS CASCADE;
DROP TABLE IF EXISTS ClassicBike CASCADE;
DROP TABLE IF EXISTS ElectricBike CASCADE;
DROP TABLE IF EXISTS Bike CASCADE;
DROP TABLE IF EXISTS Person CASCADE;
DROP TABLE IF EXISTS Store CASCADE;
DROP TABLE IF EXISTS Reservation CASCADE;
DROP FUNCTION IF EXISTS isBikeAvailable;
DROP FUNCTION IF EXISTS createReservation;
DROP FUNCTION IF EXISTS personHasReservation;
DROP FUNCTION IF EXISTS reservationsAvailable;
DROP PROCEDURE IF EXISTS cancelReservation CASCADE;


CREATE TABLE GPS(
        serialNumber SERIAL PRIMARY KEY,
        latitude numeric(6,4) NOT null, -- coordenada
        longitude NUMERIC (6,4) NOT null, -- coordenada
        battery integer NOT null CHECK (battery BETWEEN 0 AND 100)
);

create table Bike (
      id SERIAL primary key,
      weight numeric(4,2) not null, -- em gramas
      model varchar(20) not null,
      brand varchar(30) not null,
      gear INT, CHECK (gear IN (1, 6, 18, 24)),
      state varchar(30) not null check (state in ('livre', 'ocupado', 'em reserva', 'em manutenção')),
      atrdisc CHAR(2) NOT NULL CHECK (atrdisc IN ('C', 'E')),
      gps integer UNIQUE REFERENCES GPS(serialNumber) ON DELETE CASCADE,
      CHECK (((gear IS NULL) AND (atrdisc = 'C')) or gear is not null),
      active boolean NOT NULL DEFAULT TRUE
);

CREATE TABLE ClassicBike(
     bike integer PRIMARY key REFERENCES Bike(id) ON DELETE CASCADE,
     gears integer CHECK (gears BETWEEN 0 AND 5) NOT null
);

CREATE TABLE ElectricBike(
      bike integer PRIMARY KEY REFERENCES Bike(id) ON DELETE CASCADE,
      autonomy integer NOT null, -- em km
      speed integer NOT null -- em km/h
);

CREATE TABLE Person (
    id SERIAL PRIMARY key,
    name varchar(40) NOT null,
    address varchar(150) NOT NULL,
    email varchar(40) UNIQUE NOT null check (email like '%@%'),
    phone varchar(20) UNIQUE NOT NULL,
    identification char(12) UNIQUE NOT NULL,
    nationality varchar(20),
    atrdisc char(2) NOT NULL CHECK (atrdisc IN ('G', 'C')),
    active boolean NOT NULL DEFAULT TRUE
);

CREATE TABLE Store (
    code SERIAL PRIMARY KEY,
    email varchar(40) UNIQUE NOT NULL,
    address varchar(100) NOT NULL,
    location varchar(30) NOT NULL,
    phone varchar(20) NOT NULL,
    manager integer REFERENCES Person(id) ON DELETE CASCADE,
    active boolean NOT NULL DEFAULT TRUE
);

CREATE TABLE Reservation (
    reservation_no serial NOT null,
    store integer NOT NULL REFERENCES Store (code) ON DELETE CASCADE,
    bike integer REFERENCES Bike(id) ON DELETE CASCADE,
    client integer REFERENCES Person(id) ON DELETE CASCADE,
    start_date timestamp NOT NULL,
    end_date timestamp CHECK ((start_date < end_date) or (end_date is null)),
    price numeric(4,2) NOT NULL, -- em euros
    PRIMARY KEY(reservation_no, store),
    version integer NOT NULL DEFAULT 0,
    active boolean NOT NULL DEFAULT TRUE
);

create or replace function isBikeAvailable(bike_id integer, start_time timestamp, end_time timestamp)
    returns boolean
as $$
declare
    bike_state varchar(30);
    overlapping_reservation integer;
begin
    select state into bike_state from Bike where id = bike_id;
    if bike_state is NULL then
        RAISE EXCEPTION 'Bike with id % not found', bike_id;
    elsif bike_state = 'livre' then
        return true;
    elsif bike_state = 'em reserva' then
        if start_time is NULL or end_time is NULL then
            RAISE EXCEPTION 'Start and end time must be provided';
        end if;
        select 1 into overlapping_reservation from Reservation
        where bike = bike_id and
        ((start_date <= start_time and end_date >= start_time) or
        (start_date <= end_time and end_date >= end_time));
        if overlapping_reservation is NULL then
            return true;
        else
            return false;
        end if;
    else
        return false;
    end if;
end;
$$ language plpgsql;

create or replace function createReservation(
    IN store_id integer,
    IN bike_id integer,
    IN client_id integer,
    IN start_date timestamp,
    IN end_date timestamp,
    IN price double precision,
    OUT reservation_number integer
) as $$
begin
    if not reservationsAvailable() then
        raise exception 'Cannot make any more reservations at this time';
    end if;
    if not isBikeAvailable(bike_id, start_date, end_date) then
        raise exception 'Bike is not available';
    end if;
    insert into Reservation(store, bike, client, start_date, end_date, price) values (store_id, bike_id, client_id, start_date, end_date, price)
    returning reservation_no into reservation_number;
    update Bike set state = 'em reserva' where id = bike_id;
end;
$$ language plpgsql;

CREATE OR REPLACE FUNCTION reservationsAvailable()
    RETURNS BOOLEAN AS $$
DECLARE
    total_bikes INTEGER;
    reserved_bikes INTEGER;
BEGIN
    SELECT COUNT(*) INTO total_bikes FROM Bike WHERE state IN ('livre', 'em reserva');
    SELECT COUNT(*) INTO reserved_bikes FROM Reservation WHERE active = TRUE;
    IF reserved_bikes >= total_bikes * 1.1 THEN
        RETURN TRUE;
    ELSE
        RETURN FALSE;
    END IF;
END;
$$ LANGUAGE plpgsql;

create OR REPLACE function personHasReservation(client_id integer, date timestamp)
    returns boolean as $$
declare
    reservation_id integer;
begin
    select reservation_no into reservation_id from Reservation where client = client_id and date = start_date;
    if reservation_id is NULL then
        return false;
    else
        return true;
    end if;
end;
$$ language plpgsql;

CREATE OR REPLACE FUNCTION cancelReservation()
    RETURNS TRIGGER AS $$
BEGIN
    UPDATE Bike SET state = 'livre' WHERE id = OLD.bike;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER cancelReservation
AFTER DELETE ON Reservation
FOR EACH ROW
EXECUTE FUNCTION cancelReservation();

-- Populate GPS
INSERT INTO GPS (latitude, longitude, battery) VALUES (41.14961, -8.61099, 100);
INSERT INTO GPS (latitude, longitude, battery) VALUES (41.14961, -8.61099, 100);
INSERT INTO GPS (latitude, longitude, battery) VALUES (41.14961, -8.61099, 100);

-- Populate Bike
INSERT INTO Bike (weight, model, brand, gear, state, atrdisc, gps, active) VALUES (10.5, 'Model1', 'Brand1', 1, 'livre', 'C', 1, TRUE);
INSERT INTO Bike (weight, model, brand, gear, state, atrdisc, gps, active) VALUES (12.5, 'Model2', 'Brand2', 6, 'livre', 'E', 2, TRUE);
INSERT INTO Bike (weight, model, brand, gear, state, atrdisc, gps, active) VALUES (15.5, 'Model3', 'Brand3', 18, 'livre', 'E', 3, TRUE);


-- Populate ClassicBike
INSERT INTO ClassicBike (bike, gears) VALUES (1, 5);

-- Populate ElectricBike
INSERT INTO ElectricBike (bike, autonomy, speed) VALUES (2, 100, 30);
INSERT INTO ElectricBike (bike, autonomy, speed) VALUES (3, 150, 40);

-- Populate Person
INSERT INTO Person (name, address, email, phone, identification, nationality, atrdisc, active) VALUES ('John Doe', '123 Street', 'john@example.com', '1234567890', 'ID123', 'USA', 'C', TRUE);
INSERT INTO Person (name, address, email, phone, identification, nationality, atrdisc, active) VALUES ('Jane Doe', '456 Avenue', 'jane@example.com', '0987654321', 'ID456', 'USA', 'G', TRUE);

-- Populate Store
INSERT INTO Store (email, address, location, phone, manager, active) VALUES ('store@example.com', '789 Boulevard', 'Location1', '1112223334', 2, TRUE);

-- Populate Reservation
INSERT INTO Reservation (store, bike, client, start_date, end_date, price, version, active) VALUES (1, 1, 1, '2023-01-01 00:00:00', '2023-01-02 00:00:00', 10.0, 0, TRUE);
INSERT INTO Reservation (store, bike, client, start_date, end_date, price, version, active) VALUES (1, 1, 1, '2023-02-01 00:00:00', '2023-02-02 00:00:00', 10.0, 0, TRUE);
INSERT INTO Reservation (store, bike, client, start_date, end_date, price, version, active) VALUES (1, 1, 1, '2023-03-01 00:00:00', '2023-03-02 00:00:00', 10.0, 0, TRUE);
INSERT INTO Reservation (store, bike, client, start_date, end_date, price, version, active) VALUES (1, 1, 1, '2023-04-01 00:00:00', '2023-04-02 00:00:00', 10.0, 0, TRUE);
INSERT INTO Reservation (store, bike, client, start_date, end_date, price, version, active) VALUES (1, 1, 1, '2023-05-01 00:00:00', '2023-06-02 00:00:00', 10.0, 0, TRUE);
INSERT INTO Reservation (store, bike, client, start_date, end_date, price, version, active) VALUES (1, 1, 1, '2023-06-01 00:00:00', '2023-06-02 00:00:00', 10.0, 0, TRUE);

UPDATE Bike SET state = 'em reserva' WHERE id = 1;

SELECT * FROM GPS;
SELECT * FROM Bike;
SELECT * FROM ClassicBike;
SELECT * FROM ElectricBike;
SELECT * FROM Person;
SELECT * FROM Store;
SELECT * FROM Reservation;
SELECT * FROM Bike;