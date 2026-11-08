package com.kelvinleite.estoque.api.handler;

import com.kelvinleite.estoque.domain.exception.EntidadeNaoEncontradaException;
import com.kelvinleite.estoque.domain.exception.RegraNegocioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ProblemDetail tratarEntidadeNaoEncontrada(EntidadeNaoEncontradaException exception) {
        return criar(HttpStatus.NOT_FOUND, "Registro não encontrado", exception.getMessage());
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ProblemDetail tratarRegraNegocioException(RegraNegocioException exception) {
        return criar(HttpStatus.UNPROCESSABLE_CONTENT, "Regra de negócio não respeitada", exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail tratarArgumentoInvalido(IllegalArgumentException ex) {
        return criar(HttpStatus.BAD_REQUEST, "Requisição inválida", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail tratarValidacao(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(erro -> erros.putIfAbsent(erro.getField(), erro.getDefaultMessage()));

        ProblemDetail problema = criar(HttpStatus.BAD_REQUEST, "Dados inválidos", "Um ou mais campos estão inválidos");
        problema.setProperty("campos", erros);
        return problema;
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ProblemDetail tratarConflitoConcorrencia(ObjectOptimisticLockingFailureException exception) {
        return criar(HttpStatus.CONFLICT, "Conflito de concorrência",
                "O registro foi alterado por outra operação. Tente novamente.");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail tratarViolacaoIntegridade(DataIntegrityViolationException exception) {
        log.warn("Violação de integridade de dados", exception);
        return criar(HttpStatus.CONFLICT, "Violação de integridade",
                "A operação viola uma restrição de integridade dos dados.");
    }

    private ProblemDetail criar(HttpStatus status, String titulo, String detalhe) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(status, detalhe);
        problema.setTitle(titulo);
        return problema;
    }
}

