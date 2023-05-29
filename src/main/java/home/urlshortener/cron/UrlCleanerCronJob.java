package home.urlshortener.cron;

import home.urlshortener.service.UrlShortenerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UrlCleanerCronJob {
    @Autowired
    private UrlShortenerService urlShortenerService;
    @Scheduled(cron = "0 0 * * * *")
    public void cleanDeactivatedUrls() {
        log.info("UrlCleanerCronJob is executing..");
        try {
            urlShortenerService.removeDeactivatedUrls();
        } catch (Throwable t) {
            log.error("Execution of UrlCleanerCronJob was failed.", t);
        }
    }
}
