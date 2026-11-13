
package com.uriel.grueact3t4.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uriel.grueact3t4.model.Producto;

public interface ProductoRepository
        extends JpaRepository<Producto, Long> {
}
