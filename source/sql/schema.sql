-- Schema for curadoria project
CREATE DATABASE IF NOT EXISTS curadoria DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE curadoria;

CREATE TABLE IF NOT EXISTS usuario (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(100) NOT NULL,
  idade INT,
  tipo ENUM('ADMIN','COMUM') NOT NULL,
  email VARCHAR(100) NOT NULL UNIQUE,
  senha_hash VARCHAR(255) NOT NULL,
  salt VARCHAR(64) NOT NULL,
  ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS interesse_usuario (
  id INT AUTO_INCREMENT PRIMARY KEY,
  usuario_id INT NOT NULL,
  categoria ENUM('IA','CIBER','PRIVACIDADE') NOT NULL,
  FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS recurso (
  id INT AUTO_INCREMENT PRIMARY KEY,
  usuario_id INT NOT NULL,
  titulo VARCHAR(200) NOT NULL,
  autor VARCHAR(100),
  categoria ENUM('IA','CIBER','PRIVACIDADE') NOT NULL,
  FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
);

-- Seed admin user (email: admin@local, senha: admin123)
-- Uses SHA256(salt + password). Salt and hash below generated and inserted.
INSERT INTO usuario (nome, idade, tipo, email, senha_hash, salt, ativo)
VALUES ('Administrador', 30, 'ADMIN', 'admin@local', '9f02b427a22d3427ce52533f689aac3b8870bb9fe13a69911d979c2e7e94b0cb', 'df39195294b1586305e3f10a5c66b434', TRUE)
ON DUPLICATE KEY UPDATE nome=VALUES(nome);
