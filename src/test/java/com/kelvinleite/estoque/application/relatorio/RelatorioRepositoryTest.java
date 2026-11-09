package com.kelvinleite.estoque.application.relatorio;

import com.kelvinleite.estoque.application.relatorio.dto.LucroPorProdutoResponse;
import com.kelvinleite.estoque.application.relatorio.dto.ProdutoPorTipoResponse;
import com.kelvinleite.estoque.domain.produto.Produto;
import com.kelvinleite.estoque.domain.produto.TipoMovimentacaoEstoque;
import com.kelvinleite.estoque.domain.tipoproduto.TipoProduto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RelatorioRepositoryTest {

    private static final LocalDateTime DATA = LocalDateTime.of(2026, 11, 8, 10, 0);

    @Autowired
    private RelatorioRepository relatorioRepository;

    @Autowired
    private TestEntityManager entityManager;

    private TipoProduto eletronico;
    private TipoProduto movel;

    @BeforeEach
    void setUp() {
        eletronico = entityManager.persist(new TipoProduto("Eletrônico Teste"));
        movel = entityManager.persist(new TipoProduto("Móvel Teste"));
    }

    @Test
    @DisplayName("Deve calcular o lucro usando o custo registrado no momento de cada saída")
    void deveCalcularLucroComCustoDoMomentoDaSaida() {
        Produto notebook = criarProduto("NB-001", "Notebook", eletronico, "10.00");
        entrada(notebook, 10);
        saida(notebook, 3, "15.00");                                      // (15 - 10) x 3 = 15
        notebook.atualizar("Notebook", eletronico, new BigDecimal("12.00"));
        saida(notebook, 2, "15.00");                                      // (15 - 12) x 2 = 6

        List<LucroPorProdutoResponse> resultado =
                relatorioRepository.buscarLucroPorProduto(TipoMovimentacaoEstoque.SAIDA);

        assertThat(resultado).singleElement().satisfies(lucro -> {
            assertThat(lucro.quantidadeSaida()).isEqualTo(5);
            assertThat(lucro.totalLucro()).isEqualByComparingTo("21.00");
        });
    }

    @Test
    @DisplayName("Deve listar produto sem saída com quantidade e lucro zerados")
    void deveListarProdutoSemSaidaComValoresZerados() {
        Produto mouse = criarProduto("MS-001", "Mouse", eletronico, "50.00");
        entrada(mouse, 5);

        List<LucroPorProdutoResponse> resultado =
                relatorioRepository.buscarLucroPorProduto(TipoMovimentacaoEstoque.SAIDA);

        assertThat(resultado).singleElement().satisfies(lucro -> {
            assertThat(lucro.quantidadeSaida()).isZero();
            assertThat(lucro.totalLucro()).isEqualByComparingTo(BigDecimal.ZERO);
        });
    }

    @Test
    @DisplayName("Deve retornar lucro negativo quando a venda for abaixo do custo")
    void deveRetornarLucroNegativoQuandoVendaAbaixoDoCusto() {
        Produto teclado = criarProduto("TC-001", "Teclado", eletronico, "10.00");
        entrada(teclado, 5);
        saida(teclado, 2, "8.00");                                        // (8 - 10) x 2 = -4

        List<LucroPorProdutoResponse> resultado =
                relatorioRepository.buscarLucroPorProduto(TipoMovimentacaoEstoque.SAIDA);

        assertThat(resultado).singleElement()
                .satisfies(lucro -> assertThat(lucro.totalLucro()).isEqualByComparingTo("-4.00"));
    }

    @Test
    @DisplayName("Deve listar apenas produtos do tipo com quantidade de saída e disponível")
    void deveListarProdutosDoTipoComSaidaEDisponivel() {
        Produto notebook = criarProduto("NB-001", "Notebook", eletronico, "10.00");
        entrada(notebook, 10);
        saida(notebook, 3, "15.00");

        Produto mouse = criarProduto("MS-001", "Mouse", eletronico, "5.00");
        entrada(mouse, 5);

        Produto mesa = criarProduto("MS-100", "Mesa", movel, "200.00");
        entrada(mesa, 4);
        saida(mesa, 1, "300.00");

        List<ProdutoPorTipoResponse> resultado =
                relatorioRepository.buscarProdutosPorTipo(eletronico.getId(), TipoMovimentacaoEstoque.SAIDA);

        assertThat(resultado)
                .extracting(ProdutoPorTipoResponse::codigo,
                        ProdutoPorTipoResponse::quantidadeSaida,
                        ProdutoPorTipoResponse::quantidadeDisponivel)
                .containsExactly(
                        tuple("MS-001", 0L, 5),
                        tuple("NB-001", 3L, 7));
    }

    @Test
    @DisplayName("Deve listar produtos de todos os tipos quando o tipo não for informado")
    void deveListarTodosQuandoTipoNaoInformado() {
        Produto notebook = criarProduto("NB-001", "Notebook", eletronico, "10.00");
        entrada(notebook, 10);
        saida(notebook, 3, "15.00");

        Produto mesa = criarProduto("ME-001", "Mesa", movel, "200.00");
        entrada(mesa, 4);

        List<ProdutoPorTipoResponse> resultado =
                relatorioRepository.buscarProdutosPorTipo(null, TipoMovimentacaoEstoque.SAIDA);

        assertThat(resultado)
                .extracting(ProdutoPorTipoResponse::codigo, ProdutoPorTipoResponse::tipo)
                .containsExactlyInAnyOrder(
                        tuple("NB-001", "Eletrônico Teste"),
                        tuple("ME-001", "Móvel Teste"));
    }

    private Produto criarProduto(String codigo, String descricao, TipoProduto tipo, String valorFornecedor) {
        return entityManager.persist(new Produto(codigo, descricao, tipo, new BigDecimal(valorFornecedor)));
    }

    private void entrada(Produto produto, int quantidade) {
        entityManager.persist(produto.registrarEntrada(quantidade, DATA));
    }

    private void saida(Produto produto, int quantidade, String valorVenda) {
        entityManager.persist(produto.registrarSaida(quantidade, new BigDecimal(valorVenda), DATA));
    }
}