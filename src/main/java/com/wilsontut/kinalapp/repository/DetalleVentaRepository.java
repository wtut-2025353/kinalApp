package com.wilsontut.kinalapp.repository;

import com.wilsontut.kinalapp.entity.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    List<DetalleVenta> findByVenta_CodigoVenta(long codigoVenta);
}