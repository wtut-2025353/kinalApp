package com.wilsontut.kinalapp.controller;

import com.wilsontut.kinalapp.entity.Productos;
import com.wilsontut.kinalapp.service.IProductosService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductosController {

    private final IProductosService productosService;

    public ProductosController(IProductosService productosService) {
        this.productosService = productosService;
    }

    private boolean verificarSesion(HttpSession session) {
        return session.getAttribute("usuarioLogueado") != null;
    }

    // HTML Views

    @GetMapping
    public String listar(Model model, HttpSession session) {
        if (!verificarSesion(session)) {
            return "redirect:/login";
        }
        List<Productos> productos = productosService.listarTodos();
        model.addAttribute("productos", productos);
        return "productos";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model, HttpSession session) {
        if (!verificarSesion(session)) {
            return "redirect:/login";
        }
        model.addAttribute("producto", new Productos());
        model.addAttribute("esNuevo", true);
        return "form-producto";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Productos producto, BindingResult bindingResult,
                          Model model, RedirectAttributes redirectAttributes, HttpSession session) {
        if (!verificarSesion(session)) {
            return "redirect:/login";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("producto", producto);
            model.addAttribute("esNuevo", true);
            model.addAttribute("error", "Errores en el formulario: " + bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "form-producto";
        }
        try {
            productosService.guardar(producto);
            redirectAttributes.addFlashAttribute("mensaje", "Producto guardado exitosamente");
            return "redirect:/productos";
        } catch (IllegalArgumentException e) {
            model.addAttribute("producto", producto);
            model.addAttribute("esNuevo", true);
            model.addAttribute("error", e.getMessage());
            return "form-producto";
        }
    }

    @GetMapping("/editar/{codigo}")
    public String mostrarFormularioEdicion(@PathVariable int codigo, Model model, HttpSession session) {
        if (!verificarSesion(session)) {
            return "redirect:/login";
        }
        return productosService.buscarPorCodigo(codigo)
                .map(producto -> {
                    model.addAttribute("producto", producto);
                    model.addAttribute("esNuevo", false);
                    return "form-producto";
                })
                .orElse("redirect:/productos");
    }

    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute Productos producto,
                             RedirectAttributes redirectAttributes, HttpSession session) {
        if (!verificarSesion(session)) {
            return "redirect:/login";
        }
        try {
            if (!productosService.existePorCodigo(producto.getCodigoProducto())) {
                return "redirect:/productos";
            }
            productosService.actualizar(producto.getCodigoProducto(), producto);
            redirectAttributes.addFlashAttribute("mensaje", "Producto actualizado exitosamente");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/productos";
    }

    @GetMapping("/eliminar/{codigo}")
    public String eliminar(@PathVariable int codigo, RedirectAttributes redirectAttributes, HttpSession session) {
        if (!verificarSesion(session)) {
            return "redirect:/login";
        }
        try {
            if (!productosService.existePorCodigo(codigo)) {
                return "redirect:/productos";
            }
            productosService.eliminar(codigo);
            redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el producto");
        }
        return "redirect:/productos";
    }

    // REST API endpoints (kept for compatibility)

    @GetMapping("/api/{codigo}")
    @ResponseBody
    public ResponseEntity<Productos> buscarPorCodigo(@PathVariable int codigo){
        return productosService.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<?> guardarApi(@RequestBody Productos producto){
        try {
            Productos nuevoProducto = productosService.guardar(producto);
            return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/api/{codigo}")
    @ResponseBody
    public ResponseEntity<Void> eliminarApi(@PathVariable int codigo){
        try{
            if(!productosService.existePorCodigo(codigo)){
                return ResponseEntity.notFound().build();
            }
            productosService.eliminar(codigo);
            return ResponseEntity.noContent().build();
        }
        catch (RuntimeException e){
            return  ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/api/{codigo}")
    @ResponseBody
    public ResponseEntity<?> actualizarApi(@PathVariable int codigo, @RequestBody Productos producto){
        try{
            if(!productosService.existePorCodigo(codigo)){
                return ResponseEntity.notFound().build();
            }
            Productos productoActualizado = productosService.actualizar(codigo,producto);
            return ResponseEntity.ok(productoActualizado);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }
}