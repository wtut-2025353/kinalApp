package com.wilsontut.kinalapp.controller;

import com.wilsontut.kinalapp.entity.Cliente;
import com.wilsontut.kinalapp.repository.ClienteRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private final ClienteRepository repo;

    public ClienteController(ClienteRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Cliente> listar() {
        return repo.findAll();
    }

    @PostMapping
    public Cliente guarda(@RequestBody Cliente c) {
        return repo.save(c);
    }
}