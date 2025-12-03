# Variáveis de Ambiente

Este projeto utiliza o arquivo `application.properties` para configurações. Para usar variáveis de ambiente, você pode criar um arquivo `.env` na raiz do projeto com as seguintes variáveis:

```env
# Configurações do Banco de Dados
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

