DROP TABLE IF EXISTS desaparecidos;

CREATE TABLE desaparecidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255),
    data_nascimento DATE,
    descricao TEXT,
    status VARCHAR(50)
);