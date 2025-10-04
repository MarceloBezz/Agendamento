CREATE TABLE eventos (
    id VARCHAR(255) PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    inicio DATETIME NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_evento_usuario FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
        ON DELETE CASCADE
);
