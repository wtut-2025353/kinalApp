package com.wilsontut.kinalapp.repository;

import com.wilsontut.kinalapp.entity.Productos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductosRepository extends JpaRepository<Productos, Integer> {
    List<Productos> findByEstado(int estado);
}