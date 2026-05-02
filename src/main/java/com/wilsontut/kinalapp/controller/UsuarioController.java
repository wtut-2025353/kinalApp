package com.wilsontut.kinalapp.controller;

import com.wilsontut.kinalapp.entity.Usuario;
import com.wilsontut.kinalapp.service.IUsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final IUsuarioService usuarioService;

    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listar(Model model) {
        List<Usuario> usuarios = usuarioService.listarTodos();
        model.addAttribute("usuarios", usuarios);
        return "usuarios";
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro-usuario";
    }

    @PostMapping("/registro")
    public String guardar(@ModelAttribute("usuario") Usuario usuario, BindingResult bindingResult,
                          Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("error", "Errores en el formulario");
            return "registro-usuario";
        }
        try {
            //el registro publico siempre crea usuarios con ROLE_USER
            usuario.setRol("ROLE_USER");
            usuario.setEstado(1);
            usuarioService.guardar(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario registrado exitosamente. Inicie sesion.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/usuarios/registro";
        }
    }

    @GetMapping("/{codigo:[0-9]+}")
    public String buscarPorCodigo(@PathVariable long codigo, Model model) {
        return usuarioService.buscarPorCodigo(codigo)
                .map(usuario -> {
                    model.addAttribute("usuario", usuario);
                    return "detalle-usuario";
                })
                .orElse("redirect:/usuarios");
    }

    @GetMapping("/{codigo:[0-9]+}/editar")
    public String mostrarFormularioEdicion(@PathVariable long codigo, Model model) {
        return usuarioService.buscarPorCodigo(codigo)
                .map(usuario -> {
                    model.addAttribute("usuario", usuario);
                    return "editar-usuario";
                })
                .orElse("redirect:/usuarios");
    }

    @PostMapping("/{codigo:[0-9]+}/editar")
    public String actualizar(@PathVariable long codigo, @ModelAttribute Usuario usuario,
                             RedirectAttributes redirectAttributes) {
        try {
            if (!usuarioService.existePorCodigo(codigo)) {
                return "redirect:/usuarios";
            }
            usuarioService.actualizar(codigo, usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado exitosamente");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/usuarios";
    }

    @GetMapping("/{codigo:[0-9]+}/eliminar")
    public String eliminar(@PathVariable long codigo, RedirectAttributes redirectAttributes) {
        try {
            if (!usuarioService.existePorCodigo(codigo)) {
                return "redirect:/usuarios";
            }
            usuarioService.eliminar(codigo);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el usuario");
        }
        return "redirect:/usuarios";
    }
}
