DROP TABLE IF EXISTS practicas CASCADE;
DROP TABLE IF EXISTS jefes_directos CASCADE;
DROP TABLE IF EXISTS empresas CASCADE;
DROP TABLE IF EXISTS profesores CASCADE;
DROP TABLE IF EXISTS estudiantes CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE;

CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    apellido VARCHAR(80) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL CHECK (rol IN ('ESTUDIANTE', 'PROFESOR'))
);

CREATE TABLE estudiantes (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    carrera VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    direccion VARCHAR(200),
    CONSTRAINT fk_estudiantes_usuarios FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE profesores (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    especialidad VARCHAR(100) NOT NULL,
    cargo VARCHAR(80),
    CONSTRAINT fk_profesores_usuarios FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE empresas (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    direccion VARCHAR(200) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    email VARCHAR(150),
    descripcion VARCHAR(200)
);

CREATE TABLE jefes_directos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    apellido VARCHAR(80) NOT NULL,
    cargo VARCHAR(80),
    telefono VARCHAR(20),
    email VARCHAR(150),
    empresa_id BIGINT NOT NULL,
    CONSTRAINT fk_jefes_empresa FOREIGN KEY (empresa_id) REFERENCES empresas(id) ON DELETE CASCADE
);

CREATE TABLE practicas (
    id BIGSERIAL PRIMARY KEY,
    fecha_inicio DATE NOT NULL,
    fecha_termino DATE NOT NULL,
    descripcion_actividades TEXT NOT NULL,
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

INSERT INTO usuarios (id, nombre, apellido, email, password, rol)
VALUES
    (1, 'Ana', 'García', 'ana.garcia@email.com', '123456', 'ESTUDIANTE'),
    (2, 'Luis', 'Pérez', 'luis.perez@email.com', '123456', 'PROFESOR'),
    (3, 'Marta', 'Ruiz', 'marta.ruiz@email.com', '123456', 'ESTUDIANTE')
ON CONFLICT (id) DO NOTHING;

INSERT INTO estudiantes (id, usuario_id, carrera, telefono, direccion)
VALUES
    (1, 1, 'Informática', '987654321', 'Calle Los Álamos 123'),
    (2, 3, 'Administración', '912345678', 'Avenida Central 456')
ON CONFLICT (id) DO NOTHING;

INSERT INTO profesores (id, usuario_id, especialidad, cargo)
VALUES
    (1, 2, 'Programación', 'Docente Supervisor')
ON CONFLICT (id) DO NOTHING;

INSERT INTO empresas (id, nombre, direccion, telefono, email, descripcion)
VALUES
    (1, 'IPSS Tech', 'Av. Las Industrias 1050', '56223344', 'contacto@ipsstech.cl', 'Empresa de desarrollo de software y soluciones TI')
ON CONFLICT (id) DO NOTHING;

INSERT INTO jefes_directos (id, nombre, apellido, cargo, telefono, email, empresa_id)
VALUES
    (1, 'Carlos', 'Molina', 'Ingeniero de Proyecto', '987123456', 'carlos.molina@ipsstech.cl', 1)
ON CONFLICT (id) DO NOTHING;

INSERT INTO practicas (id, fecha_inicio, fecha_termino, descripcion_actividades, estado, estudiante_id, profesor_id, empresa_id, jefe_directo_id)
VALUES
    (1, '2026-03-10', '2026-05-10', 'Desarrollar módulos backend, participar en reuniones de coordinación y revisar documentación técnica.', 'PENDIENTE', 1, 1, 1, 1),
    (2, '2026-04-01', '2026-06-15', 'Apoyar en tareas administrativas, documentación de procesos y análisis de reportes.', 'EN_CURSO', 2, 1, 1, 1)
ON CONFLICT (id) DO NOTHING;
