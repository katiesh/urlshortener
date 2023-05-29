package home.urlshortener.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.TimeZone;

@Configuration
@Slf4j
public class LocaleConfig {

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        log.info("Locale was set to UTC. Current timestamp {}", LocalDateTime.now());
    }
}
