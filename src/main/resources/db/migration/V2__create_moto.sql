CREATE TABLE moto (
                      id SERIAL PRIMARY KEY,
                      modelo VARCHAR(255) NOT NULL,
                      status VARCHAR(50),
                      posicao VARCHAR(255),
                      problema VARCHAR(255),
                      placa VARCHAR(50),
                      ala_id BIGINT,
                      CONSTRAINT fk_ala
                          FOREIGN KEY(ala_id)
                              REFERENCES ala(id)
                              ON DELETE SET NULL
);