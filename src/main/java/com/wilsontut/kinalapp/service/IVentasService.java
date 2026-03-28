package com.wilsontut.kinalapp.service;

import com.wilsontut.kinalapp.entity.Ventas;

import java.util.List;
import java.util.Optional;

public interface IVentasService {
    List<Ventas> listarTodos();
    //Metodo que guarda una venta en la BD
    Ventas guardar(Ventas ventas);
    Optional<Ventas> buscarPorCodigo(long codigoVenta);

    Ventas actualizar (long codigoVenta, Ventas ventas);

    void eliminar(long codigoVenta);

    boolean existePorCodigo(long codigoVenta);

    //listar activos
    List<Ventas> listarPorEstado(int estado);


}