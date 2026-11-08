package com.kelvinleite.estoque.domain.produto;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovimentoEstoque {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private TipoMovimentacaoEstoque tipo;

    @Column(precision = 15, scale = 2)
    private BigDecimal valorVenda;

    @Column(nullable = false)
    private LocalDateTime dataMovimentacao;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal custoUnitario;

    @Column(nullable = false)
    private int quantidade;

    private MovimentoEstoque(Produto produto, TipoMovimentacaoEstoque tipo, int quantidade, BigDecimal valorVenda, LocalDateTime dataMovimentacao) {
        this.produto = produto;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.valorVenda = valorVenda;
        this.custoUnitario = produto.getValorFornecedor();
        this.dataMovimentacao = dataMovimentacao;
    }


    static MovimentoEstoque entrada(Produto produto, int quantidade, LocalDateTime dataMovimentacao) {
        return new MovimentoEstoque(produto, TipoMovimentacaoEstoque.ENTRADA, quantidade, null, dataMovimentacao);
    }

    static MovimentoEstoque saida(Produto produto, int quantidade, BigDecimal valorVenda, LocalDateTime dataMovimentacao) {
        if (valorVenda == null || valorVenda.signum() < 0) {
            throw new IllegalArgumentException("Valor de venda deve ser maior ou igual a zero");
        }
        return new MovimentoEstoque(produto, TipoMovimentacaoEstoque.SAIDA, quantidade, valorVenda, dataMovimentacao);
    }
}

