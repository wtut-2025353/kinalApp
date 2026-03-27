package com.wilsontut.kinalapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "Productos")
public class Productos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_producto")
    private int codigoProducto;
    @Column
    private String nombre_producto;
    @Column(precision = 10, scale = 2)
    private BigDecimal precio;
    @Column
    private int stock;
    @Column
    private int estado;

    public Productos() {
    }

    public Productos(int codigoProducto, String nombre_producto, BigDecimal precio, int stock, int estado) {
        this.codigoProducto = codigoProducto;
        this.nombre_producto = nombre_producto;
        this.precio = precio;
        this.stock = stock;
        this.estado = estado;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getNombreProducto() {
        return nombre_producto;
    }

    public void setNombreProducto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

}