package com.emilancius.top5.artist.favourite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserFavouriteArtistRepository extends JpaRepository<UserFavouriteArtistEntity, Long> {

    Optional<UserFavouriteArtistEntity> findByUserId(final Long userId);
}
