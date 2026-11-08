package com.kelvinleite.estoque.domain.exception;

public abstract class EntidadeNaoEncontradaException extends RuntimeException {

    protected EntidadeNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}

