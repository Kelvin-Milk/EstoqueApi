package com.kelvinleite.estoque.domain.produto;

import com.kelvinleite.estoque.domain.tipoproduto.TipoProduto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProdutoTest {

    private final TipoProduto eletronico = new TipoProduto("Eletrônico");

    @Test
    @DisplayName("Deve criar produto com estoque zerado")
    void deveCriarProdutoComEstoqueZerado() {
        Produto produto = new Produto("NB-001", "Notebook", eletronico, new BigDecimal("3500.00"));

        assertThat(produto.getQuantidadeEstoque()).isZero();
        assertThat(produto.getCodigo()).isEqualTo("NB-001");
    }

    @Test
    @DisplayName("Não deve criar produto com valor do fornecedor negativo")
    void naoDeveCriarProdutoComValorNegativo() {
        assertThatThrownBy(() -> new Produto("NB-001", "Notebook", eletronico, new BigDecimal("-1")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Valor no fornecedor");
    }

    @Test
    @DisplayName("Não deve alterar nenhum campo quando a atualização for inválida")
    void naoDeveAlterarCamposQuandoAtualizacaoInvalida() {
        Produto produto = new Produto("NB-001", "Notebook", eletronico, new BigDecimal("3500.00"));

        assertThatThrownBy(() -> produto.atualizar("Nova descrição", null, new BigDecimal("100")))
                .isInstanceOf(IllegalArgumentException.class);

        assertThat(produto.getDescricao()).isEqualTo("Notebook");
    }
}
