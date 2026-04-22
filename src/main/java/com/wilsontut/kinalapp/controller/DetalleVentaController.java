package com.wilsontut.kinalapp.controller;

import com.wilsontut.kinalapp.entity.DetalleVenta;
import com.wilsontut.kinalapp.entity.Productos;
import com.wilsontut.kinalapp.entity.Venta;
import com.wilsontut.kinalapp.service.IDetalleVentaService;
import com.wilsontut.kinalapp.service.IProductosService;
import com.wilsontut.kinalapp.service.IVentaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/detalleventa")
public class DetalleVentaController {

    private final IDetalleVentaService detalleVentaService;
    private final IVentaService ventaService;
    private final IProductosService productosService;

    public DetalleVentaController(IDetalleVentaService detalleVentaService, 
                                   IVentaService ventaService,
                                   IProductosService productosService) {
        this.detalleVentaService = detalleVentaService;
        this.ventaService = ventaService;
        this.productosService = productosService;
    }

    private boolean verificarSesion(HttpSession session) {
        return session.getAttribute("usuarioLogueado") != null;
    }

    @GetMapping

    public ResponseEntity<List<DetalleVenta>> listar(){
        List<DetalleVenta> detalles = detalleVentaService.listarTodos();
        return ResponseEntity.ok(detalles);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<DetalleVenta> buscarPorCodigo(@PathVariable long codigo){
        return detalleVentaService.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody DetalleVenta detalleVenta){
        try {
            DetalleVenta nuevoDetalle = detalleVentaService.guardar(detalleVenta);
            return new ResponseEntity<>(nuevoDetalle, HttpStatus.CREATED);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable long codigo){
        try{
            if(!detalleVentaService.existePorCodigo(codigo)){
                return ResponseEntity.notFound().build();
            }
            detalleVentaService.eliminar(codigo);
            return ResponseEntity.noContent().build();
        }
        catch (RuntimeException e){
            return  ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizar(@PathVariable long codigo, @RequestBody DetalleVenta detalleVenta){
        try{
            if(!detalleVentaService.existePorCodigo(codigo)){
                return ResponseEntity.notFound().build();
            }
            DetalleVenta detalleActualizado = detalleVentaService.actualizar(codigo,detalleVenta);
            return ResponseEntity.ok(detalleActualizado);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/venta/{codigoVenta}")
    public ResponseEntity<List<DetalleVenta>> listarPorVenta(@PathVariable long codigoVenta){
        List<DetalleVenta> detalles = detalleVentaService.listarPorVenta(codigoVenta);

        return ResponseEntity.ok(detalles);
    }

    @GetMapping("/por-venta/{codigoVenta}")
    public String listarPorVentaHtml(@PathVariable Long codigoVenta, Model model, HttpSession session) {
        if (!verificarSesion(session)) {
            return "redirect:/login";
        }
        Venta venta = ventaService.buscarPorCodigo(codigoVenta).orElse(null);
        if (venta == null) {
            return "redirect:/ventas";
        }
        List<DetalleVenta> detalles = detalleVentaService.listarPorVenta(codigoVenta);
        model.addAttribute("venta", venta);
        model.addAttribute("detalles", detalles);
        return "lista-detalles";
    }

    @GetMapping("/nuevo/{codigoVenta}")
    public String mostrarFormularioNuevo(@PathVariable Long codigoVenta, Model model, HttpSession session) {
        if (!verificarSesion(session)) {
            return "redirect:/login";
        }
        Venta venta = ventaService.buscarPorCodigo(codigoVenta).orElse(null);
        if (venta == null) {
            return "redirect:/ventas";
        }
        DetalleVenta detalle = new DetalleVenta();
        detalle.setVenta(venta);
        model.addAttribute("detalle", detalle);
        model.addAttribute("productos", productosService.listarTodos());
        model.addAttribute("venta", venta);
        return "form-detalle";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute DetalleVenta detalle, 
                         @RequestParam Long codigoVenta,
                         @RequestParam int codigoProducto,
                         @RequestParam int cantidad,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        if (!verificarSesion(session)) {
            return "redirect:/login";
        }
        try {
            Venta venta = ventaService.buscarPorCodigo(codigoVenta).orElse(null);
            Productos producto = productosService.buscarPorCodigo(codigoProducto).orElse(null);
            if (venta == null || producto == null) {
                redirectAttributes.addFlashAttribute("error", "Venta o producto no encontrado");
                return "redirect:/ventas";
            }
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(producto.getPrecio().multiply(java.math.BigDecimal.valueOf(cantidad)));
            detalleVentaService.guardar(detalle);
            redirectAttributes.addFlashAttribute("mensaje", "Producto agregado a la venta");
            return "redirect:/detalleventa/por-venta/" + codigoVenta;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al agregar producto: " + e.getMessage());
            return "redirect:/detalleventa/nuevo/" + codigoVenta;
        }
    }

    @GetMapping("/eliminar/{codigo}")
    public String eliminar(@PathVariable long codigo, RedirectAttributes redirectAttributes, HttpSession session) {
        if (!verificarSesion(session)) {
            return "redirect:/login";
        }
        try {
            DetalleVenta detalle = detalleVentaService.buscarPorCodigo(codigo).orElse(null);
            Long codigoVenta = detalle != null && detalle.getVenta() != null ? detalle.getVenta().getCodigoVenta() : null;
            detalleVentaService.eliminar(codigo);
            redirectAttributes.addFlashAttribute("mensaje", "Detalle eliminado");
            if (codigoVenta != null) {
                return "redirect:/detalleventa/por-venta/" + codigoVenta;
            }
            return "redirect:/ventas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar detalle");
            return "redirect:/ventas";
        }
    }
}