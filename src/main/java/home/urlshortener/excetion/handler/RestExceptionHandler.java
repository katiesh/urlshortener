package home.urlshortener.excetion.handler;

import home.urlshortener.excetion.DeactivatedUrlException;
import home.urlshortener.excetion.UrlNotFoundException;
import home.urlshortener.excetion.ValidationFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler({UrlNotFoundException.class, DeactivatedUrlException.class})
    public ResponseEntity<String> handleUrlNotFoundException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ValidationFailedException.class)
    public ResponseEntity<String> handleValidationException(ValidationFailedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
