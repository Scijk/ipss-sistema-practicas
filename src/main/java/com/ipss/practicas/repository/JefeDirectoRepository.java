package com.ipss.practicas.repository;

import com.ipss.practicas.entity.JefeDirecto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JefeDirectoRepository extends JpaRepository<JefeDirecto, Long> {
    List<JefeDirecto> findByEmpresaId(Long empresaId);
}
