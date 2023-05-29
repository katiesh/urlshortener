package home.urlshortener.service;

import home.urlshortener.converter.UrlConverter;
import home.urlshortener.entity.UrlEntity;
import home.urlshortener.excetion.DeactivatedUrlException;
import home.urlshortener.excetion.ValidationFailedException;
import home.urlshortener.excetion.UrlNotFoundException;
import home.urlshortener.model.UrlDto;
import home.urlshortener.repository.UrlRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class UrlShortenerServiceImpl implements UrlShortenerService{
    @Autowired
    private UrlRepository urlRepository;

    @Override
    public String getFullUrl(String shortUrl) {
        UrlEntity urlEntity = urlRepository.findByShortUrl(shortUrl);
        String fullUrl = Optional.ofNullable(urlEntity).map(UrlEntity::getFullUrl).orElse(StringUtils.EMPTY);
        if (urlEntity == null || StringUtils.isBlank(fullUrl)) {
            log.info("No full URL found for specified short url {}", shortUrl);
            throw new UrlNotFoundException("There is no URL associated with specified short URL:" + shortUrl);
        }
        LocalDateTime ttl = urlEntity.getTtl();
        if (ttl != null && ttl.isBefore(LocalDateTime.now())) {
            log.info("Short URL {} is deactivated", shortUrl);
            throw new DeactivatedUrlException(String.format("Short URL %s is deactivated", shortUrl));
        }
        return fullUrl;
    }

    @Override
    public void deleteShortUrl(String shortUrl) {
        if (urlRepository.deleteByShortUrl(shortUrl) == 0) {
            log.info("Specified short URL {} wasn't found.", shortUrl);
           throw new UrlNotFoundException(String.format("Specified short URL %s wasn't found", shortUrl));
        } else {
            log.info("Short URL {} was deleted", shortUrl);
        }
    }

    @Override
    public UrlDto createNewShortUrl(UrlDto urlDto) {
        String shortUrl = urlDto.getShortUrl();
        LocalDateTime ttl = urlDto.getTtl();
        validateShortUrl(shortUrl);
        validateTtl(ttl);
        UrlEntity entity = new UrlEntity();
        entity.setFullUrl(urlDto.getFullUrl());
        entity.setShortUrl(shortUrl);
        entity.setTtl(ttl);
        entity = urlRepository.save(entity);
        log.info("New short URL {} was generated for {}", entity.getShortUrl(), entity.getFullUrl());
        return UrlConverter.convertUrlEntityToDto(entity);
    }

    @Override
    public void removeDeactivatedUrls() {
        LocalDateTime currentTime = LocalDateTime.now();
        int deletedRows = urlRepository.deleteByTtlBefore(currentTime);
        log.info("{} deactivated URLs after {} were deleted.", deletedRows, currentTime);
    }

    private void validateTtl(final LocalDateTime ttl) {
        if (ttl != null && ttl.isBefore(LocalDateTime.now())) {
            log.info("Invalid ttl value was specified {}. Ttl should be a future date time", ttl);
            throw new ValidationFailedException("Ttl should be a future date time");
        }
    }

    private void validateShortUrl(final String shortUrl) {
        UrlEntity urlEntity = Optional.ofNullable(shortUrl).map(urlRepository::findByShortUrl).orElse(null);
        if (urlEntity != null) {
            log.info("Short URL {} is already in use", shortUrl);
            throw new ValidationFailedException("Short URL is already in use");
        }
    }
}
