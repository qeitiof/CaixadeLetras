# 📦 Caixa de Letras

Sistema de avaliação de **filmes, séries e livros**, inspirado no Letterboxd.  
Desenvolvido como projeto acadêmico para praticar **desenvolvimento backend com Java e Spring Boot**.

## 🎯 Objetivo

Permitir que os usuários cadastrem obras (filmes, séries ou livros), deixem notas e comentários, e visualizem avaliações de outros usuários.

## 🛠️ Tecnologias utilizadas

### Backend
- **Java 17+**
- **Spring Boot 3.5.7**
- **Spring Web**
- **Spring Data JPA**
- **Spring Security**
- **H2 Database** (para ambiente local)
- **Maven**

### Frontend
- **HTML5**
- **CSS3**
- **JavaScript (Vanilla)**

## 📁 Estrutura do Projeto

```
CaixadeLetras/
├── ApiLetter/              # Backend Spring Boot (Maven)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/ApiLetter/
│   │   │   │       ├── config/          # Configurações (Security)
│   │   │   │       ├── controller/      # Controllers REST
│   │   │   │       ├── dto/             # Data Transfer Objects
│   │   │   │       ├── model/           # Entidades JPA
│   │   │   │       ├── repository/      # Repositórios JPA
│   │   │   │       └── service/         # Lógica de negócio
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   ├── pom.xml
│   └── mvnw, mvnw.cmd
├── frontend/                # Frontend (HTML, CSS, JS)
│   ├── index.html
│   ├── script.js
│   └── style.css
├── package.json
└── README.md
```

## 📚 Funcionalidades principais

### Usuários
- ✅ CRUD completo (Create, Read, Update, Delete)
- ✅ Cadastro de usuários com validação de senha forte
- ✅ Login e autenticação
- ✅ Sistema de seguir/seguidores
- ✅ Perfil de usuário
- ✅ Paginação e ordenação na listagem
- ✅ Filtros de busca por username e email

### Filmes
- ✅ CRUD completo (Create, Read, Update, Delete)
- ✅ Busca de filmes via API externa (OMDB)
- ✅ Sugestões de filmes
- ✅ Visualização de detalhes
- ✅ Paginação e ordenação na listagem
- ✅ Filtros de busca por título, ano e imdbId

### Avaliações (Reviews)
- ✅ CRUD completo (Create, Read, Update, Delete)
- ✅ Avaliação com notas (1-5) e comentários
- ✅ Validação de dados de entrada
- ✅ Listagem das avaliações por obra ou por usuário
- ✅ Paginação e ordenação na listagem
- ✅ Filtros de busca por nota mínima/máxima, usuário e filme

### Watchlists
- ✅ CRUD completo (Create, Read, Update, Delete)
- ✅ Criar múltiplas watchlists por usuário
- ✅ Adicionar/remover filmes das watchlists
- ✅ Visualizar watchlists próprias e de outros usuários
- ✅ Controle de permissões (apenas o dono pode modificar)
- ✅ Sistema de inativação/ativação
- ✅ Rastreamento de última atualização
- ✅ Listagem de watchlists inativas por mais de uma semana

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas:

- **Controller**: Recebe requisições HTTP e retorna respostas
- **Service**: Contém a lógica de negócio
- **Repository**: Gerencia acesso aos dados (JPA)
- **DTO**: Objetos de transferência de dados para entrada/saída
- **Model**: Entidades JPA que representam as tabelas do banco

## 📊 Entidades e Relacionamentos

### Entidades principais:
1. **User** - Usuários do sistema
2. **Movie** - Filmes cadastrados
3. **Review** - Avaliações de filmes
4. **Watchlist** - Listas de filmes dos usuários
5. **WatchlistChange** - Histórico de mudanças nas watchlists
6. **Follow** - Relacionamento de seguir usuários

### Relacionamentos:
- **User 1:N Review** - Um usuário pode ter várias avaliações
- **Movie 1:N Review** - Um filme pode ter várias avaliações
- **User 1:N Watchlist** - Um usuário pode ter várias watchlists
- **Watchlist 1:N WatchlistChange** - Uma watchlist tem várias mudanças
- **Movie N:N Watchlist** (via WatchlistChange) - Muitos filmes em muitas watchlists
- **User N:N User** (via Follow) - Usuários podem seguir outros usuários

## 🔌 API REST - Endpoints

### Usuários (`/users`)
- `GET /users` - Lista todos (com paginação, ordenação e filtros)
- `GET /users/{id}` - Busca por ID
- `POST /users` - Cria novo usuário (retorna 201)
- `PUT /users/{id}` - Atualiza usuário
- `DELETE /users/{id}` - Deleta usuário (retorna 204)
- `POST /users/login` - Login
- `POST /users/{id}/follow` - Seguir usuário
- `GET /users/{id}/followers` - Lista seguidores
- `GET /users/{id}/following` - Lista quem está seguindo

