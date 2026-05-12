package com.wilsontut.kinalapp.controller;

import com.wilsontut.kinalapp.entity.Cliente;
import com.wilsontut.kinalapp.service.IClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final IClienteService clienteService;

    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public String listar(Model model) {
        List<Cliente> clientes = clienteService.listarTodos();
        model.addAttribute("clientes", clientes);
        return "clientes";
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "registro-cliente";
    }

    @PostMapping("/registro")
    public String guardar(@ModelAttribute("cliente") Cliente cliente, BindingResult bindingResult,
                          Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("cliente", cliente);
            model.addAttribute("error", "Errores en el formulario: " + bindingResult.getAllErrors());
            return "registro-cliente";
        }
        try {
            clienteService.guardar(cliente);
            redirectAttributes.addFlashAttribute("mensaje", "Cliente registrado exitosamente");
            return "redirect:/clientes";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/clientes/registro";
        }
    }

    @GetMapping("/{dpi}/editar")
    public String mostrarFormularioEdicion(@PathVariable String dpi, Model model) {
        return clienteService.buscarPorDPI(dpi)
                .map(cliente -> {
                    model.addAttribute("cliente", cliente);
                    return "editar-cliente";
                })
                .orElse("redirect:/clientes");
    }

    @PostMapping("/{dpi}/editar")
    public String actualizar(@PathVariable String dpi, @ModelAttribute Cliente cliente,
                             RedirectAttributes redirectAttributes) {
        try {
            if (!clienteService.existePorDPI(dpi)) {
                return "redirect:/clientes";
            }
            clienteService.actualizar(dpi, cliente);
            redirectAttributes.addFlashAttribute("mensaje", "Cliente actualizado exitosamente");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/clientes";
    }

    @GetMapping("/{dpi}/eliminar")
    public String eliminar(@PathVariable String dpi, RedirectAttributes redirectAttributes) {
        try {
            if (!clienteService.existePorDPI(dpi)) {
                return "redirect:/clientes";
            }
            clienteService.eliminar(dpi);
            redirectAttributes.addFlashAttribute("mensaje", "Cliente eliminado exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el cliente");
        }
        return "redirect:/clientes";
    }

    @GetMapping("/{dpi}")
    public ResponseEntity<Cliente> buscarPorDpi(@PathVariable String dpi) {
        return clienteService.buscarPorDPI(dpi)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<Cliente> guardarApi(@RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteService.guardar(cliente);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }

    @PutMapping("/{dpi}")
    @ResponseBody
    public ResponseEntity<Cliente> actualizar(@PathVariable String dpi, @RequestBody Cliente cliente) {
        if (!clienteService.existePorDPI(dpi)) {
            return ResponseEntity.notFound().build();
        }
        Cliente actualizado = clienteService.actualizar(dpi, cliente);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{dpi}")
    @ResponseBody
    public ResponseEntity<Void> eliminar(@PathVariable String dpi) {
        if (!clienteService.existePorDPI(dpi)) {
            return ResponseEntity.notFound().build();
        }
        clienteService.eliminar(dpi);
        return ResponseEntity.noContent().build();
    }
}