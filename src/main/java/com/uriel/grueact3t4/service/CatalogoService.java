
package com.uriel.grueact3t4.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.uriel.grueact3t4.dto.ProductoDTO;
import com.uriel.grueact3t4.model.Categoria;
import com.uriel.grueact3t4.model.Producto;
import com.uriel.grueact3t4.repository.CategoriaRepository;
import com.uriel.grueact3t4.repository.ProductoRepository;

@Service
@Transactional
public class CatalogoService {

    private final ProductoRepository productos;
    private final CategoriaRepository categorias;

    public CatalogoService(ProductoRepository productos,
                           CategoriaRepository categorias) {
        this.productos = productos;
        this.categorias = categorias;
    }

    // Crear categoria
    public Categoria crearCategoria(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Nombre obligatorio");
        }

        Categoria c = new Categoria();
        c.setNombre(nombre.trim());
        return categorias.save(c);
    }

    // Consultar categorias
    public List<Categoria> listarCategorias() {
        return categorias.findAll();
    }

    // READ: Consultar productos
    public List<ProductoDTO> listar() {
        return productos.findAll().stream()
                .map(this::convertir).toList();
    }

    // READ: Consultar un producto
    public ProductoDTO buscar(Long id) {
        return convertir(encontrar(id));
    }

    // CREATE: Agregar producto
    public ProductoDTO crear(ProductoDTO datos) {
        return guardar(new Producto(), datos);
    }

    // UPDATE: Modificar producto
    public ProductoDTO actualizar(Long id, ProductoDTO datos) {
        return guardar(encontrar(id), datos);
    }

    // DELETE: Eliminar producto
    public void eliminar(Long id) {
        productos.delete(encontrar(id));
    }

    // Buscar por ID
    private Producto encontrar(Long id) {
        return productos.findById(id).orElseThrow(() ->
            new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }

    // Guardar datos y relacionar categoria
    private ProductoDTO guardar(Producto p, ProductoDTO datos) {

        if (datos == null || datos.nombre() == null ||
            datos.nombre().isBlank() || datos.precio() == null ||
            datos.precio().signum() <= 0 || datos.categoriaId() == null) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Datos invalidos");
        }

        Categoria c = categorias.findById(datos.categoriaId())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Categoria no existe"));

        p.setNombre(datos.nombre().trim());
        p.setPrecio(datos.precio());
        p.setCategoria(c);

        return convertir(productos.save(p));
    }

    // Convertir Entity a DTO
    private ProductoDTO convertir(Producto p) {
        return new ProductoDTO(
            p.getId(),
            p.getNombre(),
            p.getPrecio(),
            p.getCategoria().getId(),
            p.getCategoria().getNombre()
        );
    }
}
