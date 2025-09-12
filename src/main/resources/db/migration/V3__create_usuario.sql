CREATE TABLE usuario (
                         id SERIAL PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL,
                         email VARCHAR(100) NOT NULL UNIQUE,
                         role VARCHAR(50) NOT NULL,
                         senha VARCHAR(255) NOT NULL
);