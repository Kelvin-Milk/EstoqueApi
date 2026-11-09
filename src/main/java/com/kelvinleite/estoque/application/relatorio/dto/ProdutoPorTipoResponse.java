package com.kelvinleite.estoque.application.relatorio.dto;

public record ProdutoPorTipoResponse(
        Long id,
        String codigo,
        String descricao,
        String tipo,
        long quantidadeSaida,
        int quantidadeDisponivel
) {
}