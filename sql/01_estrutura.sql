-- Execute em um esquema Oracle vazio destinado ao Vagaí.

CREATE TABLE tb_usuario (
    id_usuario NUMBER(10) PRIMARY KEY,
    nm_usuario VARCHAR2(40 CHAR) NOT NULL UNIQUE,
    ds_senha_hash VARCHAR2(255 CHAR) NOT NULL,
    dt_cadastro DATE DEFAULT SYSDATE NOT NULL
);
COMMENT ON TABLE tb_usuario IS 'Contas de motoristas';
CREATE SEQUENCE sq_tb_usuario START WITH 1 INCREMENT BY 1 NOCYCLE;

CREATE TABLE tb_estacionamento (
    id_estacionamento NUMBER(10) PRIMARY KEY,
    id_usuario_criador NUMBER(10) REFERENCES tb_usuario(id_usuario) ON DELETE SET NULL,
    nm_estacionamento VARCHAR2(100 CHAR) NOT NULL,
    ds_estacionamento VARCHAR2(255 CHAR),
    tp_estacionamento VARCHAR2(20 CHAR) NOT NULL CHECK (tp_estacionamento IN ('GRATUITO','ROTATIVO','PRIVADO')),
    nm_rua VARCHAR2(150 CHAR) NOT NULL,
    nm_bairro VARCHAR2(100 CHAR) NOT NULL,
    ds_numero VARCHAR2(20 CHAR) NOT NULL,
    vl_primeira_hora NUMBER(10,2) CHECK (vl_primeira_hora >= 0),
    vl_demais_horas NUMBER(10,2) CHECK (vl_demais_horas >= 0),
    st_ativo NUMBER(1) NOT NULL CHECK (st_ativo IN (0,1)),
    CONSTRAINT ck_gratuito CHECK (tp_estacionamento <> 'GRATUITO' OR
        (NVL(vl_primeira_hora,0) = 0 AND NVL(vl_demais_horas,0) = 0))
);
COMMENT ON TABLE tb_estacionamento IS 'Locais cadastrados pelos usuarios para listagem por rua e bairro';
CREATE SEQUENCE sq_tb_estacionamento START WITH 1 INCREMENT BY 1 NOCYCLE;

CREATE TABLE tb_reporte (
    id_reporte NUMBER(10) PRIMARY KEY,
    id_usuario NUMBER(10) NOT NULL REFERENCES tb_usuario(id_usuario),
    id_estacionamento NUMBER(10) NOT NULL REFERENCES tb_estacionamento(id_estacionamento),
    tp_reporte VARCHAR2(20 CHAR) NOT NULL CHECK (tp_reporte IN ('VAGA_ENCONTRADA','SEM_VAGA')),
    ds_observacao VARCHAR2(255 CHAR),
    dt_reporte DATE DEFAULT SYSDATE NOT NULL
);
COMMENT ON TABLE tb_reporte IS 'Histórico de relatos de disponibilidade';
CREATE SEQUENCE sq_tb_reporte START WITH 1 INCREMENT BY 1 NOCYCLE;

CREATE TABLE tb_avaliacao (
    id_avaliacao NUMBER(10) PRIMARY KEY,
    id_usuario NUMBER(10) NOT NULL REFERENCES tb_usuario(id_usuario),
    id_estacionamento NUMBER(10) NOT NULL REFERENCES tb_estacionamento(id_estacionamento),
    nr_nota NUMBER(1) NOT NULL CHECK (nr_nota BETWEEN 1 AND 5),
    ds_comentario VARCHAR2(500 CHAR),
    dt_avaliacao DATE DEFAULT SYSDATE NOT NULL,
    CONSTRAINT uk_avaliacao UNIQUE (id_usuario, id_estacionamento)
);
COMMENT ON TABLE tb_avaliacao IS 'Notas e comentários sobre locais';
CREATE SEQUENCE sq_tb_avaliacao START WITH 1 INCREMENT BY 1 NOCYCLE;

CREATE TABLE tb_regra_estacionamento (
    id_regra NUMBER(10) PRIMARY KEY,
    id_estacionamento NUMBER(10) NOT NULL REFERENCES tb_estacionamento(id_estacionamento),
    ds_regra VARCHAR2(255 CHAR) NOT NULL,
    nr_dia_semana NUMBER(1) NOT NULL CHECK (nr_dia_semana BETWEEN 1 AND 7),
    nr_inicio NUMBER(4) NOT NULL CHECK (nr_inicio BETWEEN 0 AND 1439),
    nr_fim NUMBER(4) NOT NULL CHECK (nr_fim BETWEEN 0 AND 1439),
    tp_regra VARCHAR2(10 CHAR) NOT NULL CHECK (tp_regra IN ('PERMITIDO','PROIBIDO')),
    st_ativo NUMBER(1) NOT NULL CHECK (st_ativo IN (0,1))
);
COMMENT ON TABLE tb_regra_estacionamento IS 'Restrições e horários por local';
CREATE SEQUENCE sq_tb_regra_estacionamento START WITH 1 INCREMENT BY 1 NOCYCLE;

CREATE TABLE tb_usuario_favorito (
    id_usuario_favorito NUMBER(10) PRIMARY KEY,
    id_usuario NUMBER(10) NOT NULL REFERENCES tb_usuario(id_usuario),
    id_estacionamento NUMBER(10) NOT NULL REFERENCES tb_estacionamento(id_estacionamento),
    dt_favorito DATE DEFAULT SYSDATE NOT NULL,
    CONSTRAINT uk_favorito UNIQUE (id_usuario, id_estacionamento)
);
COMMENT ON TABLE tb_usuario_favorito IS 'Associação entre motorista e local favorito';
CREATE SEQUENCE sq_tb_usuario_favorito START WITH 1 INCREMENT BY 1 NOCYCLE;

CREATE INDEX ix_reporte_local_data ON tb_reporte (id_estacionamento, dt_reporte);
COMMENT ON COLUMN tb_estacionamento.st_ativo IS '1 ativo, 0 inativo';
COMMENT ON COLUMN tb_usuario.ds_senha_hash IS 'Hash PBKDF2 com salt individual';
COMMENT ON COLUMN tb_regra_estacionamento.nr_dia_semana IS '1 segunda-feira a 7 domingo; dia de inicio do intervalo';
COMMENT ON COLUMN tb_regra_estacionamento.nr_inicio IS 'Minutos desde 00h00, limite inicial incluido';
COMMENT ON COLUMN tb_regra_estacionamento.nr_fim IS 'Limite final excluido; menor que inicio atravessa meia-noite; igual representa 24 horas';
