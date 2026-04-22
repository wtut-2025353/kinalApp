package com.wilsontut.kinalapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.criteria.CriteriaBuilder;

@Entity
@Table(name = "clientes")
public class Cliente {
    @Id
    @Column(name = "dpi_cliente")
    private String DPICliente;
    @Column
    private String nombreCliente;
    @Column
    private String apellidoCliente;
    @Column
    private String direccion;
    @Column
    private Integer estado;

    public Cliente() {
    }

    public Cliente(String apellidoCliente, String DPICliente, String nombreCliente, String direccion, Integer estado) {
        this.apellidoCliente = apellidoCliente;
        this.DPICliente = DPICliente;
        this.nombreCliente = nombreCliente;
        this.direccion = direccion;
        this.estado = estado;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getApellidoCliente() {
        return apellidoCliente;
    }

    public void setApellidoCliente(String apellidoCliente) {
        this.apellidoCliente = apellidoCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDPICliente() {
        return DPICliente;
    }

    public void setDPICliente(String DPICliente) {
        this.DPICliente = DPICliente;
    }
}

