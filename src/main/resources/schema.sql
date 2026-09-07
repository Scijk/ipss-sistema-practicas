CREATE TABLE IF NOT EXISTS usuarios (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    apellido VARCHAR(80) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL CHECK (rol IN ('ESTUDIANTE', 'PROFESOR'))
);

CREATE TABLE IF NOT EXISTS estudiantes (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    carrera VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    direccion VARCHAR(200),
    CONSTRAINT fk_estudiantes_usuarios FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS profesores (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    especialidad VARCHAR(100) NOT NULL,
    cargo VARCHAR(80),
    CONSTRAINT fk_profesores_usuarios FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS empresas (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    direccion VARCHAR(200) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    email VARCHAR(150),
    descripcion VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS jefes_directos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    apellido VARCHAR(80) NOT NULL,
    cargo VARCHAR(80),
    telefono VARCHAR(20),
    email VARCHAR(150),
    empresa_id BIGINT NOT NULL,
    CONSTRAINT fk_jefes_empresa FOREIGN KEY (empresa_id) REFERENCES empresas(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS practicas (
    id BIGSERIAL PRIMARY KEY,
    fecha_inicio DATE NOT NULL,
    fecha_termino DATE NOT NULL,
    descripcion_actividades VARCHAR(1000) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE' CHECK (estado IN ('PENDIENTE', 'EN_CURSO', 'FINALIZADA', 'RECHAZADA')),
    estudiante_id BIGINT NOT NULL,
    profesor_id BIGINT NOT NULL,
    empresa_id BIGINT NOT NULL,
    jefe_directo_id BIGINT NOT NULL,
    CONSTRAINT chk_practicas_fechas CHECK (fecha_termino >= fecha_inicio),
    CONSTRAINT fk_practicas_estudiante FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id) ON DELETE CASCADE,
    CONSTRAINT fk_practicas_profesor FOREIGN KEY (profesor_id) REFERENCES profesores(id) ON DELETE CASCADE,
    CONSTRAINT fk_practicas_empresa FOREIGN KEY (empresa_id) REFERENCES empresas(id) ON DELETE CASCADE,
    CONSTRAINT fk_practicas_jefe FOREIGN KEY (jefe_directo_id) REFERENCES jefes_directos(id) ON DELETE CASCADE
);
