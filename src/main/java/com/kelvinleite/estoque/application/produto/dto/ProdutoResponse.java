package com.kelvinleite.estoque.application.produto.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProdutoResponse(
        Long id,
        String codigo,
        String descricao,
        TipoProdutoResumo tipo,
        int quantidadeEstoque,
        BigDecimal valorFornecedor
) {
    public record TipoProdutoResumo(Long id, String descricao) {
    }
}
