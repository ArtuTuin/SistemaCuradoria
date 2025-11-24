# Sistema de Curadoria e Compartilhamento de Recursos sobre IA
Projeto desktop em Java Swing com MySQL.

**Conteúdo do pacote**
- src/: código-fonte Java (Swing + JDBC)
- sql/schema.sql: script para criar o banco e um usuário admin inicial
- project_package.zip: pacote com todos os arquivos

**Instruções**
1. Instale MySQL e crie um banco chamado `curadoria`.
2. Execute `sql/schema.sql` para criar tabelas e inserir um admin.
3. Adicione o MySQL Connector/J ao classpath (jar).
4. Compile com `javac -d out src/**/*.java` e rode `java -cp out:mysql-connector-java.jar app.Main` (on Windows adjust classpath separator `;`).

**Admin inicial**
- email: admin@local
- senha: admin123 (hash incluído no SQL; troque após primeiro login)

**Notas**
- Senhas são armazenadas com SHA-256 + salt (simples). Para produção, use bcrypt/Argon2.
- Categorias fixas: IA, CIBER, PRIVACIDADE.
