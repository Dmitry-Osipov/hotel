package web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import utils.exceptions.TechnicalException;

@RestControllerAdvice
public class RestGlobalExceptionHandler {
    @ExceptionHandler(TechnicalException.class)
    public ResponseEntity<String> handleTechnicalException(TechnicalException e) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Сущности с таким ID нет в БД");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Произошла ошибка. Повторите попытку позже");
    }
}
