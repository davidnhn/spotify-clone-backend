package fr.codecake.spotify_clone.catalogcontext.repository;

import fr.codecake.spotify_clone.catalogcontext.domain.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SongRepository extends JpaRepository<Song, Long> {

    @Query("""
            SELECT s FROM Song s\s
            WHERE lower(s.title) LIKE lower(concat('%', :searchTerm, '%'))
            OR lower(s.author) LIKE lower(concat('%', :searchTerm, '%'))\s
""")
    List<Song> findByTitleOrAuthorContaining(String searchTerm);

    Optional<Song> findOneByPublicId(UUID publicId);

    @Query("""
            SELECT song FROM Song song\s
            JOIN Favorite fav ON song.publicId = fav.songPublicId
            WHERE fav.userEmail = :email
""")
    List<Song> findAllFavoriteByUserEmail(String email);
}
