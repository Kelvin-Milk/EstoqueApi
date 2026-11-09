package com.kelvinleite.estoque.api;

import com.kelvinleite.estoque.application.relatorio.RelatorioService;
import com.kelvinleite.estoque.application.relatorio.dto.LucroPorProdutoResponse;
import com.kelvinleite.estoque.application.relatorio.dto.ProdutoPorTipoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService relatorioService;

    @GetMapping("/produtos-por-tipo")
    public List<ProdutoPorTipoResponse> produtosPorTipo(@RequestParam(required = false) Long tipoId) {
        return relatorioService.produtosPorTipo(tipoId);
    }

    @GetMapping("/lucro-por-produto")
    public List<LucroPorProdutoResponse> lucroPorProduto() {
        return relatorioService.lucroPorProduto();
    }
}
