
package com.uriel.grueact3t4.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uriel.grueact3t4.model.Usuario;

public interface UsuarioRepository
        extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
}
