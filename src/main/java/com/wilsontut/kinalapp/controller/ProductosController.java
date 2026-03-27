package com.wilsontut.kinalapp.controller;

import com.wilsontut.kinalapp.entity.Productos;
import com.wilsontut.kinalapp.service.IProductosService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductosController {


    private final IProductosService productosService;

    public ProductosController(IProductosService productosService) {
        this.productosService = productosService;
    }

    @GetMapping

    public ResponseEntity<List<Productos>> listar(){
        List<Productos> productos = productosService.listarTodos();
        return ResponseEntity.ok(productos);

    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Productos> buscarPorCodigo(@PathVariable int codigo){
        return productosService.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Productos producto){
        try {
            Productos nuevoProducto = productosService.guardar(producto);
            return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable int codigo){
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
    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizar(@PathVariable int codigo, @RequestBody Productos producto){
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
    @GetMapping( "/activos" )
    public ResponseEntity<List<Productos>> listarActivos(){
        List<Productos> activos = productosService.listarPorEstado(1);

        return ResponseEntity.ok(activos);
    }

    @GetMapping("/inactivos")
    public ResponseEntity<List<Productos>>listarInactivos(){
        List<Productos> inactivos = productosService.listarPorEstado(0);

        return ResponseEntity.ok(inactivos);
    }
}