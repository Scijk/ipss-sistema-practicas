# IPSS Sistema de Prácticas

Proyecto Spring Boot 3.3.4 para la gestión de prácticas profesionales, desarrollado con Java 21 y PostgreSQL 15. La aplicación permite registrar, consultar, actualizar y eliminar información relacionada con estudiantes, profesores, empresas, jefes directos y prácticas profesionales.

## Descripción del proyecto

El sistema resuelve la necesidad del colegio técnico profesional de gestionar las prácticas profesionales de sus estudiantes egresados de enseñanza media. La solución incorpora distintos niveles de acceso y validaciones para asegurar que el flujo de información sea seguro, ordenado y consistente.

### Roles y permisos

- Estudiantes: pueden registrar y consultar sus prácticas.
- Profesores: pueden supervisar y gestionar registros de prácticas.
- Administrador o backend: gestión completa de usuarios, empresas y otras entidades asociadas.

### Arquitectura implementada

La solución sigue un patrón de capas:

- `entity`: entidades JPA del modelo de datos
- `repository`: acceso a base de datos con Spring Data JPA
- `service`: lógica de negocio y validaciones
- `controller`: endpoints REST
- `dto`: transferencias de datos
- `exception`: manejadores de errores personalizados
- `enums`: enumeraciones del dominio

## Modelo de base de datos

El esquema incluye las siguientes entidades principales:

- `usuarios`
- `estudiantes`
- `profesores`
- `empresas`
- `jefes_directos`
- `practicas`

### Relaciones principales

- Cada `Usuario` tiene un rol: `ESTUDIANTE` o `PROFESOR`.
- Cada `Estudiante` está asociado a un `Usuario`.
- Cada `Profesor` está asociado a un `Usuario`.
- Cada `Empresa` puede tener varios `JefeDirecto` y varias `Practica`.
- Cada `JefeDirecto` pertenece a una `Empresa`.
- Cada `Practica` está asociada a:
  - un `Estudiante`
  - un `Profesor`
  - una `Empresa`
  - un `JefeDirecto`

### Restricciones y validaciones del modelo

- Email único en usuarios.
- Email único en empresas.
- Un usuario no puede duplicarse como estudiante o profesor.
- La fecha de término no puede ser anterior a la fecha de inicio.
- La descripción de actividades es obligatoria.
- El jefe directo debe pertenecer a la empresa indicada.

## Tecnologías utilizadas

- Java 21
- Spring Boot 3.3.4
- Spring Web
- Spring Data JPA
- Hibernate ORM
- Spring Validation
- PostgreSQL 15
- Docker Compose
- Maven

## Requisitos previos

- Java 21 instalado
- Maven instalado
- Docker Desktop o Docker Engine activo
- Puerto 5432 libre para PostgreSQL
- Puerto 8080 libre para la aplicación

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
│   │   │   ├── exception/
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
├── .gitignore
└── target/
```

## Configuración de la base de datos

La conexión a PostgreSQL por defecto está definida en `src/main/resources/application.properties`:

```properties
spring.application.name=ipss-sistema-practicas

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

## Levantar PostgreSQL con Docker

Ejecuta:

```bash
docker compose up -d
```

Esto crea un contenedor con la imagen `postgres:15-alpine` y prepara la base de datos `practicas_db`.

## Scripts de base de datos y datos semilla

El proyecto incluye dos niveles de inicialización:

1. `src/main/resources/schema.sql`: crea la estructura de la base de datos.
2. `src/main/resources/data.sql`: inserta registros de prueba para verificar el flujo de la API.
3. `db/init-db.sql`: script manual para recrear la BD y cargar datos semilla desde la línea de comandos.

### Inicialización automática con Spring Boot

Cuando la aplicación arranca, Spring Boot ejecuta automáticamente `schema.sql` y `data.sql` porque la configuración en `application.properties` usa:

```properties
spring.sql.init.mode=always
```

Esto permite que, al iniciar la app, se creen las tablas y se inserten los registros semilla si la base de datos está vacía.

### Inicialización manual con psql (si tienes cliente local)

Desde la raíz del proyecto:

```bash
psql -h localhost -U postgres -d practicas_db -f db/init-db.sql
```

### Inicialización manual usando Docker (recomendado en este entorno)

