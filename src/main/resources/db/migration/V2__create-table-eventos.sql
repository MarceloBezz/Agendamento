CREATE TABLE evento (
    id VARCHAR(255) PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    inicio VARCHAR(255) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_evento_usuario FOREIGN KEY (usuario_id)
        REFERENCES usuario(id)
        ON DELETE CASCADE
);
