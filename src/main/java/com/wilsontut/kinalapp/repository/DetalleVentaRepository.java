package com.wilsontut.kinalapp.repository;

import com.wilsontut.kinalapp.entity.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    List<DetalleVenta> findByVenta_CodigoVenta(Long codigoVenta);
    List<DetalleVenta> findByProducto_CodigoProducto(int codigoProducto);
    void deleteByVenta_CodigoVenta(Long codigoVenta);
}