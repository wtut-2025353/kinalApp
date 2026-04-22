package com.wilsontut.kinalapp.repository;

import com.wilsontut.kinalapp.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByCliente_DPICliente(String dpiCliente);
    List<Venta> findByUsuario_CodigoUsuario(Long codigoUsuario);
    List<Venta> findByEstado(Integer estado);
}
