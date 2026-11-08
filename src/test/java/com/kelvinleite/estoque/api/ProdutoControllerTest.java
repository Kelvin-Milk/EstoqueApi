package com.kelvinleite.estoque.api;

import com.kelvinleite.estoque.application.produto.ProdutoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProdutoController.class)
public class ProdutoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProdutoService produtoService;

    @Test
    void deveRetornar400QuandoCodigoNaoInformado() throws Exception {
        mockMvc.perform(post("/api/v1/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "descricao": "Notebook", "tipoId": 1, "valorFornecedor": 3500.00 }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.codigo").value("Código é obrigatório"));
    }
}
