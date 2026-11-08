package com.kelvinleite.estoque.application.tipoProduto;

import com.kelvinleite.estoque.application.tipoProduto.dto.TipoProdutoResponse;
import com.kelvinleite.estoque.domain.tipoproduto.TipoProduto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TipoProdutoMapper {
    TipoProdutoResponse toResponse(TipoProduto tipoProduto);

    List<TipoProdutoResponse> toResponse(List<TipoProduto> tiposProduto);

}
