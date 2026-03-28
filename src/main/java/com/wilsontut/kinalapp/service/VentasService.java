package com.wilsontut.kinalapp.service;

import com.wilsontut.kinalapp.entity.Ventas;
import com.wilsontut.kinalapp.repository.VentasRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service

@Transactional
public class VentasService implements IVentasService {

    private final VentasRepository ventasRepository;

    public VentasService(VentasRepository ventasRepository) {
        this.ventasRepository = ventasRepository;
    }

    @Override

    @Transactional(readOnly = true)
    public List<Ventas> listarTodos() {
        return ventasRepository.findAll();
    }

    @Override
    public Ventas guardar(Ventas ventas) {
        validarVentas(ventas);
        if(ventas.getEstado()==0){
            ventas.setEstado(1);
        }
        return ventasRepository.save(ventas);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ventas> buscarPorCodigo(long codigoVenta) {
        return ventasRepository.findById(codigoVenta);
    }

    @Override
    public Ventas actualizar(long codigoVenta, Ventas ventas) {
        if(!ventasRepository.existsById(codigoVenta)){
            throw new RuntimeException("Venta no se encontro con codigo " + codigoVenta);
        }
        ventas.setCodigoVenta(codigoVenta);
        validarVentas(ventas);

        return ventasRepository.save(ventas);
    }

    @Override
    public void eliminar(long codigoVenta) {
        if (!ventasRepository.existsById(codigoVenta)){
            throw new RuntimeException("La venta no se encontro con el codigo " +codigoVenta);
        }
        ventasRepository.deleteById(codigoVenta);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(long codigoVenta) {
        return ventasRepository.existsById(codigoVenta);
    }
    private void validarVentas(Ventas ventas){
        if (ventas.getFechaVenta() == null){
            throw new IllegalArgumentException("La fecha es un dato obligatorio");
        }

        if (ventas.getCliente()==null){
            throw new IllegalArgumentException("el cliente es un dato obligatorio");
        }

        if(ventas.getUsuario()==null){
            throw new IllegalArgumentException("el usuario es un dato obligatorio");
        }
    }
    // lista activoos
    @Transactional(readOnly = true)
    public List<Ventas> listarPorEstado(int estado){
        return ventasRepository.findByEstado(estado);
    }


}