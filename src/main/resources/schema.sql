CREATE TABLE client (
                    client_id SERIAL PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    last_name VARCHAR(255) NOT NULL
);

CREATE TABLE contact (
                    id SERIAL PRIMARY KEY,
                    phone VARCHAR(20) NOT NULL,
                    email VARCHAR(255) NOT NULL,
                    client_id INT REFERENCES client(client_id) ON DELETE CASCADE
);
