# Vaudoise API exercice

This project is a **JHipster-generated Spring Boot application**.

It runs inside a Docker container and exposes REST API endpoints that can be tested using **Swagger-UI**.

## Project Structure

Node is required for generation and recommended for development. `package.json` is always generated for a better development experience with prettier, commit hooks, scripts and so on.

In the project root, JHipster generates configuration files for tools like git, prettier, eslint, husky, and others that are well known and you can find references in the web.

`/src/*` structure follows default Java structure.

- `.yo-rc.json` - Yeoman configuration file
  JHipster configuration is stored in this file at `generator-jhipster` key. You may find `generator-jhipster-*` for specific blueprints configuration.
- `.yo-resolve` (optional) - Yeoman conflict resolver
  Allows to use a specific action when conflicts are found skipping prompts for files that matches a pattern. Each line should match `[pattern] [action]` with pattern been a [Minimatch](https://github.com/isaacs/minimatch#minimatch) pattern and action been one of skip (default if omitted) or force. Lines starting with `#` are considered comments and are ignored.
- `.jhipster/*.json` - JHipster entity configuration files
- `/src/main/docker` - Docker configurations for the application and services that the application depends on

## Requirements

- Java 17+ (required by JHipster)
- Docker (to run the containerized application)

## Development

### 1. Clone the Repository

Clone the project from GitHub:

```
git clone https://github.com/GregGaoter/VaudoiseAPI-Exercice.git
cd VaudoiseAPI-Exercice
```

### 2. Start the Application

To start the application in the dev profile, run:

```
./mvnw
```

This command will:

- Build the application
- Create and start a Docker container for the service
- Expose the API on **http://localhost:8080**

### 3. Access the API Documentation

Once the application is running, you can explore and test the endpoints using **Swagger-UI**:

👉 http://localhost:8080/swagger-ui/index.html?urls.primaryName=springdocDefault

## Design Choice: Composition over Inheritance

This project uses **composition** to model client types (`Person` and `Company`) instead of relying on JPA inheritance. While inheritance (`@Inheritance(strategy = SINGLE_TABLE)`) is supported in JPA, it can lead to complex schemas with many nullable fields and discriminator logic. To keep the domain model clean and maintainable, I chose to extract shared fields (name, email, phone) into a separate `ClientInfo` entity.

Each client type (`Person`, `Company`) holds a reference to its own `ClientInfo`, allowing reuse of common attributes without enforcing a rigid class hierarchy.

Contracts are linked directly to either `Person` or `Company`, and service-layer logic ensures that only one client type is associated per contract.

## Docker Compose support

JHipster generates a number of Docker Compose configuration files in the [src/main/docker/](src/main/docker/) folder to launch required third party services.

For example, to start required services in Docker containers, run:

```
docker compose -f src/main/docker/services.yml up -d
```

To stop and remove the containers, run:

```
docker compose -f src/main/docker/services.yml down
```

[Spring Docker Compose Integration](https://docs.spring.io/spring-boot/reference/features/dev-services.html) is enabled by default. It's possible to disable it in application.yml:

```yaml
spring:
  ...
  docker:
    compose:
      enabled: false
```

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a Docker image of your app by running:

```sh
npm run java:docker
```

Or build a arm64 Docker image when using an arm64 processor os like MacOS with M1 processor family running:

```sh
npm run java:docker:arm64
```

Then run:

```sh
docker compose -f src/main/docker/app.yml up -d
```

For more information refer to [Using Docker and Docker-Compose][], this page also contains information on the Docker Compose sub-generator (`jhipster docker-compose`), which is able to generate Docker configurations for one or several JHipster applications.
