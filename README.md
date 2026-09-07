# IPSS Sistema de Prácticas

Sistema backend para gestionar prácticas profesionales con Java 21, Spring Boot, PostgreSQL 15 y JPA. La aplicación sigue una arquitectura en capas y cuenta con autenticación JWT, gestión de usuarios por roles y CRUD para las entidades principales del dominio.

## Descripción general

El proyecto cubre los procesos necesarios para administrar estudiantes, profesores, empresas, jefes directos y las prácticas profesionales asociadas. Cada entidad se maneja a través de servicios y controladores REST, con validaciones, manejo de excepciones y persistencia en PostgreSQL.

## Stack tecnológico

- Java 21
- Spring Boot 3.3.4
- Spring Web
- Spring Data JPA
- PostgreSQL 15 Alpine
- Spring Security
- JWT (JJWT)
- Maven
- Docker Compose

## Arquitectura

El proyecto usa un patrón de n capas:

- `entity`: entidades JPA
- `repository`: repositorios Spring Data
- `service`: lógica de negocio
- `controller`: endpoints REST
- `dto`: objetos de entrada/salida
- `security`: JWT y configuración de seguridad
- `exception`: respuestas estandarizadas de errores

## Modelo de dominio

- `Usuario`: modelo base de autenticación con rol (`ESTUDIANTE` / `PROFESOR`)
- `Estudiante`: datos académicos del alumno
- `Profesor`: datos académicos del tutor docente
- `Empresa`: empresa donde se realiza la práctica
- `JefeDirecto`: responsable de la empresa
- `Practica`: registro principal de la práctica profesional

## Requisitos previos

- Java 21
- Maven 3.9+
- Docker Desktop o Docker Engine
- PostgreSQL 15 (se levanta con Docker Compose)

## Configuración rápida

### 1. Levantar la base de datos

```bash
docker compose up -d
```

Esto levanta PostgreSQL en el puerto `5432` usando la imagen `postgres:15-alpine`.

### 2. Configurar variables de entorno

La configuración se parametriza para soportar distintos ambientes sin modificar el código. Se recomienda copiar el archivo `.env.example` a `.env` y ajustar los valores según el entorno.

```bash
cp .env.example .env
```

Ejemplo de variables:

```properties
SPRING_PROFILES_ACTIVE=dev
DB_URL=jdbc:postgresql://localhost:5432/practicas_db
DB_USERNAME=postgres
DB_PASSWORD=postgres
SERVER_PORT=8080
```

En `src/main/resources/application.properties` estas mismas propiedades se leen con placeholders y cuentan con valores por defecto, lo que permite trabajar con distintos entornos (`dev`, `prod`, etc.) sin hardcodear secretos ni URLs.

### 3. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

La API queda disponible en:

- http://localhost:8080

## Autenticación y permisos

La app usa JWT para proteger endpoints.

### Login

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "ana.garcia@email.com",
  "password": "123456"
}
```

Respuesta esperada:

```json
{
  "token": "eyJ...",
  "email": "ana.garcia@email.com",
  "rol": "ESTUDIANTE"
}
```

### Registro

```http
POST /api/auth/register
Content-Type: application/json

