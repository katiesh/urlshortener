package home.urlshortener.converter;

import home.urlshortener.entity.UrlEntity;
import home.urlshortener.model.UrlDto;

import java.util.Optional;

public class UrlConverter {
    public static UrlDto convertUrlEntityToDto(final UrlEntity entity) {
        if (entity == null) {
            return null;
        }
        UrlDto dto = new UrlDto();
        dto.setShortUrl(entity.getShortUrl());
        dto.setFullUrl(entity.getFullUrl());
        Optional.ofNullable(entity.getTtl()).ifPresent(ttl -> dto.setTtl(ttl));
        return dto;
    }
}
