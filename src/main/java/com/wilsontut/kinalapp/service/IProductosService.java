package com.wilsontut.kinalapp.service;

import com.wilsontut.kinalapp.entity.Productos;
    
import java.util.List;
import java.util.Optional;

public interface IProductosService {
    List<Productos> listarTodos();
    //guarda en la bd
    Productos guardar(Productos producto);
    Optional<Productos> buscarPorCodigo(int codigoProducto);

    //metodo que actualiza un producto
    Productos actualizar (int codigoProducto, Productos producto);

    boolean existePorCodigo(int codigoProducto);

    //lista de productos por estado
    List<Productos> listarPorEstado(int estado);
    

}