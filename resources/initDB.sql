DROP TABLE IF EXISTS task;
DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(128)  NOT NULL,
    birthday        DATE          NOT NULL,
    registered_date DATE          NOT NULL,
    email           VARCHAR(128)  NOT NULL UNIQUE,
    password        VARCHAR(256)  NOT NULL,
    gender          VARCHAR(32)   NOT NULL,
    role            VARCHAR(128)  NOT NULL,
    image           VARCHAR(1024) NOT NULL
);

CREATE TABLE task
(
    id             BIGSERIAL PRIMARY KEY,
    user_id        INTEGER      NOT NULL,
    title          VARCHAR(128) NOT NULL,
    start_date   DATE         NOT NULL,
    end_date DATE         NOT NULL,
    completed_date DATE,
    status         VARCHAR(128) NOT NULL,
    priority VARCHAR(128) NOT NULL ,
    description    varchar(256),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);


