package com.example.demo.controller;

import com.example.demo.domain.Produto;
import com.example.demo.dto.ProdutoRequest;
import com.example.demo.dto.ProdutoResponse;
import com.example.demo.service.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> cadastrar(
            @RequestBody ProdutoRequest request) {

        Produto produto = produtoService.cadastrar(
                request.nome(),
                request.codigo(),
                request.preco(),
                request.quantidadeEstoque(),
                request.estoqueMinimo(),
                request.categoriaId()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(produto));
    }

    @PutMapping("/{id}")
    public ProdutoResponse atualizar(
            @PathVariable Long id,
            @RequestBody ProdutoRequest request) {

        Produto produto = produtoService.atualizar(
                id,
                request.nome(),
                request.codigo(),
                request.preco(),
                request.quantidadeEstoque(),
                request.estoqueMinimo(),
                request.categoriaId()
        );

        return toResponse(produto);
    }

    @GetMapping
    public List<ProdutoResponse> listar() {
        return produtoService.listar()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ProdutoResponse buscarPorId(@PathVariable Long id) {
        return toResponse(produtoService.buscarPorId(id));
    }

    @GetMapping("/categoria/{categoriaId}")
    public List<ProdutoResponse> listarPorCategoria(
            @PathVariable Long categoriaId) {

        return produtoService.listarPorCategoria(categoriaId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ProdutoResponse toResponse(Produto produto) {
        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getCodigo(),
                produto.getPreco(),
                produto.getQuantidadeEstoque(),
                produto.getEstoqueMinimo(),
                produto.getCategoria().getId(),
                produto.getCategoria().getNome()
        );
    }
}