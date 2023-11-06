ALTER TABLE category
    ADD CONSTRAINT uc_category_name UNIQUE (name);

ALTER TABLE make
    ADD CONSTRAINT uc_make_name UNIQUE (name);