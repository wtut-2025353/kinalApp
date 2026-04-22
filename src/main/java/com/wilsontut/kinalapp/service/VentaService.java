package com.wilsontut.kinalapp.service;

import com.wilsontut.kinalapp.entity.Venta;
import com.wilsontut.kinalapp.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VentaService implements IVentaService {

    private final VentaRepository ventaRepository;

    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    public List<Venta> listarTodos() {
        return ventaRepository.findAll();
    }

    @Override
    @Transactional
    public Venta guardar(Venta venta) {
        return ventaRepository.save(venta);
    }

    @Override
    public Optional<Venta> buscarPorCodigo(Long codigo) {
        return ventaRepository.findById(codigo);
    }

    @Override
    @Transactional
    public Venta actualizar(Long codigo, Venta venta) {
        if (!ventaRepository.existsById(codigo)) {
            return null;
        }
        venta.setCodigoVenta(codigo);
        return ventaRepository.save(venta);
    }

    @Override
    @Transactional
    public void eliminar(Long codigo) {
        ventaRepository.deleteById(codigo);
    }

    @Override
    public boolean existePorCodigo(Long codigo) {
        return ventaRepository.existsById(codigo);
    }
}