### Filmes (`/movies`)
- `GET /movies` - Lista todos (com paginação, ordenação e filtros)
- `GET /movies/{id}` - Busca por ID
- `POST /movies` - Cria novo filme (retorna 201)
- `PUT /movies/{id}` - Atualiza filme
- `DELETE /movies/{id}` - Deleta filme (retorna 204)
- `GET /movies/search?titulo=...` - Busca na API OMDB
- `GET /movies/suggest?titulo=...` - Sugestões de filmes

### Avaliações (`/reviews`)
- `GET /reviews` - Lista todas (com paginação, ordenação e filtros)
- `GET /reviews/{id}` - Busca por ID
- `POST /reviews` - Cria nova avaliação (retorna 201)
- `PUT /reviews/{id}` - Atualiza avaliação
- `DELETE /reviews/{id}` - Deleta avaliação (retorna 204)
- `GET /reviews/movie/{imdbId}` - Lista avaliações de um filme
- `GET /reviews/user/{userId}` - Lista avaliações de um usuário

### Watchlists (`/watchlists`)
- `GET /watchlists` - Lista todas (com paginação, ordenação e filtros)
- `GET /watchlists/user/{userId}` - Lista watchlists de um usuário
- `GET /watchlists/{id}` - Busca watchlist por ID
- `POST /watchlists` - Cria nova watchlist (retorna 201)
- `POST /watchlists/add-movie` - Adiciona filme à watchlist
- `DELETE /watchlists/{id}/movies/{movieId}` - Remove filme da watchlist
- `DELETE /watchlists/{id}` - Deleta watchlist (retorna 204)
- `GET /watchlists/inativos` - Lista watchlists inativas por mais de uma semana
- `PUT /watchlists/{id}/inativar?userId={userId}` - Inativa uma watchlist
- `PUT /watchlists/{id}/ativar?userId={userId}` - Ativa uma watchlist
- `GET /watchlists/{id}/historico` - Consulta histórico de mudanças de uma watchlist

## 📋 Paginação e Ordenação

Todos os endpoints `GET ALL` suportam paginação e ordenação usando Spring Data:

**Exemplo:**
```
GET /users?page=0&size=10&sort=username,asc
GET /movies?page=0&size=20&sort=year,desc&sort=titulo,asc
GET /reviews?page=0&size=5&sort=id,desc
```

**Parâmetros:**
- `page` - Número da página (começa em 0)
- `size` - Tamanho da página (padrão: 10)
- `sort` - Campo para ordenação (pode repetir para múltiplos campos)

## 🔍 Filtros de Busca

### Usuários
- `username` - Busca parcial por nome de usuário
- `email` - Busca parcial por email

**Exemplo:**
```
GET /users?username=joao&email=gmail
GET /users?page=0&size=10&sort=username,asc&username=joao
```

### Filmes
- `titulo` - Busca parcial por título
- `year` - Busca exata por ano
- `imdbId` - Busca exata por imdbId

**Exemplo:**
```
GET /movies?titulo=matrix&year=1999
GET /movies?page=0&size=20&sort=year,desc&titulo=matrix
```

### Avaliações
- `notaMin` - Nota mínima (1-5)
- `notaMax` - Nota máxima (1-5)
- `userId` - ID do usuário
- `imdbId` - ID do filme

**Exemplo:**
```
GET /reviews?notaMin=4&notaMax=5&userId=1
GET /reviews?page=0&size=5&sort=id,desc&notaMin=4
```

### Watchlists
- `userId` - ID do usuário (busca exata)
- `name` - Busca parcial por nome da watchlist
- `active` - Status ativo/inativo (true/false)

**Exemplo:**
```
GET /watchlists?userId=1&active=true
GET /watchlists?page=0&size=10&sort=id,desc&name=assistir&active=true
```

## ✅ Validação de Dados

O projeto utiliza validação Bean Validation (`@Valid`, `@NotNull`, `@NotBlank`, `@Min`, `@Max`, `@Email`, `@Size`):

### DTOs com Validação:

- **UserCreateDTO**: 
  - `username`: obrigatório, entre 3 e 50 caracteres
  - `email`: obrigatório, formato de email válido
  - `password`: obrigatório, mínimo 8 caracteres

- **UserUpdateDTO**: 
  - `username`: opcional, entre 3 e 50 caracteres (se fornecido)
  - `email`: opcional, formato de email válido (se fornecido)
  - `password`: opcional, mínimo 8 caracteres (se fornecido)

- **UserLoginDTO**: 
  - `username`: obrigatório
  - `password`: obrigatório

- **MovieCreateDTO**: 
  - `titulo`: obrigatório, máximo 255 caracteres
  - `imdbId`: obrigatório
  - `year`: opcional
  - `poster`: opcional

- **MovieUpdateDTO**: 
  - `titulo`: opcional, máximo 255 caracteres (se fornecido)
  - `imdbId`: opcional
  - `year`: opcional
  - `poster`: opcional

- **ReviewCreateDTO**: 
  - `imdbId`: obrigatório
  - `nota`: obrigatório, entre 1 e 5
  - `userId`: obrigatório
  - `comentario`: opcional

- **ReviewUpdateDTO**: 
  - `nota`: obrigatório, entre 1 e 5
  - `comentario`: opcional

