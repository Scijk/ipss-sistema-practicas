# IPSS Sistema de Prácticas

Backend para la gestión de prácticas profesionales con Java 21, Spring Boot 3.3.4, PostgreSQL 15 y JPA/Hibernate. El proyecto sigue una arquitectura en capas, incorpora autenticación JWT y roles, y expone un CRUD completo para las entidades del dominio académico y empresarial.

## 1. Descripción general

La aplicación permite administrar:

- Usuarios con roles `ESTUDIANTE` y `PROFESOR`
- Estudiantes con información académica
- Profesores con especialidad y cargo
- Empresas y jefes directos
- Prácticas profesionales con fechas, estado y relación con los actores del proceso

La lógica de negocio se encuentra centralizada en la capa de servicios, la persistencia en repositorios JPA y la exposición de datos a través de controladores REST con DTOs.

## 2. Stack tecnológico

- Java 21
- Spring Boot 3.3.4
- Spring Web
- Spring Data JPA
- PostgreSQL 15 Alpine
- Spring Security
- JWT con JJWT
- Maven
- Docker Compose

## 3. Arquitectura en capas

El proyecto sigue el patrón n capas:

- `entity`: entidades JPA del dominio
- `repository`: accesos a base de datos con Spring Data JPA
- `service`: validaciones y lógica de negocio
- `controller`: endpoints REST
- `dto`: request/response DTOs
- `security`: JWT, filtros y configuración de seguridad
- `exception`: manejo centralizado de errores

## 4. Modelo de dominio

Entidades principales:

- `Usuario`: credenciales, email, rol y autenticación
- `Estudiante`: información académica del estudiante y relación 1:1 con usuario
- `Profesor`: especialidad, cargo y relación 1:1 con usuario
- `Empresa`: empresa donde se realiza la práctica
- `JefeDirecto`: responsable de la empresa y vínculo con la práctica
- `Practica`: registro principal de la práctica profesional

Relaciones clave:

- Un usuario puede ser estudiante o profesor
- Un estudiante puede tener varias prácticas
- Un profesor puede supervisar varias prácticas
- Una empresa puede tener varios jefes directos y múltiples prácticas
- Cada práctica relaciona estudiante, profesor, empresa y jefe directo

## 5. Requisitos previos

- Java 21
- Maven 3.9+
- Docker Desktop o Docker Engine
- PostgreSQL 15 (se levanta con Docker Compose)

## 6. Configuración por ambiente

El proyecto usa placeholders para externalizar la configuración por perfil (`dev`, `prod`) y por variables de entorno. La configuración base está en:

- `src/main/resources/application.properties`
- `src/main/resources/application-dev.properties`
- `src/main/resources/application-prod.properties`

Archivo de referencia:

- `.env.example`

Variables principales:

```properties
SPRING_PROFILES_ACTIVE=dev
APP_NAME=ipss-sistema-practicas
DB_URL=jdbc:postgresql://localhost:5432/practicas_db
DB_USERNAME=postgres
DB_PASSWORD=postgres
SERVER_PORT=8080
JPA_DDL_AUTO=none
JPA_SHOW_SQL=false
JPA_FORMAT_SQL=true
JPA_DEFER_INIT=true
SQL_INIT_MODE=always

POSTGRES_DB=practicas_db
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_PORT=5432
```

También existe configuración JWT con valores por defecto en `JwtService`:

```properties
jwt.secret=ipss-practicas-secret-key-2026-very-long-value
jwt.expiration=86400000
```

En entornos reales se recomienda sobrescribir estas propiedades con variables de entorno o con un archivo `.env` no versionado.

## 7. Ejecutar el proyecto

### 7.1 Levantar la base de datos con Docker

```bash
docker compose up -d
```

Esto levanta PostgreSQL 15 en el puerto `5432` usando `postgres:15-alpine`.

### 7.2 Configurar entorno local

Copiar el ejemplo de variables:

```bash
cp .env.example .env
```

Ajustar los valores según el ambiente local o productivo.

### 7.3 Ejecutar la aplicación

En desarrollo:

```bash
SPRING_PROFILES_ACTIVE=dev mvn spring-boot:run
```

O bien, con la variable exportada antes:

```bash
export SPRING_PROFILES_ACTIVE=dev
mvn spring-boot:run
```

La API queda disponible en:

- http://localhost:8080

## 8. Base de datos y scripts SQL

La carpeta `db/` contiene los scripts principales:

- `db/init-db.sql`: recrea el esquema y agrega datos semilla
- `db/seed.sql`: carga los datos de prueba sin recrear tablas

Ejecutar recreación completa:

```bash
psql -U postgres -d postgres -f db/init-db.sql
```

Cargar solo datos semilla:

```bash
psql -U postgres -d practicas_db -f db/seed.sql
```

Nota: el esquema se define con restricciones de integridad, claves foráneas, checks y relaciones 1:1 y 1:N según el dominio.

## 9. Datos semilla por defecto

Al inicializar la base de datos se cargan usuarios de prueba para validar la app de inmediato:

- Ana García — estudiante — `ana.garcia@email.com` — password `123456`
- Luis Pérez — profesor — `luis.perez@email.com` — password `123456`
- Marta Ruiz — estudiante — `marta.ruiz@email.com` — password `123456`

Las contraseñas se almacenan con `BCryptPasswordEncoder`.

## 10. Seguridad y autenticación

La seguridad se implementa con Spring Security + JWT.

### Roles implementados

- `ESTUDIANTE`
- `PROFESOR`

### Endpoints públicos

- `POST /api/auth/login`
- `POST /api/auth/register`

### Protección por rol

Las rutas están protegidas por permisos:

