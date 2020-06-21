package com.emilancius.top5.album;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumEntity, Long> {

    List<AlbumEntity> findByArtistId(final Long artistId);

    void deleteByArtistId(final Long artistId);
}
