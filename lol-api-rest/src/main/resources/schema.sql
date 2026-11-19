CREATE TABLE IF NOT EXISTS jogador (
    id BIGINT PRIMARY KEY,
    apelido VARCHAR(50) NOT NULL UNIQUE,
    regiao VARCHAR(50) NOT NULL,
    data_cadastro TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS campeao (
    id BIGINT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    classe_principal VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS item (
    id BIGINT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    custo INT NOT NULL,
    tipo VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS runa (
    id BIGINT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    caminho VARCHAR(50) NOT NULL,
    tipo VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS partida (
    id BIGINT PRIMARY KEY,
    jogador_id BIGINT NOT NULL,
    campeao_id BIGINT NOT NULL,
    data TIMESTAMP NOT NULL,
    duracao_min INT NOT NULL,
    role VARCHAR(50) NOT NULL,
    abates INT NOT NULL,
    mortes INT NOT NULL,
    assistencias INT NOT NULL,
    resultado VARCHAR(50) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    pdl_variacao INT NULL,
    modo_jogo VARCHAR(50) NULL,

    CONSTRAINT fk_partida_jogador FOREIGN KEY (jogador_id) REFERENCES jogador(id),
    CONSTRAINT fk_partida_campeao FOREIGN KEY (campeao_id) REFERENCES campeao(id)
);

CREATE TABLE IF NOT EXISTS partida_item (
    partida_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    PRIMARY KEY (partida_id, item_id),
    CONSTRAINT fk_pi_partida FOREIGN KEY (partida_id) REFERENCES partida(id),
    CONSTRAINT fk_pi_item    FOREIGN KEY (item_id)    REFERENCES item(id)
);

CREATE TABLE IF NOT EXISTS partida_runa (
    partida_id BIGINT NOT NULL,
    runa_id BIGINT NOT NULL,
    PRIMARY KEY (partida_id, runa_id),
    CONSTRAINT fk_pr_partida FOREIGN KEY (partida_id) REFERENCES partida(id),
    CONSTRAINT fk_pr_runa    FOREIGN KEY (runa_id)    REFERENCES runa(id)
);