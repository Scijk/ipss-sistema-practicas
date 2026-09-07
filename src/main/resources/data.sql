INSERT INTO usuarios (id, nombre, apellido, email, password, rol)
VALUES
    (1, 'Ana', 'García', 'ana.garcia@email.com', '$2b$12$JrPZeKTia64eEuh5AK4M0uv9QldZX5rOpvWX.oWMUdQ4ibKHKM1CW', 'ESTUDIANTE')
ON CONFLICT (id) DO NOTHING;

INSERT INTO usuarios (id, nombre, apellido, email, password, rol)
VALUES
    (2, 'Luis', 'Pérez', 'luis.perez@email.com', '$2b$12$JrPZeKTia64eEuh5AK4M0uv9QldZX5rOpvWX.oWMUdQ4ibKHKM1CW', 'PROFESOR')
ON CONFLICT (id) DO NOTHING;

INSERT INTO usuarios (id, nombre, apellido, email, password, rol)
VALUES
    (3, 'Marta', 'Ruiz', 'marta.ruiz@email.com', '$2b$12$JrPZeKTia64eEuh5AK4M0uv9QldZX5rOpvWX.oWMUdQ4ibKHKM1CW', 'ESTUDIANTE')
ON CONFLICT (id) DO NOTHING;

INSERT INTO estudiantes (id, usuario_id, carrera, telefono, direccion)
VALUES
    (1, 1, 'Informática', '987654321', 'Calle Los Álamos 123')
ON CONFLICT (id) DO NOTHING;

INSERT INTO estudiantes (id, usuario_id, carrera, telefono, direccion)
VALUES
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
    (1, '2026-03-10', '2026-05-10', 'Desarrollar módulos backend, participar en reuniones de coordinación y revisar documentación técnica.', 'PENDIENTE', 1, 1, 1, 1)
ON CONFLICT (id) DO NOTHING;

INSERT INTO practicas (id, fecha_inicio, fecha_termino, descripcion_actividades, estado, estudiante_id, profesor_id, empresa_id, jefe_directo_id)
VALUES
    (2, '2026-04-01', '2026-06-15', 'Apoyar en tareas administrativas, documentación de procesos y análisis de reportes.', 'EN_CURSO', 2, 1, 1, 1)
ON CONFLICT (id) DO NOTHING;
