package fr.codecake.spotify_clone.catalogcontext.application.mapper;

import fr.codecake.spotify_clone.catalogcontext.application.dto.SaveSongDTO;
import fr.codecake.spotify_clone.catalogcontext.application.dto.SongContentDTO;
import fr.codecake.spotify_clone.catalogcontext.domain.Song;
import fr.codecake.spotify_clone.catalogcontext.domain.SongContent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SongContentMapper {

    //signifie que la valeur de song.publicId (l'ID public de la chanson dans l'entité Song)
    // doit être mappée sur le champ publicId de l'objet SongContentDTO.
    @Mapping(source = "song.publicId", target = "publicId")
    SongContentDTO songContentToSongContentDTO(SongContent  songContent);

    SongContent saveSongDTOToSongContent (SaveSongDTO saveSongDTO);
}
