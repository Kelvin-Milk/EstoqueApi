package com.kelvinleite.estoque.application.movimentoestoque;

import com.kelvinleite.estoque.application.movimentoestoque.dto.MovimentoEstoqueResponse;
import com.kelvinleite.estoque.domain.produto.MovimentoEstoque;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovimentoEstoqueMapper {

    @Mapping(source = "produto.id", target = "produtoId")
    @Mapping(source = "produto.quantidadeEstoque", target = "saldoAtual")
    MovimentoEstoqueResponse toResponse(MovimentoEstoque movimento);
}
