package com.wilsontut.kinalapp.service;

import com.wilsontut.kinalapp.entity.DetalleVenta;

import java.util.List;
import java.util.Optional;

public interface IDetalleVentaService {

    List<DetalleVenta> listarTodos();
    
    DetalleVenta guardar(DetalleVenta detalleVenta);

    Optional<DetalleVenta> buscarPorCodigo(long codigoDetalleVenta);

    DetalleVenta actualizar (long codigoDetalleVenta, DetalleVenta detalleVenta);

    void eliminar(long codigoDetalleVenta);

    boolean existePorCodigo(long codigoDetalleVenta);

    List<DetalleVenta> listarPorVenta(long codigoVenta);
}