Si no tienes `psql` instalado localmente, puedes copiar el script al contenedor y ejecutarlo directamente:

```bash
docker cp db/init-db.sql practicas-postgres:/tmp/init-db.sql
docker exec -i practicas-postgres psql -U postgres -d practicas_db -f /tmp/init-db.sql
```

También puedes cargar solo los datos semilla:

```bash
docker cp db/seed.sql practicas-postgres:/tmp/seed.sql
docker exec -i practicas-postgres psql -U postgres -d practicas_db -f /tmp/seed.sql
```

Si deseas limpiar y reprovisionar la base de datos desde cero:

```bash
docker exec -i practicas-postgres psql -U postgres -d postgres -c "DROP DATABASE IF EXISTS practicas_db;"
docker exec -i practicas-postgres psql -U postgres -d postgres -c "CREATE DATABASE practicas_db;"
docker cp db/init-db.sql practicas-postgres:/tmp/init-db.sql
docker exec -i practicas-postgres psql -U postgres -d practicas_db -f /tmp/init-db.sql
```

### Verificar datos semilla

Puedes consultar una tabla de prueba con:

```bash
docker exec -i practicas-postgres psql -U postgres -d practicas_db -c "SELECT * FROM usuarios;"
docker exec -i practicas-postgres psql -U postgres -d practicas_db -c "SELECT * FROM practicas;"
```

## Levantar el proyecto

### 1) Compilar

```bash
mvn clean install
```

### 2) Ejecutar la aplicación

```bash
mvn spring-boot:run
```

También puedes ejecutarlo desde el IDE usando la clase principal:

```text
com.ipss.practicas.PracticasProfesionalesApplication
```

### URL base

```text
http://localhost:8080
```

## CRUD implementado

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

## Ejemplos de payload

### Crear usuario

```json
{
  "nombre": "Ana",
  "apellido": "García",
  "email": "ana@mail.com",
  "password": "123456",
  "rol": "ESTUDIANTE"
}
```

### Crear estudiante

```json
{
  "usuarioId": 1,
  "carrera": "Informática",
  "telefono": "987654321",
  "direccion": "Av. Central 123"
}
```

### Crear profesor

```json
{
  "usuarioId": 2,
  "especialidad": "Programación",
  "cargo": "Docente"
}
```

### Crear empresa

```json
{
  "nombre": "IPSS Tech",
  "direccion": "Calle Falsa 123",
  "telefono": "5551234",
  "email": "contacto@ipsstech.cl",
  "descripcion": "Empresa de desarrollo de software"
}
```

### Crear jefe directo

```json
{
  "nombre": "Carlos",
  "apellido": "Molina",
  "cargo": "Ingeniero de proyecto",
  "telefono": "912345678",
  "email": "carlos@ipsstech.cl",
  "empresaId": 1
}
```

### Crear práctica

```json
{
  "estudianteId": 1,
  "profesorId": 1,
  "empresaId": 1,
  "jefeDirectoId": 1,
  "fechaInicio": "2026-03-10",
  "fechaTermino": "2026-05-10",
  "descripcionActividades": "Desarrollar módulos backend y participar en reuniones de coordinación."
}
```

## Manejo de errores

Se implementó un `@RestControllerAdvice` para devolver respuestas estructuradas en caso de:

- recurso no encontrado
- validación fallida
- error de negocio
- argumentos inválidos

Ejemplo de respuesta:

```json
{
  "timestamp": "2026-09-06T20:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "La fecha de término no puede ser menor que la de inicio"
}
```

## Validaciones implementadas

- campos obligatorios
- longitudes máximas
- email válido
- contraseña mínima
- fecha de término >= fecha de inicio
- relación consistente entre empresa y jefe directo
- unicidad de usuarios y empresas
- restricción de roles por entidad

## Ejecución de pruebas

```bash
mvn test
```

## Observaciones finales

- El proyecto usa `schema.sql` para crear la estructura inicial de la base de datos.
- La solución está preparada para ser extendida con autenticación, seguridad y capa de UI.
- Sigue un patrón de n capas para facilitar mantenimiento, escalabilidad y prueba unitaria.

## Licencia

Este proyecto se desarrolla como parte de la evaluación de la unidad para el curso de Desarrollo de Software Web 2.
