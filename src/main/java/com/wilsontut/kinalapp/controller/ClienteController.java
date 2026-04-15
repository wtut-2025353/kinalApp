package com.wilsontut.kinalapp.controller;

import com.wilsontut.kinalapp.entity.Cliente;
import com.wilsontut.kinalapp.repository.ClienteRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteController {
    private final ClienteRepository repo;

    public ClienteController(ClienteRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public String listar(Model model) {
        List<Cliente> clientes = repo.findAll();
        model.addAttribute("clientes", clientes);
        return "clientes";
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "registro-cliente";
    }

    @PostMapping("/registro")
    public String guardarCliente(@ModelAttribute Cliente cliente, RedirectAttributes redirectAttributes) {
        repo.save(cliente);
        redirectAttributes.addFlashAttribute("mensaje", "Cliente registrado exitosamente");
        return "redirect:/clientes/registro";
    }
}