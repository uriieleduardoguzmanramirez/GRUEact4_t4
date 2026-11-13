
package com.uriel.grueact3t4.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.uriel.grueact3t4.dto.ProductoDTO;
import com.uriel.grueact3t4.model.Categoria;
import com.uriel.grueact3t4.service.CatalogoService;

@RestController
@RequestMapping("/api")
public class CatalogoController {

    private final CatalogoService servicio;

    public CatalogoController(CatalogoService servicio) {
        this.servicio = servicio;
    }

    // Consultar categorias
    @GetMapping("/categorias")
    public List<Map<String, Object>> categorias() {
        return servicio.listarCategorias().stream()
            .map(c -> Map.<String, Object>of(
                "id", c.getId(),
                "nombre", c.getNombre()
            )).toList();
    }

    // Crear categoria
    @PostMapping("/categorias")
    public ResponseEntity<Map<String, Object>> crearCategoria(
            @RequestBody Map<String, String> datos) {

        Categoria c = servicio.crearCategoria(datos.get("nombre"));

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(Map.of("id", c.getId(), "nombre", c.getNombre()));
    }

    // READ: Consultar productos
    @GetMapping("/productos")
    public List<ProductoDTO> listar() {
        return servicio.listar();
    }

    // READ: Buscar producto por ID
    @GetMapping("/productos/{id}")
    public ProductoDTO buscar(@PathVariable Long id) {
        return servicio.buscar(id);
    }

    // CREATE: Crear producto
    @PostMapping("/productos")
    public ResponseEntity<ProductoDTO> crear(
            @RequestBody ProductoDTO datos) {

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(servicio.crear(datos));
    }

    // UPDATE: Actualizar producto
    @PutMapping("/productos/{id}")
    public ProductoDTO actualizar(@PathVariable Long id,
                                  @RequestBody ProductoDTO datos) {
        return servicio.actualizar(id, datos);
    }

    // DELETE: Eliminar producto
    @DeleteMapping("/productos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
    }
}