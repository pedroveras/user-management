# user-management
### Stack:
- DB: Postgresql 17.2
- API: JAVA JDK 21 + SpringBoot 3.4.1 + Flyway
- WEB: Angular 19.6 + NODE 22.10.0

## Comando docker para rodar o banco de dados Postgres (usuário postgres, senha admin e banco usermanagement)
    docker run --name postgres17-container -e POSTGRES_PASSWORD=admin -e POSTGRES_USER=postgres -e POSTGRES_DB=usermanagement -p 5432:5432 -d postgres:17

## API (user-management-back) execução local sem IDE (deve-se instalar a versão 21 do java)
    ./mvnw spring-boot:run

## FRONT (user-management-front) execução local sem IDE (deve-se instalar node 22.10.0)
    executar comando no diretório do projeto: npm install
    npm run start
    http://localhost:4200

## Acesso Admin
    Admin: admin/12345
Outros usuários serão criados pelo admin 


Melhorias futuras:
 * Criar CI/CD via Actions
 * Melhorar a interface
 * Monitoria da aplicação
 * Criar environments para desenvolvimento e produção 
