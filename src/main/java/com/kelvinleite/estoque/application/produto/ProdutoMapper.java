package com.kelvinleite.estoque.application.produto;

import com.kelvinleite.estoque.application.produto.dto.ProdutoResponse;
import com.kelvinleite.estoque.domain.produto.Produto;
import com.kelvinleite.estoque.domain.tipoproduto.TipoProduto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    ProdutoResponse toResponse(Produto produto);

    List<ProdutoResponse> toResponse(List<Produto> produtos);

    ProdutoResponse.TipoProdutoResumo toResumo(TipoProduto tipo);
}
