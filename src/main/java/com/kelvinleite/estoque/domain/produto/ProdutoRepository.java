package com.kelvinleite.estoque.domain.produto;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Override
    @EntityGraph(attributePaths = "tipo")
    List<Produto> findAll();

    boolean existsByCodigo(String codigo);
}
