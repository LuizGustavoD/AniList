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
| Spring Boot | 4.0.5 |
| Spring Security + OAuth2 | 6 |
| Spring Data JPA (Hibernate) | - |
| Spring WebSocket (STOMP) | - |
| Spring Mail | - |
| Flyway | - |
| MySQL | 8+ |
| Lombok | - |
| SpringDoc OpenAPI (Swagger) | 2.6.0 |

### Frontend

| Tecnologia | Versão |
|---|---|
| Flutter | ^3.7.2 |
| Dart | 3.7.2+ |
| Material Design 3 | - |

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

## 📁 Estrutura do Projeto

```
AnimeList/
├── backend/server/
│   └── src/main/java/com/anilist/backend/server/
│       ├── controller/
│       │   ├── anime/          # AnimeController
│       │   ├── auth/           # Login, Register
│       │   ├── profile/        # Profile, Friendships
│       │   └── messages/       # WebSocket handlers
│       ├── service/
│       │   ├── anime/          # Relacionamento usuário-anime
│       │   ├── auth/           # Login, Register
│       │   ├── profile/        # Profile, Friendships
│       │   ├── message/        # Mensagens em grupo/diretas
│       │   ├── mail/           # Envio de e-mails
│       │   └── storage/        # Upload de arquivos
│       ├── models/
│       │   ├── anime/          # Anime, Reviews, UserAnime
│       │   ├── user/           # UserModel
│       │   ├── role/           # Roles, EnumRole
│       │   ├── friendship/     # Friendships, Requests
│       │   ├── message/        # Direct, Group (SINGLE_TABLE)
│       │   └── group/          # Groups, Memberships
│       ├── repository/
│       │   ├── user/           # User, Roles
│       │   ├── anime/          # Anime, Reviews, UserAnime
│       │   ├── friendship/     # Friendships, Requests
│       │   ├── message/        # Direct, Group messages
│       │   └── group/          # Groups, Memberships
│       ├── DTO/
│       │   ├── anime/          # Add, Delete, Status, Favorite, Review
│       │   ├── auth/           # Login, Register
│       │   ├── profile/        # Attributes, Friendship requests
│       │   └── message/        # Group messages
│       ├── config/             # WebSocket, RestTemplate
│       └── security/
│           ├── config/         # SecurityConfig
│           └── jwt/            # JwtService, JwtConfig, JwtUtils
├── frontend/anilist_front_application/
│   └── lib/
│       ├── main.dart
│       ├── API/                # Clients HTTP
│       ├── service/            # Lógica de negócio
│       └── ui/
│           ├── pages/          # Telas
│           └── widgets/        # Componentes reutilizáveis
├── docker/                     # Docker Compose + Dockerfile
├── server/                     # Caddy + WAF (OWASP CRS)
├── uploads/                    # Arquivos de usuários
└── logs/                       # Logs da aplicação
```

---

## 🗄 Banco de Dados

MySQL 8+ com migrações gerenciadas pelo **Flyway**:

| Migração | Descrição |
|---|---|
| `V1__init_schemas.sql` | Tabelas iniciais: users, roles, anime, user_anime, reviews |
| `V2__add_profile_picture.sql` | Coluna profile_picture em users |
| `V3__add_friendship_tables.sql` | Tabelas de amizade e solicitações |
| `V4__add_messages_tables.sql` | Mensagens (SINGLE_TABLE), grupos e memberships |
| `V5__update_anime_tables.sql` | MAL ID, favoritos, about, constraints |

### Herança JPA (Mensagens)

Estratégia **SINGLE_TABLE** com coluna discriminadora `message_type`:

```
messages
├── DIRECT → receiver_id (FK → users)
└── GROUP  → group_id (FK → user_groups)
```

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
