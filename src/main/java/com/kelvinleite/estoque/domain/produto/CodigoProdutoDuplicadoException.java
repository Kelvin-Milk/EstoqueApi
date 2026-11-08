package com.kelvinleite.estoque.domain.produto;

import com.kelvinleite.estoque.domain.exception.RegraNegocioException;

public class CodigoProdutoDuplicadoException extends RegraNegocioException {
    public CodigoProdutoDuplicadoException(String codigo) {
        super("Já existe um produto com o código " + codigo);
    }
}
