package com.kelvinleite.estoque.application.produto;

import com.kelvinleite.estoque.application.produto.dto.CriarProdutoRequest;
import com.kelvinleite.estoque.domain.produto.CodigoProdutoDuplicadoException;
import com.kelvinleite.estoque.domain.produto.ProdutoNaoEncontradoException;
import com.kelvinleite.estoque.domain.produto.ProdutoRepository;
import com.kelvinleite.estoque.domain.tipoproduto.TipoProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private TipoProdutoRepository tipoProdutoRepository;

    @Mock
    private ProdutoMapper produtoMapper;

    @InjectMocks
    private ProdutoService produtoService;

    @Test
    void naoDeveCriarProdutoComCodigoDuplicado() {
        var request = new CriarProdutoRequest("NB-001", "Notebook", 1L, new BigDecimal("3500.00"));
        when(produtoRepository.existsByCodigo("NB-001")).thenReturn(true);

        assertThatThrownBy(() -> produtoService.criar(request))
                .isInstanceOf(CodigoProdutoDuplicadoException.class);

        verify(produtoRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoAoExcluirProdutoInexistente() {
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produtoService.excluir(99L))
                .isInstanceOf(ProdutoNaoEncontradoException.class);

        verify(produtoRepository, never()).delete(any());
    }
}

