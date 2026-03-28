package com.wilsontut.kinalapp.service;

import com.wilsontut.kinalapp.entity.DetalleVenta;
import com.wilsontut.kinalapp.repository.DetalleVentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DetalleVentaService implements IDetalleVentaService {

    private final DetalleVentaRepository detalleVentaRepository;

    public DetalleVentaService(DetalleVentaRepository detalleVentaRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
    }

    @Override

    @Transactional(readOnly = true)
    public List<DetalleVenta> listarTodos() {
        return detalleVentaRepository.findAll();
    }

    @Override
    public DetalleVenta guardar(DetalleVenta detalleVenta) {
        validarDetalleVenta(detalleVenta);
        return detalleVentaRepository.save(detalleVenta);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DetalleVenta> buscarPorCodigo(long codigoDetalleVenta) {
        return detalleVentaRepository.findById(codigoDetalleVenta);
    }

    @Override
    public DetalleVenta actualizar(long codigoDetalleVenta, DetalleVenta detalleVenta) {
        if(!detalleVentaRepository.existsById(codigoDetalleVenta)){
            throw new RuntimeException("DetalleVenta no se encontro con codigo "+codigoDetalleVenta);
        }
        detalleVenta.setCodigoDetalleVenta(codigoDetalleVenta);
        validarDetalleVenta(detalleVenta);

        return detalleVentaRepository.save(detalleVenta);
    }

    @Override
    public void eliminar(long codigoDetalleVenta) {
        if (!detalleVentaRepository.existsById(codigoDetalleVenta)){
            throw new RuntimeException("El detalle de venta no se encontro con el codigo "+codigoDetalleVenta);
        }
        detalleVentaRepository.deleteById(codigoDetalleVenta);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(long codigoDetalleVenta) {
        return detalleVentaRepository.existsById(codigoDetalleVenta);
    }
    private void validarDetalleVenta(DetalleVenta detalleVenta){
        if (detalleVenta.getProducto() == null){
            throw new IllegalArgumentException("El producto es un dato obligatorio");
        }

        if (detalleVenta.getVenta()==null){
            throw new IllegalArgumentException("la venta es un dato obligatorio");
        }

        if (detalleVenta.getCantidad() <= 0){
            throw new IllegalArgumentException("la cantidad debe ser mayor a cero");
        }
    }
    @Transactional(readOnly = true)
    public List<DetalleVenta> listarPorVenta(long codigoVenta){
        return detalleVentaRepository.findByVenta_CodigoVenta(codigoVenta);
    }


}