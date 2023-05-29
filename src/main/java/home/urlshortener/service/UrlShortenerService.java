package home.urlshortener.service;

import home.urlshortener.excetion.UrlNotFoundException;
import home.urlshortener.model.UrlDto;

public interface UrlShortenerService {

    String getFullUrl(String shortUrl) throws UrlNotFoundException;
    void deleteShortUrl(String shortUrl);
    UrlDto createNewShortUrl(UrlDto urlDto);
    void removeDeactivatedUrls();
}
