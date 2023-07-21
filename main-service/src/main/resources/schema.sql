drop table IF EXISTS PUBLIC.EVENTS;
drop table IF EXISTS PUBLIC.USERS;
drop table IF EXISTS PUBLIC.CATEGORIES;
drop table IF EXISTS PUBLIC.LOCATIONS;

create TABLE IF NOT EXISTS users(
  id      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  email   VARCHAR(254) NOT NULL UNIQUE,
  name    VARCHAR(250) NOT NULL
);

create TABLE IF NOT EXISTS categories(
  id      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name    VARCHAR(250) NOT NULL UNIQUE
);

create TABLE IF NOT EXISTS locations(
  id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  lat    FLOAT NOT NULL,
  lon    FLOAT NOT NULL
);

create TABLE IF NOT EXISTS events(
id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
annotation         VARCHAR(2000) NOT NULL,
category_id        BIGINT REFERENCES categories(id),
--confirmed_requests INT NOT NULL,
created_on         TIMESTAMP,
description        VARCHAR(7000) NOT NULL,
event_date         TIMESTAMP,
initiator_id       BIGINT NOT NULL REFERENCES users(id),
location_id        BIGINT NOT NULL REFERENCES locations(id),
paid               BOOLEAN,
participant_limit  INTEGER,
published_on       TIMESTAMP,
request_moderation BOOLEAN,
state              VARCHAR(255) NOT NULL,
title              VARCHAR(120) NOT NULL
);