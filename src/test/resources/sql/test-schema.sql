-- DROP das tabelas (ordem reversa para evitar erro de FK)
DROP TABLE IF EXISTS entity_nine CASCADE;
DROP TABLE IF EXISTS entity_eight CASCADE;
DROP TABLE IF EXISTS entity_seven CASCADE;
DROP TABLE IF EXISTS entity_six CASCADE;
DROP TABLE IF EXISTS entity_five CASCADE;
DROP TABLE IF EXISTS entity_four CASCADE;
DROP TABLE IF EXISTS entity_tree CASCADE;
DROP TABLE IF EXISTS entity_two CASCADE;
DROP TABLE IF EXISTS entity_one CASCADE;
DROP TABLE IF EXISTS entity_status CASCADE;

-- DROP de sequences
DROP SEQUENCE IF EXISTS seq_entity_one_id;
DROP SEQUENCE IF EXISTS seq_entity_status_id;

-- Criação de sequences
CREATE SEQUENCE seq_entity_one_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_entity_status_id START WITH 1 INCREMENT BY 1;

-- Criação das tabelas

CREATE TABLE entity_status (
    id_entity_status BIGINT AUTO_INCREMENT PRIMARY KEY,
    ativo BOOLEAN,
    name VARCHAR(255),
    start_date_time TIMESTAMP,
    status INTEGER
);

CREATE TABLE entity_five (
    id_entity_five UUID PRIMARY KEY,
    factor INTEGER NOT NULL,
    reference VARCHAR(255) NOT NULL,
    id_entity_status BIGINT REFERENCES entity_status(id_entity_status) ON DELETE SET NULL
);

CREATE TABLE entity_four (
    id_entity_four UUID PRIMARY KEY,
    attribute INTEGER,
    fruit VARCHAR(255) NOT NULL,
    inclusion_date_time TIMESTAMP NOT NULL,
    id_entity_status BIGINT REFERENCES entity_status(id_entity_status) ON DELETE SET NULL,
    id_entity_five UUID REFERENCES entity_five(id_entity_five) ON DELETE SET NULL
);

CREATE TABLE entity_tree (
    id_entity_tree UUID PRIMARY KEY,
    amount DOUBLE PRECISION NOT NULL,
    animal VARCHAR(255) NOT NULL,
    indicator INTEGER,
    local_date DATE NOT NULL,
    local_date_time TIMESTAMP NOT NULL,
    id_entity_status BIGINT REFERENCES entity_status(id_entity_status) ON DELETE SET NULL,
    id_entity_four UUID REFERENCES entity_four(id_entity_four) ON DELETE SET NULL
);

CREATE TABLE entity_two (
    id_entity_two UUID PRIMARY KEY,
    color VARCHAR(255) NOT NULL,
    cost DOUBLE PRECISION NOT NULL,
    hex INTEGER,
    inclusion_date DATE NOT NULL,
    id_entity_status BIGINT REFERENCES entity_status(id_entity_status) ON DELETE SET NULL,
    id_entity_tree UUID REFERENCES entity_tree(id_entity_tree) ON DELETE SET NULL
);

CREATE TABLE entity_one (
    id_entity_one BIGINT AUTO_INCREMENT PRIMARY KEY,
    age INTEGER,
    birth_date DATE NOT NULL,
    code BOOLEAN NOT NULL,
    height DOUBLE PRECISION NOT NULL,
    name VARCHAR(255) NOT NULL,
    prohibited_date_time TIMESTAMP NOT NULL,
    id_entity_status BIGINT REFERENCES entity_status(id_entity_status) ON DELETE SET NULL,
    id_entity_two UUID REFERENCES entity_two(id_entity_two) ON DELETE SET NULL
);

ALTER TABLE entity_four 
  ADD CONSTRAINT uq_entity_four_id_entity_five UNIQUE (id_entity_five);

ALTER TABLE entity_tree 
  ADD CONSTRAINT uq_entity_tree_id_entity_four UNIQUE (id_entity_four);

ALTER TABLE entity_two 
  ADD CONSTRAINT uq_entity_two_id_entity_tree UNIQUE (id_entity_tree);

ALTER TABLE entity_one 
  ADD CONSTRAINT uq_entity_one_id_entity_two UNIQUE (id_entity_two);

CREATE TABLE entity_six (
    id_entity_six INT AUTO_INCREMENT PRIMARY KEY,
    package_name VARCHAR(100) NOT NULL,
    start_date DATE NOT NULL,
    stop_date DATE NOT NULL
);

CREATE TABLE entity_seven (
    id_entity_seven UUID NOT NULL,
    id_entity_six INT NOT NULL,
    dado TEXT,
    PRIMARY KEY (id_entity_seven, id_entity_six),
    FOREIGN KEY (id_entity_six) REFERENCES entity_six(id_entity_six)
);

CREATE TABLE entity_eight (
    id_entity_eight INT AUTO_INCREMENT PRIMARY KEY,
    id_entity_seven UUID NOT NULL,
    id_entity_six INT NOT NULL,
    position VARCHAR(100) NOT NULL,
    properties VARCHAR(100),
    FOREIGN KEY (id_entity_seven, id_entity_six) REFERENCES entity_seven(id_entity_seven, id_entity_six)
);

CREATE TABLE entity_nine (
    id_entity_eight BIGINT NOT NULL,
    id_entity_seven UUID NOT NULL,
    id_entity_six BIGINT NOT NULL,
 	key_nine VARCHAR(100) NOT NULL,
	code VARCHAR(100),
	variable VARCHAR(100),
    PRIMARY KEY (id_entity_eight, id_entity_seven, id_entity_six),
	FOREIGN KEY (id_entity_eight) REFERENCES entity_eight(id_entity_eight),
    FOREIGN KEY (id_entity_seven, id_entity_six) REFERENCES entity_seven(id_entity_seven, id_entity_six)
);

-- Adiciona colunas como NULLABLE
ALTER TABLE entity_one ADD COLUMN id_entity_eight BIGINT;
ALTER TABLE entity_one ADD COLUMN id_entity_seven UUID;
ALTER TABLE entity_one ADD COLUMN id_entity_six BIGINT;

ALTER TABLE entity_one
ADD CONSTRAINT fk_entity_one_entity_nine FOREIGN KEY (id_entity_eight, id_entity_seven, id_entity_six)
REFERENCES entity_nine(id_entity_eight, id_entity_seven, id_entity_six)
ON DELETE NO ACTION;