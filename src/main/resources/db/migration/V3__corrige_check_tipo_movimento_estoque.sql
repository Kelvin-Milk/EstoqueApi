ALTER TABLE movimento_estoque DROP CONSTRAINT ck_movimento_estoque_tipo;

ALTER TABLE movimento_estoque
    ADD CONSTRAINT ck_movimento_estoque_tipo CHECK (tipo = 'ENTRADA' OR tipo = 'SAIDA');