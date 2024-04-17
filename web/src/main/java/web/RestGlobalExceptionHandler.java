package web;

import com.opencsv.exceptions.CsvValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import utils.exceptions.AccessDeniedException;
import utils.exceptions.EntityContainedException;
import utils.exceptions.InvalidDataException;
import utils.exceptions.NoEntityException;
import utils.exceptions.TechnicalException;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeParseException;

/**
 * Глобальный обработчик исключений для обработки технических ошибок в приложении.
 */
@RestControllerAdvice
public class RestGlobalExceptionHandler {

    /**
     * Обработчик исключения EntityContainedException.
     * @param e объект исключения EntityContainedException.
     * @return ResponseEntity с кодом состояния 502 (Bad Gateway) и сообщением об ошибке.
     */
    @ExceptionHandler(EntityContainedException.class)
    public ResponseEntity<String> handleEntityContainedException(EntityContainedException e) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(e.getMessage() + ". Please try again");
    }

    /**
     * Обработчик исключения DateTimeParseException.
     * @param e объект исключения DateTimeParseException.
     * @return ResponseEntity с кодом состояния 502 (Bad Gateway) и сообщением об ошибке.
     */
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException e) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body("There's been a time parse error. Please try again with pattern YYYY-MM-DD-hh-mm-ss");
    }

    /**
     * Обработчик исключения TechnicalException.
     * @param e объект исключения TechnicalException.
     * @return ResponseEntity с кодом состояния 502 (Bad Gateway) и сообщением об ошибке.
     */
    @ExceptionHandler(TechnicalException.class)
    public ResponseEntity<String> handleTechnicalException(TechnicalException e) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body("There's been a technical error. Please try again later");
    }

    /**
     * Обработчик исключения SQLException.
     * @param e объект исключения SQLException.
     * @return ResponseEntity с кодом состояния 500 (Internal Server Error) и сообщением об ошибке.
     */
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> handleSQLException(SQLException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while working with the database. Please try again later");
    }

    /**
     * Обработчик исключения IOException.
     * @param e объект исключения IOException.
     * @return ResponseEntity с кодом состояния 500 (Internal Server Error) и сообщением об ошибке.
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An I/O error has occurred. Please try again later");
    }

    /**
     * Обработчик исключения CsvValidationException.
     * @param e объект исключения CsvValidationException.
     * @return ResponseEntity с кодом состояния 500 (Internal Server Error) и сообщением об ошибке.
     */
    @ExceptionHandler(CsvValidationException.class)
    public ResponseEntity<String> handleCsvValidationException(CsvValidationException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("CSV file validation error occurred. Please try again later");
    }

    /**
     * Обработчик исключения NoEntityException.
     * @param e объект исключения NoEntityException.
     * @return ResponseEntity с кодом состояния 500 (Internal Server Error) и сообщением об ошибке.
     */
    @ExceptionHandler(NoEntityException.class)
    public ResponseEntity<String> handleNoEntityException(NoEntityException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Entity is not in the database. Please try again later");
    }

    /**
     * Обработчик исключения AccessDeniedException.
     * @param e объект исключения AccessDeniedException.
     * @return ResponseEntity с кодом состояния 500 (Internal Server Error) и сообщением об ошибке.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Access denied. Please try again later");
    }

    /**
     * Обработчик исключения InvalidDataException.
     * @param e объект исключения InvalidDataException.
     * @return ResponseEntity с кодом состояния 500 (Internal Server Error) и сообщением об ошибке.
     */
    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<String> handleInvalidDataException(InvalidDataException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Incorrect data was received. Please try again later");
    }

    /**
     * Обработчик исключения Exception.
     * @param e объект исключения Exception.
     * @return ResponseEntity с кодом состояния 500 (Internal Server Error) и сообщением об ошибке.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error has occurred. Please try again later");
    }
}
