package com.example.demo.dto;

import java.math.BigDecimal;

public record ProdutoResponse(
        Long id,
        String nome,
        String codigo,
        BigDecimal preco,
        Integer quantidadeEstoque,
        Integer estoqueMinimo,
        Long categoriaId,
        String categoriaNome
) {
}