package com.kelvinleite.estoque.domain.exception;

public abstract class RegraNegocioException extends RuntimeException {

    protected RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
