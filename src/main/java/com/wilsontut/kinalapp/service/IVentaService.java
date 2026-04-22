package com.wilsontut.kinalapp.service;

import com.wilsontut.kinalapp.entity.Venta;

import java.util.List;
import java.util.Optional;

public interface IVentaService {
    List<Venta> listarTodos();
    Venta guardar(Venta venta);
    Optional<Venta> buscarPorCodigo(Long codigo);
    Venta actualizar(Long codigo, Venta venta);
    void eliminar(Long codigo);
    boolean existePorCodigo(Long codigo);
}
