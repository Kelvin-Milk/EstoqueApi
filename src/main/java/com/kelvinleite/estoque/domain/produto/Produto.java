package com.kelvinleite.estoque.domain.produto;

import com.kelvinleite.estoque.domain.tipoproduto.TipoProduto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(nullable = false)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_produto_id", nullable = false)
    private TipoProduto tipo;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valorFornecedor;

    @Column(nullable = false)
    private int quantidadeEstoque;

    @Version
    @Column(nullable = false)
    private Long versao;

    public Produto(String codigo, String descricao, TipoProduto tipoProduto, BigDecimal valorFornecedor) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código é obrigatório");
        }
        this.codigo = codigo;
        this.quantidadeEstoque = 0;
        atualizar(descricao, tipoProduto, valorFornecedor);
    }

    public void atualizar(String descricao, TipoProduto tipoProduto, BigDecimal valorFornecedor) {
        if (valorFornecedor == null || valorFornecedor.signum() < 0) {
            throw new IllegalArgumentException("Valor no fornecedor deve ser maior ou igual a zero");
        }
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Descrição é obrigatório");
        }
        if (tipoProduto == null) {
            throw new IllegalArgumentException("Tipo é obrigatório");
        }

        this.tipo = tipoProduto;
        this.descricao = descricao;
        this.valorFornecedor = valorFornecedor;
    }
}

