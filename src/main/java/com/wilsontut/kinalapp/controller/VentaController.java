package com.wilsontut.kinalapp.controller;

import com.wilsontut.kinalapp.entity.DetalleVenta;
import com.wilsontut.kinalapp.entity.Productos;
import com.wilsontut.kinalapp.entity.Usuario;
import com.wilsontut.kinalapp.entity.Venta;
import com.wilsontut.kinalapp.service.IClienteService;
import com.wilsontut.kinalapp.service.IProductosService;
import com.wilsontut.kinalapp.service.IUsuarioService;
import com.wilsontut.kinalapp.service.IVentaService;
import com.wilsontut.kinalapp.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    private final IVentaService ventaService;
    private final IClienteService clienteService;
    private final IUsuarioService usuarioService;
    private final IProductosService productosService;
    private final UsuarioRepository usuarioRepository;

    public VentaController(IVentaService ventaService, IClienteService clienteService,
                            IUsuarioService usuarioService, IProductosService productosService,
                            UsuarioRepository usuarioRepository) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.usuarioService = usuarioService;
        this.productosService = productosService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public String listar(Model model) {
        List<Venta> ventas = ventaService.listarTodos();
        model.addAttribute("ventas", ventas);
        return "ventas";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNueva(Model model) {
        Venta venta = new Venta();
        venta.setFechaVenta(LocalDateTime.now());
        venta.setDetalles(new ArrayList<>());

        model.addAttribute("venta", venta);
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("productos", productosService.listarTodos());
        model.addAttribute("esNuevo", true);
        return "form-venta";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Venta venta,
                         @RequestParam(value = "productosIds", required = false) List<Integer> productosIds,
                         @RequestParam(value = "cantidades", required = false) List<Integer> cantidades,
                         RedirectAttributes redirectAttributes) {

        try {
            //obtener el usuario autenticado desde Spring Security
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            Usuario usuarioLogueado = usuarioRepository.findByUsername(username).orElse(null);
            
            venta.setUsuario(usuarioLogueado);
            venta.setFechaVenta(LocalDateTime.now());
            venta.setEstado(1);

            if (productosIds != null && cantidades != null && !productosIds.isEmpty()) {
                BigDecimal total = BigDecimal.ZERO;
                for (int i = 0; i < productosIds.size(); i++) {
                    Productos producto = productosService.buscarPorCodigo(productosIds.get(i)).orElse(null);
                    if (producto != null && i < cantidades.size() && cantidades.get(i) > 0) {
                        DetalleVenta detalle = new DetalleVenta();
                        detalle.setProducto(producto);
                        detalle.setCantidad(cantidades.get(i));
                        detalle.setPrecioUnitario(producto.getPrecio());
                        BigDecimal subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(cantidades.get(i)));
                        detalle.setSubtotal(subtotal);
                        venta.addDetalle(detalle);
                        total = total.add(subtotal);
                    }
                }
                venta.setTotal(total);
            } else {
                venta.setTotal(BigDecimal.ZERO);
            }

            ventaService.guardar(venta);
            redirectAttributes.addFlashAttribute("mensaje", "Venta creada exitosamente con codigo: " + venta.getCodigoVenta());
            return "redirect:/ventas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear la venta: " + e.getMessage());
            return "redirect:/ventas/nueva";
        }
    }

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model model) {
        return ventaService.buscarPorCodigo(id)
                .map(venta -> {
                    model.addAttribute("venta", venta);
                    return "ver-venta";
                })
                .orElse("redirect:/ventas");
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        return ventaService.buscarPorCodigo(id)
                .map(venta -> {
                    model.addAttribute("venta", venta);
                    model.addAttribute("clientes", clienteService.listarTodos());
                    model.addAttribute("productos", productosService.listarTodos());
                    model.addAttribute("esNuevo", false);
                    return "form-venta";
                })
                .orElse("redirect:/ventas");
    }

    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute Venta venta,
                            @RequestParam(value = "productosIds", required = false) List<Integer> productosIds,
                            @RequestParam(value = "cantidades", required = false) List<Integer> cantidades,
                            RedirectAttributes redirectAttributes) {

        try {
            if (!ventaService.existePorCodigo(venta.getCodigoVenta())) {
                return "redirect:/ventas";
            }

            Venta ventaExistente = ventaService.buscarPorCodigo(venta.getCodigoVenta()).orElse(null);
            if (ventaExistente == null) {
                return "redirect:/ventas";
            }

            ventaExistente.setCliente(venta.getCliente());
            ventaExistente.setEstado(venta.getEstado());
            ventaExistente.getDetalles().clear();

            if (productosIds != null && cantidades != null && !productosIds.isEmpty()) {
                BigDecimal total = BigDecimal.ZERO;
                for (int i = 0; i < productosIds.size(); i++) {
                    Productos producto = productosService.buscarPorCodigo(productosIds.get(i)).orElse(null);
                    if (producto != null && i < cantidades.size() && cantidades.get(i) > 0) {
                        DetalleVenta detalle = new DetalleVenta();
                        detalle.setProducto(producto);
                        detalle.setCantidad(cantidades.get(i));
                        detalle.setPrecioUnitario(producto.getPrecio());
                        BigDecimal subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(cantidades.get(i)));
                        detalle.setSubtotal(subtotal);
                        ventaExistente.addDetalle(detalle);
                        total = total.add(subtotal);
                    }
                }
                ventaExistente.setTotal(total);
            }

            ventaService.actualizar(ventaExistente.getCodigoVenta(), ventaExistente);
            redirectAttributes.addFlashAttribute("mensaje", "Venta actualizada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar la venta: " + e.getMessage());
        }
        return "redirect:/ventas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            if (!ventaService.existePorCodigo(id)) {
                return "redirect:/ventas";
            }
            ventaService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Venta eliminada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la venta");
        }
        return "redirect:/ventas";
    }
}
