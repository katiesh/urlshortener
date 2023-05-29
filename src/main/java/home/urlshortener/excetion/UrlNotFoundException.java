package home.urlshortener.excetion;

public class UrlNotFoundException extends RuntimeException {
    public UrlNotFoundException (String errorMessage) {
        super(errorMessage);
    }
}
