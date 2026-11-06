package com.example.demo.dto;

import com.example.demo.domain.enums.TipoMovimentacao;
import java.time.LocalDateTime;

public record MovimentacaoResponse(
        Long id,
        TipoMovimentacao tipo,
        Integer quantidade,
        LocalDateTime dataMovimentacao,
        String observacao,
        Long produtoId,
        String produtoNome
) {
}