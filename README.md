#### Personal Finance Manager ####

Backend service for managing personal finances, accounts, transactions, and financial reports.
Built with Hexagonal Architecture, Spring Boot, and OAuth2.

## 🧠 Features

Clean Hexagonal Architecture (Ports & Adapters)

OAuth2 with Spring Authorization Server

PostgreSQL (prod) and H2 (dev/test) support

External user provider integration — JSONPlaceholder

Accounts, categories and transactions management

PDF report generation with JasperReports

Full test suite — unit, integration and E2E

Swagger / OpenAPI documentation

## 🏗 Architecture Overview

The system follows Hexagonal Architecture, where the domain is completely
isolated from frameworks and external systems.

                ┌─────────────────────────┐
                │        REST API         │
                │     (Controllers)       │
                └─────────────┬───────────┘
                              │
                              ▼
                     Application Layer
                       (DTOs / Mappers)
                              │
                              ▼
                    Domain (Core Business)
                 Entities + Use Cases + Ports
                              │
                              ▼
                ┌─────────────────────────┐
                │      Outbound Ports     │
                │   Repositories / Users  │
                └─────────────┬───────────┘
                              │
        ┌─────────────────────┴─────────────────────┐
        │                                           │
        ▼                                           ▼
 Persistence Adapter                       External Adapter
  (PostgreSQL / H2)                       JSONPlaceholder API

## 🚀 Getting Started

Prerequisites

Java 21

Maven

Docker (optional, for PostgreSQL)

Node.js & npm (frontend — coming soon)

Clone the Repository
git clone https://github.com/camilagksantos/personal-finance-manager.git
cd personal-finance-manager

## ▶️ Running the Backend

Dev Profile (H2 + mock user adapter)
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev
Production Profile (PostgreSQL + JSONPlaceholder)

Start PostgreSQL with Docker:

docker run -p 5432:5432 \
  -e POSTGRES_DB=finance \
  -e POSTGRES_USER=finance \
  -e POSTGRES_PASSWORD=finance \
  postgres

Then run the application:

mvn spring-boot:run -Dspring-boot.run.profiles=prod

## 🧪 Tests

Run all tests:

mvn test
Layer	Description
Unit	Domain services and mappers
Integration	Persistence, adapters, external clients
E2E	Full flow: Controller → Domain → Database

## 📚 API Documentation

Swagger UI is available when the application is running:

http://localhost:8080/swagger-ui.html

## 📁 Project Structure

backend/src/main/java/com/camilagksantos/finance/
├── domain/
│   ├── model/         ← core entities (no framework dependencies)
│   ├── ports/
│   │   ├── input/     ← use case interfaces
│   │   └── output/    ← repository and service interfaces
│   ├── service/       ← use case implementations
│   └── exception/     ← domain exceptions
│
├── application/
│   ├── dto/           ← request / response models
│   └── mapper/        ← MapStruct mappers
│
├── adapters/
│   ├── inbound/
│   │   ├── rest/      ← controllers + exception handler
│   │   └── security/  ← OAuth2 configuration
│   │
│   └── outbound/
│       ├── persistence/  ← JPA entities, repositories, adapters
│       └── external/     ← JSONPlaceholder client (Feign)
│
└── infrastructure/
    ├── config/        ← Spring beans, OpenAPI, cache
    └── profile/       ← environment-specific configuration

## 🛢 Database Migrations

Managed with Flyway.

Location:

src/main/resources/db/migration/
Migration	Description
V1	Create accounts table
V2	Create categories table
V3	Create transactions table
V4	Create reports table
V5	Create performance indexes

## 🔐 Security

Authentication and authorization are handled by
Spring Authorization Server using the OAuth2 protocol.

All financial endpoints require a valid access token.

## 🛠 Tech Stack

Category	Technology
Framework	Spring Boot
Security	Spring Authorization Server (OAuth2)
Persistence	Spring Data JPA
Database	PostgreSQL / H2
Migrations	Flyway
HTTP Client	OpenFeign
Mapping	MapStruct
Boilerplate	Lombok
Reporting	JasperReports
Build	Maven
Documentation	Swagger / OpenAPI 3
Testing	JUnit 5 + Mockito + Testcontainers

## ⚙️ System Design Decisions

**Why Hexagonal Architecture instead of traditional MVC?**
MVC couples business logic directly to the framework. With Hexagonal Architecture
the domain has zero dependencies on Spring, JPA or any external library — it is
plain Java. This means the core business rules can be tested in isolation and the
infrastructure can be swapped without touching the domain.

**Why two User adapters (JSONPlaceholder + H2Mock)?**
The domain defines a `UserRepositoryPort` interface and does not care where users
come from. In production, `JsonPlaceholderUserAdapter` fetches real users from an
external API. In dev and test, `H2UserAdapter` provides controlled data locally.
Switching between them requires zero changes to business logic — only a Spring
`@Profile` annotation. This demonstrates the Dependency Inversion Principle in
practice.

**Why store reports as `byte[]` in the database instead of the filesystem?**
Storing PDFs as binary directly in PostgreSQL keeps the application fully
self-contained. There is no dependency on a mounted volume, a cloud storage bucket
or a specific file path. This simplifies deployment, backup and portability —
especially in containerized environments.

**Why Spring Authorization Server instead of Keycloak or Auth0?**
For a portfolio project, embedding the Authorization Server inside the application
exposes the full OAuth2 flow — token issuance, validation and resource protection —
in a single, reviewable codebase. Keycloak would hide that complexity behind an
external service. The goal here is to demonstrate understanding of the protocol,
not just configuration of a third-party tool.

**Why Flyway for migrations instead of `spring.jpa.hibernate.ddl-auto`?**
`ddl-auto` is convenient but unpredictable in production — it can silently alter or
drop schema. Flyway applies versioned, explicit SQL scripts in a controlled order.
Every schema change is tracked, reproducible and reviewable in version control.

## 🔭 Future Improvements

Docker Compose setup for full stack (backend + PostgreSQL)

Frontend with Angular 18+ and Signals

CI/CD pipeline with GitHub Actions

Coverage report with JaCoCo

## 👩‍💻 Author

Camila G. K. Santos — Backend Developer

LinkedIn:
https://www.linkedin.com/in/camilagksantos

This project is part of a portfolio series focused on modern backend architecture and real-world practices.

Se quiser, posso fazer um ajuste que normalmente aumenta bastante a chance de recrutador parar no teu projeto: adicionar uma seção curta chamada

System Design Decisions
