package home.urlshortener.repository;

import home.urlshortener.entity.UrlEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface UrlRepository extends CrudRepository<UrlEntity, Long> {
    UrlEntity findByShortUrl(String shortUrl);

    @Transactional
    int deleteByShortUrl(String shortUrl);

    @Transactional
    int deleteByTtlBefore(LocalDateTime localDateTime);
}
