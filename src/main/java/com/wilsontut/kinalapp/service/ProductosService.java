package com.wilsontut.kinalapp.service;

import com.wilsontut.kinalapp.entity.Productos;
import com.wilsontut.kinalapp.repository.ProductosRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service

@Transactional
public class ProductosService implements IProductosService {
    private final ProductosRepository productoRepository;

    public ProductosService(ProductosRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override

    @Transactional(readOnly = true)
    public List<Productos> listarTodos() {
        return productoRepository.findAll();
    }

    @Override
    public Productos guardar(Productos producto) {

        validarProducto(producto);
        if(producto.getEstado()==0){
            producto.setEstado(1);
        }
        return productoRepository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Productos> buscarPorCodigo(int codigoProducto) {
        return productoRepository.findById(codigoProducto);
    }

    @Override
    public Productos actualizar(int codigoProducto, Productos producto) {
        if(!productoRepository.existsById(codigoProducto)){
            throw new RuntimeException("producto no se encontro con codigo "+codigoProducto);
        }

        producto.setCodigoProducto(codigoProducto);
        validarProducto(producto);

        return productoRepository.save(producto);
    }

    @Override
    public void eliminar(int codigoProducto) {
        if (!productoRepository.existsById(codigoProducto)){
            throw new RuntimeException("el producto no se encontro con el codigo " +  codigoProducto);
        }
        productoRepository.deleteById(codigoProducto);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(int codigoProducto) {
        return productoRepository.existsById(codigoProducto);
    }
    private void validarProducto(Productos producto){
        if (producto.getNombreProducto() == null || producto.getNombreProducto().trim().isEmpty()){
            throw new IllegalArgumentException("el nombre es un dato obligatorio");
        }

        if (producto.getPrecio()==null){
            throw new IllegalArgumentException("el precio es un dato obligatorio");
        }
    }
    @Transactional(readOnly = true)
    public List<Productos> listarPorEstado(int estado){
        return productoRepository.findByEstado(estado);
    }

}