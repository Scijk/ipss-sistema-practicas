package com.ipss.practicas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "empresas")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String nombre;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String direccion;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    private String telefono;

    @Email
    @Size(max = 150)
    @Column(length = 150)
    private String email;

    @Size(max = 200)
    @Column(length = 200)
    private String descripcion;

    @JsonIgnore
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JefeDirecto> jefesDirectos = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Practica> practicas = new ArrayList<>();
}
