package com.kelvinleite.estoque.application.movimentoestoque;

import com.kelvinleite.estoque.application.movimentoestoque.dto.MovimentoEstoqueResponse;
import com.kelvinleite.estoque.application.movimentoestoque.dto.RegistrarEntradaRequest;
import com.kelvinleite.estoque.application.movimentoestoque.dto.RegistrarSaidaRequest;
import com.kelvinleite.estoque.domain.produto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class MovimentoEstoqueService {

    private final ProdutoRepository produtoRepository;
    private final MovimentoEstoqueRepository movimentoEstoqueRepository;
    private final MovimentoEstoqueMapper movimentoEstoqueMapper;
    private final Clock clock;

    public MovimentoEstoqueResponse registrarEntrada(Long produtoId, RegistrarEntradaRequest request) {
        Produto produto = buscarProduto(produtoId);

        MovimentoEstoque movimento = produto.registrarEntrada(request.quantidade(), LocalDateTime.now(clock));

        return movimentoEstoqueMapper.toResponse(movimentoEstoqueRepository.save(movimento));
    }

    public MovimentoEstoqueResponse registrarSaida(Long produtoId, RegistrarSaidaRequest request) {
        Produto produto = buscarProduto(produtoId);

        MovimentoEstoque movimento = produto.registrarSaida(
                request.quantidade(), request.valorVenda(), LocalDateTime.now(clock));

        return movimentoEstoqueMapper.toResponse(movimentoEstoqueRepository.save(movimento));
    }

    private Produto buscarProduto(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
    }

}

