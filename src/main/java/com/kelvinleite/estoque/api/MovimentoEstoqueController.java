package com.kelvinleite.estoque.api;

import com.kelvinleite.estoque.application.movimentoestoque.MovimentoEstoqueService;
import com.kelvinleite.estoque.application.movimentoestoque.dto.MovimentoEstoqueResponse;
import com.kelvinleite.estoque.application.movimentoestoque.dto.RegistrarEntradaRequest;
import com.kelvinleite.estoque.application.movimentoestoque.dto.RegistrarSaidaRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/produtos/{produtoId}")
@RequiredArgsConstructor
public class MovimentoEstoqueController {

    private final MovimentoEstoqueService movimentoEstoqueService;

    @PostMapping("/entradas")
    @ResponseStatus(HttpStatus.CREATED)
    public MovimentoEstoqueResponse registrarEntrada(@PathVariable Long produtoId,
                                                     @Valid @RequestBody RegistrarEntradaRequest request) {
        return movimentoEstoqueService.registrarEntrada(produtoId, request);
    }

    @PostMapping("/saidas")
    @ResponseStatus(HttpStatus.CREATED)
    public MovimentoEstoqueResponse registrarSaida(@PathVariable Long produtoId,
                                                   @Valid @RequestBody RegistrarSaidaRequest request) {
        return movimentoEstoqueService.registrarSaida(produtoId, request);
    }
}

