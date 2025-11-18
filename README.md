# Coffee Sulwork

**Desafio Técnico:** Aplicação desenvolvida como parte de um teste para avaliação de habilidades em Angular (frontend), Spring Boot (backend) e PostgreSQL, utilizando Docker para orquestração dos serviços.  

O objetivo da aplicação é organizar cafés da manhã entre colaboradores, evitando duplicidades de itens, datas e participantes. Todos os serviços são executados via Docker e estão hospedados no Railway.

---

## Deploy

Frontend (Cadastro):  
https://frontend-production-2a69.up.railway.app/cadastro

API (Swagger):  
https://coffee-challenge-sulwork-production.up.railway.app/swagger-ui/index.html

---

## Tecnologias Utilizadas

### Frontend
- Angular
- TypeScript
- HTML / CSS
- Serviços com HttpClient
- Estrutura para testes (Jasmine/Jest)

### Backend
- Java 17
- Spring Boot
- JPA com NativeQuery para todas as operações CRUD
- Validações customizadas
- Testes unitários e de integração com JUnit 5

### Infraestrutura
- PostgreSQL
- Docker e Docker Compose
- Railway para deploy

---

## Como Executar Localmente

### 1. Clonar o repositório
```bash
git clone https://github.com/Lalset/coffee-challenge-sulwork.git
cd coffee-challenge-sulwork
```
## Executar com Docker (recomendo)
docker compose up --build

Isso iniciará:

- Backend (porta 8080)

- Frontend (porta 4200)

- Banco PostgreSQL (porta 5432)

### 2. Acessar

Frontend:
http://localhost:4200

Backend:
http://localhost:8080/api/colaboradores

Swagger:
http://localhost:8080/swagger-ui/index.html

## Regras de Negócio

- Não é permitido repetir colaborador.

- CPF não pode ser repetido e deve possuir 11 dígitos válidos.

- Não é permitido repetir uma opção de café na mesma data, mesmo que seja outro colaborador.

- A data do café deve ser superior à data atual.

- Um colaborador pode trazer mais de um item no mesmo dia.

- Após a data do café, itens não entregues devem ser marcados como não trazidos.

## Testes
## Backend

- JUnit 5

- Cobertura das regras de negócio

## Frontend

Estrutura configurada para testes com Jasmine/Jest

Possível integração com Cypress para testes e2e

# Documentação da API
Swagger disponível no deploy:
https://coffee-challenge-sulwork-production.up.railway.app/swagger-ui/index.html

## Docker

O projeto possui:

- Dockerfile do backend

- Dockerfile do frontend

- docker-compose.yml unificando:

- frontend

- backend

- banco PostgreSQL

## Autor
- Talles Ian
- https://github.com/Lalset/
