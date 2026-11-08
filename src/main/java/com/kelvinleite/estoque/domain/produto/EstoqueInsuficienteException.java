package com.kelvinleite.estoque.domain.produto;

import com.kelvinleite.estoque.domain.exception.RegraNegocioException;

public class EstoqueInsuficienteException extends RegraNegocioException {
    public EstoqueInsuficienteException(String codigo, int disponivel, int solicitado) {
        super("Estoque insuficiente para o produto " + codigo + ". Disponível: " + disponivel + ", solicitado: " + solicitado);
    }
}
