package com.emilancius.top5.artist;

import com.emilancius.top5.persistence.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
@AllArgsConstructor
@Table(schema = "TOP5", name = "ARTISTS")
public class ArtistEntity extends BaseEntity {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "GENRE")
    private String genre;

    @Column(name = "ALBUMS_LAST_UPDATED_AT")
    private Instant albumsLastUpdated;

    public ArtistEntity() {

    }

    public ArtistEntity(
            final Long id,
            final String name,
            final String genre
    ) {
        this.id = id;
        this.name = name;
        this.genre = genre;
    }
}
