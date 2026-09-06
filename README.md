# IPSS Sistema de Prácticas

Proyecto Spring Boot 3.3.4 para la gestión de prácticas profesionales de estudiantes egresados de enseñanza media, desarrollado con Java 21 y PostgreSQL 15.

## Descripción del proyecto

El sistema permite gestionar el registro de prácticas profesionales con distintos niveles de acceso según el tipo de usuario:

- Estudiantes: pueden registrar y consultar sus prácticas.
- Profesores: pueden supervisar, consultar y gestionar registros de prácticas.

La solución está desarrollada con una arquitectura en capas:

- `entity`: entidades JPA
- `repository`: repositorios para acceso a datos
- `service`: lógica de negocio
- `controller`: endpoints REST
- `dto`: objetos de transferencia
- `enums`: enumeraciones del dominio

## Modelo de base de datos

Se implementa una estructura relacional para manejar:

- Usuarios
- Estudiantes
- Profesores
- Empresas
- Jefes directos
- Prácticas profesionales

### Entidades principales

- `Usuario`
- `Estudiante`
- `Profesor`
- `Empresa`
- `JefeDirecto`
- `Practica`

### Relaciones principales

- Un `Usuario` puede ser `ESTUDIANTE` o `PROFESOR`.
- Un `Estudiante` tiene un `Usuario` asociado.
- Un `Profesor` tiene un `Usuario` asociado.
- Una `Practica` pertenece a un `Estudiante`, `Profesor`, `Empresa` y `JefeDirecto`.
- Una `Empresa` puede tener varios `JefeDirecto` y varias `Practica`.

## Tecnologías usadas

- Java 21
- Spring Boot 3.3.4
- Spring Web
- Spring Data JPA
- Spring Validation
- PostgreSQL 15
- Maven
- Docker / Docker Compose

## Requisitos previos

- Java 21 instalado
- Maven instalado
- Docker Desktop o Docker Engine instalado y funcionando
- Puerto 5432 disponible para PostgreSQL
- Puerto 8080 disponible para la aplicación

## Estructura del proyecto

```text
ipss-sistema-practicas/
├── src/
│   ├── main/
│   │   ├── java/com/ipss/practicas/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── entity/
│   │   │   ├── enums/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   └── PracticasProfesionalesApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── schema.sql
│   └── test/
│       └── java/com/ipss/practicas/
├── docker-compose.yml
├── pom.xml
├── README.md
└── .gitignore
```

## Configuración de la base de datos

El proyecto usa PostgreSQL con la siguiente configuración por defecto en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/practicas_db
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=always
```

La base de datos puede levantarse con Docker Compose usando el archivo `docker-compose.yml`:

```bash
docker compose up -d
```

## Levantar el proyecto

### 1) Levantar PostgreSQL

```bash
docker compose up -d
```

### 2) Compilar el proyecto

```bash
mvn clean install
```

### 3) Ejecutar la aplicación

```bash
mvn spring-boot:run
```

o desde el IDE con la clase principal:

```text
com.ipss.practicas.PracticasProfesionalesApplication
```

### URL base de la API

```text
http://localhost:8080
```

## Endpoints principales

### Prácticas

- `GET /api/practicas` — listar todas las prácticas
- `GET /api/practicas/estudiante/{estudianteId}` — listar prácticas por estudiante
- `GET /api/practicas/profesor/{profesorId}` — listar prácticas por profesor
- `POST /api/practicas` — crear práctica
- `PUT /api/practicas/{id}` — actualizar práctica
- `DELETE /api/practicas/{id}` — eliminar práctica

## Ejemplo de payload para crear práctica

```json
{
  "estudianteId": 1,
  "profesorId": 1,
  "empresaId": 1,
  "jefeDirectoId": 1,
  "fechaInicio": "2026-01-10",
  "fechaTermino": "2026-03-10",
  "descripcionActividades": "Desarrollar módulos de backend y participar en revisiones de código."
}
```

## Observaciones importantes

- El proyecto usa `schema.sql` para crear la estructura de tablas.
- La aplicación está preparada para trabajar con JPA y PostgreSQL.
- Los nombres de las clases actuales pueden mantenerse tal como están, mientras se complete el desarrollo del proyecto.
- La solución sigue un patrón de capas para separar responsabilidades y facilitar mantenimiento.

## Ejecución de pruebas

```bash
mvn test
```

## Licencia

Este proyecto se desarrolla como parte de la evaluación de la unidad para el curso de Desarrollo de Software Web 2.
