package com.wilsontut.kinalapp.controller;

import com.wilsontut.kinalapp.entity.Ventas;
import com.wilsontut.kinalapp.service.IVentasService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/ventas")
public class VentasController {
    private final IVentasService ventasService;

    public VentasController(IVentasService ventasService) {
        this.ventasService = ventasService;
    }

    @GetMapping
    public ResponseEntity<List<Ventas>> listar(){
        List<Ventas> ventas = ventasService.listarTodos();
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Ventas> buscarPorCodigo(@PathVariable long codigo){
        return ventasService.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Ventas ventas){
        try {
            Ventas nuevaVenta = ventasService.guardar(ventas);
            return new ResponseEntity<>(nuevaVenta, HttpStatus.CREATED);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable long codigo){
        try{
            if(!ventasService.existePorCodigo(codigo)){
                return ResponseEntity.notFound().build();
            }
            ventasService.eliminar(codigo);
            return ResponseEntity.noContent().build();
        }
        catch (RuntimeException e){
            return  ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizar(@PathVariable long codigo, @RequestBody Ventas ventas){
        try{
            if(!ventasService.existePorCodigo(codigo)){
                return ResponseEntity.notFound().build();
            }
            Ventas ventaActualizada = ventasService.actualizar(codigo,ventas);
            return ResponseEntity.ok(ventaActualizada);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping( "/activos" )
    public ResponseEntity<List<Ventas>> listarActivos(){
        List<Ventas> activos = ventasService.listarPorEstado(1);

        return ResponseEntity.ok(activos);
    }

    @GetMapping("/inactivos")
    public ResponseEntity<List<Ventas>>listarInactivos(){
        List<Ventas> inactivos = ventasService.listarPorEstado(0);

        return ResponseEntity.ok(inactivos);
    }
}