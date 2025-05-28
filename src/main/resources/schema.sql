CREATE TABLE IF NOT EXISTS "users" (
        id BIGSERIAL PRIMARY KEY,
        first_name VARCHAR(50),
        last_name VARCHAR(50),
        username VARCHAR(50) UNIQUE,
        user_id BIGINT NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS message (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    message TEXT NOT NULL,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_user
        FOREIGN KEY(user_id)
        REFERENCES "users"(id)
        ON DELETE CASCADE
    );
