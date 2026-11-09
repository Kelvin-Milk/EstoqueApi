package com.kelvinleite.estoque.application.relatorio;

import com.kelvinleite.estoque.application.relatorio.dto.LucroPorProdutoResponse;
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
                p.id, p.codigo, p.descricao, t.descricao,
                coalesce(sum(m.quantidade), 0L), p.quantidadeEstoque)
            from Produto p
            join p.tipo t
            left join MovimentoEstoque m on m.produto = p and m.tipo = :saida
            where (:tipoId is null or t.id = :tipoId)
            group by p.id, p.codigo, p.descricao, t.descricao, p.quantidadeEstoque
            order by t.descricao, p.descricao
            """)
    List<ProdutoPorTipoResponse> buscarProdutosPorTipo(@Param("tipoId") Long tipoId,
                                                       @Param("saida") TipoMovimentacaoEstoque saida);

    @Query("""
            select new com.kelvinleite.estoque.application.relatorio.dto.LucroPorProdutoResponse(
                p.id, p.codigo, p.descricao,
                coalesce(sum(m.quantidade), 0L),
                cast(sum((m.valorVenda - m.custoUnitario) * m.quantidade) as BigDecimal))
            from Produto p
            left join MovimentoEstoque m on m.produto = p and m.tipo = :saida
            group by p.id, p.codigo, p.descricao
            order by p.descricao
            """)
    List<LucroPorProdutoResponse> buscarLucroPorProduto(@Param("saida") TipoMovimentacaoEstoque saida);
}
