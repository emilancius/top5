package com.emilancius.top5.artist.favourite;

import com.emilancius.top5.persistence.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
@AllArgsConstructor
@Table(schema = "TOP5", name = "USER_FAVOURITE_ARTISTS")
public class UserFavouriteArtistEntity extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "ARTIST_ID")
    private Long artistId;

    public UserFavouriteArtistEntity() {

    }
}