{
  "nombre": "Pedro",
  "apellido": "Mendoza",
  "email": "pedro.mendoza@email.com",
  "password": "123456",
  "rol": "ESTUDIANTE",
  "carrera": "Ingeniería de Software",
  "especialidad": null
}
```

## Usuarios semilla por defecto

Estos usuarios se cargan con datos semilla para pruebas:

- Ana García - estudiante - `ana.garcia@email.com` - `123456`
- Luis Pérez - profesor - `luis.perez@email.com` - `123456`
- Marta Ruiz - estudiante - `marta.ruiz@email.com` - `123456`

## Endpoints principales

### Autenticación

- `POST /api/auth/login`
- `POST /api/auth/register`

### Usuarios

- `GET /api/usuarios`
- `GET /api/usuarios/{id}`
- `POST /api/usuarios`
- `PUT /api/usuarios/{id}`
- `DELETE /api/usuarios/{id}`

### Estudiantes

- `GET /api/estudiantes`
- `GET /api/estudiantes/{id}`
- `POST /api/estudiantes`
- `PUT /api/estudiantes/{id}`
- `DELETE /api/estudiantes/{id}`

### Profesores

- `GET /api/profesores`
- `GET /api/profesores/{id}`
- `POST /api/profesores`
- `PUT /api/profesores/{id}`
- `DELETE /api/profesores/{id}`

### Empresas

- `GET /api/empresas`
- `GET /api/empresas/{id}`
- `POST /api/empresas`
- `PUT /api/empresas/{id}`
- `DELETE /api/empresas/{id}`

### Jefes directos

- `GET /api/jefes-directos`
- `GET /api/jefes-directos/{id}`
- `GET /api/jefes-directos/empresa/{empresaId}`
- `POST /api/jefes-directos`
- `PUT /api/jefes-directos/{id}`
- `DELETE /api/jefes-directos/{id}`

### Prácticas

- `GET /api/practicas`
- `GET /api/practicas/{id}`
- `GET /api/practicas/estudiante/{estudianteId}`
- `GET /api/practicas/profesor/{profesorId}`
- `POST /api/practicas`
- `PUT /api/practicas/{id}`
- `DELETE /api/practicas/{id}`

## Ejemplos de uso

### Crear una empresa

```http
POST /api/empresas
Authorization: Bearer <token>
Content-Type: application/json

{
  "nombre": "ACME Tecnología",
  "direccion": "Av. Los Copihues 245",
  "telefono": "+56912345678",
  "email": "rrhh@acme.cl",
  "descripcion": "Empresa de desarrollo de software"
}
```

### Crear una práctica

```http
POST /api/practicas
Authorization: Bearer <token>
Content-Type: application/json

{
  "estudianteId": 1,
  "profesorId": 1,
  "empresaId": 1,
  "jefeDirectoId": 1,
  "fechaInicio": "2025-01-15",
  "fechaTermino": "2025-03-15",
  "descripcionActividades": "Desarrollo de APIs y documentación del módulo de usuarios"
}
```

## Base de datos y scripts SQL

La base de datos puede inicializarse mediante scripts preparados en la carpeta `db/`.

### Crear y recrear la BD

```bash
psql -U postgres -d postgres -f db/init-db.sql
```

### Cargar únicamente datos semilla

```bash
psql -U postgres -d ipss_practicas -f db/seed.sql
```

También la aplicación carga datos iniciales automáticamente con `schema.sql` y `data.sql` al arrancar, si `spring.sql.init.mode=always` está habilitado.

## Datos semilla y pruebas

Los scripts incorporados permiten validar rápidamente la funcionalidad con registros de prueba pre-cargados. Esto reduce la necesidad de crear datos manualmente para cada flujo de validación.

## Colección Bruno

Se incluye una colección de Bruno para probar todos los endpoints desde una herramienta HTTP visual:

- `bruno/bruno.json`
- `bruno/environments/local.bru`
- `bruno/*.bru`

Para usarla:

1. Abrir Bruno.
2. Importar la carpeta `bruno/`.
3. Confirmar que `baseUrl` apunte a `http://localhost:8080`.
4. Ejecutar login para obtener el token JWT.
5. Copiar el token en la variable `token` del entorno.
6. Asegurarse de que la cabecera `Authorization` esté en una línea separada y no con comas, por ejemplo:
   `Authorization: Bearer {{token}}`

## Documentación adicional

- `docs/informe-tecnico.md`: diagrama de base de datos y explicación de la solución implementada.

## Estado del proyecto

Actualmente el proyecto cumple con:

- Java 21 + Spring Boot 3.3.4
- PostgreSQL 15 con Docker
- JPA para persistencia
- Patrones en capas
- CRUD completo
- Validaciones de backend
- JWT + autenticación
- roles por perfil
- semilla de datos para pruebas
- documentación técnica y de uso

## Comandos útiles

```bash
# levantar BD

docker compose up -d

# compilar proyecto
mvn clean package

# ejecutar app
mvn spring-boot:run

# correr tests
mvn test
```

## Notas finales

El proyecto quedó listo para levantar en entorno local, probar con datos reales de ejemplo y extender con nuevas funcionalidades según los requisitos del negocio.
