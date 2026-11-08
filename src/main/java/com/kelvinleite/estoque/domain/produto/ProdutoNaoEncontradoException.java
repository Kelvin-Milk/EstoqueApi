package com.kelvinleite.estoque.domain.produto;

import com.kelvinleite.estoque.domain.exception.EntidadeNaoEncontradaException;

public class ProdutoNaoEncontradoException extends EntidadeNaoEncontradaException {

    public ProdutoNaoEncontradoException(Long id) {
        super("Produto com id " + id.toString() + " não encontrado");
    }
}
