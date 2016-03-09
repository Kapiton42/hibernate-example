CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    first_name VARCHAR (128),
    last_name VARCHAR (128),
    money INTEGER CHECK (money > 0)
);