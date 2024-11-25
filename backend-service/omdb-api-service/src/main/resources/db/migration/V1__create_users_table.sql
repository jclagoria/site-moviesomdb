CREATE TABLE user_app (
                       id SERIAL PRIMARY KEY,
                       username TEXT NOT NULL UNIQUE,
                       email TEXT NOT NULL UNIQUE,
                       password_hash TEXT NOT NULL
);

CREATE TABLE tokens (
                        id SERIAL PRIMARY KEY,
                        user_id BIGINT REFERENCES user_app (id),
                        token TEXT NOT NULL,
                        is_active BOOLEAN DEFAULT FALSE
);
