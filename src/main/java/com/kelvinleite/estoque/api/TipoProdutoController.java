package com.kelvinleite.estoque.api;

import com.kelvinleite.estoque.application.tipoProduto.TipoProdutoService;
import com.kelvinleite.estoque.application.tipoProduto.dto.TipoProdutoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tipos-produto")
@RequiredArgsConstructor
public class TipoProdutoController {

    private final TipoProdutoService tipoProdutoService;

    @GetMapping
    public List<TipoProdutoResponse> listar() {
        return tipoProdutoService.listar();
    }
}
