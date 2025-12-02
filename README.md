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
- Cadastro de usuários
- Login e autenticação
- Sistema de seguir/seguidores
- Perfil de usuário

### Filmes
- Busca de filmes via API externa (OMDB)
- Sugestões de filmes
- Visualização de detalhes

### Avaliações
- Avaliação com notas e comentários
- Listagem das avaliações por obra ou por usuário
- Exclusão e edição de avaliações

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
