package ru.indeece.urlshortener.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "urls")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UrlJpaEntity {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "long_url")
    private String url;
    @Column(name = "slug")
    private String slug;
}
