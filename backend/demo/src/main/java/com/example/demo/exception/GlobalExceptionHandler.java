package com.example.demo.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> tratarDadosInvalidos(
            IllegalArgumentException exception) {

        return criarResposta(
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiError> tratarNaoEncontrado(
            NoSuchElementException exception) {

        return criarResposta(
                HttpStatus.NOT_FOUND,
                exception.getMessage()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> tratarConflito(
            DataIntegrityViolationException exception) {

        return criarResposta(
                HttpStatus.CONFLICT,
                "Já existe um registro com esses dados ou há uma restrição no banco."
        );
    }

    private ResponseEntity<ApiError> criarResposta(
            HttpStatus status,
            String mensagem) {

        ApiError erro = new ApiError(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                mensagem
        );

        return ResponseEntity.status(status).body(erro);
    }
}