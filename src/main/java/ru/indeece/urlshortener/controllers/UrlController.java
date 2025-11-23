package ru.indeece.urlshortener.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.indeece.urlshortener.dto.UrlDto;
import ru.indeece.urlshortener.exceptions.MissingUrlException;
import ru.indeece.urlshortener.services.impl.UrlServiceImpl;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "URL Shortener", description = "API for generation shortened URLs")
public class UrlController {

    private final UrlServiceImpl urlService;

    public UrlController(UrlServiceImpl urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/generate-slug")
    @Operation(summary = "Generate a short URL slug",
            description = "Creates a shortened slug for the provided long url")
    public ResponseEntity<UrlDto> generateSlug(
            @Parameter(description = "Long URL", example = "https://google.com")
            @RequestParam String url
    ) {
        return ResponseEntity.ok(urlService.generateShortUrl(url));
    }

    @GetMapping("/redirect-url")
    @Operation(summary = "Redirect to the original url",
            description = "Redirects the client to the original URL")
    public ResponseEntity<Void> redirectUrl(
            @Parameter(description = "Slug", example = "aB3dE1")
            @RequestParam String slug
    ) {
        UrlDto longUrl = urlService.redirectToUrl(slug);

        if (longUrl == null) {
            throw new MissingUrlException("Could not redirect to url: " + slug);
        }

        return ResponseEntity.status(302)
                .header("Location", longUrl.url())
                .build();

    }
}
