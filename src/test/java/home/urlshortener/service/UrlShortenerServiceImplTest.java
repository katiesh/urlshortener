package home.urlshortener.service;

import home.urlshortener.entity.UrlEntity;
import home.urlshortener.excetion.DeactivatedUrlException;
import home.urlshortener.excetion.UrlNotFoundException;
import home.urlshortener.excetion.ValidationFailedException;
import home.urlshortener.model.UrlDto;
import home.urlshortener.repository.UrlRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UrlShortenerServiceImplTest {
    private static final String SHORT_URL = "shortUrl";
    private static final String FULL_URL = "fullUrl";
    private static final LocalDateTime VALID_TTL = LocalDateTime.parse("2024-05-29T00:00:00");
    private static final LocalDateTime INVALID_TTL = LocalDateTime.parse("2022-05-29T00:00:00");
    @Mock
    private UrlRepository urlRepository;
    @InjectMocks
    private UrlShortenerServiceImpl sut;

    @Test
    public void shouldGetFullUrl() {
        when(urlRepository.findByShortUrl(eq(SHORT_URL))).thenReturn(buildUrlEntity(VALID_TTL));
        String actualFullUrl = sut.getFullUrl(SHORT_URL);
        verify(urlRepository).findByShortUrl(eq(SHORT_URL));
        assertEquals(FULL_URL, actualFullUrl);
    }

    @Test
    public void shouldThrowUrlNotFoundExceptionIfUrlNotExist() {
        when(urlRepository.findByShortUrl(eq(SHORT_URL))).thenReturn(null);
        assertThrows(UrlNotFoundException.class,() -> sut.getFullUrl(SHORT_URL));
    }

    @Test
    public void shouldThrowDeactivatedUrlExceptionIfTtlIsReached() {
        when(urlRepository.findByShortUrl(eq(SHORT_URL))).thenReturn(buildUrlEntity(INVALID_TTL));
        assertThrows(DeactivatedUrlException.class,() -> sut.getFullUrl(SHORT_URL));
    }

    @Test
    public void shouldDeleteShortUrl() {
        when(urlRepository.deleteByShortUrl(eq(SHORT_URL))).thenReturn(2);
        sut.deleteShortUrl(SHORT_URL);
        verify(urlRepository).deleteByShortUrl(eq(SHORT_URL));
    }

    @Test
    public void shouldThrowUrlNotFoundExceptionWhenDeleteNonExistentShortUrl() {
        when(urlRepository.deleteByShortUrl(eq(SHORT_URL))).thenReturn(0);
        assertThrows(UrlNotFoundException.class,() -> sut.deleteShortUrl(SHORT_URL));
    }

    @Test
    public void shouldCreateNewShortUrl() {
        UrlEntity urlEntity = buildUrlEntity(VALID_TTL);
        when(urlRepository.findByShortUrl(eq(SHORT_URL))).thenReturn(null);
        when(urlRepository.save(urlEntity)).thenReturn(urlEntity);

        UrlDto actualUrlDto = sut.createNewShortUrl(buildUrlDto(VALID_TTL));

        verify(urlRepository).findByShortUrl(eq(SHORT_URL));
        verify(urlRepository).save(eq(urlEntity));

        assertEquals(actualUrlDto.getShortUrl(), SHORT_URL);
        assertEquals(actualUrlDto.getFullUrl(), FULL_URL);
        assertEquals(actualUrlDto.getTtl(), VALID_TTL);
    }

    @Test
    public void shouldThrowValidationFailedExceptionWhenShortUrlAlreadyExist() {
        when(urlRepository.findByShortUrl(eq(SHORT_URL))).thenReturn(buildUrlEntity(VALID_TTL));
        assertThrows(ValidationFailedException.class, () -> sut.createNewShortUrl(buildUrlDto(VALID_TTL)));
    }

    @Test
    public void shouldThrowValidationFailedExceptionWhenTtlIsBeforeNow() {
        assertThrows(ValidationFailedException.class, () -> sut.createNewShortUrl(buildUrlDto(INVALID_TTL)));
    }

    @Test
    public void shouldRemoveDeactivatedUrls() {
        when(urlRepository.deleteByTtlBefore(any(LocalDateTime.class))).thenReturn(3);
        sut.removeDeactivatedUrls();
        verify(urlRepository).deleteByTtlBefore(any(LocalDateTime.class));
    }

    private UrlEntity buildUrlEntity(LocalDateTime ttl) {
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setShortUrl(SHORT_URL);
        urlEntity.setFullUrl(FULL_URL);
        urlEntity.setTtl(ttl);
        return urlEntity;
    }

    private UrlDto buildUrlDto(LocalDateTime ttl) {
        UrlDto urlDto = new UrlDto();
        urlDto.setShortUrl(SHORT_URL);
        urlDto.setFullUrl(FULL_URL);
        urlDto.setTtl(ttl);
        return urlDto;
    }
}