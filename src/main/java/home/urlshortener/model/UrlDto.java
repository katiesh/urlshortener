package home.urlshortener.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UrlDto {
    @Pattern(regexp = "^[A-Za-z0-9]*$", message = "shortUrl field value is invalid. Only alphanumeric characters are allowed.")
    private String shortUrl;
    @NotBlank
    private String fullUrl;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ttl;
}
