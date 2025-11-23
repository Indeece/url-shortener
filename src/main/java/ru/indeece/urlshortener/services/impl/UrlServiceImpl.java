package ru.indeece.urlshortener.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.indeece.urlshortener.dto.UrlDto;
import ru.indeece.urlshortener.entities.UrlJpaEntity;
import ru.indeece.urlshortener.entities.UrlRedisEntity;
import ru.indeece.urlshortener.exceptions.MissingUrlException;
import ru.indeece.urlshortener.mapper.UrlMapper;
import ru.indeece.urlshortener.repositories.UrlCrudRepository;
import ru.indeece.urlshortener.repositories.UrlJpaRepository;
import ru.indeece.urlshortener.services.UrlService;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {

    private final UrlJpaRepository urlJpaRepository;
    private final UrlCrudRepository urlCrudRepository;
    private final UrlMapper urlMapper;

    private final String ALPHABET = "abcdefghijklmnopqrstuvwxyz" +
                                    "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                                    "0123456789";

    @Override
    public String generateRandomSlug(String ALPHABET) {

        StringBuilder slug = new StringBuilder();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < 6; ++i) {
            slug.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }

        log.info("Generated slug: {}", slug.toString());

        return slug.toString();
    }

    @Override
    public UrlDto generateShortUrl(String url) {
        if (urlJpaRepository.findByUrl(url).isPresent()) {
            // UrlDto dto = urlMapper.toDto(urlCrudRepository.findByUrl(url).get());
            String existing = urlJpaRepository.findByUrl(url).get().getSlug();
            String id = urlJpaRepository.findByUrl(url).get().getId();
            log.info("Found already created url: {}", url);
            return new UrlDto(id, url, existing);
        }

        String slug;
        do {
            slug = generateRandomSlug(ALPHABET);
        } while (urlJpaRepository.findBySlug(slug).isPresent());

        String id = UUID.randomUUID().toString();

        UrlJpaEntity urlJpaEntity = UrlJpaEntity.builder()
                .id(id)
                .url(url)
                .slug(slug)
                .build();

        UrlRedisEntity urlRedisEntity = UrlRedisEntity.builder()
                .id(id)
                .url(url)
                .slug(slug)
                .build();

        urlJpaRepository.save(urlJpaEntity);
        urlCrudRepository.save(urlRedisEntity);

        log.info("Saved slug: {}", url);

        // return urlMapper.toDto(urlRedisEntity);
        return toDto(urlRedisEntity);
    }

    @Override
    public UrlDto redirectToUrl(String slug) {
        log.info("Trying to find long url by slug in Redis: {}", slug);

        return urlCrudRepository.findBySlug(slug).map(
                cached -> {
                    log.info("Cache hit for slug: {}", slug);
                    // return urlMapper.toDto(cached);
                    return toDto(cached);
                }
        ).orElseGet(() -> {
            log.info("Cache miss for slug: {}", slug);
            return urlJpaRepository.findBySlug(slug).map(entity -> {
                // UrlDto dto = urlMapper.toDto(entity);
                UrlDto dto = toDto(entity);
                // UrlRedisEntity urlRedisEntity = urlMapper.toRedisEntity(entity);
                UrlRedisEntity urlRedisEntity = toRedisEntity(entity);
                urlCrudRepository.save(urlRedisEntity);
                log.info("Entity id: {} cached after loading from Postgres", entity.getId());
                return dto;
            })
                    .orElseThrow(() -> new MissingUrlException("Could not find url"));
        });
    }

    // TODO: MapStruct returns null DTO fields

    public UrlDto toDto(UrlJpaEntity entity) {
        if (entity == null) return null;
        return new UrlDto(entity.getId(), entity.getUrl(), entity.getSlug());
    }

    public UrlDto toDto(UrlRedisEntity entity) {
        if (entity == null) return null;
        return new UrlDto(entity.getId(), entity.getUrl(), entity.getSlug());
    }

    public UrlRedisEntity toRedisEntity(UrlJpaEntity jpa) {
        if (jpa == null) return null;
        return UrlRedisEntity.builder()
                .id(jpa.getId())
                .url(jpa.getUrl())
                .slug(jpa.getSlug())
                .build();
    }
}