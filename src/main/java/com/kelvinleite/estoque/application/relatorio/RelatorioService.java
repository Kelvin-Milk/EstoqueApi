package com.kelvinleite.estoque.application.relatorio;

import com.kelvinleite.estoque.application.relatorio.dto.ProdutoPorTipoResponse;
import com.kelvinleite.estoque.domain.produto.TipoMovimentacaoEstoque;
import com.kelvinleite.estoque.domain.tipoproduto.TipoProdutoNaoEncontradoException;
import com.kelvinleite.estoque.domain.tipoproduto.TipoProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RelatorioService {

    private final RelatorioRepository relatorioRepository;
    private final TipoProdutoRepository tipoProdutoRepository;

    public List<ProdutoPorTipoResponse> produtosPorTipo(Long tipoId) {
        if (!tipoProdutoRepository.existsById(tipoId)) {
            throw new TipoProdutoNaoEncontradoException(tipoId);
        }
        return relatorioRepository.buscarProdutosPorTipo(tipoId, TipoMovimentacaoEstoque.SAIDA);
    }
}
