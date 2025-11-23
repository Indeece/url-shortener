package ru.indeece.urlshortener.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.indeece.urlshortener.entities.UrlJpaEntity;

import java.util.Optional;

public interface UrlJpaRepository extends JpaRepository<UrlJpaEntity, String> {
    Optional<UrlJpaEntity> findByUrl(String url);
    Optional<UrlJpaEntity> findBySlug(String slug);
}