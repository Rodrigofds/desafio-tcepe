package com.pitang.desafio.tcepe.exception;

import com.pitang.desafio.tcepe.dto.UserResponseDTO;
import com.pitang.desafio.tcepe.exception.expections.ErrorMessage;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<UserResponseDTO> handleValidationExceptions(MethodArgumentNotValidException e) {

        List<String> errorMessages = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        ErrorMessage errorMessage = new ErrorMessage(String.join(", ", errorMessages), 104);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(UserResponseDTO.error(errorMessage));
    }
}