- **WatchlistCreateDTO**: 
  - `name`: obrigatório
  - `userId`: obrigatório

- **AddMovieToWatchlistDTO**: 
  - `watchlistId`: obrigatório
  - `imdbId`: obrigatório
  - `userId`: obrigatório

Erros de validação retornam status **400 Bad Request** com mensagem de erro detalhada através do `GlobalExceptionHandler`.

## 📝 Códigos HTTP Utilizados

- **200 OK** - Sucesso em operações GET, PUT
- **201 Created** - Recurso criado com sucesso (POST)
- **204 No Content** - Sucesso sem conteúdo (DELETE)
- **400 Bad Request** - Dados inválidos ou erro de validação
- **404 Not Found** - Recurso não encontrado
- **409 Conflict** - Conflito (ex: recurso já existe)
- **500 Internal Server Error** - Erro interno do servidor

## 🔧 Tratamento de Erros

O projeto implementa um **GlobalExceptionHandler** centralizado que trata todos os erros da aplicação de forma consistente:

### Tipos de Exceções Tratadas:

1. **MethodArgumentNotValidException** - Erros de validação Bean Validation
   - Retorna: `400 Bad Request`
   - Formato: `{ "status": 400, "error": "Erro de validação", "message": "...", "timestamp": "..." }`

2. **IllegalArgumentException** - Argumentos inválidos
   - Retorna: `400 Bad Request`

3. **ResourceNotFoundException** - Recurso não encontrado (exceção customizada)
   - Retorna: `404 Not Found`

4. **ConflictException** - Conflito (exceção customizada)
   - Retorna: `409 Conflict`

5. **RuntimeException** - Erros de runtime
   - Analisa a mensagem para determinar se é 404 ou 400
   - Retorna: `404 Not Found` ou `400 Bad Request`

6. **Exception** - Erros genéricos
   - Retorna: `500 Internal Server Error`

### Formato de Resposta de Erro:

```json
{
  "status": 400,
  "error": "Erro de validação",
  "message": "O username é obrigatório",
  "timestamp": "2024-01-15T10:30:00"
}
```

## 🔄 Sistema de Inativação/Ativação (Watchlists)

O sistema de watchlists possui funcionalidade de inativação/ativação:

### Funcionalidades:
- **Última Atualização**: Cada watchlist possui um campo `lastUpdate` que é atualizado automaticamente quando:
  - Um filme é adicionado
  - Um filme é removido
  - A watchlist é ativada/inativada

- **Status Ativo/Inativo**: Cada watchlist possui um campo `active` (boolean) que indica se está ativa ou inativa.

- **Listagem de Inativas**: O endpoint `/watchlists/inativos` retorna todas as watchlists que estão inativas há mais de uma semana.

### Exemplo de uso:
```bash
# Listar watchlists inativas por mais de uma semana
GET /watchlists/inativos

# Inativar uma watchlist
PUT /watchlists/1/inativar?userId=1

# Ativar uma watchlist
PUT /watchlists/1/ativar?userId=1
```

## 🔐 Variáveis de Ambiente

O projeto utiliza o arquivo `application.properties` para configurações. Um arquivo de exemplo `env.example` está disponível na raiz do projeto `ApiLetter/` com todas as variáveis de ambiente necessárias.

### Arquivo env.example

O arquivo `env.example` contém:

```env
# Configurações do Banco de Dados H2
SPRING_DATASOURCE_URL=jdbc:h2:mem:apiletterdb
SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.h2.Driver
SPRING_DATASOURCE_USERNAME=sa
SPRING_DATASOURCE_PASSWORD=

# Configurações do H2 Console
SPRING_H2_CONSOLE_ENABLED=true
SPRING_H2_CONSOLE_PATH=/h2-console

# Configurações JPA/Hibernate
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=true
SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL=true
SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.H2Dialect

# Chave da API OMDB
OMDB_API_KEY=e110f0dc

# Configurações do Servidor
SERVER_PORT=8080
```

**Nota:** O Spring Boot não lê arquivos `.env` nativamente. Para usar variáveis de ambiente, você pode:
1. Configurar as variáveis no sistema operacional
2. Usar um plugin como `spring-boot-dotenv` (não incluído no projeto)
3. Continuar usando o `application.properties` (recomendado para este projeto)

**Configurações principais:**
- Banco de dados H2 (em memória)
- Chave da API OMDB
- Configurações JPA/Hibernate

## 🚀 Como executar

### Backend (ApiLetter)

1. Navegue até o diretório do backend:
```bash
cd ApiLetter
```

2. Execute o projeto com Maven:
```bash
./mvnw spring-boot:run
```

Ou no Windows:
```bash
mvnw.cmd spring-boot:run
```

O backend estará disponível em `http://localhost:8080`

### Frontend

1. Abra o arquivo `frontend/index.html` em um navegador web
2. Certifique-se de que o backend está rodando na porta 8080

## 📝 Notas

- O projeto utiliza H2 Database em memória para desenvolvimento
- A API de filmes utiliza a OMDB API (requer chave de API configurada)
- O frontend faz requisições para `http://localhost:8080`
