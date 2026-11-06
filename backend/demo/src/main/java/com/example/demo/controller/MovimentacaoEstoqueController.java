package com.example.demo.controller;

import com.example.demo.domain.MovimentacaoEstoque;
import com.example.demo.dto.MovimentacaoRequest;
import com.example.demo.dto.MovimentacaoResponse;
import com.example.demo.service.MovimentacaoEstoqueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimentacoes")
public class MovimentacaoEstoqueController {

    private final MovimentacaoEstoqueService movimentacaoService;

    public MovimentacaoEstoqueController(
            MovimentacaoEstoqueService movimentacaoService) {
        this.movimentacaoService = movimentacaoService;
    }

    @PostMapping
    public ResponseEntity<MovimentacaoResponse> registrar(
            @RequestBody MovimentacaoRequest request) {

        MovimentacaoEstoque movimentacao = movimentacaoService.registrar(
                request.produtoId(),
                request.tipo(),
                request.quantidade(),
                request.observacao()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(movimentacao));
    }

    @GetMapping
    public List<MovimentacaoResponse> listar() {
        return movimentacaoService.listar()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/produto/{produtoId}")
    public List<MovimentacaoResponse> listarPorProduto(
            @PathVariable Long produtoId) {

        return movimentacaoService.listarPorProduto(produtoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private MovimentacaoResponse toResponse(
            MovimentacaoEstoque movimentacao) {

        return new MovimentacaoResponse(
                movimentacao.getId(),
                movimentacao.getTipo(),
                movimentacao.getQuantidade(),
                movimentacao.getDataMovimentacao(),
                movimentacao.getObservacao(),
                movimentacao.getProduto().getId(),
                movimentacao.getProduto().getNome()
        );
    }
}