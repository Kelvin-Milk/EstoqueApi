package com.kelvinleite.estoque.domain.produto;

import com.kelvinleite.estoque.domain.tipoproduto.TipoProduto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ProdutoMovimentacaoEstoqueTest {

    private static final LocalDateTime AGORA = LocalDateTime.of(2026, 11, 8, 10, 0);

    private final TipoProduto eletronico = new TipoProduto("Eletrônico");

    private Produto produtoComEstoque(int quantidade) {
        Produto produto = new Produto("NB-001", "Notebook", eletronico, new BigDecimal("3000.00"));
        produto.registrarEntrada(quantidade, AGORA);
        return produto;
    }

    @Test
    @DisplayName("Entrada deve somar ao saldo e registrar o movimento")
    void entradaDeveSomarAoSaldo() {
        Produto produto = new Produto("NB-001", "Notebook", eletronico, new BigDecimal("3000.00"));

        MovimentoEstoque movimento = produto.registrarEntrada(10, AGORA);

        assertThat(produto.getQuantidadeEstoque()).isEqualTo(10);
        assertThat(movimento.getTipo()).isEqualTo(TipoMovimentacaoEstoque.ENTRADA);
        assertThat(movimento.getQuantidade()).isEqualTo(10);
        assertThat(movimento.getValorVenda()).isNull();
    }

    @Test
    @DisplayName("Saída deve subtrair do saldo e registrar valor de venda e custo")
    void saidaDeveSubtrairDoSaldo() {
        Produto produto = produtoComEstoque(10);

        MovimentoEstoque movimento = produto.registrarSaida(4, new BigDecimal("4500.00"), AGORA);

        assertThat(produto.getQuantidadeEstoque()).isEqualTo(6);
        assertThat(movimento.getTipo()).isEqualTo(TipoMovimentacaoEstoque.SAIDA);
        assertThat(movimento.getValorVenda()).isEqualByComparingTo("4500.00");
        assertThat(movimento.getCustoUnitario()).isEqualByComparingTo("3000.00");
    }

    @Test
    @DisplayName("Saída de todo o saldo deve zerar o estoque")
    void saidaDeTodoSaldoDeveZerarEstoque() {
        Produto produto = produtoComEstoque(5);

        produto.registrarSaida(5, new BigDecimal("4500.00"), AGORA);

        assertThat(produto.getQuantidadeEstoque()).isZero();
    }

    @Test
    @DisplayName("Saída maior que o saldo deve lançar exceção com mensagem específica e não alterar o saldo")
    void saidaMaiorQueSaldoDeveLancarExcecao() {
        Produto produto = produtoComEstoque(5);

        assertThatThrownBy(() -> produto.registrarSaida(6, new BigDecimal("4500.00"), AGORA))
                .isInstanceOf(EstoqueInsuficienteException.class)
                .hasMessageContaining("NB-001")
                .hasMessageContaining("Disponível: 5")
                .hasMessageContaining("solicitado: 6");

        assertThat(produto.getQuantidadeEstoque()).isEqualTo(5);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("Movimentação com quantidade zero ou negativa deve ser rejeitada")
    void quantidadeInvalidaDeveSerRejeitada(int quantidade) {
        Produto produto = produtoComEstoque(5);

        assertThatThrownBy(() -> produto.registrarEntrada(quantidade, AGORA))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> produto.registrarSaida(quantidade, new BigDecimal("4500.00"), AGORA))
                .isInstanceOf(IllegalArgumentException.class);

        assertThat(produto.getQuantidadeEstoque()).isEqualTo(5);
    }

    @Test
    @DisplayName("Saída com valor de venda inválido não deve alterar o saldo")
    void saidaComValorDeVendaInvalidoNaoDeveAlterarSaldo() {
        Produto produto = produtoComEstoque(5);

        assertThatThrownBy(() -> produto.registrarSaida(2, null, AGORA))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> produto.registrarSaida(2, new BigDecimal("-1"), AGORA))
                .isInstanceOf(IllegalArgumentException.class);

        assertThat(produto.getQuantidadeEstoque()).isEqualTo(5);
    }

    @Test
    @DisplayName("Custo unitário do movimento não deve mudar quando o valor do fornecedor for alterado")
    void custoUnitarioDeveSerCongeladoNoMovimento() {
        Produto produto = produtoComEstoque(5);
        MovimentoEstoque saida = produto.registrarSaida(1, new BigDecimal("4500.00"), AGORA);

        produto.atualizar("Notebook", eletronico, new BigDecimal("3500.00"));

        assertThat(saida.getCustoUnitario()).isEqualByComparingTo("3000.00");
    }
}
