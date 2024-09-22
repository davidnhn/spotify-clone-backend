package fr.codecake.spotify_clone.catalogcontext.application;

import fr.codecake.spotify_clone.catalogcontext.application.dto.FavoriteSongDTO;
import fr.codecake.spotify_clone.catalogcontext.application.dto.ReadSongInfoDTO;
import fr.codecake.spotify_clone.catalogcontext.application.dto.SaveSongDTO;
import fr.codecake.spotify_clone.catalogcontext.application.dto.SongContentDTO;
import fr.codecake.spotify_clone.catalogcontext.application.mapper.SongContentMapper;
import fr.codecake.spotify_clone.catalogcontext.application.mapper.SongMapper;
import fr.codecake.spotify_clone.catalogcontext.domain.Favorite;
import fr.codecake.spotify_clone.catalogcontext.domain.FavoriteId;
import fr.codecake.spotify_clone.catalogcontext.domain.Song;
import fr.codecake.spotify_clone.catalogcontext.domain.SongContent;
import fr.codecake.spotify_clone.catalogcontext.repository.FavoriteRepository;
import fr.codecake.spotify_clone.catalogcontext.repository.SongContentRepository;
import fr.codecake.spotify_clone.catalogcontext.repository.SongRepository;

import fr.codecake.spotify_clone.infrastucture.service.dto.State;
import fr.codecake.spotify_clone.infrastucture.service.dto.StateBuilder;
import fr.codecake.spotify_clone.usercontext.ReadUserDTO;
import fr.codecake.spotify_clone.usercontext.application.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
public class SongService {

    private final SongMapper songMapper;

    private final SongRepository songRepository;

    private final SongContentRepository songContentRepository;

    private final SongContentMapper songContentMapper;
    private final UserService userService;
    private final FavoriteRepository favoriteRepository;

    public SongService(
            SongMapper songMapper,
            SongRepository songRepository,
            SongContentRepository songContentRepository,
            SongContentMapper songContentMapper,
            UserService userService,
            FavoriteRepository favoriteRepository) {
        this.songMapper = songMapper;
        this.songRepository = songRepository;
        this.songContentRepository = songContentRepository;
        this.songContentMapper = songContentMapper;
        this.userService = userService;
        this.favoriteRepository = favoriteRepository;
    }

    public ReadSongInfoDTO create(SaveSongDTO saveSongDTO) {
        Song song = songMapper.saveSongDTOToSong(saveSongDTO);
        Song savedSong = songRepository.save(song);

        SongContent songContent = songContentMapper.saveSongDTOToSongContent(saveSongDTO);
        songContent.setSong(savedSong);

        songContentRepository.save(songContent);                     
        return songMapper.songToReadSongInfoDTO(savedSong);          
    }

    /*
   1. Mapping de SaveSongDTO à Song :
        saveSongDTO est mappé à un objet Song à l'aide de songMapper.
        Le champ id de Song est ignoré dans le mapping, donc il reste null.

    2. Sauvegarde de Song :
           songRepository.save(song) persiste l'objet Song et génère un ID (savedSong.id).

    3. Mapping de SaveSongDTO à SongContent :
        saveSongDTO est mappé à un objet SongContent à l'aide de songContentMapper.
        Le champ publicId de SongContentDTO est ignoré dans ce mapping.

    4.  Association de Song avec SongContent :
        songContent.setSong(savedSong) : Cette opération synchronise automatiquement songContent.songId avec savedSong.id grâce à l'annotation @MapsId.

    5  Sauvegarde de SongContent :
            songContentRepository.save(songContent) persiste l'objet SongContent avec l'ID synchronisé.

    6  Retour de ReadSongInfoDTO :
        L'objet Song persistant est mappé à un ReadSongInfoDTO et retourné.
     */

    @Transactional(readOnly = true)
    public List<ReadSongInfoDTO> getAll() {
        List<ReadSongInfoDTO> allSongs = songRepository.findAll()
                .stream()
                .map(songMapper::songToReadSongInfoDTO)
                .toList();

        if(userService.isAuthenticated()) {
            return fetchFavoritesStatusForSongs(allSongs);
        }

        return allSongs;
    }

    public Optional<SongContentDTO> getOneByPublicId(UUID publicId) {
        Optional<SongContent> songByPublicId = songContentRepository.findOneBySongPublicId(publicId);

        return songByPublicId.map(songContentMapper::songContentToSongContentDTO);
    }

    public List<ReadSongInfoDTO> search(String query) {
        List<ReadSongInfoDTO> searchSongs = songRepository.findByTitleOrAuthorContaining(query)
                .stream()
                .map(songMapper::songToReadSongInfoDTO)
                .collect(Collectors.toList());

        if(userService.isAuthenticated()) {
            return fetchFavoritesStatusForSongs(searchSongs);
        }
        return searchSongs;
    }

    public State<FavoriteSongDTO, String> addOrRemoveFromFavorite(FavoriteSongDTO favoriteSongDTO, String email) {
        StateBuilder<FavoriteSongDTO, String> builder = State.builder();
        Optional<Song> songToLikeOpt = songRepository.findOneByPublicId(favoriteSongDTO.publicId());
        if(songToLikeOpt.isEmpty()) {
            return builder.forError("Song public id doesn't exist").build();
        }

        Song songToLike = songToLikeOpt.get();

        ReadUserDTO userWhoLikedSong = userService.getByEmail(email).orElseThrow();

        if(favoriteSongDTO.favorite()) {
            Favorite favorite = new Favorite();
            favorite.setSongPublicId(songToLike.getPublicId());
            favorite.setUserEmail(userWhoLikedSong.email());
            favoriteRepository.save(favorite);
        } else {
            FavoriteId favoriteId = new FavoriteId(songToLike.getPublicId(), userWhoLikedSong.email());
            favoriteRepository.deleteById(favoriteId);
            favoriteSongDTO = new FavoriteSongDTO(false, songToLike.getPublicId());
        }

        return builder.forSuccess(favoriteSongDTO).build();
    }

    public List<ReadSongInfoDTO> fetchFavoriteSongs(String email) {
        return songRepository.findAllFavoriteByUserEmail(email)
                .stream()
                .map(songMapper::songToReadSongInfoDTO)
                .toList();
    }

    private List<ReadSongInfoDTO> fetchFavoritesStatusForSongs(List<ReadSongInfoDTO> songs) {
        ReadUserDTO authenticatedUser = userService.getAuthenticatedUserFormSecurityContext();

        List<UUID> songPublicIds = songs.stream().map(ReadSongInfoDTO::getPublicId).toList();

        List<UUID> userFavoriteSongs = favoriteRepository.findAllByUserEmailAndSongPublicIdIn(authenticatedUser.email(), songPublicIds)
                .stream().map(Favorite::getSongPublicId).toList();

        return songs.stream().peek(song -> {
            if(userFavoriteSongs.contains(song.getPublicId())) {
                song.setFavorite(true);
            }
        }).toList();
    }

    /*
    L'avantage d'utiliser peek() ici est qu'il permet de modifier l'état interne des objets ReadSongInfoDTO
    (en définissant leur statut de favori) sans interrompre le flux du stream.
    Après peek(), le stream contient toujours les mêmes objets ReadSongInfoDTO,
    mais certains d'entre eux ont été modifiés pour refléter leur statut de favori.

    Il est important de noter que peek() est principalement utilisé pour ses effets secondaires
    (comme la modification d'objets ou le débogage) et non pour transformer les éléments du stream.
    Si vous vouliez transformer les éléments, vous utiliseriez plutôt map().
     */
}                                                                    
                                                                     