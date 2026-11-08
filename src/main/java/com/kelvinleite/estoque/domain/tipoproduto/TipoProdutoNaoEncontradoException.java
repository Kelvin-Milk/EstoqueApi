package com.kelvinleite.estoque.domain.tipoproduto;

import com.kelvinleite.estoque.domain.exception.EntidadeNaoEncontradaException;

public class TipoProdutoNaoEncontradoException extends EntidadeNaoEncontradaException {

    public TipoProdutoNaoEncontradoException(Long id) {
        super("Tipo de Produto com id " + id.toString() + " não encontrado");
    }
}
