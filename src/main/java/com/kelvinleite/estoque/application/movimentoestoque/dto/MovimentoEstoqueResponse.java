package com.kelvinleite.estoque.application.movimentoestoque.dto;

import com.kelvinleite.estoque.domain.produto.TipoMovimentacaoEstoque;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovimentoEstoqueResponse(
        Long id,
        Long produtoId,
        TipoMovimentacaoEstoque tipo,
        int quantidade,
        BigDecimal valorVenda,
        BigDecimal custoUnitario,
        LocalDateTime dataMovimentacao,
        int saldoAtual
) {
}

