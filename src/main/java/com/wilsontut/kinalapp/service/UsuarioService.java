package com.wilsontut.kinalapp.service;

import com.wilsontut.kinalapp.entity.Usuario;
import com.wilsontut.kinalapp.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService implements IUsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override

    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        validarUsuario(usuario);
        if(usuario.getEstado()==0){
            usuario.setEstado(1);
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorCodigo(int codigoUsuario) {
        //buscar un usuario por codigo
        return usuarioRepository.findById(codigoUsuario);
    }

    @Override
    public Usuario actualizar(int codigoUsuario, Usuario usuario) {
        //actualiza un usuario existente
        if(!usuarioRepository.existsById(codigoUsuario)){
            throw new RuntimeException("Usuario no se encontro con codigo "+codigoUsuario);
            //si no existe, se lanza una excepcion(error controlado)
        }
        usuario.setCodigoUsuario(codigoUsuario);
        validarUsuario(usuario);

        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminar(int codigoUsuario) {
        //eliminar un usuario
        if (!usuarioRepository.existsById(codigoUsuario)){
            throw new RuntimeException("El usuario no se encontro con el codigo "+codigoUsuario);
        }
        usuarioRepository.deleteById(codigoUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(int codigoUsuario) {
        //verificar si existe el usuario
        return usuarioRepository.existsById(codigoUsuario);
    }
    // Meotodo privado (Solo pueden utilizarce dentro de la clase)
    private void validarUsuario(Usuario usuario){
        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()){
            throw new IllegalArgumentException("El username es un dato obligatorio");
        }

        if (usuario.getPassword()==null || usuario.getPassword().trim().isEmpty()){
            throw new IllegalArgumentException("el password es un dato obligatorio");
        }

        if(usuario.getEmail()==null || usuario.getEmail().trim().isEmpty()){
            throw new IllegalArgumentException("el email es un dato obligatorio");
        }
    }
    @Transactional(readOnly = true)
    public List<Usuario> listarPorEstado(int estado){
        return usuarioRepository.findByEstado(estado);
    }


}