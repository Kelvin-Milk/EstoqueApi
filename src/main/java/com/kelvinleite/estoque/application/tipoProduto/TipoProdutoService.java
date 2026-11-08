package com.kelvinleite.estoque.application.tipoProduto;


import com.kelvinleite.estoque.application.tipoProduto.dto.TipoProdutoResponse;
import com.kelvinleite.estoque.domain.tipoproduto.TipoProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TipoProdutoService {
    private final TipoProdutoRepository tipoProdutoRepository;
    private final TipoProdutoMapper tipoProdutoMapper;

    public List<TipoProdutoResponse> listar() {
        return tipoProdutoMapper.toResponse(tipoProdutoRepository.findAll(Sort.by("descricao")));
    }
}
