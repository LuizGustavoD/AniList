# 🎌 AniList

Plataforma social full-stack para gerenciamento de listas de animes, com funcionalidades de acompanhamento, avaliações, amizades e mensagens em tempo real.

---

## 📋 Índice

- [Tecnologias](#-tecnologias)
- [Arquitetura](#-arquitetura)
- [Funcionalidades](#-funcionalidades)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Banco de Dados](#-banco-de-dados)
- [Variáveis de Ambiente](#-variáveis-de-ambiente)
- [Como Executar](#-como-executar)

---

## 🛠 Tecnologias

### Backend

| Tecnologia | Versão |
|---|---|
| Java | 21 |
| Spring Boot | 3.2.5 |
| Spring Security + OAuth2 | 6.2.4 |
| Spring Data JPA (Hibernate) | - |
| Spring WebSocket (STOMP) | - |
| Spring Mail | - |
| Flyway | - |
| MySQL | 8+ |
| Lombok | - |
| SpringDoc OpenAPI (Swagger) | 2.6.0 |
| Resilience4j RateLimiter | 1.7.0 |

### Frontend

| Tecnologia | Versão |
|---|---|
| Flutter | ^3.7.2 |
| Dart | 3.7.2+ |
| Material Design 3 | - |
| http | ^1.3.0 |
| web_socket_channel | - |

**Plataformas suportadas:** Android, iOS, Web, Windows, Linux, macOS

### Infraestrutura

| Tecnologia | Finalidade |
|---|---|
| Docker / Docker Compose | Containerização |
| Caddy | Reverse proxy |
| OWASP CRS | WAF (Web Application Firewall) |

---

## 🏗 Arquitetura

O projeto segue uma arquitetura **cliente-servidor** com separação clara entre frontend e backend:

```
┌──────────────┐       WebSocket (STOMP)        ┌──────────────────┐
│              │◄──────────/ws──────────────────►│                  │
│   Flutter    │                                 │   Spring Boot    │
│   (Client)   │◄──────REST API (/api/**)───────►│   (Server)       │
│              │                                 │                  │
└──────────────┘                                 └────────┬─────────┘
                                                          │
                                                   ┌──────┴──────┐
                                                   │   MySQL 8   │
                                                   │  (Flyway)   │
                                                   └─────────────┘
```

### Padrão de camadas (Backend)

```
Controller → Service → Repository → Database
     │
     └── DTO (validação de entrada/saída)
```

- **Controllers**: Recebem requisições HTTP/WebSocket e delegam para services
- **Services**: Contêm a lógica de negócio
- **Repositories**: Acesso ao banco via Spring Data JPA
- **DTOs**: Validação e transferência de dados entre camadas
- **Models**: Entidades JPA mapeadas para tabelas do banco

### Segurança

- Autenticação via **JWT com chaves RSA** (par público/privado)
- OAuth2 Resource Server para validação de tokens
- Sessões stateless
- Roles: `USER`, `ADMIN`

---

## ✨ Funcionalidades

### Autenticação
- Registro com confirmação por e-mail
- Login com JWT (expiração: 1h)
- Controle de acesso baseado em roles

### Gerenciamento de Animes
- Adicionar/remover animes da lista pessoal
- Status de acompanhamento: `WATCHING`, `COMPLETED`, `DROPPED`, `PLAN_TO_WATCH`, `ON_HOLD`
- Marcar animes como favoritos
- Avaliações com nota (1-5) e comentário
- Integração com MyAnimeList via MAL ID

### Perfil de Usuário
- Upload/remoção de foto de perfil (máx. 10MB)
- Edição de atributos do perfil

### Sistema Social
- Envio e gerenciamento de solicitações de amizade (`PENDING`, `ACCEPTED`, `REJECTED`)
- Lista de amigos bidirecional

### Mensagens em Tempo Real
- Mensagens diretas entre usuários
- Mensagens em grupo com roles (`MEMBER`, `ADMIN`)
- WebSocket via protocolo STOMP (`/ws`)
- Broker: `/topic` (broadcast), `/queue` (ponto-a-ponto)

### Notificações
- Notificação em tempo real de solicitações de amizade
- Confirmação de conta via e-mail (SMTP)

---

## 📁 Estrutura do Projeto (atualizada)

```
AnimeList/
├── backend/server/
│   └── src/main/java/com/anilist/backend/server/
│       ├── controller/      # REST e WebSocket controllers
│       ├── service/        # Lógica de negócio
│       ├── repository/     # Spring Data JPA
│       ├── models/         # Entidades JPA
│       ├── DTO/            # Data Transfer Objects
│       ├── config/         # Configurações (WebSocket, Security, etc)
│       └── security/       # JWT, OAuth2, etc
│   └── src/main/resources/db/migration/ # Migrações Flyway
├── frontend/anilist_front_application/
│   └── lib/
│       ├── API/            # HTTP/WebSocket clients
│       ├── service/        # Serviços de negócio
│       └── ui/             # Páginas e widgets
├── docker/                 # Docker Compose + Dockerfile
├── server/                 # Caddy + WAF (OWASP CRS)
├── uploads/                # Arquivos de usuários
└── logs/                   # Logs da aplicação
```

---

## 🗄 Banco de Dados (atualizado)

MySQL 8+ com migrações Flyway:

| Migração | Descrição |
|---|---|
| `V1__init_schemas.sql` | users, roles, anime, user_anime, reviews |
| `V2__add_profile_picture.sql` | profile_picture em users |
| `V3__add_friendship_tables.sql` | friendships e requests |
| `V4__add_messages_tables.sql` | grupos, memberships, mensagens (SINGLE_TABLE) |
| `V5__update_anime_tables.sql` | MAL ID, favoritos, about, constraints |
| `V6__update_users_table.sql` | banner_url em users |
| `V7__add_token_tables.sql` | tokens de confirmação/sessão |

- **Herança JPA**: Mensagens (direta/grupo) em SINGLE_TABLE.
- **Enums**: Status de anime, roles, status de amizade, roles de grupo.

---

## 🔐 Variáveis de Ambiente

| Variável | Descrição | Default |
|---|---|---|
| `DB_HOST` | Host do MySQL | `localhost` |
| `DB_PORT` | Porta do MySQL | `3306` |
| `DB_NAME` | Nome do banco | `anilist_db` |
| `DB_USERNAME` | Usuário do banco | `root` |
| `DB_PASSWORD` | Senha do banco | — |
| `SMTP_HOST` | Host SMTP | `smtp.gmail.com` |
| `SMTP_PORT` | Porta SMTP | `587` |
| `SMTP_USERNAME` | E-mail SMTP | — |
| `SMTP_PASSWORD` | Senha SMTP | — |
| `FILE_UPLOAD_DIR` | Diretório de uploads | `./uploads/profile-pictures` |

---

## 🚀 Como Executar

### Pré-requisitos

- Java 21+
- MySQL 8+
- Flutter SDK ^3.7.2
- Maven

### Backend

```bash
cd backend/server

# Configurar variáveis de ambiente
export DB_PASSWORD=sua_senha
export SMTP_USERNAME=seu_email
export SMTP_PASSWORD=sua_senha_smtp

# Executar
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`.  
Swagger UI: `http://localhost:8080/swagger-ui.html`

### Frontend

```bash
cd frontend/anilist_front_application

flutter pub get
flutter run
```

---

## ❗ Tratamento de Erros

Todas as regras de negócio lançam exceções personalizadas (ex: `UserNotFoundException`, `InvalidTokenException`, etc) localizadas em `service/exceptions/custom`. O `ControllerExceptionHandler` centraliza o tratamento dessas exceptions, retornando respostas padronizadas com status HTTP e mensagens claras para o frontend.

Exemplo de resposta de erro:

```json
{
  "timestamp": "2026-04-10T12:34:56.789Z",
  "status": 404,
  "error": "User Not Found",
  "message": "Usuário não encontrado"
}
```

---

## 🆕 Novidades da Versão Atual

- **Rate Limiting**: Todos os endpoints REST do backend agora estão protegidos por Resilience4j RateLimiter, com configuração dedicada para cada controller no `application.yml`.
- **WebSocket**: Limite de mensagens por minuto e tamanho máximo configurados para conexões WebSocket.
- **Tokens**: Nova tabela para gerenciamento de tokens de confirmação e sessão (`V7__add_token_tables.sql`).
- **Banner de Usuário**: Campo `banner_url` adicionado à tabela de usuários.
- **Validação e tratamento de erros** centralizados via `ControllerExceptionHandler`.
- **Frontend**: Serviços para autenticação, mensagens, perfil, integração HTTP e WebSocket, arquitetura modularizada.

---

> Para detalhes completos, consulte as migrações Flyway, o código dos models e os serviços no frontend.
