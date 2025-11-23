package ru.indeece.urlshortener.mapper;

import org.mapstruct.Mapper;
import ru.indeece.urlshortener.dto.UrlDto;
import ru.indeece.urlshortener.entities.UrlJpaEntity;
import ru.indeece.urlshortener.entities.UrlRedisEntity;

@Mapper(componentModel = "spring")
public interface UrlMapper {
    UrlDto toDto(UrlRedisEntity event);

    UrlDto toDto(UrlJpaEntity event);

    UrlRedisEntity toRedisEntity(UrlJpaEntity jpaEntity);
}