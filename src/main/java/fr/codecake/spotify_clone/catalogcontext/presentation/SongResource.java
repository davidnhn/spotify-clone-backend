package fr.codecake.spotify_clone.catalogcontext.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.codecake.spotify_clone.catalogcontext.application.SongService;
import fr.codecake.spotify_clone.catalogcontext.application.dto.FavoriteSongDTO;
import fr.codecake.spotify_clone.catalogcontext.application.dto.ReadSongInfoDTO;
import fr.codecake.spotify_clone.catalogcontext.application.dto.SaveSongDTO;
import fr.codecake.spotify_clone.catalogcontext.application.dto.SongContentDTO;
import fr.codecake.spotify_clone.infrastucture.service.dto.State;
import fr.codecake.spotify_clone.infrastucture.service.dto.StatusNotification;
import fr.codecake.spotify_clone.usercontext.ReadUserDTO;
import fr.codecake.spotify_clone.usercontext.application.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SongResource {

    private final SongService songService;

    private final Validator validator;

    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper(); // deserialiser le JSON

    public SongResource(Validator validator, UserService userService, SongService songService) {
        this.validator = validator;
        this.userService = userService;
        this.songService = songService;
    }

    @PostMapping(value = "/songs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReadSongInfoDTO> add(
            @RequestPart(name = "cover") MultipartFile cover,
            @RequestPart(name = "file") MultipartFile file,
            @RequestPart(name = "dto") String saveSongDTOString
    ) throws IOException {
        SaveSongDTO saveSongDTO = objectMapper.readValue(saveSongDTOString, SaveSongDTO.class);
        saveSongDTO = new SaveSongDTO(
                saveSongDTO.title(),
                saveSongDTO.author(),
                cover.getBytes(),
                cover.getContentType(),
                file.getBytes(),
                file.getContentType()
        );

        Set<ConstraintViolation<SaveSongDTO>> constraintViolations = validator.validate(saveSongDTO);

        if(!constraintViolations.isEmpty()) {
            String violationsJoined = constraintViolations
                    .stream()
                    .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                    .collect(Collectors.joining());

            ProblemDetail validationIssue = ProblemDetail.forStatusAndDetail(
                    HttpStatus.BAD_REQUEST,
                    "Validation errors for the fields : " + violationsJoined
            );
            return ResponseEntity.of(validationIssue).build();
        } else {
            return ResponseEntity.ok(songService.create(saveSongDTO));
        }
    }

    @GetMapping("/songs")
    public ResponseEntity<List<ReadSongInfoDTO>> getAll() {
        return ResponseEntity.ok(songService.getAll());
    }

    @GetMapping("/songs/get-content")
    public ResponseEntity<SongContentDTO> getOneByPublicId(@RequestParam UUID publicId) {
        Optional<SongContentDTO> songContent = songService.getOneByPublicId(publicId);
        return songContent
                .map(ResponseEntity::ok)
                .orElseGet(
                        () -> ResponseEntity.of(
                                ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,"UUID Unknown")
                                )
                                .build()
                );
    }


    @GetMapping("/songs/search")
    public ResponseEntity<List<ReadSongInfoDTO>> search(@RequestParam String term) {
        return ResponseEntity.ok(songService.search(term));
    }

    @PostMapping("/songs/like")
    public ResponseEntity<FavoriteSongDTO> addOrRemoveFromFavorite(@Valid @RequestBody FavoriteSongDTO favoriteSongDTO) {
        ReadUserDTO userFromAuthentification = userService.getAuthenticatedUserFormSecurityContext();
        State<FavoriteSongDTO, String> favoriteSongResponse = songService.addOrRemoveFromFavorite(favoriteSongDTO, userFromAuthentification.email());

        if(favoriteSongResponse.getStatus().equals(StatusNotification.ERROR)) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, favoriteSongResponse.getError());
            return ResponseEntity.of(problemDetail).build();
        } else {
            return ResponseEntity.ok(favoriteSongResponse.getValue());
        }
    }

    @GetMapping("/songs/like")
    public ResponseEntity<List<ReadSongInfoDTO>> fetchFavoriteSongs() {
        ReadUserDTO userFromAuthentification = userService.getAuthenticatedUserFormSecurityContext();
        return ResponseEntity.ok(songService.fetchFavoriteSongs(userFromAuthentification.email()));
    }


}
