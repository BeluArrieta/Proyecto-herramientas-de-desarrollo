package com.example.proyec_herramientas.service;

import com.example.proyec_herramientas.model.ProductoDTO;
import com.example.proyec_herramientas.persistence.ProductoDocument;
import com.example.proyec_herramientas.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository repository;

    public ProductoServiceImpl(ProductoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ProductoDTO> listar() {
        return repository.findAll().stream()
                .sorted((a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()))
                .map(this::toDTO)
                .toList();
    }

    @Override
    public ProductoDTO obtenerPorId(int idProducto) {
        ProductoDocument doc = repository.findById(idProducto).orElse(null);
        return doc != null ? toDTO(doc) : null;
    }

    @Override
    public ProductoDTO crear(ProductoDTO producto) {
        Integer id = producto.getIdProducto();
        if (id == null || id <= 0) {
            id = siguienteId();
        }
        ProductoDocument doc = new ProductoDocument(
                id,
                producto.getNombre(),
                producto.getStock(),
                producto.getPrecio(),
                producto.getCategoria());
        return toDTO(repository.save(doc));
    }

    @Override
    public ProductoDTO actualizar(int idProducto, ProductoDTO producto) {
        ProductoDocument doc = repository.findById(idProducto).orElse(null);
        if (doc == null) {
            return null;
        }
        doc.setNombre(producto.getNombre());
        doc.setStock(producto.getStock());
        doc.setPrecio(producto.getPrecio());
        doc.setCategoria(producto.getCategoria());
        return toDTO(repository.save(doc));
    }

    @Override
    public void eliminar(int idProducto) {
        repository.deleteById(idProducto);
    }

    @Override
    public boolean actualizarStock(int idProducto, int cantidad) {
        ProductoDocument doc = repository.findById(idProducto).orElse(null);
        if (doc == null) {
            return false;
        }
        doc.setStock(doc.getStock() - cantidad);
        repository.save(doc);
        return true;
    }

    private int siguienteId() {
        return repository.findAll().stream()
                .mapToInt(ProductoDocument::getId)
                .max()
                .orElse(0) + 1;
    }

    private ProductoDTO toDTO(ProductoDocument doc) {
        ProductoDTO dto = new ProductoDTO();
        dto.setIdProducto(doc.getId());
        dto.setNombre(doc.getNombre());
        dto.setStock(doc.getStock());
        dto.setPrecio(doc.getPrecio());
        dto.setCategoria(doc.getCategoria());
        return dto;
    }
}