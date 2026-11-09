package com.kelvinleite.estoque.application.relatorio.dto;

import java.math.BigDecimal;

public record LucroPorProdutoResponse(
        Long id,
        String codigo,
        String descricao,
        long quantidadeSaida,
        BigDecimal totalLucro
) {
    public LucroPorProdutoResponse {
        if (totalLucro == null) {
            totalLucro = BigDecimal.ZERO;
        }
    }
}