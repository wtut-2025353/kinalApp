package com.wilsontut.kinalapp.service;

import com.wilsontut.kinalapp.entity.Usuario;
import com.wilsontut.kinalapp.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService implements IUsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
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
        //encriptar el password antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorCodigo(long codigoUsuario) {
        //buscar un usuario por codigo
        return usuarioRepository.findById(codigoUsuario);
    }

    @Override
    public Usuario actualizar(long codigoUsuario, Usuario usuario) {
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
    public void eliminar(long codigoUsuario) {
        //eliminar un usuario
        if (!usuarioRepository.existsById(codigoUsuario)){
            throw new RuntimeException("El usuario no se encontro con el codigo "+codigoUsuario);
        }
        usuarioRepository.deleteById(codigoUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(long codigoUsuario) {
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
    public List<Usuario> listarPorEstado(long estado){
        return usuarioRepository.findByEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> login(String username, String password) {
        String usernameLimpio = username != null ? username.trim() : "";
        String passwordLimpio = password != null ? password.trim() : "";

        System.out.println("DEBUG LOGIN - Username ingresado: [" + usernameLimpio + "]");
        System.out.println("DEBUG LOGIN - Password ingresado: [" + passwordLimpio + "]");

        Optional<Usuario> usuario = usuarioRepository.findByUsername(usernameLimpio);

        if (usuario.isPresent()) {
            Usuario u = usuario.get();
            System.out.println("DEBUG LOGIN - Usuario encontrado: " + u.getUsername());
            System.out.println("DEBUG LOGIN - Password en DB: [" + u.getPassword() + "]");
            System.out.println("DEBUG LOGIN - Estado en DB: " + u.getEstado());

            if (!u.getPassword().equals(passwordLimpio)) {
                System.out.println("DEBUG LOGIN - ERROR: Password no coincide");
                return Optional.empty();
            }
            if (u.getEstado() != 1) {
                System.out.println("DEBUG LOGIN - ERROR: Usuario inactivo (estado=" + u.getEstado() + ")");
                return Optional.empty();
            }
            System.out.println("DEBUG LOGIN - EXITO: Login correcto");
            return usuario;
        } else {
            System.out.println("DEBUG LOGIN - ERROR: Usuario no encontrado: " + usernameLimpio);
            return Optional.empty();
        }
    }

}