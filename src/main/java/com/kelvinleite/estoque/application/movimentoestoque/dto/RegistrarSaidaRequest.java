package com.kelvinleite.estoque.application.movimentoestoque.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record RegistrarSaidaRequest(

        @NotNull(message = "Quantidade é obrigatória")
        @Positive(message = "Quantidade deve ser maior que zero")
        Integer quantidade,

        @NotNull(message = "Valor de venda é obrigatório")
        @PositiveOrZero(message = "Valor de venda deve ser maior ou igual a zero")
        @Digits(integer = 13, fraction = 2, message = "Valor de venda deve ter até 13 dígitos inteiros e 2 decimais")
        BigDecimal valorVenda
) {
}
