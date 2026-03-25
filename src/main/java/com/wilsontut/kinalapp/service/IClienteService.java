package com.wilsontut.kinalapp.service;

import com.wilsontut.kinalapp.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface IClienteService {
    //Interfaz: es un contrato que dice  QUE metodos debe tener
    //cualquier servicio de Clientes, no tiene implementacion
    //solo la definicion de los metodos

    //Metodo que devuelven una lista de todos los clientes
    List<Cliente> listarTodos();
    //List<Cliente> lo que hace es devolver una lista
    //de objetos de la entidad Clientes

    //Metodo que guarda un cliente en la BD
    Cliente guardar(Cliente cliente);
    //Parametros -Recibe un objeto de tipo cliente con los datos a guardar

    //Optional -contenedor qeu puede o no tener un valor
    //evita el error de NullPointerException
    Optional<Cliente> buscarPorDPI(String dpi);

    //Metodo que actualiza un cliente
    Cliente actualizar (String dpi, Cliente cliente);
    //Parametros - dpi: DPI del cliente a actualizar
    //Cliente cliente: objeto con los datos nuevos
    //Retorna un objeto de tipo cliente ya actualizado

    //Metodo de tipo void para eliminar a un cliente
    //void: no retorna nada
    //Elimina un cliente por su DPI
    void eliminar(String dpi);

    //boolean retorna verdadero si existe y falso si no existe
    boolean existePorDPI(String dpi);

    //
}
