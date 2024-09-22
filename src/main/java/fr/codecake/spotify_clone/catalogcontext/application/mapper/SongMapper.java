package fr.codecake.spotify_clone.catalogcontext.application.mapper;

import fr.codecake.spotify_clone.catalogcontext.application.dto.ReadSongInfoDTO;
import fr.codecake.spotify_clone.catalogcontext.application.dto.SaveSongDTO;
import fr.codecake.spotify_clone.catalogcontext.application.vo.SongAuthorVO;
import fr.codecake.spotify_clone.catalogcontext.application.vo.SongTitleVO;
import fr.codecake.spotify_clone.catalogcontext.domain.Song;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SongMapper {

    @Mapping(target = "id", ignore = true) // dans l'objet cible "Song" le champ "id" ne sera pas défini
    @Mapping(target = "publicId", ignore = true)
    @Mapping(source = "title.value", target = "title")
    @Mapping(source = "author.value", target = "author")
    Song saveSongDTOToSong(SaveSongDTO saveSongDTO);

//    @Mapping(source = "title", target = "title.value")
//    @Mapping(source = "author", target = "author.value")
    @Mapping(target = "favorite", ignore = true)
    ReadSongInfoDTO songToReadSongInfoDTO(Song song);

    default SongTitleVO stringToSongTitleVO(String songTitle) {
        return new SongTitleVO(songTitle);
    }

    default SongAuthorVO stringToSongAuthorVO(String songAuthor) {
        return new SongAuthorVO(songAuthor);
    }

    default String songTitleVOToString(SongTitleVO songTitleVO) {
        return songTitleVO.value();
    }

    default String songAuthorVOToString(SongAuthorVO songAuthorVO) {
        return songAuthorVO.value();
    }
}
