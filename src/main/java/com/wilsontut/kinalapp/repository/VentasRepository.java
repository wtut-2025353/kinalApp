package com.wilsontut.kinalapp.repository;

import com.wilsontut.kinalapp.entity.Ventas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VentasRepository extends JpaRepository<Ventas, Long> {
    List<Ventas> findByEstado(int estado);
}