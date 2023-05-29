package home.urlshortener.excetion;

public class DeactivatedUrlException extends RuntimeException {
    public DeactivatedUrlException(String errorMessage) {
        super(errorMessage);
    }
}
