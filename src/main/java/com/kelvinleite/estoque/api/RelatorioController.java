package com.kelvinleite.estoque.api;

import com.kelvinleite.estoque.application.relatorio.RelatorioService;
import com.kelvinleite.estoque.application.relatorio.dto.LucroPorProdutoResponse;
import com.kelvinleite.estoque.application.relatorio.dto.ProdutoPorTipoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService relatorioService;

    @GetMapping("/produtos-por-tipo/{tipoId}")
    public List<ProdutoPorTipoResponse> produtosPorTipo(@PathVariable Long tipoId) {
        return relatorioService.produtosPorTipo(tipoId);
    }

    @GetMapping("/lucro-por-produto")
    public List<LucroPorProdutoResponse> lucroPorProduto() {
        return relatorioService.lucroPorProduto();
    }
}
