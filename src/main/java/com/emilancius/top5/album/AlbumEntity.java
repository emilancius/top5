package com.emilancius.top5.album;

import com.emilancius.top5.persistence.BaseEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
@Table(schema = "TOP5", name = "ARTIST_TOP_ALBUMS")
public class AlbumEntity extends BaseEntity {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "ARTIST_ID")
    private Long artistId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "GENRE")
    private String genre;

    @Column(name = "RELEASED_AT")
    private Instant released;

    public AlbumEntity() {

    }

    public AlbumEntity(
            final Long id,
            final Long artistId,
            final String name,
            final String genre,
            final Instant released
    ) {
        this.id = id;
        this.artistId = artistId;
        this.name = name;
        this.genre = genre;
        this.released = released;
    }

    public Album toAlbum() {
        return new Album(id, artistId, name, genre, released);
    }
}
