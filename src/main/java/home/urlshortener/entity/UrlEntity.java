package home.urlshortener.entity;

import home.urlshortener.util.Base62Encoder;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "URL")
public class UrlEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE)
    @Access( AccessType.PROPERTY )
    @Setter(AccessLevel.NONE)
    private Long id;
    @Column(name = "SHORT_URL", unique = true)
    private String shortUrl;
    @Column(name = "FULL_URL", nullable = false)
    private String fullUrl;
    @Column(name = "TTL")
    private LocalDateTime ttl;

    public void setId(Long id) {
        this.id = id;
        if (this.shortUrl == null) {
            this.shortUrl = Base62Encoder.encode(id);
        }
    }
}
