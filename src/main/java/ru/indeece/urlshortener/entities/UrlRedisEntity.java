package ru.indeece.urlshortener.entities;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "urls", timeToLive = 2592000)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UrlRedisEntity {
    @Id
    private String id;
    private String url;
    private String slug;
}
