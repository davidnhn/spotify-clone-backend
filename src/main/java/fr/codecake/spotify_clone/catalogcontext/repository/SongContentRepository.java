package fr.codecake.spotify_clone.catalogcontext.repository;

import fr.codecake.spotify_clone.catalogcontext.domain.SongContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SongContentRepository extends JpaRepository<SongContent, Long> {
    Optional<SongContent> findOneBySongPublicId(UUID publicId);
}
