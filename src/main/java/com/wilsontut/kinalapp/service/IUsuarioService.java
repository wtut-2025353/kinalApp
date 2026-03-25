package com.wilsontut.kinalapp.service;

import com.wilsontut.kinalapp.entity.Usuario;
    
import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    //Metodo que devuelven una lista de todos los usuarios
    List<Usuario> listarTodos();

    //Metodo que guarda un usuario en la BD
    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorCodigo(int codigoUsuario);

    //Metodo que actualiza un usuario
    Usuario actualizar (int codigoUsuario, Usuario usuario);

    void eliminar(int codigoUsuario);

    boolean existePorCodigo(int codigoUsuario);

    //listar activos
    List<Usuario> listarPorEstado(int estado);

}