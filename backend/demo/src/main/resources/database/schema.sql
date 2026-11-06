CREATE TABLE IF NOT EXISTS categorias (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT uk_categorias_nome UNIQUE (nome)
);

CREATE TABLE IF NOT EXISTS produtos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(150) NOT NULL,
    codigo VARCHAR(50) NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    quantidade_estoque INT NOT NULL,
    estoque_minimo INT NOT NULL,
    categoria_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_produtos_codigo UNIQUE (codigo),
    CONSTRAINT chk_produtos_preco CHECK (preco > 0),
    CONSTRAINT chk_produtos_quantidade CHECK (quantidade_estoque >= 0),
    CONSTRAINT chk_produtos_estoque_minimo CHECK (estoque_minimo >= 0),
    CONSTRAINT fk_produtos_categoria
        FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);

CREATE TABLE IF NOT EXISTS movimentacoes_estoque (
    id BIGINT NOT NULL AUTO_INCREMENT,
    tipo VARCHAR(10) NOT NULL,
    quantidade INT NOT NULL,
    data_movimentacao DATETIME NOT NULL,
    observacao VARCHAR(255),
    produto_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT chk_movimentacao_tipo CHECK (tipo IN ('ENTRADA', 'SAIDA')),
    CONSTRAINT chk_movimentacao_quantidade CHECK (quantidade > 0),
    CONSTRAINT fk_movimentacao_produto
        FOREIGN KEY (produto_id) REFERENCES produtos(id)
);