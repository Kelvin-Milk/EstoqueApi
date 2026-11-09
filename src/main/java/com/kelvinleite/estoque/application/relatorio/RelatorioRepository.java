package com.kelvinleite.estoque.application.relatorio;

import com.kelvinleite.estoque.application.relatorio.dto.ProdutoPorTipoResponse;
import com.kelvinleite.estoque.domain.produto.Produto;
import com.kelvinleite.estoque.domain.produto.TipoMovimentacaoEstoque;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RelatorioRepository extends Repository<Produto, Long> {
    @Query("""
            select new com.kelvinleite.estoque.application.relatorio.dto.ProdutoPorTipoResponse(
                p.id, p.codigo, p.descricao, coalesce(sum(m.quantidade), 0L), p.quantidadeEstoque)
            from Produto p
            left join MovimentoEstoque m on m.produto = p and m.tipo = :saida
            where p.tipo.id = :tipoId
            group by p.id, p.codigo, p.descricao, p.quantidadeEstoque
            order by p.descricao
            """)
    List<ProdutoPorTipoResponse> buscarProdutosPorTipo(@Param("tipoId") Long tipoId,
                                                       @Param("saida") TipoMovimentacaoEstoque saida);
}
