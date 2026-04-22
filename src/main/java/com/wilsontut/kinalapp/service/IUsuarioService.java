package com.wilsontut.kinalapp.service;

import com.wilsontut.kinalapp.entity.Usuario;
    
import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    //Metodo que devuelven una lista de todos los usuarios
    List<Usuario> listarTodos();

    //Metodo que guarda un usuario en la BD
    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorCodigo(long codigoUsuario);

    //Metodo que actualiza un usuario
    Usuario actualizar (long codigoUsuario, Usuario usuario);

    void eliminar(long codigoUsuario);

    boolean existePorCodigo(long codigoUsuario);

    //listar activos
    List<Usuario> listarPorEstado(long estado);

    //login
    Optional<Usuario> login(String username, String password);

}