- Estudiantes: acceso de lectura a la mayoría de recursos y lectura del propio perfil
- Profesores: acceso total a gestión de usuarios, estudiantes, profesores, empresas, jefes directos y prácticas
- Todas las llamadas deben incluir el header JWT válido

### Login ejemplo

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
  "role": "ESTUDIANTE"
}
```

### Registro ejemplo

```http
POST /api/auth/register
Content-Type: application/json

{
  "nombre": "Pedro",
  "apellido": "Mendoza",
  "email": "pedro.mendoza@email.com",
  "password": "123456",
  "rol": "ESTUDIANTE",
  "carrera": "Ingeniería de Software"
}
```

## 11. Endpoints REST

### Autenticación

- `POST /api/auth/login`
- `POST /api/auth/register`

### Usuarios

- `GET /api/usuarios`
- `GET /api/usuarios/{id}`
- `POST /api/usuarios`
- `PUT /api/usuarios/{id}`
- `PATCH /api/usuarios/{id}`
- `DELETE /api/usuarios/{id}`

### Estudiantes

- `GET /api/estudiantes`
- `GET /api/estudiantes/{id}`
- `POST /api/estudiantes`
- `PUT /api/estudiantes/{id}`
- `PATCH /api/estudiantes/{id}`
- `DELETE /api/estudiantes/{id}`

### Profesores

- `GET /api/profesores`
- `GET /api/profesores/{id}`
- `POST /api/profesores`
- `PUT /api/profesores/{id}`
- `PATCH /api/profesores/{id}`
- `DELETE /api/profesores/{id}`

### Empresas

- `GET /api/empresas`
- `GET /api/empresas/{id}`
- `POST /api/empresas`
- `PUT /api/empresas/{id}`
- `PATCH /api/empresas/{id}`
- `DELETE /api/empresas/{id}`

### Jefes directos

- `GET /api/jefes-directos`
- `GET /api/jefes-directos/{id}`
- `GET /api/jefes-directos/empresa/{empresaId}`
- `POST /api/jefes-directos`
- `PUT /api/jefes-directos/{id}`
- `PATCH /api/jefes-directos/{id}`
- `DELETE /api/jefes-directos/{id}`

### Prácticas

- `GET /api/practicas`
- `GET /api/practicas/{id}`
- `GET /api/practicas/estudiante/{estudianteId}`
- `GET /api/practicas/profesor/{profesorId}`
- `POST /api/practicas`
- `PUT /api/practicas/{id}`
- `PATCH /api/practicas/{id}`
- `DELETE /api/practicas/{id}`

## 12. Validaciones y reglas de negocio

La capa de servicios valida los datos antes de guardar:

- campos obligatorios no nulos o vacíos
- email válido
- longitud máxima de cadenas
- coincidencia de jefe directo con la empresa
- existencia de estudiante/profesor/empresa asociados
- fechas coherentes (`fechaTermino >= fechaInicio`)
- unicidad de email para usuarios y empresas

Los errores se manejan con `@RestControllerAdvice`, devolviendo respuestas JSON con código HTTP consistente.

## 13. Colección Bruno

Se incluye la carpeta `bruno/` con requests listos para probar todos los endpoints.

Archivos principales:

- `bruno/bruno.json`
- `bruno/auth-login.bru`
- `bruno/auth-register.bru`
- `bruno/usuarios-crear.bru`
- `bruno/estudiantes-crear.bru`
- `bruno/profesores-crear.bru`
- `bruno/empresas-crear.bru`
- `bruno/jefes-directos-crear.bru`
- `bruno/Practicas - Crear-.bru`

Importante para Bruno:

Los requests con `body: json` deben tener este formato exacto:

```bru
body:json {
  {
    "email": "ana.garcia@email.com",
    "password": "123456"
  }
}
```

Esto es necesario porque Bruno requiere que el JSON de request quede encapsulado en una pareja extra de llaves al crear el body desde el editor.

## 14. Ejemplos de uso HTTP

### Crear empresa autenticado

```http
POST /api/empresas
Authorization: Bearer <token>
Content-Type: application/json

{
  "nombre": "Open Cloud Ltda",
  "direccion": "Polígono Industrial Norte 100",
  "telefono": "+56234567890",
  "email": "contacto@opencloud.cl",
  "descripcion": "Empresa de servicios cloud y software"
}
```

### Crear práctica autenticada

```http
POST /api/practicas
Authorization: Bearer <token>
Content-Type: application/json

{
  "estudianteId": 1,
  "profesorId": 1,
  "empresaId": 1,
  "jefeDirectoId": 1,
  "fechaInicio": "2026-03-10",
  "fechaTermino": "2026-05-10",
  "descripcionActividades": "Desarrollar módulos backend y documentar resultados"
}
```

## 15. Comandos útiles

```bash
# levantar PostgreSQL

docker compose up -d

# compilar proyecto
mvn clean package

# ejecutar la app en desarrollo
mvn spring-boot:run

# ejecutar tests
mvn test
```

## 16. Estado del proyecto

El proyecto cumple con:

- Java 21 + Spring Boot 3.3.4
- PostgreSQL 15 en Docker
- Spring Data JPA
- Patrón n capas
- CRUD completo para entidades principales
- Validaciones backend con DTOs y `@Valid`
- Seguridad JWT con roles
- Datos semilla para pruebas
- Documentación técnica y de uso
- Colección Bruno para pruebas de endpoints

## 17. Observaciones finales

La solución está lista para levantarse localmente, probar con datos reales de ejemplo y extenderse según nuevos requerimientos del negocio. La configuración está externalizada por ambiente y el proyecto sigue una estructura clara y mantenible para desarrollos futuros.