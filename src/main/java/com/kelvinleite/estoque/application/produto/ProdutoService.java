package com.kelvinleite.estoque.application.produto;

import com.kelvinleite.estoque.application.produto.dto.AtualizarProdutoRequest;
import com.kelvinleite.estoque.application.produto.dto.CriarProdutoRequest;
import com.kelvinleite.estoque.application.produto.dto.ProdutoResponse;
import com.kelvinleite.estoque.domain.produto.CodigoProdutoDuplicadoException;
import com.kelvinleite.estoque.domain.produto.Produto;
import com.kelvinleite.estoque.domain.produto.ProdutoNaoEncontradoException;
import com.kelvinleite.estoque.domain.produto.ProdutoRepository;
import com.kelvinleite.estoque.domain.tipoproduto.TipoProduto;
import com.kelvinleite.estoque.domain.tipoproduto.TipoProdutoNaoEncontradoException;
import com.kelvinleite.estoque.domain.tipoproduto.TipoProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final TipoProdutoRepository tipoProdutoRepository;
    private final ProdutoMapper produtoMapper;

    @Transactional
    public ProdutoResponse criar(CriarProdutoRequest produtoRequest) {
        if (produtoRepository.existsByCodigo(produtoRequest.codigo())) {
            throw new CodigoProdutoDuplicadoException(produtoRequest.codigo());
        }
        TipoProduto tipo = buscarTipo(produtoRequest.tipoId());

        Produto produto = new Produto(produtoRequest.codigo(),
                produtoRequest.descricao(),
                tipo,
                produtoRequest.valorFornecedor()
        );

        return produtoMapper.toResponse(produtoRepository.save(produto));
    }

    @Transactional
    public ProdutoResponse atualizar(Long id, AtualizarProdutoRequest produtoRequest) {
        Produto produto = buscarProduto(id);
        TipoProduto tipo = buscarTipo(produtoRequest.tipoId());

        produto.atualizar(produtoRequest.descricao(), tipo, produtoRequest.valorFornecedor());

        return produtoMapper.toResponse(produto);
    }

    @Transactional
    public void excluir(Long id) {
        produtoRepository.delete(buscarProduto(id));
    }

    public ProdutoResponse buscarPorId(Long id) {
        return produtoMapper.toResponse(buscarProduto(id));
    }

    public List<ProdutoResponse> listar() {
        return produtoMapper.toResponse(produtoRepository.findAll());
    }

    private Produto buscarProduto(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
    }

    private TipoProduto buscarTipo(Long id) {
        return tipoProdutoRepository.findById(id)
                .orElseThrow(() -> new TipoProdutoNaoEncontradoException(id));
    }
}
