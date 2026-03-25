package com.wilsontut.kinalapp.service;

import com.wilsontut.kinalapp.entity.Cliente;
import com.wilsontut.kinalapp.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//Anotacion que registra un Bean como un Bean de Spring
//Que la clase contiene la logica de negocio
@Service
//Por defecto todos los metodos de esta clase seran
//transaccionales
//una transaccion es que puede o no ocurrir algo
@Transactional
public class ClienteService implements IClienteService {
    /* private: solo es accesible dentro de la clase
        ClienteRepository: es el repositorio para acceder a la base de datos
        Inyeccion de dependencias Spring nos da el repositorio

     */
    private final ClienteRepository clienteRepository;



    @Override
    public List<Cliente> listarTodos() {
        return List.of();
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        return null;
    }

    @Override
    public Optional<Cliente> buscarPorDPI(String dpi) {
        return Optional.empty();
    }

    @Override
    public Cliente actualizar(String dpi, Cliente cliente) {
        return null;
    }

    @Override
    public void eliminar(String dpi) {

    }

    @Override
    public boolean existePorDPI(String dpi) {
        return false;
    }
}
