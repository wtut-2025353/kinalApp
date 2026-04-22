package com.wilsontut.kinalapp.repository;

import com.wilsontut.kinalapp.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findByEstado(long estado);
    Optional<Usuario> findByUsername(String username);
}