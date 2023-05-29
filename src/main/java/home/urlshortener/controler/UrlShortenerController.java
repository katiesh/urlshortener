package home.urlshortener.controler;

import home.urlshortener.model.UrlDto;
import home.urlshortener.service.UrlShortenerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@Slf4j
public class UrlShortenerController {
    @Autowired
    private UrlShortenerService urlShortenerService;

    @GetMapping("/{shortUrl}")
    public RedirectView redirectUrl(@PathVariable @Valid String shortUrl, HttpServletRequest httpServletRequest) {
        log.debug("Received shortened url to redirect: " + shortUrl);
        String redirectUrlString = urlShortenerService.getFullUrl(shortUrl);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(String.format("http://%s/%s", httpServletRequest.getHeader("Host"), redirectUrlString));
        return redirectView;
    }

    @DeleteMapping("/{shortUrl}")
    public void deleteShortUrl(@PathVariable String shortUrl) {
        log.debug("Received request to delete short URL {}", shortUrl);
        urlShortenerService.deleteShortUrl(shortUrl);
    }

    @PostMapping("/urlShortener")
    public UrlDto createShortUrl(@RequestBody @Valid @NotNull UrlDto urlDto) {
        log.info("New short URL for {} will be created", urlDto.getFullUrl());
        return urlShortenerService.createNewShortUrl(urlDto);
    }
}
