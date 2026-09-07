package com.ipss.practicas.controller;

import com.ipss.practicas.dto.AuthResponse;
import com.ipss.practicas.dto.LoginRequest;
import com.ipss.practicas.dto.RegisterRequest;
import com.ipss.practicas.entity.Estudiante;
import com.ipss.practicas.entity.Profesor;
import com.ipss.practicas.entity.Usuario;
import com.ipss.practicas.enums.RolUsuario;
import com.ipss.practicas.exception.BusinessException;
import com.ipss.practicas.repository.EstudianteRepository;
import com.ipss.practicas.repository.ProfesorRepository;
import com.ipss.practicas.repository.UsuarioRepository;
import com.ipss.practicas.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final EstudianteRepository estudianteRepository;
    private final ProfesorRepository profesorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new BusinessException("Ya existe un usuario registrado con ese email");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.nombre().trim())
                .apellido(request.apellido().trim())
                .email(request.email().trim())
                .password(passwordEncoder.encode(request.password()))
                .rol(request.rol())
                .build();

        Usuario savedUser = usuarioRepository.save(usuario);

        if (request.rol() == RolUsuario.ESTUDIANTE) {
            if (request.carrera() == null || request.carrera().isBlank()) {
                throw new BusinessException("La carrera es obligatoria para estudiantes");
            }
            Estudiante estudiante = Estudiante.builder()
                    .usuario(savedUser)
                    .carrera(request.carrera().trim())
                    .build();
            estudianteRepository.save(estudiante);
        }

        if (request.rol() == RolUsuario.PROFESOR) {
            if (request.especialidad() == null || request.especialidad().isBlank()) {
                throw new BusinessException("La especialidad es obligatoria para profesores");
            }
            Profesor profesor = Profesor.builder()
                    .usuario(savedUser)
                    .especialidad(request.especialidad().trim())
                    .build();
            profesorRepository.save(profesor);
        }

        String token = jwtService.generateToken(savedUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse(token, savedUser.getEmail(), savedUser.getRol().name()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException("Usuario no encontrado"));

        String token = jwtService.generateToken(usuario);
        return ResponseEntity.ok(new AuthResponse(token, usuario.getEmail(), usuario.getRol().name()));
    }
}
