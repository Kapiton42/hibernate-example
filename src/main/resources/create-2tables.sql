CREATE TABLE real_estates (
    real_estate_id SERIAL PRIMARY KEY,
    address VARCHAR (128),
    owner_id INTEGER,
    cost INTEGER
);