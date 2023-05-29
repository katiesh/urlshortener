package home.urlshortener.excetion;

public class ValidationFailedException extends RuntimeException {
    public ValidationFailedException(String errorMessage) {
        super(errorMessage);
    }
}
