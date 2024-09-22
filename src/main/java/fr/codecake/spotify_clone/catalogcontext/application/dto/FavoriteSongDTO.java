package fr.codecake.spotify_clone.catalogcontext.application.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record FavoriteSongDTO(
        @NotNull boolean favorite,
        @NotNull UUID publicId
) {
}
