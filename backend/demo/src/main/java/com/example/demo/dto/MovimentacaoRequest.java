package com.example.demo.dto;

import com.example.demo.domain.enums.TipoMovimentacao;

public record MovimentacaoRequest(
        Long produtoId,
        TipoMovimentacao tipo,
        Integer quantidade,
        String observacao
) {
}