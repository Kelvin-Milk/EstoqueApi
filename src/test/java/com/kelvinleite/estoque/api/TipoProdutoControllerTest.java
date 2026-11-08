package com.kelvinleite.estoque.api;

import com.kelvinleite.estoque.application.tipoProduto.TipoProdutoService;
import com.kelvinleite.estoque.application.tipoProduto.dto.TipoProdutoResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TipoProdutoController.class)
public class TipoProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TipoProdutoService tipoProdutoService;

    @Test
    void deveListarTiposDeProduto() throws Exception {
        when(tipoProdutoService.listar()).thenReturn(List.of(
                new TipoProdutoResponse(1L, "Eletrônico"),
                new TipoProdutoResponse(2L, "Móvel")));

        mockMvc.perform(get("/api/v1/tipos-produto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].descricao").value("Eletrônico"));
    }
}
