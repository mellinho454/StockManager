package com.example.demo.dto;

import java.math.BigDecimal;

public record ProdutoRequest(
        String nome,
        String codigo,
        BigDecimal preco,
        Integer quantidadeEstoque,
        Integer estoqueMinimo,
        Long categoriaId
) {
}