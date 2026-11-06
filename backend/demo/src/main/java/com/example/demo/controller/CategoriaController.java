package com.example.demo.controller;

import com.example.demo.domain.Categoria;
import com.example.demo.dto.CategoriaRequest;
import com.example.demo.dto.CategoriaResponse;
import com.example.demo.service.CategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> cadastrar(
            @RequestBody CategoriaRequest request) {

        Categoria categoria = categoriaService.cadastrar(
                request.nome(),
                request.descricao()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(categoria));
    }

    @GetMapping
    public List<CategoriaResponse> listar() {
        return categoriaService.listar()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public CategoriaResponse buscarPorId(@PathVariable Long id) {
        return toResponse(categoriaService.buscarPorId(id));
    }

    private CategoriaResponse toResponse(Categoria categoria) {
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao()
        );
    }
}