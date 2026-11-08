package com.kelvinleite.estoque.application.produto.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record AtualizarProdutoRequest(
        @NotBlank(message = "Descrição é obrigatória")
        @Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres")
        String descricao,

        @NotNull(message = "Tipo do produto é obrigatório")
        Long tipoId,

        @NotNull(message = "Valor no fornecedor é obrigatório")
        @PositiveOrZero(message = "Valor no fornecedor deve ser maior ou igual a zero")
        @Digits(integer = 13, fraction = 2, message = "Valor no fornecedor deve ter até 13 dígitos inteiros e 2 decimais")
        BigDecimal valorFornecedor
) {
}
