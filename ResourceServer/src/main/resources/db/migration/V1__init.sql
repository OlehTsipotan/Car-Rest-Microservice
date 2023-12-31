CREATE SEQUENCE IF NOT EXISTS car_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS category_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS make_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE car
(
    car_id  BIGINT       NOT NULL,
    make_id BIGINT       NOT NULL,
    year    INTEGER      NOT NULL,
    model   VARCHAR(255) NOT NULL,
    CONSTRAINT pk_car PRIMARY KEY (car_id)
);

CREATE TABLE cars_categories
(
    car_id      BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    CONSTRAINT pk_cars_categories PRIMARY KEY (car_id, category_id)
);

CREATE TABLE category
(
    category_id BIGINT       NOT NULL,
    name        VARCHAR(255) NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (category_id)
);

CREATE TABLE make
(
    make_id BIGINT       NOT NULL,
    name    VARCHAR(255) NOT NULL,
    CONSTRAINT pk_make PRIMARY KEY (make_id)
);

ALTER TABLE category
    ADD CONSTRAINT uc_category_name UNIQUE (name);

ALTER TABLE make
    ADD CONSTRAINT uc_make_name UNIQUE (name);

ALTER TABLE car
    ADD CONSTRAINT FK_CAR_ON_MAKE FOREIGN KEY (make_id) REFERENCES make (make_id);

ALTER TABLE cars_categories
    ADD CONSTRAINT fk_carcat_on_car FOREIGN KEY (car_id) REFERENCES car (car_id);

ALTER TABLE cars_categories
    ADD CONSTRAINT fk_carcat_on_category FOREIGN KEY (category_id) REFERENCES category (category_id);