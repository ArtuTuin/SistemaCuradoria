CREATE DATABASE IF NOT EXISTS curadoria_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE curadoria_db;

CREATE TABLE IF NOT EXISTS usuarios (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(150) NOT NULL,
  idade INT,
  email VARCHAR(150) UNIQUE NOT NULL,
  senha_hash VARCHAR(128) NOT NULL,
  tipo ENUM('ADMIN','USER') NOT NULL DEFAULT 'USER',
  ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS interesses (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(100) NOT NULL UNIQUE
);

INSERT INTO interesses (nome) VALUES
('IA Responsavel'),
('Ciberseguranca'),
('Privacidade & Etica Digital')
ON DUPLICATE KEY UPDATE nome=nome;

CREATE TABLE IF NOT EXISTS usuario_interesse (
  usuario_id INT NOT NULL,
  interesse_id INT NOT NULL,
  PRIMARY KEY (usuario_id, interesse_id),
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
  FOREIGN KEY (interesse_id) REFERENCES interesses(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS recursos (
  id INT AUTO_INCREMENT PRIMARY KEY,
  titulo VARCHAR(255) NOT NULL,
  autor VARCHAR(150),
  categoria VARCHAR(100) NOT NULL,
  usuario_id INT NOT NULL,
  criado_em DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Default admin (senha: admin123)
INSERT INTO usuarios (nome, idade, email, senha_hash, tipo, ativo) VALUES
('Admin Default', 25, 'admin@curadoria.local',
 '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9',
 'ADMIN', TRUE)
ON DUPLICATE KEY UPDATE nome=VALUES(nome);
