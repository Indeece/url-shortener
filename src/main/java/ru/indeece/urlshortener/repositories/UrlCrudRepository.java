package ru.indeece.urlshortener.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.indeece.urlshortener.entities.UrlRedisEntity;

import java.util.Optional;

public interface UrlCrudRepository extends CrudRepository<UrlRedisEntity, String> {
    Optional<UrlRedisEntity> findByUrl(String url);
    Optional<UrlRedisEntity> findBySlug(String slug);
}