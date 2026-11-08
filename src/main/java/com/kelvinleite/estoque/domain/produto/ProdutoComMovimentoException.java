package com.kelvinleite.estoque.domain.produto;

import com.kelvinleite.estoque.domain.exception.RegraNegocioException;

public class ProdutoComMovimentoException extends RegraNegocioException {

    public ProdutoComMovimentoException(String codigo) {
        super("O produto " + codigo + " não pode ser excluído, pois possui movimentações de estoque");
    }
}
