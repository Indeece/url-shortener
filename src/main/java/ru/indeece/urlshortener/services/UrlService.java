package ru.indeece.urlshortener.services;

import ru.indeece.urlshortener.dto.UrlDto;

public interface UrlService {

    public String generateRandomSlug(String alphabet);

    public UrlDto generateShortUrl(String url);

    public UrlDto redirectToUrl(String slug);

}
